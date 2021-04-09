package projeto.algorithms_process_mining.heuristic_miner;


import lombok.Getter;
import projeto.algorithms_process_mining.FootprintMatrix;
import projeto.core.Event;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class FootprintHeuristic extends FootprintMatrix
{
    private HeuristicMiner heuristicMiner;

    @Getter
    private int[][] frequency;
    private int[][] frequencyLoopLength2;
    private float[][] heuristic;
    private float[][] heuristicLoopLength2;

    public FootprintHeuristic(HeuristicMiner algorithm, List<List<Event>> eventsListSet, LinkedHashSet<String> eventNames, boolean statistics)
    {
        super( algorithm, eventNames, statistics );
        this.heuristicMiner = algorithm;

        int numberOfEvents = eventNames.size();
        frequency = new int[numberOfEvents][numberOfEvents];
        frequencyLoopLength2 = new int[numberOfEvents][numberOfEvents];

        heuristic = new float[numberOfEvents][numberOfEvents];
        heuristicLoopLength2 = new float[numberOfEvents][numberOfEvents];
        for (int i = 0; i < numberOfEvents; i++)
        {
            Arrays.fill( heuristic[i], Float.MIN_VALUE );
            Arrays.fill( heuristicLoopLength2[i], Float.MIN_VALUE );
        }


        for (int i = 0; i < eventsListSet.size(); i++ )
        {
            List<Event> eventsList = eventsListSet.get( i );
            extractStartEndEvents( eventsList );
            int size = eventsList.size();
            for (int j = 0; j <  size - 2; j++)
            {

                Event currentEvent = eventsList.get(j);
                Event nextEvent = eventsList.get(j+1);
                Event secondNextEvent = eventsList.get(j+2);
                processFollowedEvents( currentEvent, nextEvent );

                if( currentEvent.getActivity().getName().equals( secondNextEvent.getActivity().getName() ) )
                    processLoopLength2( currentEvent, nextEvent );
            }

            // Second Last event
            if( size > 1 )
                processFollowedEvents( eventsList.get(size - 2), eventsList.get(size - 1) );

            // Last event
            if( !statistics )
                continue;

            processLastEvent( eventsList.get( size - 1 ) );
        }

        computeHeuristicValues();

//        printMatrix( heuristic );
//        System.out.println();
//        printMatrix( heuristicLoopLength2 );

    }

    protected void processFollowedEvents(Event currentEvent, Event nextEvent)
    {
        int currentEventNum = eventNamesMapper
                .get( currentEvent.getActivity().getName() );
        int nextEventNum = eventNamesMapper
                .get( nextEvent.getActivity().getName() );

        if( !statistics )
        {
            // Calculate frequency statistics
            frequency[currentEventNum][nextEventNum]++;
            return;
        }

        footprintStatistics.processFollowedEvents( currentEvent, nextEvent,
                currentEventNum, nextEventNum);

    }

    protected void processLastEvent(Event lastEvent)
    {
        int lastEventNum = eventNamesMapper.get( lastEvent.getActivity().getName() );
        footprintStatistics.processLastEvent( lastEvent, lastEventNum );
    }

    private void processLoopLength2(Event firstEvent, Event secondEvent)
    {
        int a = eventNamesMapper.get( firstEvent.getActivity().getName() );
        int b = eventNamesMapper.get( secondEvent.getActivity().getName() );

        frequencyLoopLength2[a][b]++;
    }

    private void computeHeuristicValues()
    {
        if( statistics )
            computeHeuristicValuesExternal();
        else
            computeHeuristicValuesInner();

    }

    private void computeHeuristicValues( int i, int j )
    {
        heuristic[i][j] = calculateHeuristic( frequency[i][j], frequency[j][i], i == j );
        heuristicLoopLength2[i][j] = calculateHeuristicLoopLength2( frequencyLoopLength2[i][j], frequencyLoopLength2[j][i] );
    }

    private void computeHeuristicValuesInner()
    {
        int size = frequency.length;

        for (int i = 0; i < size; i++ )
        {
            for (int j = 0; j < size; j++)
            {
                computeHeuristicValues( i, j );
            }

        }
    }

    private void computeHeuristicValuesExternal()
    {
        int[][] frequency = footprintStatistics.getFrequency();
        int size = frequency.length;

        for (int i = 0; i < size; i++ )
        {
            for (int j = 0; j < size; j++)
            {
                this.frequency[i][j] = frequency[i][j];
                computeHeuristicValues( i, j );
            }

        }
    }


    private float calculateHeuristic(int f1, int f2, boolean mainDiagonal )
    {
        // i = j
        if( mainDiagonal )
            return (float) f1 / ( f1 + 1 );

        // i ≠ j
        return (float)( f1 - f2 ) / ( f1 + f2 + 1 );

    }

    private float calculateHeuristicLoopLength2(int f1, int f2 )
    {
        return (float)( f1 + f2 ) / ( f1 + f2 + 1 );
    }

    @Override
    public boolean areEventsConnected(int a, int b)
    {
        float threshold = heuristicMiner.getThreshold();

        boolean res = heuristic[a][b] >= threshold;
        //return res;

        return res ?
                res :
                heuristicLoopLength2[a][b] >= threshold;

    }


    /*
    public static void printMatrix( float[][] matrix )
    {
        StringBuilder toReturn = new StringBuilder("  ");
        for (int i = 0; i < matrix.length; i++) {
            toReturn.append( i ).append(' ');
        }

        toReturn.append('\n');
        for (int i = 0; i < matrix.length; i++) {
            toReturn.append( i ).append(' ');
            for (int j = 0; j < matrix.length; j++) {
                toReturn.append( matrix[i][j] ).append(' ');
            }

            toReturn.append('\n');
        }

        System.out.println( toReturn.toString() );
    }
    */
}
