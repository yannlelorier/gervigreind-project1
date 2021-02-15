import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyAgent implements Agent {
    private String role; // the name of this agent's role (white or black)
    private int playclock; // this is how much time (in seconds) we have before nextAction needs to return a move
    private boolean myTurn; // whether it is this agent's turn or not
    private int width, height; // dimensions of the board
    private boolean isWhiteTurn;
    private boolean isTerminalState;
    private Environment env;

    /*
        init(String role, int playclock) is called once before you have to select the first action. Use it to initialize the agent. role is either "white" or "black" and playclock is the number of seconds after which nextAction must return.
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
    }

    // lastMove is null the first time nextAction gets called (in the initial state)
    // otherwise it contains the coordinates x1,y1,x2,y2 of the move that the last player did
    public String nextAction(int[] lastMove) {
        if (lastMove != null) {
            boolean didKill = true;
            if (env.currentState.myMap[lastMove[2]][lastMove[3]] == 0) {
                didKill = true;
            }
            Moves lm = new Moves(lastMove[0], lastMove[1], lastMove[2], lastMove[3]);
            String roleOfLastPlayer;
            if (myTurn && role.equals("white") || !myTurn && role.equals("black")) {
                roleOfLastPlayer = "white";
            } else {
                roleOfLastPlayer = "black";
            }
            System.out.println(roleOfLastPlayer + " moved from " + lm.x + "," + lm.y + " to " + lm.x2 + "," + lm.y2);
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
            // Remember to always test everything you do as soon as you can do it!!

            return "noop"; // This has to be changed
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
    // todo: evaluate and find best value
    // todo: find best move based on the best value
    // todo: white - black = minimax

    @Override
    public int search(int depth) {
        if (depth == 0) {
            System.out.println("Val: " + env.eval(env.currentState));
            return env.eval(env.currentState);
        }
        int bestEval = Integer.MAX_VALUE;
        bestEval = -bestEval;
        List<Moves> moves = env.legalMoves(env.currentState);
        if (moves.size() == 0) {
            if (isTerminalState) {
                return bestEval;
            }
            return 0;
        }
        for (Moves m : moves) {
            env.doMove(env.currentState, m);
            int evaluation = -search(depth - 1);
            bestEval = Math.max(evaluation, bestEval);
            env.undoMove(env.currentState, m);
        }
        return bestEval;
    }

    @Override
    public int dfs_depth(int depth) {
        if (depth == 0) {
            return env.eval(env.currentState);
        }
        Moves bestMove = new Moves(-1, -1, -1, -1);
        int bestEval = Integer.MAX_VALUE;
        bestEval = -bestEval;
        List<Moves> moves = env.legalMoves(env.currentState);
        if (moves.size() == 0) {
            if (isTerminalState) {
                return bestEval;
            }
            return 0;
        }
        for (Moves m : moves) {
            env.doMove(env.currentState, m);
            int evaluation = -dfs_depth(depth - 1);
            bestEval = Math.max(evaluation, bestEval);
            env.undoMove(env.currentState, m);
        }
        return bestEval;
    }

    private static void traverseNodes(Node node, int depth) {
        if (node.parent != null) {
            System.out.println(node.m);
            traverseNodes(node.parent, depth - 1);
        } else {
            System.out.println(node.m);
        }
    }


//    @Override
//    public int dfs_with_depth(int remainingDepth) {
//        if (remainingDepth == 0) { // if we have reached our depth, we evaluate the state
//            System.out.println("Val: " + env.eval(env.currentState));
//            return env.eval(env.currentState);
//        } else { // else we have to continue going
//            // iterate through all available moves
//            if (env.currentState.isWhiteTurn) {
//                int maxEval = Integer.MAX_VALUE;
//                maxEval = -maxEval;
//                for (Moves m : env.legalMoves(env.currentState)) {
//                    env.doMove(env.currentState, m); // do the move to change the state
//                    int eval = dfs_with_depth(remainingDepth - 1);
//                    env.undoMove(env.currentState, m); // undo the move to get the old state back
//                    maxEval = Math.max(maxEval, eval);
//                }
//                return maxEval;
//            } else {
//                int minEval = Integer.MAX_VALUE;
//                for (Moves m : env.legalMoves(env.currentState)) {
//                    env.doMove(env.currentState, m); // do the move to change the state
//                    int eval = dfs_with_depth(remainingDepth - 1);
//                    env.undoMove(env.currentState, m); // undo the move to get the old state back
//                    minEval = Math.min(minEval, eval);
//                }
//                return minEval;
//            }
//        }
//    }
}
