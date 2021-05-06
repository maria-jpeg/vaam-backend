package projeto.algorithms_process_mining.inductive_miner;

import gnu.trove.set.TIntSet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.processmining.plugins.InductiveMiner.Pair;
import org.processmining.plugins.inductiveVisualMiner.helperClasses.IvMModel;

import java.util.HashMap;
@Getter @Setter
@NoArgsConstructor
public class IvMHelper {
    private HashMap<Integer,Long> activityCardinalitiesComplete;
    private HashMap<Pair<Integer,Integer>,Long> edgeCardinality;
    private IvMModel model;

    public long getActivityCardinality(int node){
        return activityCardinalitiesComplete.get(node);
    }
    public long getEdgeCardinality(int from, int to){
        //Pode dar null? ou erro? tratar
        return edgeCardinality.get(Pair.of(from,to));
    }
}
