package projeto.algorithms_process_mining;

import lombok.Getter;
import projeto.core.Event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
public abstract class FootprintMatrix implements AlgorithmInfo
{
    protected ProcessMiningAlgorithm algorithm;
    protected Set<String> eventNames;
    protected Map<String, Integer> eventNamesMapper = new HashMap<>();
    protected HashMap<Integer, Integer> startEvents = new HashMap<>();
    protected HashMap<Integer, Integer> endEvents = new HashMap<>();

    protected boolean statistics;
    protected FootprintStatistics footprintStatistics;

    public FootprintMatrix(ProcessMiningAlgorithm algorithm, Set<String> eventNames, boolean statistics )
    {
        this.algorithm = algorithm;
        this.eventNames = eventNames;
        this.eventNamesMapper = buildEventsMapper( eventNames );

        this.statistics = statistics;
        if( statistics )
            footprintStatistics = new FootprintStatistics( this );

    }

    public abstract boolean areEventsConnected( int a, int b );

    public void extractStartEndEvents(List<Event> events)
    {
        if( events.isEmpty() )
            return;

        int startEventNum = eventNamesMapper.get( events.get(0).getActivity().getName());
        int endEventNum = eventNamesMapper.get( events.get(events.size() - 1).getActivity().getName() );

        incrementEvent( startEvents, startEventNum );
        incrementEvent( endEvents, endEventNum );

    }

    public String getInfo()
    {
        return algorithm.getInfo();
    }

    protected void incrementEvent( HashMap<Integer, Integer> eventsList, int event )
    {
        int count;
        count = eventsList.getOrDefault( event, 0 );
        eventsList.put( event, count + 1 );
    }


    public static Map<String, Integer> buildEventsMapper( Set<String> eventNames )
    {
        Map<String, Integer> eventNamesMapper = new HashMap<>();
        int index = 0;
        // assign each of the events an index in the matrix
        for (String s : eventNames) {

            //System.out.println( s + " " + index );
            eventNamesMapper.put(s, index++);
        }
        return eventNamesMapper;
    }

    /*
    public static Map<String, Integer> buildEventsMapper( List<String> eventNames )
    {
        Map<String, Integer> eventNamesMapper = new HashMap<>();
        int index = 0;
        // assign each of the events an index in the matrix
        for (String s : eventNames) {
            //System.out.println( s + " " + index );
            eventNamesMapper.put(s, index++);
        }
        return eventNamesMapper;
    }
    */


}
