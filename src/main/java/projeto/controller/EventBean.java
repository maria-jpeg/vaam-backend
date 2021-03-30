package projeto.controller;

import org.joda.time.DateTime;
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

import javax.xml.bind.JAXBException;
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
    public List<EventDTO> getEventsByMouldCodeWithUsersAndWorkstations(String mouldCode) throws EntityDoesNotExistException
    {
        List<EventDTO> eventDTOSFullUsers = new LinkedList<>();
        List<Event> events = eventDAO.getEventByMouldCode(mouldCode);

        for (Event e:events)
        {
            List<ActivityUserEntry> aue = activityDAO.getEntriesAssociatedToEventActivity(e.getActivity().getId(),e.getStartDate(),e.getEndDate());
            List<ActivityUserEntryDTO> aueDTO = aue.stream().map(this::activityUserEntryToDTO).collect(Collectors.toList());
            double avgDuration = eventDAO.getAverageEventDurationByActivity(e.getActivity().getId());
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

        return eventDTOSFullUsers;
    }

    public ProcessTree getEventTree(){
        List<Event> events = eventDAO.getAll();
        //InductiveMiner algo = new InductiveMiner();

        try {
            XESHelper.eventsToIMLog(events);
        }catch (JAXBException ex){
            System.out.println( "EX: "+ex);
        }
        //algo.miner();
        return null;
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
