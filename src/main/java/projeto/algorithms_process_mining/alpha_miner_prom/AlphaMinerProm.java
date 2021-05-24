package projeto.algorithms_process_mining.alpha_miner_prom;

import projeto.algorithms_process_mining.FootprintMatrix;
import projeto.algorithms_process_mining.ProcessMiningAlgorithm;
import projeto.api.dtos.workflow_network.WorkflowNetworkDTO;
import projeto.core.Event;

import java.util.LinkedHashSet;
import java.util.List;

public class AlphaMinerProm implements ProcessMiningAlgorithm {

    public AlphaMinerProm(){

    }


    @Override
    public WorkflowNetworkDTO discoverWorkflowNetwork(List<List<Event>> eventsListSet, LinkedHashSet<String> eventNamesList) {
        FootprintAlphaProm footprintAlphaProm = new FootprintAlphaProm(this,eventsListSet.get(0),eventNamesList,true);
        return new WorkflowNetworkDTO(footprintAlphaProm);
    }

    @Override
    public FootprintMatrix getFootprintStatistics(List<List<Event>> eventsListSet, LinkedHashSet<String> eventNamesList) {
        return null;
    }

    @Override
    public FootprintMatrix getFootprint(List<List<Event>> eventsListSet, LinkedHashSet<String> eventNamesList) {
        return null;
    }

    @Override
    public String getInfo() {
        return "Alpha Miner ProM";
    }
}
