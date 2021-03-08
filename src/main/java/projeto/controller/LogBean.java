package projeto.controller;


import projeto.algorithms_process_mining.ProcessMiningAlgorithm;
import projeto.api.dtos.entities.LogDTO;
import projeto.api.dtos.workflow_network.WorkflowNetworkDTO;
import projeto.controller.exceptions.*;
import projeto.core.Event;
import projeto.core.Log;
import projeto.core.extra.FilterWrapper;
import projeto.data.CSVHelper;
import projeto.data.LogDAO;
import projeto.data.XESHelper;
import projeto.resources.LogServ;

import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogBean extends BaseBean<Log, LogDTO> {
    private static final Logger LOGGER = Logger.getLogger(LogServ.class.getName());

    final private LogDAO logDAO;

    public LogBean(LogDAO logDAO) {
        super(logDAO);
        this.logDAO = logDAO;
    }

    /*public Log create(InputStream inputStream, String filename, String description) throws EntityExistsException, ParsingException, InvalidLogException {
        List<Event> events = new ArrayList<>();

        String extension = getFileExtension( filename );

        // Check if extension is allowed and throw exception
        isExtensionAllowed( extension );

        try {
            switch (extension) {
                case "csv":
                    events = getEventsFromCSV(inputStream);
                    break;

                case "xes":
                    events = getEventsFromXES(inputStream);
                    break;
            }
        } catch (ParsingException e) {
            LOGGER.log(Level.SEVERE, "ParsingException", e);
            throw e;
        }

        if (events.isEmpty())
            throw new ParsingException("No events found!");

        Log log = new Log(filename, description);

        // Event stats
        HashSet<String> activities = new HashSet<>();
        HashSet<Long> cases = new HashSet<>();

        for (Event event : events ) {
            activities.add( event.getName() );
            cases.add( event.getCaseId() );
        }

        log.setStartDate( events.get( 0 ).getStartDate() );
        log.setEndDate( events.get( events.size() -1 ).getEndDate() );
        log.setNumberOfCases( cases.size() );
        log.setNumberOfActivities( activities.size() );

        // Assign the log to the events
        log.setEvents(events);
        for (Event e : events) {
            e.setLog(log);
        }

        return create(log);

    }

    private List<Event> getEventsFromCSV(InputStream inputStream) throws ParsingException {
        try {
            return CSVHelper.csvToEvents(inputStream);
        } catch (Exception e) {

            if (e instanceof ParsingException)
                throw e;

            LOGGER.log(Level.SEVERE, "Exception parsing", e);
            throw new ParsingException(e);
        }
    }

    private List<Event> getEventsFromXES(InputStream inputStream) throws ParsingException {

        try {
            return XESHelper.xesToEvents(inputStream);
        } catch (Exception e) {

            if (e instanceof ParsingException)
                throw e;

            LOGGER.log(Level.SEVERE, "Exception parsing", e);
            throw new ParsingException(e);
        }
    }

    private String getFileExtension(String fileName )
    {
        if( fileName == null )
            return null;

        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? null : fileName.substring(dotIndex + 1) .toLowerCase();
    }

    public void isExtensionAllowed(String extension) throws InvalidLogException
    {
        String exceptionMsg = "Invalid file extension only allowed .csv or .xes";

        if ( extension == null )
            throw new InvalidLogException( exceptionMsg );

        List<String> allowedExtensions = Arrays.asList("csv", "xes");

        boolean contains = allowedExtensions.contains(extension);
        if( !contains )
            throw new InvalidLogException( exceptionMsg );

    }

    /*
    public boolean isExtensionAllowed(String extension) {
        if (extension.isEmpty())
            return false;

        List<String> allowedExtensions = Arrays.asList("csv", "xes");
        return allowedExtensions.contains(extension);

    }
    */


    /*public HashMap<Long, List<Event>> sortEventsByCaseID( List<Event> logEvents )
    {
        HashMap<Long, List<Event>> eventsListHashMap = new HashMap<>();

        for( Event e : logEvents )
        {
            long caseID = e.getCaseId();
//            List<Event> eventList = eventsListHashMap.get( caseID );
//            if( eventList == null )
//            {
//                eventList = new ArrayList<>();
//                eventsListHashMap.put( caseID, eventList );
//            }

            List<Event> eventList = eventsListHashMap.computeIfAbsent(caseID, k -> new ArrayList<>());

            eventList.add( e );
        }

        return eventsListHashMap;
    }

    public HashMap<Long, List<Event>> sortEventsByCaseIDExtractEventNames( List<Event> logEvents, Set<String> eventsNames )
    {
        HashMap<Long, List<Event>> eventsListHashMap = new HashMap<>();
        eventsNames.clear();

        for( Event e : logEvents )
        {
            long caseID = e.getCaseId();
            List<Event> eventList = eventsListHashMap.computeIfAbsent( caseID, k -> new ArrayList<>() );

            eventList.add( e );
            eventsNames.add( e.getName() );
        }

        return eventsListHashMap;
    }

    public WorkflowNetworkDTO getWorkFlowNetworkFromLog(long logId, ProcessMiningAlgorithm algorithm ) throws EntityDoesNotExistException
    {
        List<Event> logEvents = logDAO.getEventsByLogID( logId );
        Set<String> eventsNames = new HashSet<>();

        HashMap<Long, List<Event>> eventsListHashMap = sortEventsByCaseIDExtractEventNames( logEvents, eventsNames );


        return algorithm.discoverWorkflowNetwork(
                    new ArrayList<>( eventsListHashMap.values() ), eventsNames );


    }

    public List<Event> getEventsByLogID( long logId ) throws EntityDoesNotExistException {
        return logDAO.getEventsByLogID( logId );
    }

    public List<Long> getCaseIDsfromLogID( long logId ) throws EntityDoesNotExistException {
        return logDAO.getCaseIDsfromLogID( logId );
    }

    public List<Event> getEventsFromCasesfromLog(long logId, List<Long> cases) throws EntityDoesNotExistException {
        return logDAO.getEventsFromCasesfromLog( logId, cases );
    }

    public List<String> getActivitiesfromLogID( long logId ) throws EntityDoesNotExistException {
        return logDAO.getActivitiesfromLogID( logId );
    }

    public List<String> getResourcesfromLogID( long logId ) throws EntityDoesNotExistException {
        return logDAO.getResourcesfromLogID( logId );
    }

    /*public HashMap<Long, List<Event>> getCasesFromFilter(long logId, FilterWrapper filter ) throws MyException, EntityDoesNotExistException
    {
        // Get specified cases
        if( filter.isCasesFilter() )
        {
            List<Event> eventCases = getEventsFromCasesfromLog( logId, filter.getCases() );
            return sortEventsByCaseID( eventCases );
        }


        // Get all events and find cases that match filters
        List<Event> eventsLog = getEventsByLogID( logId );

        if( eventsLog.isEmpty() )
            throw new MyException( "Couldn't any events matching the given filters" ); //????????tou??? não deveria ser "couldn't find any events for this log???"


        ArrayList<String> activities = filter.getActivities();
        ArrayList<String> resources = filter.getResources();
        long startDateMillis = filter.getStartDate() != null ? filter.getStartDate().getMillis() : 0;
        long endDateMillis = filter.getEndDate() != null ? filter.getEndDate().getMillis() : 0;


        // Events sorted HashMap
        HashMap<Long, List<Event>> eventsByCaseID = new HashMap<>();
        // Case filter status
        HashMap<Long, FilterWrapper.FilterStatus> caseFilterStatus = new HashMap<>();

        // Sort by case and save filter results at same time
        for( Event e : eventsLog )
        {
            long caseID = e.getCaseId();

            List<Event> eventList = eventsByCaseID.get( caseID );
            FilterWrapper.FilterStatus filterStatus = caseFilterStatus.get( caseID );
            if( eventList == null )
            {
                eventList = new ArrayList<>();
                eventsByCaseID.put( caseID, eventList );

                filterStatus = new FilterWrapper.FilterStatus();
                caseFilterStatus.put( caseID, filterStatus );
            }
            eventList.add( e );

            // Check if event matches filters

            // All dates need to match the range to pass
            // Start Date
            if( filter.isStartDateFilter() &&
                    e.getStartDate().getMillis() < startDateMillis &&
                    filterStatus.startDateMatch )
            {
                filterStatus.startDateMatch = false;
            }

            // End Date
            if( filter.isEndDateFilter() &&
                    e.getEndDate().getMillis() > endDateMillis &&
                    filterStatus.endDateMatch )
            {
                filterStatus.endDateMatch = false;
            }

            // Just 1 event needs to have the activity or resource to pass
            // Activities
            if( filter.isActivitiesFilter() &&
                    activities.contains( e.getName() ) &&
                    ! filterStatus.hasActivity )
            {
                filterStatus.hasActivity = true;
            }

            // Resources
            if( filter.isResourcesFilter() &&
                    resources.contains( e.getResource() ) &&
                    ! filterStatus.hasResource )
            {
                filterStatus.hasResource = true;
            }


        }

        // Check all filter results and filter the cases that doesnt match
        Iterator<Map.Entry<Long, List<Event>>> iterator = eventsByCaseID.entrySet().iterator();
        while ( iterator.hasNext() )
        {
            Long caseID = iterator.next() .getKey();
            FilterWrapper.FilterStatus filterStatus = caseFilterStatus.get( caseID );

            if( ! filterStatus.isConform( filter ) )
                iterator.remove();

        }

        if( eventsByCaseID.isEmpty() )
            throw new MyException( "Couldn't any events matching the given filters" );

        System.out.println( "Size: " + eventsByCaseID.size() );


//        for ( Map.Entry<Long, List<Event>> entry : eventsByCaseID.entrySet() )
//        {
//
//            System.out.println(  "\n Case: "  + entry.getKey() );
//
//            List<Event> events = entry.getValue();
//
//            for (Event e : events ) {
//                System.out.println( e.getStart().toString( "dd-MM-yyyy HH:mm" ) );
//            }
//            System.out.println( "Events : " + eventsLog.size() );
//
//        }



        return eventsByCaseID;
    }*/


}
