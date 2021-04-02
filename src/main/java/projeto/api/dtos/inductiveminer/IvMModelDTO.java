package projeto.api.dtos.inductiveminer;

import gnu.trove.map.TObjectIntMap;
import lombok.Getter;
import lombok.Setter;
import org.processmining.directlyfollowsmodelminer.model.DirectlyFollowsModel;
import org.processmining.plugins.inductiveVisualMiner.helperClasses.IvMEfficientTree;
import org.processmining.plugins.inductiveVisualMiner.helperClasses.IvMModel;
import org.processmining.processtree.ProcessTree;
import org.processmining.processtree.impl.ProcessTreeImpl;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
//Classe DTO para Inductive Visual Miner model
public class IvMModelDTO {
    public String[] int2Activity;
    public TObjectIntMap<String> activity2Int;
    public Iterable<Integer> nodes;
    public IvMEfficientTreeDTO tree;

    public IvMModelDTO(IvMModel model) {
        int2Activity = model.getTree().getInt2activity();
        activity2Int = model.getTree().getActivity2int();
        nodes = model.getAllNodes();
        tree = new IvMEfficientTreeDTO(model.getTree());
    }
}
