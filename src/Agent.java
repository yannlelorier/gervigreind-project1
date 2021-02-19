
public interface Agent {
    public void init(String role, int width, int height, int playclock);

    public String nextAction(int[] lastmove);

    public void cleanup();

    public int minimax(int depth, boolean maximizingPlayer);

    public int dfs_depth(int depth);

    public void setEnv(Environment env);

//    public int search(int depth);
}
