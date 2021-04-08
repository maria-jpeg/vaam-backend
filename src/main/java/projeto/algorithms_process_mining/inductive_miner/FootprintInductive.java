package projeto.algorithms_process_mining.inductive_miner;

import lombok.Getter;
import org.glassfish.jersey.internal.guava.Sets;
import org.processmining.plugins.directlyfollowsgraph.DirectlyFollowsGraph;
import org.processmining.plugins.inductiveminer2.helperclasses.MultiIntSet;
import projeto.algorithms_process_mining.FootprintMatrix;
import projeto.algorithms_process_mining.ProcessMiningAlgorithm;
import projeto.core.Event;

import java.util.*;

public class FootprintInductive extends FootprintMatrix {

    @Getter
    protected Long[][] footprint;
    private DirectlyFollowsGraph dfg;

    public FootprintInductive(ProcessMiningAlgorithm algorithm, List<Event> events, DirectlyFollowsGraph directlyFollowsGraph, boolean statistics) {
        super(algorithm, new HashSet<String>(Arrays.asList(directlyFollowsGraph.getAllActivities())), statistics);

        this.dfg = directlyFollowsGraph;
        int numberOfEvents = eventNames.size();
        footprint = new Long[numberOfEvents][numberOfEvents];



        extractStartEndEvents(events);
    }

    @Override
    public boolean areEventsConnected(int a, int b) {
        return dfg.getDirectlyFollowsGraph().containsEdge(a,b);
    }
}
