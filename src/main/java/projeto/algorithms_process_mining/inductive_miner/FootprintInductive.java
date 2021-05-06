package projeto.algorithms_process_mining.inductive_miner;

import com.google.common.collect.Multiset;
import gnu.trove.map.TIntLongMap;
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
import org.processmining.plugins.InductiveMiner.dfgOnly.log2logInfo.IMLog2IMLogInfoDefault;
import org.processmining.plugins.InductiveMiner.efficienttree.EfficientTree;
import org.processmining.plugins.InductiveMiner.mining.IMLogInfo;
import org.processmining.plugins.InductiveMiner.mining.logs.LifeCycleClassifier;
import org.processmining.plugins.InductiveMiner.plugins.IMProcessTree;
import org.processmining.plugins.directlyfollowsgraph.DirectlyFollowsGraph;
import org.processmining.plugins.directlyfollowsgraph.mining.DFMMiner;
import org.processmining.plugins.directlyfollowsgraph.mining.variants.DFMMiningParametersDefault;
import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.plugins.inductiveVisualMiner.InductiveVisualMinerState;
import org.processmining.plugins.inductiveVisualMiner.alignedLogVisualisation.data.AlignedLogVisualisationData;
import org.processmining.plugins.inductiveVisualMiner.alignedLogVisualisation.data.AlignedLogVisualisationDataImplFrequencies;
import org.processmining.plugins.inductiveVisualMiner.alignment.*;
import org.processmining.plugins.inductiveVisualMiner.chain.IvMCanceller;
import org.processmining.plugins.inductiveVisualMiner.helperClasses.IvMEfficientTree;
import org.processmining.plugins.inductiveVisualMiner.helperClasses.IvMModel;
import org.processmining.plugins.inductiveVisualMiner.ivmlog.*;
import org.processmining.plugins.inductiveVisualMiner.logFiltering.FilterLeastOccurringActivities;
import org.processmining.plugins.inductiveVisualMiner.mode.ModePaths;
import org.processmining.plugins.inductiveVisualMiner.performance.Performance;
import org.processmining.plugins.inductiveVisualMiner.performance.XEventPerformanceClassifier;
import org.processmining.plugins.inductiveVisualMiner.traceview.TraceViewEventColourMap;
import org.processmining.plugins.inductiveVisualMiner.visualMinerWrapper.VisualMinerParameters;
import org.processmining.plugins.inductiveVisualMiner.visualMinerWrapper.VisualMinerWrapper;
import org.processmining.plugins.inductiveVisualMiner.visualMinerWrapper.miners.DfgMiner;
import org.processmining.plugins.inductiveVisualMiner.visualMinerWrapper.miners.Miner;
import org.processmining.plugins.inductiveVisualMiner.visualisation.DfmVisualisation;
import org.processmining.plugins.inductiveVisualMiner.visualisation.ProcessTreeVisualisationInfo;
import org.processmining.plugins.inductiveVisualMiner.visualisation.ProcessTreeVisualisationParameters;
import org.processmining.plugins.InductiveMiner.mining.logs.IMLog;
import org.processmining.plugins.inductiveminer2.logs.IMLog2XLog;
import org.processmining.plugins.inductiveminer2.logs.IMLogImpl;
import org.processmining.plugins.inductiveminer2.mining.MiningParametersAbstract;
import org.processmining.plugins.inductiveminer2.variants.MiningParametersIM;
import org.processmining.plugins.inductiveminer2.variants.MiningParametersIMLifeCycle;
import projeto.algorithms_process_mining.FootprintMatrix;
import projeto.algorithms_process_mining.FootprintStatistics;
import projeto.core.Event;
import projeto.data.XESHelper;

import java.lang.reflect.Parameter;
import java.sql.SQLOutput;
import java.util.*;

public class FootprintInductive extends FootprintMatrix {

    private InductiveMiner inductiveMiner;

    //private DirectlyFollowsGraph dfg;
    private IvMHelper ivm;

    public FootprintInductive(InductiveMiner algorithm, List<Event> events, LinkedHashSet<String> eventNames, boolean statistics) {
        super(algorithm, eventNames, statistics);
        this.inductiveMiner = algorithm;

        //this.dfg = getDfg(getEventLog(events));
        this.ivm = getIvMDFG(getEventLog(events));
        //super.eventNames.clear(); //reset de eventNames (Nomes das atividades) para coloca-las na ordem correta.
        super.eventNames = new LinkedHashSet<>(Arrays.asList(ivm.getModel().getDfg().getAllNodeNames()));
        super.eventNamesMapper = buildEventsMapper(super.eventNames);
        //Get start and end activities from dfg
        for (Integer startActivity : ivm.getModel().getDfg().getStartNodes().toArray()) {
            super.startEvents.put(startActivity,(int) ivm.getActivityCardinality(startActivity));
        }
        for (Integer endActivity : ivm.getModel().getDfg().getEndNodes().toArray()) {
            super.endEvents.put(endActivity,(int) ivm.getActivityCardinality(endActivity));
        }
        //obrigar novo
        super.footprintStatistics = new FootprintStatistics(this);
        footprintStatistics.setFrequencyFromIvMHelper(ivm);
    }

