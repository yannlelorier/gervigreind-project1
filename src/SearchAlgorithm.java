import java.util.List;

public interface SearchAlgorithm {

    /**
     * searches for a goal state in the given environment, starting in the current state of the environment,
     * stores the resulting plan and keeps track of nb. of node expansions, max. size of the frontier and cost of best solution found
     * @param env
     */
    public void doSearch(Environment env);

    /**
     *
     * @return the plan found, the last time doSearch() was executed
     */
    public List<Action> getPlan();

    /**
     *
     * @return the number of node expansions of the last search that was executed
     */
    public int getNbNodeExpansions();  // maybe we don't need this

    /**
     *
     * @return the maximal size of the frontier the last search that was executed
     */
    public int getMaxFrontierSize();  // ?

    /**
     *
     * @return the cost of the plan that was found
     */
    public int getPlanCost();   // ?

}