package projeto.api.dtos.inductiveminer;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import lombok.Getter;
import lombok.Setter;
import org.processmining.plugins.inductiveVisualMiner.helperClasses.IvMEfficientTree;
import org.processmining.plugins.inductiveVisualMiner.helperClasses.TreeUtils;
import org.processmining.processtree.Node;
import org.processmining.processtree.ProcessTree;
import org.processmining.processtree.conversion.ProcessTree2Petrinet;
import org.processmining.processtree.impl.ProcessTreeImpl;

import java.util.List;

@Getter
@Setter
public class IvMEfficientTreeDTO {
    public ProcessTreeImpl ptImpl;

    public IvMEfficientTreeDTO(IvMEfficientTree tree) {
        ptImpl = new ProcessTreeImpl();
        ptImpl.setRoot(tree.getDTree().getRoot());
        for (Node node : tree.getDTree().getNodes() ) {
            ptImpl.addNode(node);
        }

        System.out.println(123);
    }
}
