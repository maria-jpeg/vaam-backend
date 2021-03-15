package projeto.controller;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.util.Precision;
import org.joda.time.DateTime;
import projeto.api.dtos.DurationDTO;
import projeto.api.dtos.FrequencyDTO;
import projeto.api.dtos.entities.ActivityDTO;
import projeto.api.dtos.resources.operationalhours.ActivityTotalOperationalHoursDTO;
import projeto.api.dtos.resources.operationalhours.ActivityWorkstationsDTO;
import projeto.api.dtos.resources.operationalhours.WorkstationOperationalHoursDTO;
import projeto.api.dtos.resources.workhours.ActivityTotalWorkHoursDTO;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.core.Activity;
import projeto.core.ActivityUserEntry;
import projeto.core.Event;
import projeto.core.Process;
import projeto.data.ActivityDAO;
import projeto.data.BaseDAO;

import java.util.*;
import java.util.stream.Collectors;


public class ActivityBean extends BaseBean<Activity, ActivityDTO>
{
    final private ActivityDAO activityDAO;

    public ActivityBean(ActivityDAO dao) {
        super(dao);
        this.activityDAO = dao;
    }

    public ActivityDTO toDTO(Activity entity){
        return activityDAO.toDTO(entity);
    }

    public List<ActivityDTO> toDTOsList(List<Activity> activities) {
        return activities.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /*
    public List<FrequencyDTO> getFrequenciesFromCSV( String path ) throws FileNotFoundException {

        List<Event> activities = CSVHelper.csvToEvents( path );
        return computeFrequency( activities );
    }

    public List<FrequencyDTO> getFrequenciesFromCSV( InputStream inputStream ) {

        List<Event> activities = CSVHelper.csvToEvents( inputStream );
        return computeFrequency( activities );
    }

    public List<FrequencyDTO> getFrequenciesFromXES( String path ) throws Exception
    {
        List<Event> activities = XESHelper.xesToEvents( path );
        return computeFrequency( activities );
    }

    public List<FrequencyDTO> getFrequenciesFromXES( InputStream inputStream ) throws Exception {
        List<Event> activities = XESHelper.xesToEvents( inputStream );
        return computeFrequency( activities );
    }
    */

    /*public List<FrequencyDTO> computeFrequency( List<Event> events )
    {
        List<FrequencyDTO> frequencies = new ArrayList<>();
        //HashMap<String, List<Long>> activitiesSorted = new HashMap<>();
        HashMap<String, DescriptiveStatistics> activitiesSorted = new HashMap<>();

        for( Event e : events )
        {
            String activityName = e.getName();
            //List<Long> activityDurations = activitiesSorted.get( activityName );
            DescriptiveStatistics activityDurations = activitiesSorted.get( activityName );
            if( activityDurations == null )
            {
                //activityDurations = new ArrayList<>();
                activityDurations = new DescriptiveStatistics();
                activitiesSorted.put( activityName, activityDurations );
            }
            // Difference between dates in seconds
            activityDurations.addValue( e.getDuration() );
            //activityDurations.add( e.getDuration() );
            // activityDurations.add(
            //        e.getEnd().getMillis() - e.getStart().getMillis() );
        }

        int totalActivities = events.size();
        //for( Map.Entry<String, List<Long>> item : activitiesSorted.entrySet() )
        for( Map.Entry<String, DescriptiveStatistics> item : activitiesSorted.entrySet() )
        {
            String activityName = item.getKey();
//            List<Long> durations = item.getValue();
//            int count = durations.size();
//            System.out.println( activityName );
//
//            DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();
//            for (long d : durations) {
//                descriptiveStatistics.addValue( d );
//            }
            DescriptiveStatistics descriptiveStatistics = item.getValue();
            int count = (int)descriptiveStatistics.getN();

            long mean = (long) Precision.round( descriptiveStatistics.getMean(), 0 );
            long median = (long)Precision.round( descriptiveStatistics.getPercentile(50), 0 );
            long min = (long)Precision.round( descriptiveStatistics.getMin(), 0 );
            long max = (long)Precision.round( descriptiveStatistics.getMax(), 0 );

            frequencies.add(
                    new FrequencyDTO(
                            activityName,
                            count,
                            (float) count / totalActivities,
                            new DurationDTO( mean ),
                            new DurationDTO( median ),
                            new DurationDTO( min ),
                            new DurationDTO( max )
                    ) );

        }

        return frequencies;

    }*/


    public List<FrequencyDTO> computeFrequency( List<Event> events ) {
        List<FrequencyDTO> frequencies = new ArrayList<>();
        //HashMap<String, List<Long>> activitiesSorted = new HashMap<>();
        HashMap<String, DescriptiveStatistics> activitiesSorted = new HashMap<>();

        for( Event e : events )
        {
            if(e.getActivity() == null){
                continue;
            }
            String activityName = e.getActivity().getName();
            DescriptiveStatistics activityDurations = activitiesSorted.get( activityName );
            if( activityDurations == null )
            {
                //activityDurations = new ArrayList<>();
                activityDurations = new DescriptiveStatistics();
                activitiesSorted.put( activityName, activityDurations );
            }

            activityDurations.addValue( e.getDuration() );

        }

        int totalActivities = events.size();

        for( Map.Entry<String, DescriptiveStatistics> item : activitiesSorted.entrySet() )
        {
            String activityName = item.getKey();

            DescriptiveStatistics descriptiveStatistics = item.getValue();
            int count = (int)descriptiveStatistics.getN();

            long mean = (long) Precision.round( descriptiveStatistics.getMean(), 0 );
            long median = (long)Precision.round( descriptiveStatistics.getPercentile(50), 0 );
            long min = (long)Precision.round( descriptiveStatistics.getMin(), 0 );
            long max = (long)Precision.round( descriptiveStatistics.getMax(), 0 );

            frequencies.add(
                    new FrequencyDTO(
                            activityName,
                            count,
                            (float) count / totalActivities,
                            new DurationDTO( mean ),
                            new DurationDTO( median ),
                            new DurationDTO( min ),
                            new DurationDTO( max )
                    ) );

        }

        return frequencies;

    }


/*    public List<FrequencyDTO> computeFrequencyEffortProcess( List<Event> events ) throws EntityDoesNotExistException {
        List<FrequencyDTO> frequencies = new ArrayList<>();
        HashMap<String, DescriptiveStatistics> activitiesSorted = new HashMap<>();

        for( Event e : events )
        {
            //String activityName = e.getName();
            if(e.getActivity() == null){
                continue;
            }
            String activityName = e.getActivity().getName();
            //List<Long> activityDurations = activitiesSorted.get( activityName );
            DescriptiveStatistics activityDurations = activitiesSorted.get( activityName );
            if( activityDurations == null )
            {
                //activityDurations = new ArrayList<>();
                activityDurations = new DescriptiveStatistics();
                activitiesSorted.put( activityName, activityDurations );
            }

            //Compute the effort:
            List<ActivityUserEntry> entries = activityDAO.getEntriesAssociatedToEventActivity(e.getActivity().getId(), e.getStartDate(), e.getEndDate());

            long totalEffort = 0;
            for (ActivityUserEntry entry : entries) {

                long entryDuration = 0;
                DateTime entryEndDate = entry.getEndDate();
                if(entryEndDate == null){
                    entryEndDate = e.getEndDate();
                }

                if(entry.getStartDate().getMillis() >= e.getStartDate().getMillis() && entryEndDate.getMillis() <= e.getEndDate().getMillis()){
                    entryDuration = entryEndDate.getMillis() - entry.getStartDate().getMillis();
                }

                if(entry.getStartDate().getMillis() <= e.getStartDate().getMillis() && entryEndDate.getMillis() <= e.getEndDate().getMillis()){
                    entryDuration = entryEndDate.getMillis() - e.getStartDate().getMillis();
                }

                if(entry.getStartDate().getMillis() >= e.getStartDate().getMillis() && entryEndDate.getMillis() >= e.getEndDate().getMillis()){
                    entryDuration = e.getEndDate().getMillis() - entry.getStartDate().getMillis();
                }

                if(entry.getStartDate().getMillis() <= e.getStartDate().getMillis() && entryEndDate.getMillis() >= e.getEndDate().getMillis()){
                    entryDuration = e.getEndDate().getMillis() - e.getStartDate().getMillis();
                }

                totalEffort+=entryDuration;

            }

            activityDurations.addValue( totalEffort );

        }

        int totalActivities = events.size();
        for( Map.Entry<String, DescriptiveStatistics> item : activitiesSorted.entrySet() )
        {
            String activityName = item.getKey();

            DescriptiveStatistics descriptiveStatistics = item.getValue();
            int count = (int)descriptiveStatistics.getN();

            long mean = (long) Precision.round( descriptiveStatistics.getMean(), 0 );
            long median = (long)Precision.round( descriptiveStatistics.getPercentile(50), 0 );
            long min = (long)Precision.round( descriptiveStatistics.getMin(), 0 );
            long max = (long)Precision.round( descriptiveStatistics.getMax(), 0 );

            frequencies.add(
                    new FrequencyDTO(
                            activityName,
                            count,
                            (float) count / totalActivities,
                            new DurationDTO( mean ),
                            new DurationDTO( median ),
                            new DurationDTO( min ),
                            new DurationDTO( max )
                    ) );

        }

        return frequencies;

    }*/


    public List<ActivityTotalWorkHoursDTO> getTotalWorkHoursActivities(HashMap<String, List<Event>> eventsByCaseCode) {

        List<ActivityTotalWorkHoursDTO> activitiesTotalWorkHours = new ArrayList<>();
        HashMap<String, DescriptiveStatistics> activitiesSorted = new HashMap<>();

        List<Long> durations = new LinkedList<>();
        for ( List<Event> events : eventsByCaseCode.values() )
        {
            for (Event e : events) {
                if (e.getEndDate() == null) {
                    continue;
                }
                //String activityName = e.getName();
                if (e.getActivity() == null) {
                    continue;
                }
                String activityName = e.getActivity().getName();
                //List<Long> activityDurations = activitiesSorted.get( activityName );
                DescriptiveStatistics activityDurations = activitiesSorted.get(activityName);
                if (activityDurations == null) {
                    //activityDurations = new ArrayList<>();
                    activityDurations = new DescriptiveStatistics();
                    activitiesSorted.put(activityName, activityDurations);
                }

                List<ActivityUserEntry> entries = activityDAO.getEntriesAssociatedToEventActivity(e.getActivity().getId(), e.getStartDate(), e.getEndDate());

                for (ActivityUserEntry entry : entries) {

                    long entryDuration = getEventEntryDuration(e, entry);

                    activityDurations.addValue(entryDuration);

                }
            }
        }

        for( Map.Entry<String, DescriptiveStatistics> item : activitiesSorted.entrySet() )
        {
            String activityName = item.getKey();
            DescriptiveStatistics descriptiveStatistics = item.getValue();
            long totalWorkHours = (long) Precision.round( descriptiveStatistics.getSum(), 0 ); //activity total work hours (sum because this is a cumulative statistic)

            activitiesTotalWorkHours.add(
                    new ActivityTotalWorkHoursDTO(
                            activityName,
                            totalWorkHours
                    ) );

        }

        return activitiesTotalWorkHours;

    }




    public List<ActivityTotalOperationalHoursDTO> getTotalOperationalHoursActivity(HashMap<String, List<Event>> eventsByCaseCode) {

        List<ActivityTotalOperationalHoursDTO> activitiesTotalOperationalHours = new ArrayList<>();
        HashMap<String, DescriptiveStatistics> activitiesSorted = new HashMap<>();

        for ( List<Event> events : eventsByCaseCode.values() )
        {
            for (Event e : events) {
                if (e.getEndDate() == null) {
                    continue;
                }

                if (e.getActivity() == null) {
                    continue;
                }

                if (e.getWorkstation() == null) {
                    continue;
                }

                String activityName = e.getActivity().getName();
                DescriptiveStatistics activityDurations = activitiesSorted.get(activityName);
                if (activityDurations == null) {
                    activityDurations = new DescriptiveStatistics();
                    activitiesSorted.put(activityName, activityDurations);
                }

                activityDurations.addValue(e.getDuration());

            }
        }


        for( Map.Entry<String, DescriptiveStatistics> item : activitiesSorted.entrySet() )
        {
            String activityName = item.getKey();
            DescriptiveStatistics descriptiveStatistics = item.getValue();
            long totalOperationalHours = (long) Precision.round( descriptiveStatistics.getSum(), 0 ); //activity total operational hours (sum because this is a cumulative statistic)

            activitiesTotalOperationalHours.add(
                    new ActivityTotalOperationalHoursDTO(
                            activityName,
                            totalOperationalHours
                    ) );

        }

        return activitiesTotalOperationalHours;

    }

    public long getEventEntryDuration(Event e, ActivityUserEntry entry){

        long entryDuration = 0;
        DateTime entryEndDate = entry.getEndDate();
        if(entryEndDate == null){
            entryEndDate = e.getEndDate();
        }

        if(entry.getStartDate().getMillis() >= e.getStartDate().getMillis() && entryEndDate.getMillis() <= e.getEndDate().getMillis()){
            entryDuration = entryEndDate.getMillis() - entry.getStartDate().getMillis();
        }

        if(entry.getStartDate().getMillis() <= e.getStartDate().getMillis() && entryEndDate.getMillis() <= e.getEndDate().getMillis()){
            entryDuration = entryEndDate.getMillis() - e.getStartDate().getMillis();
        }

        if(entry.getStartDate().getMillis() >= e.getStartDate().getMillis() && entryEndDate.getMillis() >= e.getEndDate().getMillis()){
            entryDuration = e.getEndDate().getMillis() - entry.getStartDate().getMillis();
        }

        if(entry.getStartDate().getMillis() <= e.getStartDate().getMillis() && entryEndDate.getMillis() >= e.getEndDate().getMillis()){
            entryDuration = e.getEndDate().getMillis() - e.getStartDate().getMillis();
        }

        return entryDuration;
    }

    public List<ActivityDTO> getActivitiesFromMouldCode(String mouldCode) throws EntityDoesNotExistException
    {
        List<Activity> activities = activityDAO.getActivityByMouldCode(mouldCode);
        return activities.stream().map(this::toDTO).collect(Collectors.toList());
    }

}
