package projeto.algorithms_process_mining.inductive_miner.efficienttree.reductionrules;


import projeto.algorithms_process_mining.inductive_miner.efficienttree.EfficientTree;
import projeto.algorithms_process_mining.inductive_miner.efficienttree.EfficientTreeReductionRule;
import projeto.algorithms_process_mining.inductive_miner.efficienttree.EfficientTreeUtils;
import projeto.algorithms_process_mining.inductive_miner.efficienttree.UnknownTreeNodeException;

public class XorDoubleSingleChild implements EfficientTreeReductionRule {

	public boolean apply(EfficientTree tree, int node) throws UnknownTreeNodeException {
		if (tree.isXor(node)) {
			boolean[] activitiesSeen = new boolean[tree.getInt2activity().length];

			for (int child : tree.getChildren(node)) {
				if (tree.isActivity(child)) {
					int activity = tree.getActivity(child);
					if (!activitiesSeen[activity]) {
						activitiesSeen[activity] = true;
					} else {
						//remove this activity
						EfficientTreeUtils.removeChild(tree, node, child);
						return true;
					}
				}
			}
		}

		return false;
	}

}
