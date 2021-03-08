package projeto.controller;

import lombok.Getter;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.util.Precision;
import org.joda.time.DateTime;
import projeto.api.dtos.resources.ResourceActivityPerformanceDTO;
import projeto.api.dtos.resources.ResourcesActivityDTO;
import projeto.api.dtos.resources.operationalhours.ActivityWorkstationsDTO;
import projeto.api.dtos.resources.operationalhours.WorkstationOperationalHoursDTO;
import projeto.api.dtos.resources.workhours.UserWorkHoursDTO;
import projeto.api.dtos.resources.workhours.ActivityUsersDTO;
import projeto.api.dtos.resources.workhours.WorkstationUsersDTO;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.controller.exceptions.MyException;
import projeto.core.ActivityUserEntry;
import projeto.core.Event;
import projeto.core.Part;

import java.util.*;

public class ResourceBean
{
    @Getter
    private final LogBean logBean;
    @Getter
    private final ProcessBean processBean;
    @Getter
    private final ActivityBean activityBean;
    @Getter
    private final UserBean userBean;
    @Getter
    private final WorkstationBean workstationBean;
    @Getter
    private final PartBean partBean;

    public ResourceBean(LogBean logBean, ProcessBean processBean,  ActivityBean activityBean, UserBean userBean, WorkstationBean workstationBean, PartBean partBean) {
        this.logBean = logBean;
        this.processBean = processBean;
        this.activityBean = activityBean;
        this.userBean = userBean;
        this.workstationBean = workstationBean;
        this.partBean = partBean;
    }


    /*public ResourcesActivityDTO getResourcePerformance( String activityName, HashMap<Long, List<Event>> eventsByCaseID )
    {
        DescriptiveStatistics activityStats = new DescriptiveStatistics();
        HashMap<String, DescriptiveStatistics> userStats = new HashMap<>();

        for ( List<Event> events : eventsByCaseID.values() )
        {
            for (Event e : events)
            {
                if( ! e.getName().equals( activityName ) )
                    continue;

                DescriptiveStatistics stats = userStats.computeIfAbsent( e.getResource(), k -> new DescriptiveStatistics() );

                long duration = e.getDuration();
                stats.addValue( duration );
                activityStats.addValue( duration );
            }

        }


        List<ResourceActivityPerformanceDTO> resourcePerformance = new ArrayList<>();
        for ( Map.Entry<String, DescriptiveStatistics> entry : userStats.entrySet() )
        {
            String resourceName = entry.getKey();
            long meanDuration = (long) Precision.round( entry.getValue().getMean(), 0 );

            resourcePerformance.add(
                    new ResourceActivityPerformanceDTO( resourceName, meanDuration ) );
        }

        long meanDuration = (long) Precision.round( activityStats.getMean(), 0 );

        return new ResourcesActivityDTO( activityName, meanDuration, resourcePerformance, eventsByCaseID.keySet() );

    }*/

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


