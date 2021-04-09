package projeto.algorithms_process_mining;

import projeto.api.dtos.workflow_network.WorkflowNetworkDTO;
import projeto.core.Event;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public interface ProcessMiningAlgorithm
{
    WorkflowNetworkDTO discoverWorkflowNetwork(List<List<Event>> eventsListSet, LinkedHashSet<String> eventNamesList );

    FootprintMatrix getFootprintStatistics(List<List<Event>> eventsListSet, LinkedHashSet<String> eventNamesList );

    FootprintMatrix getFootprint(List<List<Event>> eventsListSet, LinkedHashSet<String> eventNamesList);



    String getInfo();

}

