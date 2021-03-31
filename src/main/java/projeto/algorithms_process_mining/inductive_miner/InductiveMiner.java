package projeto.algorithms_process_mining.inductive_miner;

import org.deckfour.xes.model.XLog;
import org.processmining.plugins.InductiveMiner.plugins.IMProcessTree;
import org.processmining.processtree.ProcessTree;
import projeto.algorithms_process_mining.FootprintMatrix;
import projeto.algorithms_process_mining.ProcessMiningAlgorithm;
import projeto.api.dtos.workflow_network.WorkflowNetworkDTO;
import projeto.core.Event;
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
    /*
    public EfficientTree miner(XLog xLog){

        //https://svn.win.tue.nl/repos/prom/Packages/InductiveMiner/Trunk/src/org/processmining/plugins/inductiveminer2/plugins/InductiveMinerPlugin.java

        InductiveMinerDialog dialog = new InductiveMinerDialog(xLog);
        MiningParameters parameters = dialog.getMiningParameters();
        IMLog log = parameters.getIMLog(xLog);


        EfficientTree efficientTree = InductiveMinerPlugin.mineTree(log, parameters, new PackageManager.Canceller() {
            public boolean isCancelled() {
                return false;
            }
        });

        System.out.println(efficientTree);
        return efficientTree;
    }
     */

    public ProcessTree miner(XLog xLog){

        //https://svn.win.tue.nl/repos/prom/Packages/InductiveMinerDeprecated/Trunk/src/org/processmining/plugins/InductiveMiner/plugins/IM.java

        ProcessTree processTree = IMProcessTree.mineProcessTree(xLog);

        return processTree;
    }
}
