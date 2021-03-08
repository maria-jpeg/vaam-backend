package projeto.controller;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.util.Precision;
import projeto.api.dtos.DurationDTO;
import projeto.api.dtos.WorkstationPerformanceDTO;
import projeto.api.dtos.entities.WorkstationDTO;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.controller.exceptions.EntityExistsException;
import projeto.core.Event;
import projeto.core.Workstation;
import projeto.data.WorkstationDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WorkstationBean extends BaseBean<Workstation, WorkstationDTO> {

    final private WorkstationDAO workstationDAO;

    public WorkstationBean(WorkstationDAO dao) {
        super(dao);
        this.workstationDAO = dao;
    }

    public WorkstationDTO toDTO(Workstation entity){
        return workstationDAO.toDTO(entity);
    }

    public List<WorkstationDTO> toDTOsList(List<Workstation> activities) {
        return activities.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<Workstation> findName(String name) throws EntityExistsException {
        return workstationDAO.findName(name);
    }

    public List<Workstation> getTaggingWorkstations() {
        return workstationDAO.getTaggingWorkstations();
    }

    public List<WorkstationPerformanceDTO> computeWorkstationsPerformance(List<Event> events, long activityId ) throws EntityDoesNotExistException{
        List<WorkstationPerformanceDTO> performances = new ArrayList<>();
        HashMap<String, DescriptiveStatistics> workstationsSorted = new HashMap<>();

        for( Event e : events )
        {
            if(e.getActivity().getId() != activityId){
                continue;
            }
            if(e.getWorkstation() == null){
                continue;
            }
            String workstationName = e.getWorkstation().getName();

            DescriptiveStatistics workstationDurations = workstationsSorted.get( workstationName );
            if( workstationDurations == null )
            {
                workstationDurations = new DescriptiveStatistics();
                workstationsSorted.put( workstationName, workstationDurations );
            }

            workstationDurations.addValue( e.getDuration() );

        }

        for( Map.Entry<String, DescriptiveStatistics> item : workstationsSorted.entrySet() )
        {
            String workstationName = item.getKey();

            DescriptiveStatistics descriptiveStatistics = item.getValue();
            int count = (int)descriptiveStatistics.getN();

            long mean = (long) Precision.round( descriptiveStatistics.getMean(), 0 );
            long median = (long)Precision.round( descriptiveStatistics.getPercentile(50), 0 );
            long min = (long)Precision.round( descriptiveStatistics.getMin(), 0 );
            long max = (long)Precision.round( descriptiveStatistics.getMax(), 0 );

            performances.add(
                    new WorkstationPerformanceDTO(
                            workstationName,
                            count,
                            new DurationDTO( mean ),
                            new DurationDTO( median ),
                            new DurationDTO( min ),
                            new DurationDTO( max )
                    ) );

        }

        if(performances.size() == 0){
            throw new EntityDoesNotExistException("Não existem máquinas associadas à atividade '" + activityId + "'.");
        }
        return performances;
    }

}