    public ResourcesActivityDTO getUsersPerformance(long activityId, HashMap<String, List<Event>> eventsByCaseCode, float threshold, boolean isSubProcess ) throws EntityDoesNotExistException {

            DescriptiveStatistics activityStats = new DescriptiveStatistics();
            HashMap<String, DescriptiveStatistics> userStats = new HashMap<>();

            for ( List<Event> events : eventsByCaseCode.values() )
            {
                for (Event e : events)
                {
                    if(e.getEndDate() == null){
                        continue;
                    }

                    if( e.getActivity().getId() != activityId ){
                        continue;
                    }

                    List<ActivityUserEntry> entries = processBean.getEntriesAssociatedToEventActivity(activityId, e.getStartDate(), e.getEndDate()); //get all the users that participated in the event

                    long totalEntriesDurations = 0; //will save the sum of the time spent by each user performing/helping the activity during the event

                    for (ActivityUserEntry entry : entries) {
                        long entryDuration = getEventEntryDuration(e,entry); //time spent by each user performing/helping the activity during the event
                        totalEntriesDurations += entryDuration; //sum
                    }

                    for (ActivityUserEntry entry : entries) {

                        long entryDuration = getEventEntryDuration(e,entry);

                        if(entryDuration >= e.getDuration()*threshold){ //only if the user has spent at least X (% threshold) amount of time performing the activity during the event, then he will be considered for the statistics
                            DescriptiveStatistics stats = userStats.computeIfAbsent( entry.getUser().getUsername(), k -> new DescriptiveStatistics() );

                            stats.addValue( totalEntriesDurations ); //note that the average performance of the task considered for the user wont be the actual time he spent performing it, but the sum of it plus the time spent by the users that helped him during the task (totalEntriesDuration)
                            activityStats.addValue( totalEntriesDurations );
                        }
                    }
                }
            }

            if(userStats.entrySet().size() == 0){
                throw new EntityDoesNotExistException("Não existem entradas associadas à atividade " + activityId);
            }

            List<ResourceActivityPerformanceDTO> resourcePerformance = new ArrayList<>();
            for ( Map.Entry<String, DescriptiveStatistics> entry : userStats.entrySet() ) //this entries are not related to the entries in the ActivityUserEntry foreach above
            {
                String username = entry.getKey();
                long meanDuration = (long) Precision.round( entry.getValue().getMean(), 0 ); //resource's meanDuration

                resourcePerformance.add(new ResourceActivityPerformanceDTO( username, meanDuration ) );
            }

            long meanDuration = (long) Precision.round( activityStats.getMean(), 0 ); //activity's meanDuration

            String activityName = activityBean.findOrFail(activityId).getName();

            return new ResourcesActivityDTO( activityName, meanDuration, resourcePerformance, eventsByCaseCode.keySet(), isSubProcess, partBean );

    }


    public ResourcesActivityDTO getSingleUserPerformance(long activityId, String username, HashMap<String, List<Event>> eventsByCaseCode, float threshold, boolean isSubProcess ) throws  EntityDoesNotExistException{

            DescriptiveStatistics activityStats = new DescriptiveStatistics();
            HashMap<String, DescriptiveStatistics> userStats = new HashMap<>();
            Set<String> userParts = new HashSet<>();
            Set<String> userMoulds = new HashSet<>();

            for ( List<Event> events : eventsByCaseCode.values() )
            {
                for (Event e : events)
                {
                    if(e.getEndDate() == null){
                        continue;
                    }

                    if( e.getActivity().getId() != activityId ){
                        continue;
                    }

                    List<ActivityUserEntry> entries = processBean.getEntriesAssociatedToEventActivity(activityId, e.getStartDate(), e.getEndDate());

                    long totalEntriesDurations = 0; //will save the sum of the time spent by each user performing/helping the activity during the event

                    for (ActivityUserEntry entry : entries) {
                        long entryDuration = getEventEntryDuration(e,entry); //time spent by each user performing/helping the activity during the event
                        totalEntriesDurations += entryDuration; //sum
                    }

                    for (ActivityUserEntry entry : entries) {

                        long entryDuration = getEventEntryDuration(e,entry);

                        if(entryDuration >= e.getDuration()*threshold){ //only if the user has spent at least X (% threshold) amount of time performing the activity during the event, then he will be considered for the statistics
                            if(entry.getUser().getUsername().equals(username)){ //we will only send specific user performance about the specified user (username)
                                DescriptiveStatistics stats = userStats.computeIfAbsent( entry.getUser().getUsername(), k -> new DescriptiveStatistics() );
                                stats.addValue( totalEntriesDurations ); //note that the average performance of the task considered for the user wont be the actual time he spent performing it, but the sum of it plus the time spent by the users that helped him during the task (totalEntriesDuration)


                                if(e.getPart() != null){
                                    userParts.add(e.getPart().getCode());
                                }
                                userMoulds.add(e.getMould().getCode());
                            }
                            activityStats.addValue( totalEntriesDurations );
                        }
                    }
                }
            }

            if(userStats.entrySet().size() == 0){
                throw new EntityDoesNotExistException("O utilizador " + username + " não tem entradas associadas à atividade " + activityId);
            }

            List<ResourceActivityPerformanceDTO> resourcePerformance = new ArrayList<>();
            for ( Map.Entry<String, DescriptiveStatistics> entry : userStats.entrySet() ) //this entries are not related to the entries in the ActivityUserEntry foreach above
            {
                String userUsername = entry.getKey();
                long meanDuration = (long) Precision.round( entry.getValue().getMean(), 0 ); //resource's meanDuration

                resourcePerformance.add(new ResourceActivityPerformanceDTO( userUsername, meanDuration ) );
            }

            long meanDuration = (long) Precision.round( activityStats.getMean(), 0 ); //activity's meanDuration

            String activityName = activityBean.findOrFail(activityId).getName();

            return new ResourcesActivityDTO( activityName, meanDuration, resourcePerformance, eventsByCaseCode.keySet(), isSubProcess, partBean );
    }


