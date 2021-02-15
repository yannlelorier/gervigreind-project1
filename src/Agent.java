
public interface Agent {
    public void init(String role, int width, int height, int playclock);

    public String nextAction(int[] lastmove);

    public void cleanup();

    //    public int dfs_with_depth(int remainingDepth);
    public int dfs_depth(int depth);

    public void setEnv(Environment env);

    public int search(int depth);
}
