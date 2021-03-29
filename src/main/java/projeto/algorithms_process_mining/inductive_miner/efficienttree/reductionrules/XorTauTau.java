package projeto.algorithms_process_mining.inductive_miner.efficienttree.reductionrules;


import projeto.algorithms_process_mining.inductive_miner.efficienttree.EfficientTree;
import projeto.algorithms_process_mining.inductive_miner.efficienttree.EfficientTreeMetrics;
import projeto.algorithms_process_mining.inductive_miner.efficienttree.EfficientTreeReductionRule;
import projeto.algorithms_process_mining.inductive_miner.efficienttree.EfficientTreeUtils;
import projeto.algorithms_process_mining.inductive_miner.efficienttree.UnknownTreeNodeException;

public class XorTauTau implements EfficientTreeReductionRule {

	public boolean apply(EfficientTree tree, int node) throws UnknownTreeNodeException {
		if (tree.isXor(node)) {
			//count the number of taus
			boolean tauSeen = false;

			//search for children that can produce epsilon
			for (int child : tree.getChildren(node)) {
				if (!tauSeen && !tree.isTau(child) && EfficientTreeMetrics.canProduceTau(tree, child)) {
					tauSeen = true;
					break;
				}
			}

			for (int child : tree.getChildren(node)) {
				if (tree.isTau(child)) {
					if (tauSeen) {
						//this is the second tau; remove it
						EfficientTreeUtils.removeChild(tree, node, child);
						return true;
					}
					tauSeen = true;
				}
			}
		}
		return false;
	}

}
