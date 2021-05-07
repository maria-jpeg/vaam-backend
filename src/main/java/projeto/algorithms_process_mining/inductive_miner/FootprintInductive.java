package projeto.algorithms_process_mining.inductive_miner;

import gnu.trove.set.TIntSet;
import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.info.impl.XLogInfoImpl;
import org.deckfour.xes.model.*;
import org.processmining.directlyfollowsmodelminer.mining.DFMMiner;
import org.processmining.directlyfollowsmodelminer.mining.DFMMiningParameters;
import org.processmining.directlyfollowsmodelminer.mining.DFMMiningParametersAbstract;
import org.processmining.directlyfollowsmodelminer.model.DirectlyFollowsModel;
import org.processmining.framework.plugin.ProMCanceller;
import org.processmining.plugins.InductiveMiner.MultiSet;
import org.processmining.plugins.InductiveMiner.Pair;
import org.processmining.plugins.InductiveMiner.dfgOnly.log2logInfo.IMLog2IMLogInfoDefault;
import org.processmining.plugins.InductiveMiner.mining.IMLogInfo;
import org.processmining.plugins.InductiveMiner.mining.logs.LifeCycleClassifier;
import org.processmining.plugins.inductiveVisualMiner.InductiveVisualMinerState;
import org.processmining.plugins.inductiveVisualMiner.alignedLogVisualisation.data.AlignedLogVisualisationData;
import org.processmining.plugins.inductiveVisualMiner.alignedLogVisualisation.data.AlignedLogVisualisationDataImplFrequencies;
import org.processmining.plugins.inductiveVisualMiner.alignment.*;
import org.processmining.plugins.inductiveVisualMiner.chain.IvMCanceller;
import org.processmining.plugins.inductiveVisualMiner.export.ExportAlignment;
import org.processmining.plugins.inductiveVisualMiner.export.XDFMExtension;
import org.processmining.plugins.inductiveVisualMiner.export.XModelAlignmentExtension;
import org.processmining.plugins.inductiveVisualMiner.helperClasses.IvMModel;
import org.processmining.plugins.inductiveVisualMiner.ivmlog.*;
import org.processmining.plugins.inductiveVisualMiner.logFiltering.FilterLeastOccurringActivities;
import org.processmining.plugins.inductiveVisualMiner.performance.Performance;
import org.processmining.plugins.inductiveVisualMiner.performance.XEventPerformanceClassifier;
import org.processmining.plugins.inductiveVisualMiner.visualMinerWrapper.VisualMinerParameters;
import org.processmining.plugins.inductiveVisualMiner.visualMinerWrapper.miners.DfgMiner;
import org.processmining.plugins.InductiveMiner.mining.logs.IMLog;
import org.processmining.plugins.InductiveMiner.mining.logs.IMLogImpl;
import projeto.algorithms_process_mining.FootprintMatrix;
import projeto.algorithms_process_mining.FootprintStatistics;
import projeto.core.Event;
import projeto.data.XESHelper;
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
        super.eventNames = new LinkedHashSet<>(Arrays.asList(ivm.getActivitiesComFreq()));
        super.eventNamesMapper = buildEventsMapper(super.eventNames);
        //Get start and end activities from dfg
        for (Integer startActivity : ivm.getModel().getDfg().getStartNodes().toArray()) {
            super.startEvents.put(startActivity,(int) 1);//Hardcoded 1. Não sei que valor meter aqui.
        }
        for (Integer endActivity : ivm.getModel().getDfg().getEndNodes().toArray()) {
            super.endEvents.put(endActivity,(int) 1);
        }
        //obrigar novo
        super.footprintStatistics = new FootprintStatistics(this);
        footprintStatistics.setFrequencyFromIvMHelper(ivm);
    }

    public static Map<String, Integer> buildEventsMapper( Set<String> eventNames )
    {
        Map<String, Integer> eventNamesMapper = new HashMap<>();
        int index = 0;
        for (String s : eventNames)
            eventNamesMapper.put(s, index++);

        return eventNamesMapper;
    }

    private XLog getEventLog(List<Event> events){
        String csvContent = XESHelper.eventsToCsv(events);//converter para csv
        XLog log = XESHelper.eventsCsvToXes(csvContent);//Converter para xes
        return log;
    }

    private IvMHelper getIvMDFG(XLog log){
        ProMCanceller proMCanceller = () -> false;
        IvMCanceller ivMCanceller = new IvMCanceller(proMCanceller);
        //todo align xlog?
        /*
        DFMMiningParametersAbstract dfmMiningParameters = new org.processmining.directlyfollowsmodelminer.mining.variants.DFMMiningParametersDefault();
        dfmMiningParameters.setNoiseThreshold(inductiveMiner.paths);
        DFMMiner.mine(log,dfmMiningParameters,null);

        XModelAlignmentExtension alignmentExtension = XModelAlignmentExtension.instance();
        alignmentExtension.assignModel(log,"dfm", XDFMExtension.fromDfg(model.getDfg()));
         */

        //IvMModel
        DfgMiner miner = new DfgMiner();
        IMLog imLog = new IMLogImpl(log,log.getClassifiers().get(0),miner.getLifeCycleClassifier());
        IMLogInfo imLogInfo = IMLog2IMLogInfoDefault.log2logInfo(imLog);
        //Algo novo
        //IMLogInfo log3Info = miner.getLog2logInfo().createLogInfo(imLog);

        FilterLeastOccurringActivities.filter(imLog,imLogInfo, inductiveMiner.activities, null);
        VisualMinerParameters visualMinerParameters = new VisualMinerParameters(inductiveMiner.paths);
        IvMModel model = miner.mine(imLog,imLogInfo,visualMinerParameters,ivMCanceller);



        /*ALIGNMENT*/
        AlignmentComputerImpl alignmentComputer = new AlignmentComputerImpl();
        //porque levou com o filter das activities
        //XLog newLog = log3.toXLog(); //Porque foi aligned?
        // Align do IMLog para IvMLogNotFiltered


        XEventClassifier classifier = imLog.getClassifier();
        XEventPerformanceClassifier performanceClassifier = new XEventPerformanceClassifier(classifier);

        XEventClasses activityEventClasses = XLogInfoFactory.createLogInfo(log,classifier).getEventClasses();
        XEventClasses performanceEventClasses = XLogInfoFactory.createLogInfo(log, performanceClassifier).getEventClasses();


        IvMLogNotFiltered notFiltered;
        try {
            notFiltered = AlignmentPerformance.alignDfg(alignmentComputer,model,performanceClassifier,log,activityEventClasses,performanceEventClasses,proMCanceller);
        }catch (Exception e){
            System.out.println("Could not align log: "+e);
            return null;
        }
        if(notFiltered==null)
            return null;

        IvMLogInfo logInfo = new IvMLogInfo(notFiltered,model);
        AlignedLogVisualisationDataImplFrequencies visualisationDataImplFrequencies = new AlignedLogVisualisationDataImplFrequencies(model,logInfo);


        //Contém os valores correspondentes no visual miner
        HashMap<Integer,Long> nodeLabels = new HashMap<>();
        String[] activitiesComFreq = new String[model.getDfg().getAllNodeNames().length];
        for (int i = 0; i < model.getDfg().getAllNodeNames().length; i++) {
            String activityName = model.getActivityName(i);
            Long freq = visualisationDataImplFrequencies.getNodeLabel(i,true).getB();
            nodeLabels.put(i,freq);
            activitiesComFreq[i] = activityName+"@"+freq;
        }

        HashMap<Pair<Integer,Integer>,Long> edgeCardinality = new HashMap<>();
        HashMap<Pair<String,String>,Long> edgeCardinalityByName = new HashMap<>();

        for (int i = 0; i < model.getDfg().getAllNodeNames().length; i++) {
            for (int j = 0; j < model.getDfg().getAllNodeNames().length; j++) {
                //Preencher os edges
                //Novo Par com source e node
                if (model.getDfg().containsEdge(i,j)){
                    Pair<String,Long> freq = visualisationDataImplFrequencies.getEdgeLabel(i,j,true);
                    edgeCardinality.put(Pair.of(i,j),freq.getRight());
                    edgeCardinalityByName.put(Pair.of(model.getActivityName(i),model.getActivityName(j)),freq.getRight());
                }
            }
        }

        IvMHelper ivMHelper = new IvMHelper();
        ivMHelper.setEdgeCardinality(edgeCardinality);
        ivMHelper.setModel(model);
        ivMHelper.setActivityCardinalitiesComplete(nodeLabels);

        ivMHelper.setActivitiesComFreq(activitiesComFreq);
        return ivMHelper;
    }

    @Override
    public boolean areEventsConnected(int a, int b) {
        return ivm.getModel().getDfg().containsEdge(a,b);
    }
}
