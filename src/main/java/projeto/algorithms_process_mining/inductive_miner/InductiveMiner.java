package projeto.algorithms_process_mining.inductive_miner;

import org.deckfour.xes.model.XLog;
import org.processmining.framework.packages.PackageManager;
import org.processmining.plugins.InductiveMiner.mining.Miner;
import org.processmining.plugins.InductiveMiner.mining.MiningParameters;
import org.processmining.plugins.InductiveMiner.mining.logs.IMLog;
import org.processmining.processtree.ProcessTree;
import projeto.algorithms_process_mining.FootprintMatrix;
import projeto.algorithms_process_mining.ProcessMiningAlgorithm;
import projeto.api.dtos.workflow_network.WorkflowNetworkDTO;
import projeto.core.Event;

import javax.validation.constraints.Min;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class InductiveMiner implements ProcessMiningAlgorithm {

    @Override
    public WorkflowNetworkDTO discoverWorkflowNetwork(List<List<Event>> eventsListSet, Set<String> eventNamesList) {
        return null;
    }

    @Override
    public FootprintMatrix getFootprintStatistics(List<List<Event>> eventsListSet, Set<String> eventNamesList) {
        return null;
    }

    @Override
    public FootprintMatrix getFootprint(List<List<Event>> eventsListSet, Set<String> eventNamesList) {
        return null;
    }

    @Override
    public String getInfo() {
        return null;
    }

    public ProcessTree miner(IMLog log){
       /*
        PackageManager.Canceller canceller = new PackageManager.Canceller() {
            @Override
            public boolean isCancelled() {
                return false;
            }
        };


        ProcessTree pt = Miner.mine(log, null, canceller);
        System.out.println(pt);
        */
        return null;
    }
}
