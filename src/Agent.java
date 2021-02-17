import java.util.concurrent.TimeoutException;

public interface Agent {
    public void init(String role, int width, int height, int playclock);

    public String nextAction(int[] lastmove);

    public void cleanup();

    public int dfs_depth(State s, int depth, int alpha, int beta) throws TimeoutException;

    public void setEnv(Environment env);

//    public int search(int depth);
}