    public ActivityUsersDTO getUsersWorkHoursActivity(long activityId, HashMap<String, List<Event>> eventsByCaseCode, boolean isSubProcess ) throws EntityDoesNotExistException {

            DescriptiveStatistics activityStats = new DescriptiveStatistics();
            HashMap<String, DescriptiveStatistics> userStats = new HashMap<>();

            List<Long> durations = new LinkedList<>();
            for ( List<Event> events : eventsByCaseCode.values() )
            {
                for (Event e : events)
                {
                    if(e.getEndDate() == null){
                        continue;
                    }

                    if( e.getActivity().getId() != activityId ){
                        continue;
                    }

                    List<ActivityUserEntry> entries = processBean.getEntriesAssociatedToEventActivity(activityId, e.getStartDate(), e.getEndDate());

                    for (ActivityUserEntry entry : entries) {

                        long entryDuration = getEventEntryDuration(e,entry);
                        durations.add(entryDuration);

                        DescriptiveStatistics stats = userStats.computeIfAbsent( entry.getUser().getUsername(), k -> new DescriptiveStatistics() );

                        stats.addValue( entryDuration );
                        activityStats.addValue( entryDuration );
                    }
                }
            }

            if(userStats.entrySet().size() == 0){
                throw new EntityDoesNotExistException("Não existem entradas associadas à atividade " + activityId);
            }

            List<UserWorkHoursDTO> usersWorkHours = new ArrayList<>();
            for ( Map.Entry<String, DescriptiveStatistics> entry : userStats.entrySet() ) //this entries are not related to the entries in the ActivityUserEntry foreach above
            {
                String username = entry.getKey();
                long userWorkHours = (long) Precision.round( entry.getValue().getSum(), 0 ); //users total work hours in activity (sum because this is a cumulative statistic)

                usersWorkHours.add(new UserWorkHoursDTO( username, userWorkHours ) );
            }

            long totalWorkHours = (long) Precision.round( activityStats.getSum(), 0 ); //activity total work hours (sum because this is a cumulative statistic)

            String activityName = activityBean.findOrFail(activityId).getName();

            return new ActivityUsersDTO( activityName, totalWorkHours, usersWorkHours, eventsByCaseCode.keySet(), isSubProcess, partBean );

    }


