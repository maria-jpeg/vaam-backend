package projeto.algorithms_process_mining.inductive_miner.efficienttree.reductionrules;

import projeto.algorithms_process_mining.inductive_miner.efficienttree.EfficientTree;
import projeto.algorithms_process_mining.inductive_miner.efficienttree.EfficientTree.NodeType;
import projeto.algorithms_process_mining.inductive_miner.efficienttree.EfficientTreeMetrics;
import projeto.algorithms_process_mining.inductive_miner.efficienttree.EfficientTreeReductionRule;
import projeto.algorithms_process_mining.inductive_miner.efficienttree.UnknownTreeNodeException;

public class IntShortLanguage implements EfficientTreeReductionRule {

	public boolean apply(EfficientTree tree, int node) throws UnknownTreeNodeException {
		if (tree.isInterleaved(node)) {
			//no child should produce a trace of length two or longer
			for (int child : tree.getChildren(node)) {
				if (!EfficientTreeMetrics.traceLengthAtMostOne(tree, child)) {
					return false;
				}
			}

			//transform the interleaved operator into a parallel operator
			tree.setNodeType(node, NodeType.concurrent);
			return true;
		}
		return false;
	}

}
