package projeto.algorithms_process_mining.inductive_miner.efficienttree;

public interface EfficientTreeReductionRule {
	/**
	 * Apply the reduction rule on tree, on the node at position i.
	 * 
	 * @param tree
	 * @param node
	 * @return whether the tree was changed or not
	 * @throws UnknownTreeNodeException
	 */
	public boolean apply(EfficientTree tree, int node) throws UnknownTreeNodeException;
}
