package projeto.algorithms_process_mining.inductive_miner;

import lombok.Getter;
import org.deckfour.xes.model.XLog;
import org.glassfish.jersey.internal.guava.Sets;
import org.processmining.plugins.directlyfollowsgraph.DirectlyFollowsGraph;
import org.processmining.plugins.directlyfollowsgraph.mining.DFMMiner;
import org.processmining.plugins.directlyfollowsgraph.mining.variants.DFMMiningParametersDefault;
import org.processmining.plugins.inductiveminer2.helperclasses.MultiIntSet;
import projeto.algorithms_process_mining.FootprintMatrix;
import projeto.algorithms_process_mining.FootprintStatistics;
import projeto.algorithms_process_mining.ProcessMiningAlgorithm;
import projeto.core.Event;
import projeto.data.XESHelper;

import java.util.*;

public class FootprintInductive extends FootprintMatrix {

    private InductiveMiner inductiveMiner;

    protected Long[][] footprint;

    private DirectlyFollowsGraph dfg;

    public FootprintInductive(InductiveMiner algorithm, List<Event> events, LinkedHashSet<String> eventNames, boolean statistics) {
        super(algorithm, eventNames, statistics);
        this.inductiveMiner = algorithm;



        this.dfg = getDfg(getEventLog(events));
        super.eventNames.clear(); //reset de eventNames (Nomes das atividades) para coloca-las na ordem correta.
        super.eventNames = new LinkedHashSet<>(Arrays.asList(dfg.getAllActivities()));

        //Get start and end activities from dfg

        for (Integer startActivity : dfg.getStartActivities()) {
            super.startEvents.put(startActivity,(int) dfg.getStartActivities().getCardinalityOf(startActivity));
        }
        for (Integer endActivity : dfg.getEndActivities()) {
            super.endEvents.put(endActivity,(int) dfg.getEndActivities().getCardinalityOf(endActivity));
        }

        //extractStartEndEvents(events);

        //FootprintStatistics footprintStatistics = new FootprintStatistics(this);
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

        return dfg;
    }

    @Override
    public boolean areEventsConnected(int a, int b) {
        return dfg.getDirectlyFollowsGraph().containsEdge(a,b);
    }
}
