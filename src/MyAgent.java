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
            Moves lm = new Moves(lastMove[0] - offSet, lastMove[1] - offSet, lastMove[2] - offSet, lastMove[3] - offSet);
            String roleOfLastPlayer;
            if (myTurn && role.equals("white") || !myTurn && role.equals("black")) {
                roleOfLastPlayer = "white";
            } else {
                roleOfLastPlayer = "black";
            }
            System.out.println(roleOfLastPlayer + " moved from " + (lm.x + offSet) + "," + (lm.y + offSet) + " to " + (lm.x2 + offSet) + "," + (lm.y2 + offSet));
            // TODO: 1. update your internal world model according to the action that was just executed
            env.doMove(env.currentState, lm);
            System.out.println("State after: " + env.currentState);
        }

        myTurn = !myTurn;

        if (myTurn) {
            // check if current player is correct
            if (!(role.equals("white") && env.currentState.isWhiteTurn || role.equals("black") && !env.currentState.isWhiteTurn)) {
                System.out.println("MyAgent : nextAction -> current player is not correct");
                // throw new RuntimeException("MyAgent : nextAction -> current player is not correct");
            }

            // TODO: 2. run alpha-beta search to determine the best move
            startTime = System.currentTimeMillis();
            move = bestMove(env.currentState);


            return "(move " + (move.x + offSet) + " " + (move.y + offSet) + " " + (move.x2 + offSet) + " " + (move.y2 + offSet) + ")";
        } else {
            return "noop";
        }
    }

    @Override
    public void cleanup() {
    }

    // TODO: change the way we generate legal moves, from which has most likely to happen,
    // TODO: forward, right, left
    // TODO: change the order from highest value - lowest and vice versa

    public State cloneState(State s) {
        short[][] newMap = new short[env.sizeX][env.sizeY];
        State state = new State();
        state.isWhiteTurn = s.isWhiteTurn;
        for (int i = 0; i < env.sizeX; i++) {
            if (env.sizeY >= 0) System.arraycopy(s.myMap[i], 0, newMap[i], 0, env.sizeY);
        }
        state.myMap = newMap;
        return state;
    }

    @Override
    public Moves bestMove(State state){
        int depth = 1;
        int loss = -100;
        int win = 100;
        Moves move = new Moves(0, 0, 0, 0);
        State clonedState = cloneState(state);

        do {
            try {
                move = negaMaxRoot(clonedState, depth, loss, win);
                depth++;
            }catch (TimeoutException e) {
                System.out.println(e.getMessage());
                break;

            }
        } while (true);
        // try {
        //     while(true){
        //         move = negaMaxRoot(clonedState, depth, loss, win);
        //         depth++;
        //     }

        // } catch (TimeoutException e) {
        //     System.out.println(e.getMessage());
        // }
        return move;
    }

    Moves negaMaxRoot(State state, int depth, int alpha, int beta) throws TimeoutException {
        if (((System.currentTimeMillis() - startTime) / 1000) >= playclock) {
            throw new TimeoutException("Timeout");
        }
        int bestVal = 0;
        int v = 0;
        Moves bestMove = null;
        List<Moves> moves = env.legalMoves(state);
        for (Moves m : moves) {
            env.doMove(state, m);
            v = -negamax(state, depth -1, -beta, -alpha);
            env.undoMove(state, m);
            if (v > bestVal){
                bestVal = v;
                bestMove = m;
                if (v>alpha) {
                    alpha = v;
                }
                if (alpha >= beta){
                    break;
                }
            }
        }
        return bestMove;
    }

    int negamax(State state, int depth, int alpha, int beta) throws TimeoutException {
        if (((System.currentTimeMillis() - startTime) / 1000) >= playclock) {
            throw new TimeoutException("Timeout");
        }
        if (depth <= 0 || env.isTerminalState(state)) {
            if (state.isWhiteTurn) {
                return env.eval(state);
            } else {
                return -env.eval(state);
            }
        }
        int bestVal = 0;
        int v = 0;
        List<Moves> moves = env.legalMoves(state);
        for (Moves m : moves) {
            env.doMove(state, m);
            v = -negamax(state, depth -1, -beta, -alpha);
            env.undoMove(state, m);
            if (v > bestVal){
                bestVal = v;
                if (v>alpha) {
                    alpha = v;
                }
                if (alpha >= beta){
                    break;
                }
            }
        }
        return bestVal;
    }
//
//    @Override
//    public Moves bestMove(State state) {
//        // TODO change to alpha beta
//        int bestVal = Integer.MAX_VALUE;
//        bestVal = -bestVal;
//        Moves bestMove = null;
//        int evaluation;
//        int negVal = Integer.MAX_VALUE;
//        negVal = -negVal;
//        State newS = cloneState(state);  // check if it is a deep copy of the state
//        try {
//            for (int depth = 1; ; depth++) {
//                List<Moves> moves = env.legalMoves(newS);
//                for (Moves m : moves) {
//                    env.doMove(newS, m);  // move and update the current state
//                    evaluation = -bestValue(newS, depth - 1, negVal, -bestVal);   // get the evaluation and do the recursive step.
//                    if (evaluation > bestVal) {  // if current eval is > best eval, then update bestEval and best move.
//                        bestVal = evaluation;
//                        bestMove = m;
//                        //TODO: missing pruning, if alpha >= beta, if we find a wining move.
//                    }
//                    env.undoMove(newS, m);
//                }
//            }
//        } catch (TimeoutException e) {
//            System.out.println(e.getMessage());
//        }
//        return bestMove;
//    }
//
//    public int bestValue(State state, int depth, int alpha, int beta) throws TimeoutException {
//        if (((System.currentTimeMillis() - startTime) / 1000) >= playclock) {
//            throw new TimeoutException("Timeout");
//        }
//        int bestEval = Integer.MAX_VALUE;
//        bestEval = -bestEval;
//        if (depth <= 0 || env.isTerminalState(state)) {
//            if (state.isWhiteTurn) {
//                return env.eval(state);
//            } else {
//                return -env.eval(state);
//            }
//        }
//        List<Moves> moves = env.legalMoves(state);
//        for (Moves m : moves) {
//            env.doMove(state, m);
//            bestEval = Math.max(bestEval, -bestValue(state, depth - 1, -beta, -alpha));
//            alpha = Math.max(alpha, bestEval);
//            if (alpha >= beta) { break; }
//            env.undoMove(state, m);
//        }
//
//        return bestEval;
//    }
}
