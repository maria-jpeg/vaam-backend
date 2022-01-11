package projeto.controller;

import org.deckfour.xes.model.XLog;
import org.processmining.plugins.directlyfollowsgraph.DirectlyFollowsGraph;
import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.plugins.inductiveVisualMiner.helperClasses.IvMModel;
import org.processmining.plugins.inductiveVisualMiner.plugins.GraphvizProcessTree;
import org.processmining.processtree.ProcessTree;
import projeto.algorithms_process_mining.inductive_miner.InductiveMiner;
import projeto.api.dtos.entities.*;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.core.*;
import projeto.core.Process;
import projeto.data.ActivityDAO;
import projeto.data.EventDAO;
import projeto.data.XESHelper;
import projeto.resources.EventServ;
import projeto.weka.ClassifyActivitiesSequence;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class EventBean extends BaseBean<Event, EventDTO> {
    private static final Logger LOGGER = Logger.getLogger(EventServ.class.getName());

    final private EventDAO eventDAO;
    final private ActivityDAO activityDAO;

    public EventBean(EventDAO eventDAO,ActivityDAO activityDAO) {
        super(eventDAO);
        this.eventDAO = eventDAO;
        this.activityDAO = activityDAO;
    }

    public EventDTO toDTO(Event entity){
        return eventDAO.toDTO(entity);
    }

    public EventDTO toFullDTO(Event entity){
        return eventDAO.toFullDTO(entity);
    }

    public ActivityUserEntryDTO activityUserEntryToDTO(ActivityUserEntry entry){
        return activityDAO.AUtoDTO(entry);
    }

    public ActivityDTO toActivityDTO(Activity activity){
        return eventDAO.activityToDTO(activity);
    }

    public ProcessDTO toProcessDTO(Process process){
        return eventDAO.processToDTO(process);
    }
    public MouldDTO toMouldDTO(Mould mould){
        return eventDAO.mouldToDTO(mould);
    }
    public PartDTO toPartDTO(Part part){
        return eventDAO.partToDTO(part);
    }

    public List<EventDTO> getEventsFromMouldCode(String mouldCode) throws EntityDoesNotExistException
    {
        List<Event> events = eventDAO.getEventByMouldCode(mouldCode);

        return events.stream().map(this::toFullDTO).collect(Collectors.toList());
    }
    public List<EventDTO> getEventsByMouldCodeWithUsersAndWorkstations(String mouldCode) throws Exception {
        List<EventDTO> eventDTOSFullUsers = new LinkedList<>();
        List<Event> events = eventDAO.getEventByMouldCode(mouldCode);

        //999,'desenho elétrodos',1004,'2016.02.10 13:18:23 +0000','2016.02.11 13:26:06 +0000',user43,ABS-PA,'K50Mue - Vorderradabdeckung Oben',119000,
        // 'preliminares>estrutura>peças>estrutura>desenho elétrodos>preliminares>desenho elétrodos>estrutura>peças>cam>peças>cam>peças>trabalho interno>estrutura>cam>trabalho interno>erosão>cam>erosão>estrutura>peças>erosão>estrutura>desenho elétrodos>estrutura>desenho elétrodos',none

        String previous_activities = "";

        for (Event e:events)
        {
            List<ActivityUserEntry> aue = activityDAO.getEntriesAssociatedToEventActivity(e.getActivity().getId(),e.getStartDate(),e.getEndDate());
            List<ActivityUserEntryDTO> aueDTO = aue.stream().map(this::activityUserEntryToDTO).collect(Collectors.toList());
            double avgDuration = eventDAO.getAverageEventDurationByActivity(e.getActivity().getId());

            previous_activities = e.getActivity().getName() + ">";

            eventDTOSFullUsers.add(new EventDTO(
                    toActivityDTO(e.getActivity()),
                    toProcessDTO(e.getProcess()),
                    toMouldDTO(e.getMould()),
                    toPartDTO(e.getPart()),
                    e.getStartDate().toString("dd-MM-yyyy HH:mm:ss.SSS"),
                    e.getEndDate().toString("dd-MM-yyyy HH:mm:ss.SSS"),
                    e.getDuration(),
                    e.getIsEstimatedEnd(),
                    e.getId(),
                    aueDTO,
                    avgDuration));
        }

        //aqui o codigo para acrescentar linha e o codigo de previsão

//        FileWriter fw = new FileWriter("C:\\SmartTracking\\vaam-backend\\src\\main\\java\\projeto\\weka\\datasets\\weka_add_prediction_lines.arff",true);
//        fw.write("\rpreliminares,none");
//        fw.close();
//
//        ClassifyActivitiesSequence classifier = new ClassifyActivitiesSequence();
//        classifier.predictActivitySequenceLastLine("weka_add_prediction_lines.arff");

        //System.out.println(next_activities + "<!<!<!<!<!<!<!");

        //de seguida o ciclo for() para percorrer cada uma das atividades previstas e acrescentar

        return eventDTOSFullUsers;
    }

    /*@Override
    public EventDTO update(EventDTO eventDTO) throws EntityDoesNotExistException {
        Event event = eventDAO.findOrFail(eventDTO.getId());

        if(event != null) {
            event.setCaseId(eventDTO.getCaseId());
            event.setName(eventDTO.getName());
            event.setResource(eventDTO.getResource());
            event.setRole(eventDTO.getRole());
            event.setIsEstimatedEnd(eventDTO.getIsEstimatedEnd());

            if(eventDTO.getStartDate() != null){ //se o startDate não vier preenchido no update, deixamos como estava (para não ficar a null)
                event.setStartDate(eventDTO.getStartDate());
            }
            if(eventDTO.getEndDate() != null){
                event.setEndDate(eventDTO.getEndDate());
            }

            if(event.getStartDate() != null && event.getEndDate() != null){ //se ambos start e end Date já estiverem preenchidos, vamos calcular a duração
                event.setDuration(event.getEndDate().getMillis() - event.getStartDate().getMillis());
            }
        }

        return toDTO(event);
    }*/

}
