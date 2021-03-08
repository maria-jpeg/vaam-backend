package projeto.algorithms_process_mining.alpha_algorithm;

import projeto.core.Event;

import java.util.List;
import java.util.Set;

/**
 *   Safe version of footprint able to end with events that are not on the eventNamesMapper
 */
public class FootprintCases extends FootprintAlpha
{

    public FootprintCases(AlphaAlgorithm algorithm, List<List<Event>> eventsListSet, Set<String> eventNames, boolean statistics) {
        super(algorithm, eventsListSet, eventNames, statistics);
    }


    @Override
    protected void processFollowedEvents(Event currentEvent, Event nextEvent)
    {
        Integer currentEventNum = eventNamesMapper
                .get( currentEvent.getActivity().getName() );
        Integer nextEventNum = eventNamesMapper
                .get( nextEvent.getActivity().getName() );

        // Safe
        if( currentEventNum == null || nextEventNum == null )
            return;

        /*
        if ( footprint[currentEventNum][nextEventNum] == RelationType.NOT_CONNECTED )
        {
            footprint[currentEventNum][nextEventNum] = RelationType.PRECEDES;
            footprint[nextEventNum][currentEventNum] = RelationType.FOLLOWS;
        } else if ( footprint[currentEventNum][nextEventNum] == RelationType.FOLLOWS )
        {

            footprint[currentEventNum][nextEventNum] = RelationType.PARALLEL;
            footprint[nextEventNum][currentEventNum] = RelationType.PARALLEL;
        }
        */

        footprintStatistics.processFollowedEvents( currentEvent, nextEvent,
                currentEventNum, nextEventNum);
    }

    @Override
    protected void processLastEvent(Event lastEvent)
    {
        Integer lastEventNum = eventNamesMapper.get( lastEvent.getActivity().getName());
        if( lastEventNum != null )
            footprintStatistics.processLastEvent( lastEvent, lastEventNum );
    }

    @Override
    public void extractStartEndEvents(List<Event> events)
    {
        if( events.isEmpty() )
            return;

        Integer startEventNum = eventNamesMapper.get( events.get(0).getActivity().getName());
        Integer endEventNum = eventNamesMapper.get( events.get(events.size() - 1).getActivity().getName() );

        if( startEventNum != null )
            incrementEvent( startEvents, startEventNum );
        if( endEventNum != null )
            incrementEvent( endEvents, endEventNum );


        // System.out.println( events.get(0).getName() + " --- " + startEventNum );
        // System.out.println( events.get(events.size() - 1).getName() + " --- " + endEventNum );

    }

    @Override
    public String getInfo() {
        return "Alpha Algorithm version optimized for moulds/parts instead of full process.\n";
    }
}
