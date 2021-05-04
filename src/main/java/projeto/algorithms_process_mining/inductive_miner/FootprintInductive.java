package projeto.algorithms_process_mining.inductive_miner;

import gnu.trove.map.TObjectIntMap;
import org.checkerframework.checker.units.qual.A;
import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.impl.XLogInfoImpl;
import org.deckfour.xes.model.*;
import org.processmining.directlyfollowsmodelminer.mining.DFMMiningParametersAbstract;
import org.processmining.directlyfollowsmodelminer.model.DirectlyFollowsModel;
import org.processmining.framework.packages.PackageManager;
import org.processmining.framework.plugin.ProMCanceller;
import org.processmining.plugins.InductiveMiner.MultiSet;
import org.processmining.plugins.InductiveMiner.Pair;
import org.processmining.plugins.InductiveMiner.Triple;
import org.processmining.plugins.InductiveMiner.efficienttree.EfficientTree;
import org.processmining.plugins.InductiveMiner.plugins.IMProcessTree;
import org.processmining.plugins.directlyfollowsgraph.DirectlyFollowsGraph;
import org.processmining.plugins.directlyfollowsgraph.mining.DFMMiner;
import org.processmining.plugins.directlyfollowsgraph.mining.variants.DFMMiningParametersDefault;
import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.plugins.inductiveVisualMiner.InductiveVisualMinerState;
import org.processmining.plugins.inductiveVisualMiner.alignedLogVisualisation.data.AlignedLogVisualisationData;
import org.processmining.plugins.inductiveVisualMiner.alignedLogVisualisation.data.AlignedLogVisualisationDataImplFrequencies;
import org.processmining.plugins.inductiveVisualMiner.alignment.*;
import org.processmining.plugins.inductiveVisualMiner.helperClasses.IvMEfficientTree;
import org.processmining.plugins.inductiveVisualMiner.helperClasses.IvMModel;
import org.processmining.plugins.inductiveVisualMiner.ivmlog.*;
import org.processmining.plugins.inductiveVisualMiner.mode.ModePaths;
import org.processmining.plugins.inductiveVisualMiner.performance.Performance;
import org.processmining.plugins.inductiveVisualMiner.performance.XEventPerformanceClassifier;
import org.processmining.plugins.inductiveVisualMiner.traceview.TraceViewEventColourMap;
import org.processmining.plugins.inductiveVisualMiner.visualMinerWrapper.miners.DfgMiner;
import org.processmining.plugins.inductiveVisualMiner.visualisation.DfmVisualisation;
import org.processmining.plugins.inductiveVisualMiner.visualisation.ProcessTreeVisualisationInfo;
import org.processmining.plugins.inductiveVisualMiner.visualisation.ProcessTreeVisualisationParameters;
import org.processmining.plugins.inductiveminer2.logs.IMLogImpl;
import org.processmining.plugins.inductiveminer2.mining.MiningParametersAbstract;
import org.processmining.plugins.inductiveminer2.variants.MiningParametersIM;
import org.processmining.plugins.inductiveminer2.variants.MiningParametersIMLifeCycle;
import projeto.algorithms_process_mining.FootprintMatrix;
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
        DFMMiningParametersAbstract dfmMiningParameters = new org.processmining.directlyfollowsmodelminer.mining.variants.DFMMiningParametersDefault();
        dfmMiningParameters.setNoiseThreshold(inductiveMiner.threshold);

        IMLogImpl log2 = new org.processmining.plugins.inductiveminer2.logs.IMLogImpl(log, new org.processmining.directlyfollowsmodelminer.mining.variants.DFMMiningParametersDefault().getClassifier(),
               new org.processmining.directlyfollowsmodelminer.mining.variants.DFMMiningParametersDefault().getLifeCycleClassifier());
        DirectlyFollowsModel dfm = org.processmining.directlyfollowsmodelminer.mining.DFMMiner.mine(log, dfmMiningParameters, null);

        PackageManager.Canceller canceller = new PackageManager.Canceller() {
            @Override
            public boolean isCancelled() {
                return false;
            }
        };
        MiningParametersAbstract miningParameters = new MiningParametersIM();
        //miningParameters.setNoiseThreshold();
        EfficientTree et = org.processmining.plugins.inductiveminer2.mining.InductiveMiner.mineEfficientTree(log2,miningParameters,canceller);
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

        IvMLogNotFiltered ivMLogNotFiltered = xLog2IvMLog(log,model);
        //IvMLogFiltered ivMLogFiltered = new IvMLogFilteredImpl(ivMLogNotFiltered);
        IvMLogInfo logInfo = new IvMLogInfo(ivMLogNotFiltered,model);
        //IvMLogInfo logInfo = new IvMLogInfo();
        DfmVisualisation visualisation = new DfmVisualisation();
        //AlignedLogVisualisationDataImplFrequencies visualisationDataFreq = new AlignedLogVisualisationDataImplFrequencies(model,logInfo);

        //ProcessTreeVisualisationParameters processTreeVisualisationParameters = new ProcessTreeVisualisationParameters();

        //Pair<Long,Long> pair = visualisationDataFreq.getExtremeCardinalities();

        //Triple<Dot, ProcessTreeVisualisationInfo, TraceViewEventColourMap> triple = visualisation.fancy(model,visualisationDataFreq,processTreeVisualisationParameters);

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


        IvMEfficientTree ivMEfficientTree = new IvMEfficientTree(IMProcessTree.mineProcessTree(log));
        ModePaths modePaths = new ModePaths();
        //AlignedLogVisualisationData visualisationData = modePaths.getVisualisationData(model,null,logInfo,null,null);

        MultiSet<Move> moves = logInfo.getActivities();
        HashMap<String,Long> cardinalities = new HashMap<>();
        for (Move move : moves) {
            //Apenas as start? são todas.
            if(move.getBottomLabel().equals(Performance.PerformanceTransition.valueOf("start").toString())){
                String activityName = move.getLabel();
                long cardinalidade = moves.getCardinalityOf(move);
                cardinalities.put(activityName,cardinalidade);
            }
        }
         */

        /*ALIGNMENT*/
        AlignmentComputerImpl alignmentComputer = new AlignmentComputerImpl();
        ProMCanceller proMCanceller = new ProMCanceller() {
            @Override
            public boolean isCancelled() {
                return false;
            }
        };
        //???
        XLogInfo xLogInfo = XLogInfoImpl.create(log);
        XEventClasses xEventClasses = xLogInfo.getEventClasses();
        IvMEventClasses ivMEventClasses = new IvMEventClasses(xEventClasses);
        IvMEfficientTree ivMEfficientTree1 = new IvMEfficientTree(et);
        XEventPerformanceClassifier performanceClassifier = new XEventPerformanceClassifier(log.getClassifiers().get(0));
        IvMLogNotFiltered notFiltered = null;
        try {
            notFiltered = AlignmentPerformance.alignDfg(alignmentComputer,model,performanceClassifier,log,ivMEventClasses,ivMEventClasses,proMCanceller);

        }catch (Exception e){
            System.out.println("Could not align log: "+e);
        }

        return dfg;
    }
    //Não funciona preciso de fazer o alignment
    private IvMLogNotFiltered xLog2IvMLog(XLog log,IvMModel model){
        IvMLogNotFilteredImpl ivMLogNotFiltered = new IvMLogNotFilteredImpl(log.size(),log.getAttributes());

        int traceCounter = 0;
        for (XTrace trace : log) {
            IvMTrace ivMTrace = new IvMTraceImpl(trace.getAttributes().get("concept:name").toString(),trace.getAttributes(),trace.size());

            int eventCounter =0;
            for (XEvent event : trace) {
                Performance.PerformanceTransition performanceTransition = Performance.PerformanceTransition.valueOf(event.getAttributes().get("lifecycle:transition").toString());
                XEventClass xEventClass = new XEventClass(event.getID().toString(),eventCounter);

                String activityName = event.getAttributes().get("concept:name").toString();
                int[] nodeIndex = new int[1];
                if (model.getDfg() != null){
                    nodeIndex = model.getDfg().getIndicesOfNodeName(activityName).toArray();
                }
                else {
                    TObjectIntMap<String> activity2Int = model.getTree().getActivity2int();
                    for (int i = 0; i < activity2Int.size(); i++) {
                        if (activity2Int.containsKey(activityName)){
                            nodeIndex[0] = activity2Int.get(activityName);
                        }
                    }
                }

                // TODO source node? Pode causar problemas. Event performance class a mesma coisa
                Move move = new Move(model,Move.Type.modelMove,-1,nodeIndex[0],xEventClass,null,performanceTransition,eventCounter);
                XAttributeTimestamp timestamp = (XAttributeTimestamp) event.getAttributes().get("time:timestamp");
                Long epochDate = timestamp.getValue().getTime();
                IvMMove ivMMove = new IvMMove(model,move,epochDate,"RESOURCE???",event.getAttributes());
                ivMTrace.add(ivMMove);
                eventCounter++;
            }
            ivMLogNotFiltered.set(traceCounter,ivMTrace);
            traceCounter++;
        }
        return ivMLogNotFiltered;
    }

    @Override
    public boolean areEventsConnected(int a, int b) {
        return dfg.getDirectlyFollowsGraph().containsEdge(a,b);
    }
}
