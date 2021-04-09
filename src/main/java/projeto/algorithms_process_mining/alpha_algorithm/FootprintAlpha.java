package projeto.algorithms_process_mining.alpha_algorithm;

import lombok.Getter;
import projeto.algorithms_process_mining.FootprintMatrix;
import projeto.core.Event;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

///**
// * A wrapper for the footprint matrix of an event log. Relations between events
// * are represented by the {@code edu.fcse.alphaalgorithm.RelationType} enum. For
// * footprint[a][b] the result can be: # = events a and b are not connected > =
// * event a precedes b < = event a follows b | = a>b and a<b
// *
// * @author blagoj atanasovski
// */
public class FootprintAlpha extends FootprintMatrix
{
    @Getter
    protected RelationType[][] footprint;


    public FootprintAlpha(AlphaAlgorithm algorithm, List<List<Event>> eventsListSet, LinkedHashSet<String> eventNames, boolean statistics )
    {
        super( algorithm, eventNames, statistics );

        int numberOfEvents = eventNames.size();
        footprint = new RelationType[numberOfEvents][numberOfEvents];
        // In the beginning there were no connections
        for (int i = 0; i < numberOfEvents; i++)
        {
            Arrays.fill( footprint[i], RelationType.NOT_CONNECTED );
        }

        // And then God said, let there be a Trace
        for (int i = 0; i < eventsListSet.size(); i++ )
        {
            List<Event> eventsList = eventsListSet.get( i );
            extractStartEndEvents( eventsList );
            for (int j = 0; j < eventsList.size() - 1; j++)
            {
                // currentEventNumber is followed by nextEventNumber in some
                // trace

                Event currentEvent = eventsList.get( j );
                Event nextEvent = eventsList.get( j +1 );
                processFollowedEvents( currentEvent, nextEvent );

            }

            // Process last event for statistics
//            if( !statistics )
//                continue;

            processLastEvent( eventsList.get( eventsList.size() - 1) );


        }

        /*
        if (lookForLoopsOfLengthTwo) {
            for (Trace singleTrace : eventsListSet) {
                List<Event> eventsList = singleTrace.getEventsList();
                for (int i = 0; i < eventsList.size() - 2; i++) {
                    if (eventsList.get(i).equals(eventsList.get(i + 2))) {
                        int currentEventNumber = eventNamesMapper
                                .get(eventsList.get(i).getName());
                        int nextEventNumber = eventNamesMapper
                                .get(eventsList.get(i + 1).getName());
                        footprint[currentEventNumber][nextEventNumber] = RelationType.PRECEDES;
                        footprint[nextEventNumber][currentEventNumber] = RelationType.PRECEDES;
                    }
                }
            }
        }
        */
    }

    protected void processFollowedEvents(Event currentEvent, Event nextEvent )
    {
        int currentEventNum = eventNamesMapper
                .get( currentEvent.getActivity().getName() );
        int nextEventNum = eventNamesMapper
                .get( nextEvent.getActivity().getName() );

        // if this is the first time these two have been
        // found next to each other
        if ( footprint[currentEventNum][nextEventNum] == RelationType.NOT_CONNECTED )
        {
            footprint[currentEventNum][nextEventNum] = RelationType.PRECEDES;
            footprint[nextEventNum][currentEventNum] = RelationType.FOLLOWS;
        } else if ( footprint[currentEventNum][nextEventNum] == RelationType.FOLLOWS )
        {
            // if nextEventNumber was before currEventNumber
            // in some trace
            footprint[currentEventNum][nextEventNum] = RelationType.PARALLEL;
            footprint[nextEventNum][currentEventNum] = RelationType.PARALLEL;
        }

        // if some of the other relation types are at this position,
        // they are not changed

//        if( !statistics )
//            return;

        //todo
        footprintStatistics.processFollowedEvents( currentEvent, nextEvent,
                currentEventNum, nextEventNum);

    }

    protected void processLastEvent(Event lastEvent)
    {
        int lastEventNum = eventNamesMapper.get( lastEvent.getActivity().getName() );
        footprintStatistics.processLastEvent( lastEvent, lastEventNum );
    }


    public RelationType getRelationType(String firstEvent, String secondEvent) {
        int rowIndex = eventNamesMapper.get(firstEvent);
        int colIndex = eventNamesMapper.get(secondEvent);
        return footprint[rowIndex][colIndex];
    }

/*
    @Override
    public String toString() {
        StringBuilder toReturn = new StringBuilder("  ");
        String[] tmp = new String[footprint.length];
        for (String key : eventNamesMapper.keySet()) {
            tmp[eventNamesMapper.get(key)] = key;
        }

        for (int i = 0; i < footprint.length; i++) {
            toReturn.append(tmp[i]).append(' ');
        }

        toReturn.append('\n');
        for (int i = 0; i < footprint.length; i++) {
            toReturn.append(tmp[i]).append(' ');
            for (int j = 0; j < footprint.length; j++) {
                toReturn.append(footprint[i][j].symbol()).append(' ');
            }

            toReturn.append('\n');
        }

        return toReturn.toString();
    }
*/

    @Override
    public boolean areEventsConnected(int a, int b)
    {
        RelationType relation = footprint[a][b];

        return ( relation == RelationType.PRECEDES ) ||
                ( relation == RelationType.PARALLEL );

    }

}
