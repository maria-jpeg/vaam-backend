package projeto.algorithms_process_mining.inductive_miner.efficienttree.reductionrules;

import projeto.algorithms_process_mining.inductive_miner.efficienttree.EfficientTree;
import projeto.algorithms_process_mining.inductive_miner.efficienttree.EfficientTreeReductionRule;
import projeto.algorithms_process_mining.inductive_miner.efficienttree.EfficientTreeUtils;

public class TauChildOfSeqAndInt implements EfficientTreeReductionRule {

	public boolean apply(EfficientTree tree, int node) {
		if (tree.isSequence(node) || tree.isConcurrent(node) || tree.isInterleaved(node)) {
			if (tree.getNumberOfChildren(node) > 1) {
				for (int child : tree.getChildren(node)) {
					if (tree.isTau(child)) {
						//remove tau
						EfficientTreeUtils.removeChild(tree, node, child);
						return true;
					}
				}
			}
		}
		return false;
	}

}
