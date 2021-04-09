package projeto.algorithms_process_mining.inductive_miner;

import lombok.Getter;
import org.deckfour.xes.model.XLog;
import org.processmining.plugins.InductiveMiner.efficienttree.ProcessTree2EfficientTree;
import org.processmining.plugins.InductiveMiner.plugins.IMProcessTree;
import org.processmining.plugins.directlyfollowsgraph.DirectlyFollowsGraph;
import org.processmining.plugins.directlyfollowsgraph.mining.DFMMiner;
import org.processmining.plugins.directlyfollowsgraph.mining.variants.DFMMiningParametersDefault;
import org.processmining.plugins.inductiveVisualMiner.helperClasses.IvMModel;
import org.processmining.plugins.inductiveminer2.helperclasses.MultiIntSet;
import org.processmining.processtree.ProcessTree;
import projeto.algorithms_process_mining.FootprintMatrix;
import projeto.algorithms_process_mining.ProcessMiningAlgorithm;
import projeto.api.dtos.workflow_network.WorkflowNetworkDTO;
import projeto.core.Event;
import projeto.data.XESHelper;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class InductiveMiner implements ProcessMiningAlgorithm {

    @Getter
    protected double threshold;

    public InductiveMiner(double threshold) {
        this.threshold = threshold;
    }

    public InductiveMiner() {
        this.threshold = 0.8;
    }

    public static ProcessTree miner(XLog xLog){

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

    public static DirectlyFollowsGraph mineDFG(XLog log)
    {
        DFMMiningParametersDefault defaultParams = new DFMMiningParametersDefault();
        //0.8 é o default que está no defaultParams.
        defaultParams.setNoiseThreshold(1);
        DirectlyFollowsGraph dfg = DFMMiner.mine(log,defaultParams,null);

        MultiIntSet startActivities = dfg.getStartActivities();
        List<String> startActivityName = new LinkedList<>();
        for (Integer startActivity : startActivities) {
            startActivityName.add(dfg.getActivityOfIndex(startActivity));
        }
        return dfg;
    }

    @Override
    public WorkflowNetworkDTO discoverWorkflowNetwork(List<List<Event>> eventsListSet, LinkedHashSet<String> eventNamesList) {
        FootprintInductive footprintInductive = new FootprintInductive(this,eventsListSet.get(0),eventNamesList,true);
        return new WorkflowNetworkDTO(footprintInductive);
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
        return "Inductive Mining";
    }


}