    public static Map<String, Integer> buildEventsMapper( Set<String> eventNames )
    {
        Map<String, Integer> eventNamesMapper = new HashMap<>();
        int index = 0;
        // assign each of the events an index in the matrix
        for (String s : eventNames) {

            //System.out.println( s + " " + index );
            eventNamesMapper.put(s, index++);
        }
        return eventNamesMapper;
    }

    private XLog getEventLog(List<Event> events){
        String csvContent = XESHelper.eventsToCsv(events);//converter para csv
        XLog log = XESHelper.eventsCsvToXes(csvContent);//Converter para xes
        return log;
    }

    private IvMHelper getIvMDFG(XLog log){
        ProMCanceller proMCanceller = new ProMCanceller() {
            @Override
            public boolean isCancelled() {
                return false;
            }
        };

        //IvMModel
        //so for 0 da null pointer
        VisualMinerParameters visualMinerParameters = new VisualMinerParameters(inductiveMiner.paths);
        DfgMiner miner = new DfgMiner();
        IMLog log3 = new org.processmining.plugins.InductiveMiner.mining.logs.IMLogImpl(log,log.getClassifiers().get(0),new LifeCycleClassifier());
        IMLogInfo log3Info = IMLog2IMLogInfoDefault.log2logInfo(log3);
        IvMCanceller ivMCanceller = new IvMCanceller(proMCanceller);

        FilterLeastOccurringActivities.filter(log3,log3Info, inductiveMiner.activities, null);
        IvMModel model = miner.mine(log3,log3Info,visualMinerParameters,ivMCanceller);

        /*ALIGNMENT*/
        AlignmentComputerImpl alignmentComputer = new AlignmentComputerImpl();
        Boolean teste = log.equals(log3.toXLog());
        //porque levou com o filter das activities
        XLog newLog = log3.toXLog();
        //??? Align do xLog para ivmlog
        XLogInfo xLogInfo = XLogInfoImpl.create(newLog);
        XEventClasses xEventClasses = xLogInfo.getEventClasses();
        IvMEventClasses ivMEventClasses = new IvMEventClasses(xEventClasses);

        XEventPerformanceClassifier performanceClassifier = new XEventPerformanceClassifier(log.getClassifiers().get(0));
        IvMLogNotFiltered notFiltered = null;
        try {
            notFiltered = AlignmentPerformance.alignDfg(alignmentComputer,model,performanceClassifier,newLog,ivMEventClasses,ivMEventClasses,proMCanceller);
            if(notFiltered==null)
                return null;

        }catch (Exception e){
            System.out.println("Could not align log: "+e);
        }

        IvMLogInfo logInfo = new IvMLogInfo(notFiltered,model);
        MultiSet<Move> moves = logInfo.getActivities();


        //Contém os valores correspondentes no visual miner
        HashMap<Integer,Long> cardinalitiesComplete = new HashMap<>();
        HashMap<String,Long> cardinalitiesStart = new HashMap<>();
        HashMap<String,Long> cardinalitiesEnqueue = new HashMap<>();
        HashMap<String,Long> cardinalitiesOther = new HashMap<>();

        for (Move move : moves) {
            //Apenas as start? são todas.
            String activityName = move.getLabel();
            long cardinalidade = moves.getCardinalityOf(move);
            if(move.getBottomLabel().equals(Performance.PerformanceTransition.valueOf("complete").toString())){
                //PERIGO
                int[] nodeNoModel = model.getDfg().getIndicesOfNodeName(activityName).toArray();
                cardinalitiesComplete.put(nodeNoModel[0],cardinalidade);
            }
            if(move.getBottomLabel().equals(Performance.PerformanceTransition.valueOf("start").toString())){
                cardinalitiesStart.put(activityName,cardinalidade);
            }if(move.getBottomLabel().equals(Performance.PerformanceTransition.valueOf("enqueue").toString())){
                cardinalitiesEnqueue.put(activityName,cardinalidade);
            }if(move.getBottomLabel().equals(Performance.PerformanceTransition.valueOf("other").toString())){
                cardinalitiesOther.put(activityName,cardinalidade);
            }
        }

        HashMap<Pair<Integer,Integer>,Long> edgeCardinality = new HashMap<>();
        HashMap<Pair<String,String>,Long> edgeCardinalityByName = new HashMap<>();
        for (int i = 0; i < model.getDfg().getAllNodeNames().length; i++) {
            for (int j = 0; j < model.getDfg().getAllNodeNames().length; j++) {
                //Preencher os edges
                //Novo Par com source e node
                if (model.getDfg().containsEdge(i,j)){
                    long weight = logInfo.getModelEdgeExecutions(i,j);
                    if (weight>0){
                        Pair<Integer,Integer> par = Pair.of(i,j);
                        Pair<String,String> par2 = Pair.of(model.getActivityName(i),model.getActivityName(j));
                        edgeCardinality.put(par,weight);
                        edgeCardinalityByName.put(par2,weight);
                    }
                }
            }
        }

        IvMHelper ivMHelper = new IvMHelper();
        ivMHelper.setEdgeCardinality(edgeCardinality);
        ivMHelper.setModel(model);
        ivMHelper.setActivityCardinalitiesComplete(cardinalitiesComplete);

        return ivMHelper;
    }

    @Override
    public boolean areEventsConnected(int a, int b) {
        return ivm.getModel().getDfg().containsEdge(a,b);
    }
}
