package projeto.algorithms_process_mining.inductive_miner;

import gnu.trove.set.TIntSet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.processmining.plugins.InductiveMiner.Pair;
import org.processmining.plugins.inductiveVisualMiner.helperClasses.IvMModel;
import projeto.controller.exceptions.ParsingException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

@Getter @Setter
public class IvMHelper {
    private HashMap<Integer,Integer> activityCardinalitiesComplete;
    private HashMap<Pair<Integer,Integer>,Integer> edgeCardinality;
    private HashMap<Pair<Integer,Integer>,Integer> deviationCardinality;
    private List<Pair<Integer,Integer>> startActivities;
    private List<Pair<Integer,Integer>> endActivities;
    private List<String> nodeNames;

    //private String[] activitiesComFreq;

    public IvMHelper(){
        activityCardinalitiesComplete = new HashMap<>();
        edgeCardinality = new HashMap<>();
        deviationCardinality = new HashMap<>();
        startActivities = new LinkedList<>();
        endActivities = new LinkedList<>();
        nodeNames = new LinkedList<>();
        //activitiesComFreq = new String[0];
    }

    public long getActivityCardinality(int node){
        return activityCardinalitiesComplete.get(node);
    }
    public long getEdgeCardinality(int from, int to){
        //Pode dar null? ou erro? tratar
        //Quando da erro, o problema não é aqui
        return edgeCardinality.get(Pair.of(from,to));
    }
    public boolean containsEdge(int from, int to){
        if (edgeCardinality.get(Pair.of(from,to))!=null){
            return true;
        }
        return false;
    }
    public boolean containsDeviation(int from, int to){
        if (deviationCardinality.get(Pair.of(from,to))!=null){
            return true;
        }
        return false;
    }
}
