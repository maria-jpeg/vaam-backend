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
@NoArgsConstructor
public class IvMHelper {
    private HashMap<Integer,Long> activityCardinalitiesComplete;
    private HashMap<Pair<Integer,Integer>,Integer> edgeCardinality;
    private IvMModel model;
    private List<Pair<Integer,Integer>> startActivities;
    private List<Pair<Integer,Integer>> endActivities;
    private String[] activitiesComFreq;

    public long getActivityCardinality(int node){
        return activityCardinalitiesComplete.get(node);
    }
    public long getEdgeCardinality(int from, int to){
        //Pode dar null? ou erro? tratar
        //Quando da erro, o problema não é aqui
        return edgeCardinality.get(Pair.of(from,to));
    }
}
