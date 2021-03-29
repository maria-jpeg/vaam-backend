package projeto.algorithms_process_mining.inductive_miner.efficienttree.reductionrules;

import projeto.algorithms_process_mining.inductive_miner.efficienttree.EfficientTree;
import projeto.algorithms_process_mining.inductive_miner.efficienttree.EfficientTree.NodeType;
import projeto.algorithms_process_mining.inductive_miner.efficienttree.EfficientTreeReductionRule;

public class SingleChild implements EfficientTreeReductionRule {

	public boolean apply(EfficientTree tree, int node) {
		if (tree.isOperator(node) && tree.getNumberOfChildren(node) == 1) {
			//remove this node
			tree.copy(node + 1, node, tree.getMaxNumberOfNodes() - node - 1);
			tree.setNodeType(tree.getMaxNumberOfNodes() - 1, NodeType.skip);

			return true;
		}
		return false;
	}
}