    public WorkstationUsersDTO getUsersWorkHoursWorkstation(long workstationId, HashMap<String, List<Event>> eventsByCaseCode, boolean isSubProcess ) throws EntityDoesNotExistException {

            DescriptiveStatistics activityStats = new DescriptiveStatistics();
            HashMap<String, DescriptiveStatistics> userStats = new HashMap<>();

            for ( List<Event> events : eventsByCaseCode.values() )
            {
                for (Event e : events)
                {
                    if(e.getEndDate() == null){
                        continue;
                    }

                    if(e.getWorkstation() == null || e.getWorkstation().getId() != workstationId ){
                        continue;
                    }

                    List<ActivityUserEntry> entries = processBean.getEntriesAssociatedToEventWorkstation(workstationId, e.getStartDate(), e.getEndDate());

                    for (ActivityUserEntry entry : entries) {

                        long entryDuration = getEventEntryDuration(e,entry);

                        DescriptiveStatistics stats = userStats.computeIfAbsent( entry.getUser().getUsername(), k -> new DescriptiveStatistics() );

                        stats.addValue( entryDuration );
                        activityStats.addValue( entryDuration );
                    }
                }
            }

            if(userStats.entrySet().size() == 0){
                throw new EntityDoesNotExistException("Não existem entradas associadas à máquina '" + workstationId + "'.");
            }

            List<UserWorkHoursDTO> usersWorkHours = new ArrayList<>();
            for ( Map.Entry<String, DescriptiveStatistics> entry : userStats.entrySet() ) //this entries are not related to the entries in the ActivityUserEntry foreach above
            {
                String username = entry.getKey();
                long userWorkHours = (long) Precision.round( entry.getValue().getSum(), 0 ); //users total work hours in activity (sum because this is a cumulative statistic)

                usersWorkHours.add(new UserWorkHoursDTO( username, userWorkHours ) );
            }

            long totalWorkHours = (long) Precision.round( activityStats.getSum(), 0 ); //activity total work hours (sum because this is a cumulative statistic)

            String workstationName = workstationBean.findOrFail(workstationId).getName();

        return new WorkstationUsersDTO( workstationName, totalWorkHours, usersWorkHours, eventsByCaseCode.keySet(), isSubProcess, partBean);

    }





    public ActivityWorkstationsDTO getWorkstationsOperationalHoursActivity(long activityId, HashMap<String, List<Event>> eventsByCaseCode, boolean isSubProcess ) throws EntityDoesNotExistException {

            DescriptiveStatistics activityStats = new DescriptiveStatistics();
            HashMap<String, DescriptiveStatistics> workstationStats = new HashMap<>();

            for ( List<Event> events : eventsByCaseCode.values() )
            {
                for (Event e : events)
                {
                    if(e.getEndDate() == null){
                        continue;
                    }

                    if( e.getActivity().getId() != activityId ){
                        continue;
                    }


                    List<ActivityUserEntry> entries = processBean.getEntriesAssociatedToEventWorkstation(e.getWorkstation().getId(), e.getStartDate(), e.getEndDate());

                    for (ActivityUserEntry entry : entries) {

                        long entryDuration = getEventEntryDuration(e,entry);

                        DescriptiveStatistics stats = workstationStats.computeIfAbsent( e.getWorkstation().getName(), k -> new DescriptiveStatistics() );

                        stats.addValue( entryDuration );
                        activityStats.addValue( entryDuration );
                    }

                }
            }

            if(workstationStats.entrySet().size() == 0){
                throw new EntityDoesNotExistException("Não existem máquinas associadas à atividade '" + activityId + "'.");
            }
            List<WorkstationOperationalHoursDTO> workstationsOperationalHours = new ArrayList<>();
            for ( Map.Entry<String, DescriptiveStatistics> entry : workstationStats.entrySet() ) //this entries are not related to the entries in the ActivityUserEntry foreach above
            {
                String workstationName = entry.getKey();
                long workstationWorkHours = (long) Precision.round( entry.getValue().getSum(), 0 ); //workstation total operational hours in activity (sum because this is a cumulative statistic)

                workstationsOperationalHours.add(new WorkstationOperationalHoursDTO( workstationName, workstationWorkHours ) );
            }

            long totalOperationalHours = (long) Precision.round( activityStats.getSum(), 0 ); //activity total operational hours (sum because this is a cumulative statistic)

            String activityName = activityBean.findOrFail(activityId).getName();

            return new ActivityWorkstationsDTO( activityName, totalOperationalHours, workstationsOperationalHours, eventsByCaseCode.keySet(), isSubProcess, partBean );

    }

}
