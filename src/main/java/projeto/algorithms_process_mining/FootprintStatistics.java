package projeto.algorithms_process_mining;


import lombok.Getter;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.util.Precision;
import org.processmining.plugins.directlyfollowsgraph.DirectlyFollowsGraph;
import projeto.algorithms_process_mining.inductive_miner.IvMHelper;
import projeto.api.dtos.DurationDTO;
import projeto.api.dtos.workflow_network.NodeStats;
import projeto.api.dtos.workflow_network.RelationMapDTO;
import projeto.api.dtos.workflow_network.StatisticsWorkflowNetworkDTO;
import projeto.core.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class FootprintStatistics
{
//    private List<Long>[][] transitionDurations;
//    private List<Long>[] taskDurations;
    @Getter
    private int[][] frequency;
    @Getter
    private DescriptiveStatistics[][] transitionDurations; //duracao da transição de cada atividade para outra atividade?
    @Getter
    private DescriptiveStatistics[] taskDurations; //duracao de cada atividade?

    private Map<String, Integer> eventNamesMapper;
    private final int numberOfEvents;

    private FootprintMatrix footprint;

    public FootprintStatistics(List<List<Event>> eventsListSet, Set<String> eventNames)
    {
        numberOfEvents = 0;
        throw new NotImplementedException("");
    }

    public FootprintStatistics( FootprintMatrix footprint )
    {
        this.footprint = footprint;
        this.eventNamesMapper = footprint.getEventNamesMapper();

        numberOfEvents = eventNamesMapper.size();
        // In the beginning there were no connections
        frequency = new int[numberOfEvents][numberOfEvents];
        transitionDurations = new DescriptiveStatistics[numberOfEvents][numberOfEvents];
        taskDurations = new DescriptiveStatistics[numberOfEvents];

        // Arrays.fill()
        for (int i = 0; i < numberOfEvents; i++)
        {
            for (int j = 0; j < numberOfEvents; j++)
            {
                transitionDurations[i][j] = new DescriptiveStatistics();
                frequency[i][j] = 0;
            }
            taskDurations[i] = new DescriptiveStatistics();

        }
        // WARNING !!!
        // Arrays.fill( taskDurations, new DescriptiveStatistics() );
    }
    //Deprecated?
    public void setFrequencyFromDfg(DirectlyFollowsGraph dfg){

        for (int i = 0; i < numberOfEvents; i++)
        {
            for (int j = 0; j < numberOfEvents; j++)
            {
                frequency[i][j] = (int)dfg.getDirectlyFollowsGraph().getEdgeWeight(i,j);
            }
        }
    }
    public  void setFrequencyFromIvMHelper(IvMHelper ivm){

        for (int i = 0; i < numberOfEvents; i++)
        {
            for (int j = 0; j < numberOfEvents; j++)
            {
                if(ivm.containsEdge(i,j)){
                    frequency[i][j] = (int)ivm.getEdgeCardinality(i,j);
                }
            }
        }
    }

    public void processFollowedEvents(Event currentEvent, Event nextEvent, int currentEventNum, int nextEventNum )
    {
        frequency[currentEventNum][nextEventNum]++;
        //frequency[nextEventNum][currentEventNum]++;
        //if( nextEventNum == 3 )
        //    System.out.println( nextEvent.getName() );

        long transitionDuration = nextEvent.getStartDate().getMillis() - currentEvent.getEndDate().getMillis();
        transitionDurations[currentEventNum][nextEventNum].addValue( transitionDuration );

        taskDurations[currentEventNum].addValue( currentEvent.getDuration() );

        /*
        DescriptiveStatistics taskDuration = taskDurations[currentEventNum];
        taskDuration.addValue( currentEventNum );

        StringBuilder sb = new StringBuilder();
        for (int c = 0; c < taskDuration.getN(); c++)
        {
            sb.append( " " ).append( taskDuration.getElement(c) );
        }

        System.out.println( currentEventNum +" -> " + sb.toString() );

        System.out.println();
        */
    }

    public void processLastEvent(Event currentEvent, int currentEventNum)
    {
        taskDurations[currentEventNum].addValue( currentEvent.getDuration() );
    }

    public StatisticsWorkflowNetworkDTO<NodeStats> getStatisticsNetwork(List<RelationMapDTO<Integer>> workflowNetworkRelations )
    {

        //Set<NodeStats> statsNodesList = new HashSet<>();
        //List<RelationMapDTO<NodeStats>> statsRelationsList = new ArrayList<>();

        StatisticsWorkflowNetworkDTO<NodeStats> statisticsNetwork = new StatisticsWorkflowNetworkDTO<>();
        List<NodeStats> statsNodesList = statisticsNetwork.getNodes();
        List<RelationMapDTO<NodeStats>> statsRelationsList = statisticsNetwork.getRelations();

        List<Integer> auxRelationsList = new ArrayList<>();
        List<NodeStats> auxNodeStatsList = new ArrayList<>();
        for (int i = 0; i < numberOfEvents; i++)
        {
            //auxRelationsList.clear();
            //auxNodeStatsList.clear();
            auxRelationsList = new ArrayList<>();
            auxNodeStatsList = new ArrayList<>();
            for (int j = 0; j < numberOfEvents; j++)
            {
                if( footprint.areEventsConnected( i, j ) )
                {
                    //System.out.println( i + " " + j + " : " + frequency[i][j] );

                    auxRelationsList.add( j );
                    DescriptiveStatistics durations = transitionDurations[i][j];
                    auxNodeStatsList.add( new NodeStats(
                            j,
                            frequency[i][j],
                            new DurationDTO((long) Precision.round( durations.getMean(), 0 )),
                            new DurationDTO((long) Precision.round( durations.getPercentile(50), 0 )),
                            new DurationDTO((long) Precision.round( durations.getMin(), 0 )),
                            new DurationDTO((long) Precision.round( durations.getMax(), 0 ))
                            )
                    );

                }
            }
            if( !auxRelationsList.isEmpty() )
            {
                workflowNetworkRelations.add( new RelationMapDTO<>( i,  auxRelationsList ) ); // new ArrayList<>()
                statsRelationsList.add( new RelationMapDTO<>( i, auxNodeStatsList ) ); // new ArrayList<>()

            }

            DescriptiveStatistics taskDurations = this.taskDurations[i];
            int taskFrequency = (int)taskDurations.getN();
            if( taskFrequency > 0 )
            {
                statsNodesList.add( new NodeStats(
                                i,
                                taskFrequency,
                                new DurationDTO((long) Precision.round( taskDurations.getMean(), 0 )),
                                new DurationDTO((long) Precision.round( taskDurations.getPercentile(50), 0 )),
                                new DurationDTO((long) Precision.round( taskDurations.getMin(), 0 )),
                                new DurationDTO((long) Precision.round( taskDurations.getMax(), 0 ))
                        )
                );
            }
            /*
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c < taskFrequency; c++)
            {
                sb.append( " " ).append( taskDurations.getElement(c) );
            }

            System.out.println( i +" -> " + sb.toString() );
            */
        }

        // return new StatisticsFlowNetworkDTO<>( statsNodesList, statsRelationsList );
        return statisticsNetwork;
    }

    public static void fillMeanDurations(FootprintStatistics footprintStatistics, long[] taskDurationsParam, long[][] transitionDurationsParam )
    {
        DescriptiveStatistics[][] transitionDurations = footprintStatistics.transitionDurations;
        DescriptiveStatistics[] taskDurations = footprintStatistics.taskDurations;
        int[][] frequency = footprintStatistics.frequency;
        int numberOfEvents = footprintStatistics.numberOfEvents;


        for (int i = 0; i < numberOfEvents; i++)
        {

            for (int j = 0; j < numberOfEvents; j++)
            {

                if( frequency[i][j] > 0 )
                {
                    DescriptiveStatistics transitionDurationsStats = transitionDurations[i][j];
                    transitionDurationsParam[i][j] = (long) Precision.round( transitionDurationsStats.getMean(), 0 );
                }
                else
                    transitionDurationsParam[i][j] = -1;
            }

            DescriptiveStatistics taskDurationsStats = taskDurations[i];
            int taskFrequency = (int) taskDurationsStats.getN();
            if (taskFrequency > 0) {
                taskDurationsParam[i] = (long) Precision.round(taskDurationsStats.getMean(), 0);
            }
            else
                taskDurationsParam[i] = -1;
        }

    }


/*
    public static void printMatrix( int[][] matrix )
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
