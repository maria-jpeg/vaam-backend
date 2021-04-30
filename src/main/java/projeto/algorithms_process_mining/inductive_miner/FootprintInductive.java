package projeto.algorithms_process_mining.inductive_miner;

import gnu.trove.set.TIntSet;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.directlyfollowsmodelminer.mining.DFMMiningParametersAbstract;
import org.processmining.directlyfollowsmodelminer.model.DirectlyFollowsModel;
import org.processmining.directlyfollowsmodelminer.model.DirectlyFollowsModel2AcceptingPetriNet;
import org.processmining.framework.packages.PackageManager;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.PetrinetNode;
import org.processmining.models.graphbased.directed.petrinet.elements.Arc;
import org.processmining.plugins.InductiveMiner.Pair;
import org.processmining.plugins.InductiveMiner.Triple;
import org.processmining.plugins.InductiveMiner.efficienttree.EfficientTree;
import org.processmining.plugins.InductiveMiner.efficienttree.EfficientTree2HumanReadableString;
import org.processmining.plugins.InductiveMiner.efficienttree.ProcessTree2EfficientTree;
import org.processmining.plugins.InductiveMiner.mining.MiningParameters;
import org.processmining.plugins.InductiveMiner.mining.MiningParametersIM;
import org.processmining.plugins.InductiveMiner.mining.logs.LifeCycleClassifier;
import org.processmining.plugins.InductiveMiner.plugins.EfficientTree2AcceptingPetriNetPlugin;
import org.processmining.plugins.InductiveMiner.plugins.IMPetriNet;
import org.processmining.plugins.InductiveMiner.plugins.IMProcessTree;
import org.processmining.plugins.InductiveMiner.plugins.IMTree;
import org.processmining.plugins.directlyfollowsgraph.DirectlyFollowsGraph;
import org.processmining.plugins.directlyfollowsgraph.mining.DFMMiner;
import org.processmining.plugins.directlyfollowsgraph.mining.variants.DFMMiningParametersDefault;
import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.plugins.graphviz.dot.DotNode;
import org.processmining.plugins.inductiveVisualMiner.InductiveVisualMiner;
import org.processmining.plugins.inductiveVisualMiner.InductiveVisualMinerPanel;
import org.processmining.plugins.inductiveVisualMiner.InductiveVisualMinerState;
import org.processmining.plugins.inductiveVisualMiner.alignedLogVisualisation.data.AlignedLogVisualisationData;
import org.processmining.plugins.inductiveVisualMiner.alignedLogVisualisation.data.AlignedLogVisualisationDataImplFrequencies;
import org.processmining.plugins.inductiveVisualMiner.configuration.InductiveVisualMinerConfiguration;
import org.processmining.plugins.inductiveVisualMiner.configuration.InductiveVisualMinerConfigurationAbstract;
import org.processmining.plugins.inductiveVisualMiner.configuration.InductiveVisualMinerConfigurationDefault;
import org.processmining.plugins.inductiveVisualMiner.helperClasses.IvMEfficientTree;
import org.processmining.plugins.inductiveVisualMiner.helperClasses.IvMModel;
import org.processmining.plugins.inductiveVisualMiner.ivmlog.*;
import org.processmining.plugins.inductiveVisualMiner.mode.ModePaths;
import org.processmining.plugins.inductiveVisualMiner.traceview.TraceViewEventColourMap;
import org.processmining.plugins.inductiveVisualMiner.visualMinerWrapper.miners.DfgMiner;
import org.processmining.plugins.inductiveVisualMiner.visualisation.DfmVisualisation;
import org.processmining.plugins.inductiveVisualMiner.visualisation.DfmVisualisationSimple;
import org.processmining.plugins.inductiveVisualMiner.visualisation.ProcessTreeVisualisationInfo;
import org.processmining.plugins.inductiveVisualMiner.visualisation.ProcessTreeVisualisationParameters;
import org.processmining.plugins.inductiveminer2.logs.IMLogImpl;
import org.processmining.plugins.inductiveminer2.logs.IMTrace;
import org.processmining.plugins.inductiveminer2.mining.MiningParametersAbstract;
import org.processmining.plugins.inductiveminer2.withoutlog.dfgmsd.Log2DfgMsd;
import org.processmining.processtree.ProcessTree;
import org.processmining.processtree.conversion.ProcessTree2Petrinet;
import projeto.algorithms_process_mining.FootprintMatrix;
import projeto.core.Event;
import projeto.data.XESHelper;
import org.processmining.plugins.inductiveVisualMiner.chain.*;

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

        IMLogImpl log2 = new org.processmining.plugins.inductiveminer2.logs.IMLogImpl(log, new org.processmining.directlyfollowsmodelminer.mining.variants.DFMMiningParametersDefault().getClassifier(),
               new org.processmining.directlyfollowsmodelminer.mining.variants.DFMMiningParametersDefault().getLifeCycleClassifier());
        DirectlyFollowsModel dfm = org.processmining.directlyfollowsmodelminer.mining.DFMMiner.mine(log, miningParameters, null);
        IvMModel model = new IvMModel(dfm);

        /*
        long[][] matrix = new long[dfm.getNumberOfNodes()][dfm.getNumberOfNodes()];
        for (Long edge : dfm.getEdges()) {
            int source = dfm.getEdgeSource(edge);
            int target = dfm.getEdgeTarget(edge);
            //dfm.
            matrix[source][target] += 1;
        }

         */

        int numberOfTraces = 0;
        for (XTrace xEvents : log) {
            numberOfTraces++;
        }
        IvMLogNotFilteredImpl ivMLogNotFiltered = new IvMLogNotFilteredImpl(numberOfTraces,log.getAttributes());
        IvMLogFiltered ivMLogFiltered = new IvMLogFilteredImpl(ivMLogNotFiltered);
        IvMLogInfo logInfo = new IvMLogInfo(ivMLogNotFiltered,model);
        //TODO Obter um IvMLog
        //IvMLogInfo logInfo = new IvMLogInfo();
        DfmVisualisation visualisation = new DfmVisualisation();
        AlignedLogVisualisationDataImplFrequencies visualisationDataFreq = new AlignedLogVisualisationDataImplFrequencies(model,logInfo);
        ProcessTreeVisualisationParameters processTreeVisualisationParameters = new ProcessTreeVisualisationParameters();

        Pair<Long,Long> pair = visualisationDataFreq.getExtremeCardinalities();

        Triple<Dot, ProcessTreeVisualisationInfo, TraceViewEventColourMap> triple = visualisation.fancy(model,visualisationDataFreq,processTreeVisualisationParameters);

        //this.state = this.createState(log);
        InductiveVisualMinerConfigurationVaam config = new InductiveVisualMinerConfigurationVaam();
        InductiveVisualMinerState state = config.createState(log);
        state.setMiner(new DfgMiner());
        state.setConfiguration(config);
        //!!!
        state.setPaths(1);
        state.setActivitiesThreshold(1);
        /*
        Chain<InductiveVisualMinerState> chain = config.createChain(state, null, null,null,null,null);
        Pair<Dot, Map<ChainLink<InductiveVisualMinerState, ?, ?>, DotNode>> dot1 = chain.toDot();
        InductiveVisualMiner.InductiveVisualMinerLauncher ee = InductiveVisualMiner.InductiveVisualMinerLauncher.launcher(log);
        ee.setMiner(new DfgMiner());

         */
        IvMEfficientTree ivMEfficientTree = new IvMEfficientTree(IMProcessTree.mineProcessTree(log));
        ModePaths modePaths = new ModePaths();
        AlignedLogVisualisationData visualisationData = modePaths.getVisualisationData(model,null,logInfo,null,null);
        Pair<Long,Long> pair2 = visualisationData.getExtremeCardinalities();

        return dfg;
    }

    @Override
    public boolean areEventsConnected(int a, int b) {
        return dfg.getDirectlyFollowsGraph().containsEdge(a,b);
    }
}
