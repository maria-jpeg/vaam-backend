package projeto.algorithms_process_mining.heuristic_miner;

import lombok.Getter;
import projeto.algorithms_process_mining.FootprintMatrix;
import projeto.algorithms_process_mining.ProcessMiningAlgorithm;
import projeto.api.dtos.workflow_network.WorkflowNetworkDTO;
import projeto.core.Event;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;


public class HeuristicMiner implements ProcessMiningAlgorithm
{
    @Getter
    private float threshold;
    private final Logger LOGGER = Logger.getLogger(HeuristicMiner.class.getName());


    public HeuristicMiner(float threshold )
    {
        setThreshold( threshold );
    }

    public HeuristicMiner() { setDefaultThreshold(); }

    public WorkflowNetworkDTO discoverWorkflowNetwork(List<List<Event>> eventsListSet, LinkedHashSet<String> eventNamesList )
    {
        //System.out.println( "HeuristicMiner" );

        FootprintHeuristic footprint = new FootprintHeuristic(this, eventsListSet, eventNamesList, true );


        return new WorkflowNetworkDTO( footprint );
    }

    public FootprintMatrix getFootprintStatistics(List<List<Event>> eventsListSet, LinkedHashSet<String> eventNamesList )
    {
        return new FootprintHeuristic(this, eventsListSet, eventNamesList, true );
    }

    public FootprintMatrix getFootprint(List<List<Event>> eventsListSet, LinkedHashSet<String> eventNamesList )
    {
        return new FootprintHeuristic(this, eventsListSet, eventNamesList, false );
    }

    public float setThreshold(float thresholdParam )
    {
         if ( thresholdParam >= 0 && thresholdParam <= 1 )
             threshold = thresholdParam;
         else
             {
                 setDefaultThreshold();
                 LOGGER.warning( "invalid " + thresholdParam+ " threshold for HeuristicMiner" );
             }

        return threshold;
    }

    private void setDefaultThreshold()
    {
        threshold = 0.75F;
    }

    // private static void setDefaultThreshold() { threshold = 0.75F; }

    @Override
    public String getInfo()
    {
        return
                "Heuristic Miner\n" +
                "Threshold: " + threshold + "\n";
    }

}
