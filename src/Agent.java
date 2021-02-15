
public interface Agent
{
    public void init(String role, int width, int height, int playclock);
    public String nextAction(int[] lastmove);
    public void cleanup();
    public void dfs_with_depth(int remainingDepth, Node parentNode);
    public void setEnv(Environment env);
}
