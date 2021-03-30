package projeto.algorithms_process_mining.inductive_miner.helperclasses;


public interface IntDfg extends Cloneable {

	public MultiIntSet getActivities();

	/**
	 * 
	 * @return The number of activities.
	 */
	public int getNumberOfActivities();

	/**
	 * Add an activity.
	 * 
	 * @param index
	 */
	public void addActivity(int index);

	/**
	 * Make sure an activity is in the graphs.
	 */
	public void touchActivity(int index);

	/**
	 * 
	 * @return The number of empty (epsilon) traces.
	 */
	public long getNumberOfEmptyTraces();

	/**
	 * Set the number of empty (epsilon) traces.
	 * 
	 * @param numberOfEmptyTraces
	 */
	public void setNumberOfEmptyTraces(long numberOfEmptyTraces);

	/**
	 * Adds empty traces.
	 * 
	 * @param cardinality
	 */
	public void addEmptyTraces(long cardinality);

	public boolean hasStartActivities();

	public boolean hasEndActivities();

	public IntDfg clone();

	/**
	 * Adds a directly follows graph edge (in each direction) for each parallel
	 * edge.
	 */
	public void collapseParallelIntoDirectly();

}
