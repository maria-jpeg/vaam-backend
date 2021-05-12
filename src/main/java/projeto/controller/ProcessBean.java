package projeto.controller;

import org.joda.time.DateTime;
import projeto.algorithms_process_mining.ProcessMiningAlgorithm;
import projeto.algorithms_process_mining.inductive_miner.InductiveMiner;
import projeto.api.dtos.entities.ProcessDTO;
import projeto.api.dtos.workflow_network.WorkflowNetworkDTO;
import projeto.api.dtos.workflow_network.WorkflowNetworkPathsAndDeviationsDTO;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.controller.exceptions.EntityExistsException;
import projeto.controller.exceptions.MyException;
import projeto.core.*;
import projeto.core.Process;
import projeto.core.extra.FilterWrapper;
import projeto.data.BaseDAO;
import projeto.data.ProcessDAO;
import projeto.resources.ProcessServ;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ProcessBean extends BaseBean<Process, ProcessDTO>{

    private static final Logger LOGGER = Logger.getLogger(ProcessServ.class.getName());

    final private ProcessDAO processDAO;

    public ProcessBean(ProcessDAO processDAO) {
        super(processDAO);
        this.processDAO = processDAO;
    }

    public ProcessDTO toDTO(Process entity){
        return processDAO.toDTO(entity);
    }

    public List<ProcessDTO> toDTOsList(List<Process> processes) {
        return processes.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<Event> getEventsByProcessID(long processId ) throws EntityDoesNotExistException {
        return processDAO.getEventsByProcessID( processId );
    }

    public List<Event> getEventsFromMouldsfromProcess(long processId, List<String> moulds) throws EntityDoesNotExistException {
        return processDAO.getEventsFromMouldsfromProcess( processId, moulds );
    }

    public List<Event> getEventsFromPartsfromProcess(long processId, List<String> parts) throws EntityDoesNotExistException {
        return processDAO.getEventsFromPartsfromProcess( processId, parts );
    }

    public List<Event> getEventsFromMouldsfromProcessSet(long processId, Set<String> moulds) throws EntityDoesNotExistException {
        return processDAO.getEventsFromMouldsfromProcessSet( processId, moulds );
    }

    public List<Event> getEventsFromPartsfromProcessSet(long processId, Set<String> parts) throws EntityDoesNotExistException {
        return processDAO.getEventsFromPartsfromProcessSet( processId, parts );
    }

    public List<Activity> getActivitiesfromProcessId(long processId ) throws EntityDoesNotExistException {
        return processDAO.getActivitiesfromProcessId( processId );
    }

    public List<Workstation> getWorkstationsfromProcessId(long processId ) throws EntityDoesNotExistException {
        return processDAO.getWorkstationsfromProcessId( processId );
    }

    public List<ActivityUserEntry> getUsersFromProcessId(long processId ) throws EntityDoesNotExistException {
        return processDAO.getUsersFromProcessId( processId );
    }


    public List<String> getMouldCodesfromProcessId( long processId ) throws EntityDoesNotExistException {
        return processDAO.getMouldCodesfromProcessId( processId );
    }

    public List<String> getPartCodesfromProcessId( long processId ) throws EntityDoesNotExistException {
        return processDAO.getPartCodesfromProcessId( processId );
    }

    public List<ActivityUserEntry> getEntriesAssociatedToEventActivity(long activityId, DateTime eventStartDate, DateTime eventEndDate) {
        return processDAO.getEntriesAssociatedToEventActivity( activityId , eventStartDate, eventEndDate);
    }

    public List<ActivityUserEntry> getEntriesAssociatedToEventWorkstation(long workstationId, DateTime eventStartDate, DateTime eventEndDate) {
        return processDAO.getEntriesAssociatedToEventWorkstation( workstationId , eventStartDate, eventEndDate);
    }

    public Process findProcessCreate(long id){
        return processDAO.findById(id);
    }

    public Process findById( long id ) throws EntityDoesNotExistException
    {
        try{
            Process process = processDAO.findById( id );
            if (process != null){
                return process;
            }else{
                throw new EntityDoesNotExistException("Processo com o id '" + id + "' não encontrado.");
            }
        }catch(EntityDoesNotExistException e){
            throw e;
        }
    }

    public List<Process> findName(String name) {
        return processDAO.findName(name);
    }

    public Process getSubProcess(long id) throws Exception {
        return processDAO.findSubProcess(id);
    }

    public HashMap<String, List<Event>> sortEventsByMouldCode(List<Event> processEvents ) throws EntityDoesNotExistException {
        HashMap<String, List<Event>> eventsListHashMap = new HashMap<>();

        for( Event e : processEvents )
        {
            if(e.getMould() == null){
                throw new EntityDoesNotExistException( "Molde '" + e.getMould().getCode() + "' não encontrado." );
            }
            String mouldCode = e.getMould().getCode();

            List<Event> eventList = eventsListHashMap.computeIfAbsent(mouldCode, k -> new ArrayList<>());

            eventList.add( e );
        }

        return eventsListHashMap;
    }

    public HashMap<String, List<Event>> sortEventsByPartCode(List<Event> processEvents ) throws EntityDoesNotExistException {
        HashMap<String, List<Event>> eventsListHashMap = new HashMap<>();

        for( Event e : processEvents )
        {
            if(e.getPart() == null){
                throw new EntityDoesNotExistException( "Peça '" + e.getPart().getCode() + "' não encontrada." );
            }
            String partCode = e.getPart().getCode();

            List<Event> eventList = eventsListHashMap.computeIfAbsent(partCode, k -> new ArrayList<>());

            eventList.add( e );
        }

        return eventsListHashMap;
    }


    public HashMap<String, List<Event>> getEventsByMouldCodeFromFilter(long processId, FilterWrapper filter) throws MyException, EntityDoesNotExistException
    {
        List<Event> eventsProcess = null;

        if( filter.isMouldsFilter() )
        {
            // Get events of specified moulds
            eventsProcess = getEventsFromMouldsfromProcess( processId, filter.getMoulds() );
            if( eventsProcess.isEmpty() )
                throw new MyException( "Não foram encontrados eventos para os moldes especificados." );
        }else{
            // Get all events
            eventsProcess = getEventsByProcessID( processId );
            if( eventsProcess.isEmpty() )
                throw new MyException( "Não foram encontrados eventos para o processo '" + processId +"'." );
        }

        ArrayList<String> activities = filter.getActivities();
        ArrayList<String> resources = filter.getResources();
        long startDateMillis = filter.getStartDate() != null ? filter.getStartDate().getMillis() : 0;
        long endDateMillis = filter.getEndDate() != null ? filter.getEndDate().getMillis() : 0;


        // Events sorted HashMap
        HashMap<String, List<Event>> eventsByMouldCode = new HashMap<>();
        // Mould filter status
        HashMap<String, FilterWrapper.FilterStatus> mouldFilterStatus = new HashMap<>();

        // Sort by mould and save filter results at same time
        for( Event e : eventsProcess )
        {
            String mouldCode = e.getMould().getCode();

            List<Event> eventList = eventsByMouldCode.get( mouldCode );
            FilterWrapper.FilterStatus filterStatus = mouldFilterStatus.get( mouldCode );
            if( eventList == null )
            {
                eventList = new ArrayList<>();
                eventsByMouldCode.put( mouldCode, eventList );

                filterStatus = new FilterWrapper.FilterStatus();
                mouldFilterStatus.put( mouldCode, filterStatus );
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
                    e.getEndDate() == null ||
                    e.getEndDate().getMillis() > endDateMillis &&
                    filterStatus.endDateMatch )
            {
                filterStatus.endDateMatch = false;
            }

            // Just 1 event needs to have the activity or resource to pass
            // Activities
            if( filter.isActivitiesFilter() &&
                    activities.contains( e.getActivity().getName()) &&
                    ! filterStatus.hasActivity )
            {
                filterStatus.hasActivity = true;
            }

            // Resources
            List<ActivityUserEntry> entries = getEntriesAssociatedToEventActivity(e.getActivity().getId(), e.getStartDate(), e.getEndDate());
            for (ActivityUserEntry entry : entries) {
                if( filter.isResourcesFilter() &&
                        resources.contains( entry.getUser().getUsername()) &&
                        ! filterStatus.hasResource )
                {
                    filterStatus.hasResource = true;
                }
            }

            // All events of mould must not have estimated end (isEstimatedEnd = 0;)
            // Estimated End
            if( filter.isEstimatedEndFilter() &&
                    e.getIsEstimatedEnd() != null &&
                    e.getIsEstimatedEnd() &&
                    !filterStatus.hasEstimatedEnd )
            {
                filterStatus.hasEstimatedEnd = true;
            }

        }

        // Check all filter results and filter the moulds that doesnt match
        Iterator<Map.Entry<String, List<Event>>> iterator = eventsByMouldCode.entrySet().iterator();
        while ( iterator.hasNext() )
        {
            String mouldCode = iterator.next() .getKey();
            FilterWrapper.FilterStatus filterStatus = mouldFilterStatus.get( mouldCode );

            if( ! filterStatus.isConform( filter ) )
                iterator.remove();

        }

        if( eventsByMouldCode.isEmpty() )
            throw new MyException( "Não foram encontrados eventos que correspondam aos filtros." );

        return eventsByMouldCode;
    }


    public HashMap<String, List<Event>> getEventsByPartCodeFromFilter(long processId, FilterWrapper filter) throws MyException, EntityDoesNotExistException {
        List<Event> eventsProcess = null;

        if( filter.isPartsFilter() )
        {
            // Get events of specified parts
            eventsProcess = getEventsFromPartsfromProcess( processId, filter.getParts() );
            if( eventsProcess.isEmpty() )
                throw new MyException( "Não foram encontrados eventos para as peças especificadas." );
        }else{
            // Get all events
            eventsProcess = getEventsByProcessID( processId );
            if( eventsProcess.isEmpty() )
                throw new MyException("Não foram encontrados eventos para o processo '" + processId +"'." );
        }

        ArrayList<String> activities = filter.getActivities();
        ArrayList<String> resources = filter.getResources();
        long startDateMillis = filter.getStartDate() != null ? filter.getStartDate().getMillis() : 0;
        long endDateMillis = filter.getEndDate() != null ? filter.getEndDate().getMillis() : 0;


        // Events sorted HashMap
        HashMap<String, List<Event>> eventsByPartCode = new HashMap<>();
        // Mould filter status
        HashMap<String, FilterWrapper.FilterStatus> partFilterStatus = new HashMap<>();

        // Sort by mould and save filter results at same time
        for( Event e : eventsProcess )
        {
            String partCode = e.getPart().getCode();

            List<Event> eventList = eventsByPartCode.get( partCode );
            FilterWrapper.FilterStatus filterStatus = partFilterStatus.get( partCode );
            if( eventList == null )
            {
                eventList = new ArrayList<>();
                eventsByPartCode.put( partCode, eventList );

                filterStatus = new FilterWrapper.FilterStatus();
                partFilterStatus.put( partCode, filterStatus );
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
                    e.getEndDate() == null ||
                    e.getEndDate().getMillis() > endDateMillis &&
                            filterStatus.endDateMatch )
            {
                filterStatus.endDateMatch = false;
            }

            // Just 1 event needs to have the activity or resource to pass
            // Activities
            if( filter.isActivitiesFilter() &&
                    activities.contains( e.getActivity().getName() ) &&
                    ! filterStatus.hasActivity )
            {
                filterStatus.hasActivity = true;
            }

            // Resources
            List<ActivityUserEntry> entries = getEntriesAssociatedToEventActivity(e.getActivity().getId(), e.getStartDate(), e.getEndDate());
            for (ActivityUserEntry entry : entries) {
                if( filter.isResourcesFilter() &&
                        resources.contains( entry.getUser().getUsername()) &&
                        ! filterStatus.hasResource )
                {
                    filterStatus.hasResource = true;
                }
            }

            // All events of part must not have estimated end (isEstimatedEnd = 0)
            // Estimated End
            if( filter.isEstimatedEndFilter() &&
                    e.getIsEstimatedEnd() != null &&
                    e.getIsEstimatedEnd() &&
                    !filterStatus.hasEstimatedEnd )
            {
                filterStatus.hasEstimatedEnd = true;
            }

        }

        // Check all filter results and filter the moulds that doesnt match
        Iterator<Map.Entry<String, List<Event>>> iterator = eventsByPartCode.entrySet().iterator();
        while ( iterator.hasNext() )
        {
            String partCode = iterator.next().getKey();
            FilterWrapper.FilterStatus filterStatus = partFilterStatus.get( partCode );

            if( ! filterStatus.isConform( filter ) )
                iterator.remove();

        }

        if( eventsByPartCode.isEmpty() )
            throw new MyException( "Não foram encontrados eventos que correspondam aos filtros." );

        return eventsByPartCode;
    }

    public WorkflowNetworkDTO getWorkFlowNetworkFromProcess(long processId, ProcessMiningAlgorithm algorithm ) throws EntityDoesNotExistException
    {
        List<Event> processEvents = processDAO.getEventsByProcessID( processId );
        LinkedHashSet<String> eventsNames = new LinkedHashSet<>();

        HashMap<String, List<Event>> eventsListHashMap;

        /* Condição antiga. Agora não existe parts
        if(findById(processId).getSubProcess() != null){ //é processo de molde
            eventsListHashMap = sortEventsByMouldCodeExtractEventNames( processEvents, eventsNames );
        }else{ //subprocesso
            eventsListHashMap = sortEventsByPartCodeExtractEventNames( processEvents, eventsNames );
        }
         */

        //if(findById(processId).getSubProcess() == null)  //é sempre null. Nunca é subprocess
        eventsListHashMap = sortEventsByMouldCodeExtractEventNames(processEvents, eventsNames);


        return algorithm.discoverWorkflowNetwork(
                new ArrayList<>( eventsListHashMap.values() ), eventsNames );


    }

    public WorkflowNetworkPathsAndDeviationsDTO getWorkFlowNetworkFromProcess(long processId, InductiveMiner inductiveMiner ) throws EntityDoesNotExistException
    {
        List<Event> processEvents = processDAO.getEventsByProcessID( processId );
        LinkedHashSet<String> eventsNames = new LinkedHashSet<>();
        for (Event processEvent : processEvents) {
            eventsNames.add(processEvent.getActivity().getName());
        }
        List<List<Event>> listOfListOfevents = new LinkedList<>();
        listOfListOfevents.add(processEvents);
        return inductiveMiner.discoverWorkflowNetworkWithDeviations(processEvents, eventsNames );
    }


    public HashMap<String, List<Event>> sortEventsByMouldCodeExtractEventNames( List<Event> processEvents, Set<String> eventsNames )
    {
        HashMap<String, List<Event>> eventsListHashMap = new HashMap<>();
        eventsNames.clear();

        for( Event e : processEvents )
        {
            String mouldCode = e.getMould().getCode();
            List<Event> eventList = eventsListHashMap.computeIfAbsent( mouldCode, k -> new ArrayList<>() );

            eventList.add( e );
            eventsNames.add( e.getActivity().getName() );
        }

        return eventsListHashMap;
    }

    public HashMap<String, List<Event>> sortEventsByPartCodeExtractEventNames( List<Event> processEvents, Set<String> eventsNames )
    {
        HashMap<String, List<Event>> eventsListHashMap = new HashMap<>();
        eventsNames.clear();

        for( Event e : processEvents )
        {
            String partCode = e.getPart().getCode();
            List<Event> eventList = eventsListHashMap.computeIfAbsent( partCode, k -> new ArrayList<>() );

            eventList.add( e );
            eventsNames.add( e.getActivity().getName() );
        }

        return eventsListHashMap;
    }


    public int getNumberOfActivities(Process p) {
       return ((int) processDAO.getNumberOfActivities(p.getId()));
    }

    public int getNumberOfCases(Process p) {
        return ((int)processDAO.getNumberOfCases(p.getId()));
    }
}
