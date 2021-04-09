package projeto.controller;

import lombok.Getter;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.util.Precision;
import projeto.algorithms_process_mining.FootprintMatrix;
import projeto.algorithms_process_mining.ProcessMiningAlgorithm;
import projeto.algorithms_process_mining.alpha_algorithm.AlphaAlgorithm;
import projeto.algorithms_process_mining.heuristic_miner.FootprintHeuristic;
import projeto.algorithms_process_mining.heuristic_miner.HeuristicMiner;
import projeto.api.dtos.DurationDTO;
import projeto.api.dtos.NodeDurationDTO;
import projeto.api.dtos.NodeFrequencyDTO;
import projeto.api.dtos.conformance.CasePerformanceDTO;
import projeto.api.dtos.conformance.deviations.ConformanceNetworkDeviationsDTO;
import projeto.api.dtos.conformance.deviations.NodeRelationDeviationsMap;
import projeto.api.dtos.conformance.performance.ConformanceNetworkPerformanceDTO;
import projeto.api.dtos.conformance.performance.DeviationFlow;
import projeto.api.dtos.workflow_network.RelationMapDTO;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.controller.exceptions.MyException;
import projeto.core.Event;
import projeto.core.Relation;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class ConformanceBean
{
    private final LogBean logBean;
    private final ProcessBean processBean;
    private final PartBean partBean;

    public ConformanceBean(LogBean logBean, ProcessBean processBean, PartBean partBean) {
        this.logBean = logBean;
        this.processBean = processBean;
        this.partBean = partBean;
    }

    /** Performance ( activities time )
     */
    /*public ConformanceNetworkPerformanceDTO getNetworkModelPerformance(long logID, long referenceModelLogID, ProcessMiningAlgorithm algorithm) throws EntityDoesNotExistException
    {
        // Extract events from logs
        List<Event> referenceEventLogs = logBean.getEventsByLogID( referenceModelLogID );
        Set<String> eventNames = new HashSet<>();

        // Sort events by case id, and extract the events names
        HashMap<Long, List<Event>> referenceEventsSortedHashMap = logBean.sortEventsByCaseIDExtractEventNames( referenceEventLogs, eventNames );

        // Get Workflow Matrix
        FootprintMatrix footprint = algorithm.getFootprintStatistics( new ArrayList<>( referenceEventsSortedHashMap.values() ), eventNames );

        // Fill taskDurations and taskTransactions
        List<NodeDurationDTO> taskDurations = new ArrayList<>();
        List< RelationMapDTO<NodeDurationDTO> > transactionDurations = new ArrayList<>();

        fillMeanDurations( footprint, taskDurations, transactionDurations );

        // Find Outliers
        DeviationFlow deviations = new DeviationFlow();
        if( algorithm instanceof HeuristicMiner )
            deviations = findOutliers( (FootprintHeuristic)footprint );

        List<Long> cases = logBean.getCaseIDsfromLogID( logID );

        return new ConformanceNetworkPerformanceDTO( footprint, taskDurations, transactionDurations, deviations, cases );

    }*/

    /*public ConformanceNetworkPerformanceDTO getNetworkModelPerformanceProcess(long processId, long referenceModelProcessId, ProcessMiningAlgorithm algorithm) throws Exception
    {
        if(processBean.findById(processId).getSubProcess() != null && processBean.findById(referenceModelProcessId).getSubProcess() == null ||
                processBean.findById(processId).getSubProcess() == null && processBean.findById(referenceModelProcessId).getSubProcess() != null){
            throw new Exception("Processes must be of the same family type (mould/part)");
        }

        // Extract events from logs
        List<Event> referenceEventLogs = processBean.getEventsByProcessID( referenceModelProcessId );
        Set<String> eventNames = new HashSet<>();

        // Sort events by case id, and extract the events names
        HashMap<String, List<Event>> referenceEventsSortedHashMap;

        if(processBean.findById(processId).getSubProcess() != null){ //é processo de molde
            referenceEventsSortedHashMap = processBean.sortEventsByMouldCodeExtractEventNames( referenceEventLogs, eventNames );
        }else{ //subprocesso
            referenceEventsSortedHashMap = processBean.sortEventsByPartCodeExtractEventNames( referenceEventLogs, eventNames );
        }

        // Get Workflow Matrix
        FootprintMatrix footprint = algorithm.getFootprintStatistics( new ArrayList<>( referenceEventsSortedHashMap.values() ), eventNames );

        // Fill taskDurations and taskTransactions
        List<NodeDurationDTO> taskDurations = new ArrayList<>();
        List< RelationMapDTO<NodeDurationDTO> > transactionDurations = new ArrayList<>();

        fillMeanDurations( footprint, taskDurations, transactionDurations );

        // Find Outliers
        DeviationFlow deviations = new DeviationFlow();
        if( algorithm instanceof HeuristicMiner )
            deviations = findOutliers( (FootprintHeuristic)footprint );

        if(processBean.findById(processId).getSubProcess() != null){ //é processo de molde
            List<String> moulds = processBean.getMouldCodesfromProcessId( processId );
            return new ConformanceNetworkPerformanceDTO( footprint, taskDurations, transactionDurations, deviations, moulds,false );
        }else{ //subprocesso
            List<String> parts = processBean.getPartCodesfromProcessId( processId );
            return new ConformanceNetworkPerformanceDTO( footprint, taskDurations, transactionDurations, deviations, parts, true );
        }

    }*/

    public ConformanceNetworkPerformanceDTO getNetworkModelPerformanceProcess(long processId, HashMap<String, List<Event>> referenceEventsSortedHashMap, ProcessMiningAlgorithm algorithm) throws EntityDoesNotExistException
    {
        // Extract events from logs
//        List<Event> referenceEventLogs = processBean.getEventsByProcessID( referenceModelProcessId );
        LinkedHashSet<String> eventNames = new LinkedHashSet<>();

        //Extract Event Names
        if(processBean.findById(processId).getSubProcess() != null){ //é processo de molde
            List<Event> eventsMoulds = processBean.getEventsFromMouldsfromProcessSet(processId, referenceEventsSortedHashMap.keySet());
            eventNames.clear();
            for( Event e : eventsMoulds )
            {
                eventNames.add( e.getActivity().getName() );
            }
        }else{ //subprocesso
            List<Event> eventsParts = processBean.getEventsFromPartsfromProcessSet(processId, referenceEventsSortedHashMap.keySet());
            eventNames.clear();
            for( Event e : eventsParts )
            {
                eventNames.add( e.getActivity().getName() );
            }
        }
//
        // Get Workflow Matrix
        FootprintMatrix footprint = algorithm.getFootprintStatistics( new ArrayList<>( referenceEventsSortedHashMap.values() ), eventNames );

        // Fill taskDurations and taskTransactions
        List<NodeDurationDTO> taskDurations = new ArrayList<>();
        List< RelationMapDTO<NodeDurationDTO> > transactionDurations = new ArrayList<>();

        fillMeanDurations( footprint, taskDurations, transactionDurations );

        // Find Outliers
        DeviationFlow deviations = new DeviationFlow();
        if( algorithm instanceof HeuristicMiner )
            deviations = findOutliers( (FootprintHeuristic)footprint );

        if(processBean.findById(processId).getSubProcess() != null){ //é processo de molde
            return new ConformanceNetworkPerformanceDTO( footprint, taskDurations, transactionDurations, deviations, referenceEventsSortedHashMap.keySet(),false, partBean );
        }else{ //subprocesso
            //List<String> parts = processBean.getPartCodesfromProcessId( processId );
            return new ConformanceNetworkPerformanceDTO( footprint, taskDurations, transactionDurations, deviations, referenceEventsSortedHashMap.keySet(), true, partBean );
        }

    }

    private DeviationFlow findOutliers(FootprintHeuristic footprint )
    {
        int[][] frequencies = footprint.getFrequency();
        int numberOfEvents = frequencies.length;

        // Find deviations
        List<Relation> outliersRelations = new ArrayList<>();
        List<Integer> outliersNodes = new ArrayList<>();
        for (int i = 0; i < numberOfEvents; i++)
        {
            AtomicInteger outliers = new AtomicInteger( 0 );
            AtomicInteger conform = new AtomicInteger( 0 );

            incrementOutliers( footprint, i, i, conform, outliers, outliersRelations );

            for (int j = 0; j < numberOfEvents; j++)
            {
                if( i == j )
                    continue;
                // Rows
                incrementOutliers( footprint, i, j, conform, outliers, outliersRelations );

                // Columns
                incrementOutliers( footprint, j, i, conform, outliers, outliersRelations );

            }
            //System.out.println( i + " -> " + conform.get() + " " + outliers.get()  );

            if( conform.get() == 0 && outliers.get() > 0 )
                outliersNodes.add( i );

        }

        return new DeviationFlow( outliersNodes, outliersRelations );
    }

    private void incrementOutliers(FootprintHeuristic footprint, int i, int j, AtomicInteger conformCount, AtomicInteger outliersCount, List<Relation> outliersRelations )
    {
        int frequency = footprint.getFrequency()[i][j];
        boolean connected = footprint.areEventsConnected( i, j );

        if( frequency > 0 )
        {
            if( !connected )
            {
                outliersRelations.add( new Relation(i, j) );
                outliersCount.incrementAndGet();
            }
            else
                conformCount.incrementAndGet();
        }
    }


    /*
    private List<Relation> findOutliers( FootprintHeuristic footprint  )
    {
        int[][] frequencies = footprint.getFrequency();
        int numberOfEvents = frequencies.length;

        // Find deviations
        List<Relation> outliers = new ArrayList<>();
        for (int i = 0; i < numberOfEvents; i++)
        {
            for (int j = 0; j < numberOfEvents; j++)
            {
                if( frequencies[i][j] > 0 && !footprint.areEventsConnected(i, j) )
                    outliers.add( new Relation(i, j) );

            }

        }

        return outliers;
    }
    */


    /** Get events information
     *  By case(s) or date range
     */
    /*public CasePerformanceDTO getEventsPerformance( HashMap<Long, List<Event>> eventsByCaseID , Set<String> eventNames )
    {

        if( eventsByCaseID.size() == 0 )
            return null;


        //Map<String, Integer> eventNamesMapper = FootprintMatrix.buildEventsMapper( eventNames );
        //List<Event> events = logBean.getEventsFromCasesfromLog(logId, cases);

        //if( events.isEmpty() )
            //return null;

        // Sort events by case id
        //HashMap<Long, List<Event>> eventsByCaseID = logBean.sortEventsByCaseID( events );


        // Build a footprint for the selected cases!
        FootprintMatrix footprintCases = new AlphaAlgorithm().getCasesFootprint( new ArrayList<>( eventsByCaseID.values() ), eventNames );

        // Fill taskDurations and taskTransactions
        List<NodeDurationDTO> taskDurations = new ArrayList<>();
        List< RelationMapDTO<NodeDurationDTO> > transactionDurations = new ArrayList<>();

        fillMeanDurations( footprintCases, taskDurations, transactionDurations );

        return new CasePerformanceDTO( footprintCases, taskDurations, transactionDurations, eventsByCaseID.keySet() );

    }*/

    /** Get events information
     *  By mould(s) or date range
     */
    public CasePerformanceDTO getEventsByMouldPerformance( HashMap<String, List<Event>> eventsByMouldCode , LinkedHashSet<String> eventNames )
    {

        if( eventsByMouldCode.size() == 0 )
            return null;

        // Build a footprint for the selected moulds!
        FootprintMatrix footprintCases = new AlphaAlgorithm().getCasesFootprint( new ArrayList<>( eventsByMouldCode.values() ), eventNames );

        // Fill taskDurations and taskTransactions
        List<NodeDurationDTO> taskDurations = new ArrayList<>();
        List< RelationMapDTO<NodeDurationDTO> > transactionDurations = new ArrayList<>();

        fillMeanDurations( footprintCases, taskDurations, transactionDurations );

        return new CasePerformanceDTO( footprintCases, taskDurations, transactionDurations, eventsByMouldCode.keySet(), false, partBean );

    }

    public CasePerformanceDTO getEventsByPartPerformance( HashMap<String, List<Event>> eventsByPartCode , LinkedHashSet<String> eventNames )
    {

        if( eventsByPartCode.size() == 0 )
            return null;

        // Build a footprint for the selected parts!
        FootprintMatrix footprintCases = new AlphaAlgorithm().getCasesFootprint( new ArrayList<>( eventsByPartCode.values() ), eventNames );

        // Fill taskDurations and taskTransactions
        List<NodeDurationDTO> taskDurations = new ArrayList<>();
        List< RelationMapDTO<NodeDurationDTO> > transactionDurations = new ArrayList<>();

        fillMeanDurations( footprintCases, taskDurations, transactionDurations );

        return new CasePerformanceDTO( footprintCases, taskDurations, transactionDurations, eventsByPartCode.keySet(), true, partBean );

    }


    private void fillMeanDurations(FootprintMatrix footprint,
                                  List<NodeDurationDTO> nodes, List< RelationMapDTO<NodeDurationDTO> > relations )
    {
        DescriptiveStatistics[] taskDurations = footprint.getFootprintStatistics().getTaskDurations();
        DescriptiveStatistics[][] transitionDurations = footprint.getFootprintStatistics().getTransitionDurations();

        long durationAux;
        List<NodeDurationDTO> nodeTransactionDurations;
        DescriptiveStatistics durationStats;

        int size = taskDurations.length;

        for (int i = 0; i < size; i++)
        {
            nodeTransactionDurations = new ArrayList<>();

            for (int j = 0; j < size; j++)
            {
                // Transaction taskDurations
                durationStats = transitionDurations[i][j];
                if( durationStats.getN() == 0 )
                    continue;

                durationAux = (long) Precision.round( durationStats.getMean(), 0 );
                nodeTransactionDurations.add( new NodeDurationDTO( j, new DurationDTO(durationAux) ) );

            }

            // Transactions
            if( nodeTransactionDurations.size() > 0 )
                relations.add( new RelationMapDTO<>( i, nodeTransactionDurations ) );

            // Task duration
            durationStats = taskDurations[i];
            if( durationStats.getN() == 0 )
                continue;

            durationAux = (long) Precision.round( durationStats.getMean(), 0 );
            nodes.add( new NodeDurationDTO( i, new DurationDTO(durationAux) ) );
        }

    }


    /** Deviations
    */
    /*public ConformanceNetworkDeviationsDTO getNotConformancesNetworkDeviations(long logID, long referenceModelLogID ) throws EntityDoesNotExistException
    {
        // Extract events from logs
        List<Event> eventLogs = logBean.getEventsByLogID( logID );
        List<Event> referenceEventLogs = logBean.getEventsByLogID( referenceModelLogID );
        Set<String> eventNames = new HashSet<>();
        Set<String> referenceEventNames = new HashSet<>();

        // Sort events by case id, and extract the events names
        HashMap<Long, List<Event>> eventsSortedHashMap = logBean.sortEventsByCaseIDExtractEventNames( eventLogs, eventNames );
        HashMap<Long, List<Event>> referenceEventsSortedHashMap = logBean.sortEventsByCaseIDExtractEventNames( referenceEventLogs, referenceEventNames );

        // Get workflow network model
        HeuristicMiner algorithm = new HeuristicMiner( 0.925F );
        FootprintMatrix footprint = algorithm.getFootprintStatistics( new ArrayList<>( eventsSortedHashMap.values() ), eventNames );
        FootprintMatrix footprintReferenceModel = algorithm.getFootprint( new ArrayList<>( referenceEventsSortedHashMap.values() ), referenceEventNames );

        // Check deviations
        return checkConformanceDeviations( footprint, footprintReferenceModel );
    }

    private ConformanceNetworkDeviationsDTO checkConformanceDeviations( FootprintMatrix footprint, FootprintMatrix footprintReferenceModel )
    {
        int[][] frequencies = footprint.getFootprintStatistics().getFrequency();
        Set<String> eventNames = footprint.getEventNames();
        int numEvents = eventNames.size();

        Map<Integer, Integer> eventIdsMapper = buildEventIdsMapper( footprint, footprintReferenceModel );

        List<Relation> deviations = new ArrayList<>();
        List<NodeRelationDeviationsMap> relations = new ArrayList<>();
        for (int i = 0; i < numEvents; i++)
        {
            int totalFrequencyEvent = 0;
            List<NodeFrequencyDTO> stats = new ArrayList<>();
            for (int j = 0; j < numEvents; j++)
            {
                Integer iMapped = eventIdsMapper.get( i );
                Integer jMapped = eventIdsMapper.get( j );

                if( iMapped == null || jMapped == null )
                    continue;

                int frequency = frequencies[i][j];
                totalFrequencyEvent += frequency;

                if( frequency > 0 )
                {
                    stats.add( new NodeFrequencyDTO( j, frequency ) );

                    boolean connected = footprintReferenceModel.areEventsConnected(iMapped, jMapped);
                    if( !connected )
                        deviations.add( new Relation(i, j) );

//                    if( connected )
//                        System.out.println( i + " " + j + "  frequency: " + frequency );
//                    else
//                        System.out.println( i + " " + j + "  frequency: " + frequency + "  Deviation! " );
                }

            }
//            System.out.println( "Node " + i + ": " + totalFrequencyEvent );
//            System.out.println();
            relations.add( new NodeRelationDeviationsMap( i, totalFrequencyEvent, stats ) );
        }

        return new ConformanceNetworkDeviationsDTO( footprint, relations, deviations );

    }

    private Map<Integer, Integer> buildEventIdsMapper( FootprintMatrix footprint, FootprintMatrix footprintReferenceModel )
    {
        Map<Integer, Integer> eventIdsMapper = new HashMap<>();

        Map<String, Integer> eventNamesMapper = footprint.getEventNamesMapper();
        Map<String, Integer> eventNamesMapperReferenceModel = footprintReferenceModel.getEventNamesMapper();

        for ( Map.Entry<String, Integer> item : eventNamesMapper.entrySet() )
        {
            int id = item.getValue();
            Integer idReferenceModel = eventNamesMapperReferenceModel.get( item.getKey() );
            if( idReferenceModel == null )
                continue;

            eventIdsMapper.put( id, idReferenceModel );
        }

        return eventIdsMapper;
    }*/


    /** Deviations of Process
     */
    public ConformanceNetworkDeviationsDTO getNotConformancesNetworkDeviationsProcess(long processId, long referenceModelProcessId ) throws EntityDoesNotExistException, MyException {
        if(processBean.findById(processId).getSubProcess() != null && processBean.findById(referenceModelProcessId).getSubProcess() == null ||
                processBean.findById(processId).getSubProcess() == null && processBean.findById(referenceModelProcessId).getSubProcess() != null){
            throw new MyException("Os processos a comparar devem ser da mesma família: processos de molde OU subprocessos de peças");
        }

        // Extract events from processes
        List<Event> eventsProcess = processBean.getEventsByProcessID(processId);
        List<Event> referenceEventsProcess = processBean.getEventsByProcessID(referenceModelProcessId);
        LinkedHashSet<String> eventNames = new LinkedHashSet<>();
        LinkedHashSet<String> referenceEventNames = new LinkedHashSet<>();

        HashMap<String, List<Event>> eventsSortedHashMap;
        HashMap<String, List<Event>> referenceEventsSortedHashMap;

        // Sort events by mould/part code, and extract the events names
        if(processBean.findById(processId).getSubProcess() != null){ //é processo de molde
            eventsSortedHashMap = processBean.sortEventsByMouldCodeExtractEventNames( eventsProcess, eventNames );
            referenceEventsSortedHashMap = processBean.sortEventsByMouldCodeExtractEventNames( referenceEventsProcess, referenceEventNames );
        }else{ //subprocesso
            eventsSortedHashMap = processBean.sortEventsByPartCodeExtractEventNames( eventsProcess, eventNames );
            referenceEventsSortedHashMap = processBean.sortEventsByPartCodeExtractEventNames( referenceEventsProcess, referenceEventNames );
        }

        // Get workflow network model
        HeuristicMiner algorithm = new HeuristicMiner( 0.1F ); //VOU ALTERAR ESTE THRESHOLD, valor antigo: 0.925F
        FootprintMatrix footprint = algorithm.getFootprintStatistics( new ArrayList<>( eventsSortedHashMap.values() ), eventNames );
        FootprintMatrix footprintReferenceModel = algorithm.getFootprint( new ArrayList<>( referenceEventsSortedHashMap.values() ), referenceEventNames );

        // Check deviations
        return checkConformanceDeviations( footprint, footprintReferenceModel );
    }

    private ConformanceNetworkDeviationsDTO checkConformanceDeviations( FootprintMatrix footprint, FootprintMatrix footprintReferenceModel )
    {
        int[][] frequencies = footprint.getFootprintStatistics().getFrequency();
        Set<String> eventNames = footprint.getEventNames();
        int numEvents = eventNames.size();

        Map<Integer, Integer> eventIdsMapper = buildEventIdsMapper( footprint, footprintReferenceModel ); //é um map que faz a correspondência entre posição/id da atividade no processo normal e a posição/id no processo modelo

        List<Relation> deviations = new ArrayList<>();
        List<NodeRelationDeviationsMap> relations = new ArrayList<>();
        for (int i = 0; i < numEvents; i++)
        {
            int totalFrequencyEvent = 0;
            List<NodeFrequencyDTO> stats = new ArrayList<>();
            for (int j = 0; j < numEvents; j++)
            {
                Integer iMapped = eventIdsMapper.get( i ); //posição da atividade no modelo
                Integer jMapped = eventIdsMapper.get( j ); //posição da atividade no modelo

                if( iMapped == null || jMapped == null ) //se o modelo não tiver alguma das atividades
                    continue;

                int frequency = frequencies[i][j]; //frequência entre as atividades no processo normal
                totalFrequencyEvent += frequency;

                if( frequency > 0 )
                {
                    stats.add( new NodeFrequencyDTO( j, frequency ) );

                    boolean connected = footprintReferenceModel.areEventsConnected(iMapped, jMapped); //vamos ver se as atividades i e j estão relacionadas no modelo
                    if( !connected ) //se não existir relação entre elas no modelo
                        deviations.add( new Relation(i, j) ); //então é um desvio

//                    if( connected )
//                        System.out.println( i + " " + j + "  frequency: " + frequency );
//                    else
//                        System.out.println( i + " " + j + "  frequency: " + frequency + "  Deviation! " );
                }

            }
//            System.out.println( "Node " + i + ": " + totalFrequencyEvent );
//            System.out.println();
            relations.add( new NodeRelationDeviationsMap( i, totalFrequencyEvent, stats ) ); //aqui mandamos a atividade i (from), que vai para 1 ou + atividades j (to) que estão nas stats, e totalFrequencyEvent é o nr de vezes que se vai de i para uma das j's
        }

        return new ConformanceNetworkDeviationsDTO( footprint, relations, deviations );

    }

    private Map<Integer, Integer> buildEventIdsMapper( FootprintMatrix footprint, FootprintMatrix footprintReferenceModel )
    {
        Map<Integer, Integer> eventIdsMapper = new HashMap<>();

        Map<String, Integer> eventNamesMapper = footprint.getEventNamesMapper();
        Map<String, Integer> eventNamesMapperReferenceModel = footprintReferenceModel.getEventNamesMapper();

        for ( Map.Entry<String, Integer> item : eventNamesMapper.entrySet() )
        {
            int id = item.getValue();
            Integer idReferenceModel = eventNamesMapperReferenceModel.get( item.getKey() );
            if( idReferenceModel == null )
                continue;

            eventIdsMapper.put( id, idReferenceModel );
        }

        return eventIdsMapper;
    }


    /*

     private ConformanceModel toConformanceModel(List<Event> eventLogs, Set<String> eventNames )
    {
        ConformanceModel conformanceModel = new ConformanceModel( eventNames );
        long[] taskDurations = conformanceModel.getTaskDurations();
        long[][] transitionDurations = conformanceModel.getTransitionDurations();
        LinkedList<Integer> path = conformanceModel.getPath();
        Map<String, Integer> eventNamesMapper = conformanceModel.getEventNamesMapper();

        int size = eventLogs.size();
        for (int i = 0; i < size -1; i++)
        {
            Event e = eventLogs.get(i);
            int currentEventNum = eventNamesMapper.get(e.getName());
            int nextEventNum = eventNamesMapper.get(e.getName());
            long transitionDuration = e.getStart().getMillis() - e.getEnd().getMillis();
            long eventDuration = e.getDuration();

            transitionDurations[currentEventNum][nextEventNum] = transitionDuration;
            taskDurations[currentEventNum] = eventDuration;
            path.add(currentEventNum);

        }

        Event lastEvent = eventLogs.get( size -1 );
        int currentEventNum = eventNamesMapper.get( lastEvent.getName() );
        long eventDuration = lastEvent.getDuration();
        taskDurations[currentEventNum] = eventDuration;
        path.add(currentEventNum);

        return conformanceModel;
    }

    */


}
