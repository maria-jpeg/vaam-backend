package projeto.algorithms_process_mining.inductive_miner;

import org.deckfour.xes.model.XLog;
import org.processmining.plugins.InductiveMiner.efficienttree.ProcessTree2EfficientTree;
import org.processmining.plugins.InductiveMiner.plugins.IMProcessTree;
import org.processmining.plugins.inductiveVisualMiner.helperClasses.IvMModel;
import org.processmining.processtree.ProcessTree;
import projeto.algorithms_process_mining.FootprintMatrix;
import projeto.algorithms_process_mining.ProcessMiningAlgorithm;
import projeto.api.dtos.workflow_network.WorkflowNetworkDTO;
import projeto.core.Event;
import java.util.List;
import java.util.Set;

public class InductiveMiner {

    public ProcessTree miner(XLog xLog){

        //https://svn.win.tue.nl/repos/prom/Packages/InductiveMinerDeprecated/Trunk/src/org/processmining/plugins/InductiveMiner/plugins/IM.java

        ProcessTree processTree = IMProcessTree.mineProcessTree(xLog);

        return processTree;
    }

    public static IvMModel minerEfficientTree(XLog xLog){

        //https://svn.win.tue.nl/repos/prom/Packages/InductiveMinerDeprecated/Trunk/src/org/processmining/plugins/InductiveMiner/plugins/IM.java

        ProcessTree processTree = IMProcessTree.mineProcessTree(xLog);
        IvMModel model = new IvMModel(ProcessTree2EfficientTree.convert(processTree));
        return model;
    }


}
