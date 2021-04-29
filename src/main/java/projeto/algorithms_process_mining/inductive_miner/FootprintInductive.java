package projeto.algorithms_process_mining.inductive_miner;

import lombok.Getter;
import org.deckfour.xes.model.XLog;
import org.glassfish.jersey.internal.guava.Sets;
import org.processmining.directlyfollowsmodelminer.mining.DFMMiningParametersAbstract;
import org.processmining.directlyfollowsmodelminer.model.DirectlyFollowsModel;
import org.processmining.framework.packages.PackageManager;
import org.processmining.plugins.InductiveMiner.Triple;
import org.processmining.plugins.directlyfollowsgraph.DirectlyFollowsGraph;
import org.processmining.plugins.directlyfollowsgraph.mining.DFMMiner;
import org.processmining.plugins.directlyfollowsgraph.mining.variants.DFMMiningParametersDefault;
import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.plugins.inductiveVisualMiner.InductiveVisualMiner;
import org.processmining.plugins.inductiveVisualMiner.alignedLogVisualisation.data.AlignedLogVisualisationData;
import org.processmining.plugins.inductiveVisualMiner.alignedLogVisualisation.data.AlignedLogVisualisationDataImplEmpty;
import org.processmining.plugins.inductiveVisualMiner.helperClasses.IvMModel;
import org.processmining.plugins.inductiveVisualMiner.traceview.TraceViewEventColourMap;
import org.processmining.plugins.inductiveVisualMiner.visualisation.DfmVisualisation;
import org.processmining.plugins.inductiveVisualMiner.visualisation.DfmVisualisationSimple;
import org.processmining.plugins.inductiveVisualMiner.visualisation.ProcessTreeVisualisationInfo;
import org.processmining.plugins.inductiveVisualMiner.visualisation.ProcessTreeVisualisationParameters;
import org.processmining.plugins.inductiveminer2.helperclasses.MultiIntSet;
import org.processmining.plugins.inductiveminer2.logs.IMLogImpl;
import projeto.algorithms_process_mining.FootprintMatrix;
import projeto.algorithms_process_mining.FootprintStatistics;
import projeto.algorithms_process_mining.ProcessMiningAlgorithm;
import projeto.core.Event;
import projeto.data.XESHelper;

import java.util.*;

public class FootprintInductive extends FootprintMatrix {

    private InductiveMiner inductiveMiner;

    private DirectlyFollowsGraph dfg;

    public FootprintInductive(InductiveMiner algorithm, List<Event> events, LinkedHashSet<String> eventNames, boolean statistics) {
        super(algorithm, eventNames, statistics);
        this.inductiveMiner = algorithm;

        this.dfg = getDfg(getEventLog(events));
        //super.eventNames.clear(); //reset de eventNames (Nomes das atividades) para coloca-las na ordem correta.
        super.eventNames = new LinkedHashSet<>(Arrays.asList(dfg.getAllActivities()));

        //Get start and end activities from dfg
        for (Integer startActivity : dfg.getStartActivities()) {
            super.startEvents.put(startActivity,(int) dfg.getStartActivities().getCardinalityOf(startActivity));
        }
        for (Integer endActivity : dfg.getEndActivities()) {
            super.endEvents.put(endActivity,(int) dfg.getEndActivities().getCardinalityOf(endActivity));
        }

        footprintStatistics.setFrequencyFromDfg(dfg);
    }

    private XLog getEventLog(List<Event> events){
        String csvContent = XESHelper.eventsToCsv(events);//converter para csv
        XLog log = XESHelper.eventsCsvToXes(csvContent);//Converter para xes
        return log;
    }

    private DirectlyFollowsGraph getDfg(XLog log){

        DFMMiningParametersDefault defaultParams = new DFMMiningParametersDefault();//Params do inductive miner
        defaultParams.setNoiseThreshold(inductiveMiner.getThreshold());
        DirectlyFollowsGraph dfg = DFMMiner.mine(log,defaultParams,null); //Dfg obtido


        //TESTE
        DFMMiningParametersAbstract miningParameters = new org.processmining.directlyfollowsmodelminer.mining.variants.DFMMiningParametersDefault();
        miningParameters.setNoiseThreshold(inductiveMiner.threshold);

        //IMLogImpl log2 = new org.processmining.plugins.inductiveminer2.logs.IMLogImpl(log, new org.processmining.directlyfollowsmodelminer.mining.variants.DFMMiningParametersDefault().getClassifier(),
        //        new org.processmining.directlyfollowsmodelminer.mining.variants.DFMMiningParametersDefault().getLifeCycleClassifier());
        DirectlyFollowsModel dfg2 = org.processmining.directlyfollowsmodelminer.mining.DFMMiner.mine(log, miningParameters, null);
        IvMModel model = new IvMModel(dfg2);
        Dot dot = DfmVisualisationSimple.fancy(model);
        return dfg;
    }

    @Override
    public boolean areEventsConnected(int a, int b) {
        return dfg.getDirectlyFollowsGraph().containsEdge(a,b);
    }
}
