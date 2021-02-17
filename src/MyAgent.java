import java.util.List;

public class MyAgent implements Agent {
    private String role; // the name of this agent's role (white or black)
    private int playclock; // this is how much time (in seconds) we have before nextAction needs to return a move
    private boolean myTurn; // whether it is this agent's turn or not
    private int width, height; // dimensions of the board
    private boolean isWhiteTurn;
    private boolean isTerminalState;
    private Environment env;
    public Moves bestMove;

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
        bestMove = new Moves(0, 0, 0, 0);
    }

    // lastMove is null the first time nextAction gets called (in the initial state)
    // otherwise it contains the coordinates x1,y1,x2,y2 of the move that the last player did
    public String nextAction(int[] lastMove) {
        if (lastMove != null) {
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
//            if (!(role.equals("white") && env.currentState.isWhiteTurn || role.equals("black") && !env.currentState.isWhiteTurn)) {
//                System.out.println("MyAgent : nextAction -> current player is not correct");
//            }
//            int worstVal = Integer.MAX_VALUE;
//            if (!env.currentState.isWhiteTurn) {
//                worstVal = -worstVal;
//            }
            role = "white";
            Moves bestM;
            bestM = bestMove(env.currentState);
            System.out.println(bestM);
            // dfs_with_depth(, new Node(0, m));
            // TODO: 2. run alpha-beta search to determine the best move
            // look at RandomAgent to start
            // You should start with something "simple" Like DFS
            // Then go add on it more, For example: DFS -> DFS with iterative deepening
            // -> Minimax with iterative deepening -> Add pruning (alpha-beta search)
            // Remember to always test everything you do as soon as you can do it!!
            //int val = dfs_depth(3);
//            int val = minimax(3, myTurn);
            // For debugging purposes
//            System.out.println("Val: " + val);
//            System.out.println("BestMove: " + bestMove);

            return "(move " + bestM.x + " " + bestM.y + " " + bestM.x2 + " " + bestM.y2 + ")";
        } else {
            return "noop";
        }
    }

    public void setEnv(Environment env) {
        this.env = env;
    }

    public Moves bestMove(State state) {
        int bestValue = Integer.MAX_VALUE;
        bestValue = -bestValue;
        Moves bestMove = null;
        int estimatedValue;
        //List<Moves> moves = new List<Moves>();
        List<Moves> moves = env.legalMoves(state);
        System.out.println(moves);
        for (Moves m: moves) {
            env.doMove(env.currentState, m);
            estimatedValue = -bestValue(env.currentState, 1); //depth = 3
            env.undoMove(env.currentState, m);
            if(estimatedValue > bestValue) {
                bestValue = estimatedValue;
                bestMove = m;
            }
        }
        return bestMove;
    }

    public int bestValue(State s, int depth) {
        int bestValue = Integer.MAX_VALUE;
        bestValue = -bestValue;
        int estimatedValue;
        if(depth==0 ) {
            //System.out.println("BV: " + bestValue);
//            System.out.println("Eval: " + env.eval(env.currentState));
//            return env.eval(env.currentState);
            if(role.equals("white")) return env.eval(env.currentState);
            else return -env.eval(env.currentState);
        }
        List<Moves> moves = env.legalMoves(s);
//        System.out.println("Legal moves: " + moves);
//        System.out.println("At depth: " + depth);
        for (Moves m: moves) {
            env.doMove(env.currentState, m);

            estimatedValue = -bestValue(env.currentState, depth-1); //depth = 3
            env.undoMove(env.currentState, m);
           System.out.println("BV: " + estimatedValue);
            if(estimatedValue > bestValue) {
                bestValue = estimatedValue;
            }
        }
        System.out.println(bestValue);
        return bestValue;
    }

    @Override
    public void cleanup() {
    }

    /**
     * First implementation of the minimax algorithm
     * Known issues: It never arrives at a terminal state and moves.size() is never 0,
     */
    @Override
    public int dfs_depth(int depth) {
        if (depth == 0) {  // when it reaches the leaf or max-depth is achieved
//            System.out.println(env.currentState); // please uncomment this to see the strange behaviour.
            return env.eval(env.currentState);  // evaluation of that state
        }
        int bestEval = Integer.MAX_VALUE;
        bestEval = -bestEval;
        List<Moves> moves = env.legalMoves(env.currentState);  // all possible moves
        if (moves.size() == 0) { // if there are no possible moves, return 0
            if (isTerminalState) {  // if it is a terminal state, return the best value, can be a draw?
                // It never arrives here
                return bestEval;
            }
            return 0;
        }
        for (Moves m : moves) {
            env.doMove(env.currentState, m);  // move and update the current state
            int evaluation = -dfs_depth(depth - 1);   // get the evaluation and do the recursive step.
//            System.out.println("BestEval: " + bestEval + ", " + evaluation);
            if (evaluation > bestEval) {  // if current eval is > best eval, then update bestEval and best move.
                bestEval = evaluation;
                bestMove = m;
            }
            env.undoMove(env.currentState, m);
        }
        return bestEval;
    }

    /**
     * This is a second implementation of minimax, which now takes depth and the role of player.
     * In theory they both should return the same values but it does not.
     * We just tried different possible ways of implementing, thinking ahead for alfa-beta pruning.
     */
    @Override
    public int minimax(int depth, boolean maximizingPlayer) {
        if (depth == 0) {
//            System.out.println(env.currentState);
            return env.eval(env.currentState);
        }
        int bestEval = Integer.MAX_VALUE;
        if (maximizingPlayer) {
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
                int evaluation = minimax(depth - 1, false);
                System.out.println("MaxEval: " + bestEval + ", " + evaluation);
                if (evaluation > bestEval) {
                    bestEval = evaluation;
                    bestMove = m;
                }
                env.undoMove(env.currentState, m);
            }
        } else {
            List<Moves> moves = env.legalMoves(env.currentState);
            if (moves.size() == 0) {
                if (isTerminalState) {
                    return bestEval;
                }
                return 0;
            }
            for (Moves m : moves) {
                env.doMove(env.currentState, m);
                int evaluation = minimax(depth - 1, true);
                System.out.println("MinEval: " + bestEval + ", " + evaluation);
                if (evaluation > bestEval) {
                    bestEval = evaluation;
                    bestMove = m;
                }
                env.undoMove(env.currentState, m);
            }
        }
        return bestEval;
    }
//    @Override
//    public int search(int depth) {
//        if (depth == 0) {
//            System.out.println("Val: " + env.eval(env.currentState));
//            return env.eval(env.currentState);
//        }
//        int bestEval = Integer.MAX_VALUE;
//        bestEval = -bestEval;
//        List<Moves> moves = env.legalMoves(env.currentState);
//        if (moves.size() == 0) {
//            if (isTerminalState) {
//                return bestEval;
//            }
//            return 0;
//        }
//        for (Moves m : moves) {
//            env.doMove(env.currentState, m);
//            int evaluation = -search(depth - 1);
//            bestEval = Math.max(evaluation, bestEval);
//            env.undoMove(env.currentState, m);
//        }
//        return bestEval;
//    }
}
