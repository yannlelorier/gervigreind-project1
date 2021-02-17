import java.util.List;
import java.util.concurrent.TimeoutException;

public class MyAgent implements Agent {
    private String role; // the name of this agent's role (white or black)
    private int playclock; // this is how much time (in seconds) we have before nextAction needs to return a move
    private boolean myTurn; // whether it is this agent's turn or not
    private int width, height; // dimensions of the board
    private boolean isWhiteTurn;
    private boolean isTerminalState;
    private Environment env;
    public Moves move;
    long startTime;
    int offSet;

    /*
     init(String role, int playclock) is called once before you have to select the first action.
     Use it to initialize the agent. role is either "white" or "black" and playclock is the
     number of seconds after which nextAction must return.
    */
    public void init(String role, int width, int height, int playclock) {
        this.role = role;
        this.playclock = playclock;
        myTurn = !role.equals("white");
        this.width = width;
        this.height = height;
        isWhiteTurn = true;
        isTerminalState = false;
        env = new Environment(width, height);
        move = new Moves(0, 0, 0, 0);
        offSet = 1;
    }

    // lastMove is null the first time nextAction gets called (in the initial state)
    // otherwise it contains the coordinates x1,y1,x2,y2 of the move that the last player did
    public String nextAction(int[] lastMove) {
        System.out.println(">>>>>>>>>>>>\n" + env.currentState);
        if (lastMove != null) {
            Moves lm = new Moves(lastMove[0] - 1, lastMove[1] - 1, lastMove[2] - 1, lastMove[3] - 1);
            String roleOfLastPlayer;
            if (myTurn && role.equals("white") || !myTurn && role.equals("black")) {
                roleOfLastPlayer = "white";
            } else {
                roleOfLastPlayer = "black";
            }
            System.out.println(roleOfLastPlayer + " moved from " + (lm.x + offSet) + "," + (lm.y + offSet) + " to " + (lm.x2 + offSet) + "," + (lm.y2 + offSet));
            // TODO: 1. update your internal world model according to the action that was just executed
            env.doMove(env.currentState, lm);
        }

        // update turn (above that line it myTurn is still for the previous state)
        myTurn = !myTurn;
        if (myTurn) {
            // check if current player is correct
            if (!(role.equals("white") && env.currentState.isWhiteTurn || role.equals("black") && !env.currentState.isWhiteTurn)) {
                System.out.println("MyAgent : nextAction -> current player is not correct");
            }
            int worstVal = Integer.MAX_VALUE;
            if (!env.currentState.isWhiteTurn) {
                worstVal = -worstVal;
            }

            // dfs_with_depth(, new Node(0, m));
            // TODO: 2. run alpha-beta search to determine the best move
            // look at RandomAgent to start
            // You should start with something "simple" Like DFS
            // Then go add on it more, For example: DFS -> DFS with iterative deepening
            // -> Minimax with iterative deepening -> Add pruning (alpha-beta search)
            // Remember to always test everything you do as soon as you can do it!
            startTime = System.currentTimeMillis();
            move = dfs_depthRoot(env.currentState);

            return "(move " + (move.x + offSet) + " " + (move.y + offSet) + " " + (move.x2 + offSet) + " " + (move.y2 + offSet) + ")";
        } else {
            return "noop";
        }
    }

    public void setEnv(Environment env) {
        this.env = env;
    }

    @Override
    public void cleanup() {
    }

    public State cloneState(State s) {
        State state = new State();
        state.isWhiteTurn = s.isWhiteTurn;
        state.myMap = s.myMap;
        return state;
    }

    public Moves dfs_depthRoot(State state) {
        int bestVal = Integer.MAX_VALUE;
        bestVal = -bestVal;
        Moves bestMove = null;
        int evaluation;
        State newS = cloneState(env.currentState);
        System.out.println("State>###################: \n" + newS + "\n");
        try {
            for (int depth = 1; ; depth++) {
                List<Moves> moves = env.legalMoves(state);
                for (Moves m : moves) {
                    env.doMove(state, m);  // move and update the current state
                    int negVal = Integer.MAX_VALUE;
                    negVal = -negVal;
                    evaluation = dfs_depth(env.currentState, depth - 1, negVal, -bestVal);   // get the evaluation and do the recursive step.
                    if (evaluation > bestVal) {  // if current eval is > best eval, then update bestEval and best move.
                        bestVal = evaluation;
                        bestMove = m;
                    }
                    env.undoMove(state, m);
                }
            }
        } catch (TimeoutException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("new current state: " + env.currentState);
        System.out.println("old STate: " + newS);
        env.currentState = newS;
        return bestMove;
    }

    @Override
    public int dfs_depth(State state, int depth, int alpha, int beta) throws TimeoutException {
        if (((System.currentTimeMillis() - startTime) / 1000) >= playclock) {
            throw new TimeoutException("Timeout");
        }
        int bestEval = Integer.MAX_VALUE;
        bestEval = -bestEval;
        if (depth <= 0 || env.isTerminalState(state)) {
            if (role.equals("white")) {
                return env.eval(state);
            } else {
                return -env.eval(state);
            }
        }
        List<Moves> moves = env.legalMoves(state);
        for (Moves m : moves) {
            env.doMove(state, m);
            bestEval = Math.max(bestEval, -dfs_depth(env.currentState, depth - 1, -beta, -alpha));
            alpha = Math.max(alpha, bestEval);
            if (alpha > beta) {
                break;
            }
            env.undoMove(state, m);
        }

        return bestEval;
    }
}
