package projeto.algorithms_process_mining.alpha_algorithm;

import projeto.algorithms_process_mining.FootprintMatrix;
import projeto.algorithms_process_mining.ProcessMiningAlgorithm;
import projeto.api.dtos.workflow_network.WorkflowNetworkDTO;
import projeto.core.Event;

import java.util.List;
import java.util.Set;

/**
 * Creates a Workflow Net Model from an eventLog.
 *
 * @author blagoj atanasovski
 */
public class AlphaAlgorithm implements ProcessMiningAlgorithm
{

    public WorkflowNetworkDTO discoverWorkflowNetwork(List<List<Event>> eventsListSet, Set<String> eventNamesList )
    {
        // Generate footprint matrix from eventsLog
        FootprintAlpha footprint = new FootprintAlpha(this, eventsListSet, eventNamesList, true );

        return new WorkflowNetworkDTO( footprint );
    }

    public FootprintMatrix getFootprintStatistics(List<List<Event>> eventsListSet, Set<String> eventNamesList )
    {
        return new FootprintAlpha(this, eventsListSet, eventNamesList, true );
    }

    public FootprintMatrix getFootprint(List<List<Event>> eventsListSet, Set<String> eventNamesList)
    {
        return new FootprintAlpha(this, eventsListSet, eventNamesList, false );
    }

    public FootprintMatrix getCasesFootprint(List<List<Event>> eventsListSet, Set<String> eventNamesList )
    {
        return new FootprintCases(this, eventsListSet, eventNamesList, true );
    }


    public String getInfo()
    {
        return
                "Alpha Algorithm\n";
    }


}
