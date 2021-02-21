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
    //    public Moves move;
    public Moves bestM = new Moves(-2, -2, -2, -3);
    public Moves bestMove = new Moves(-1, -1, -1, -2);
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
                throw new RuntimeException("MyAgent : nextAction -> current player is not correct");
            }

            // TODO: 2. run alpha-beta search to determine the best move
            startTime = System.currentTimeMillis();
            Moves move = bestMove(env.currentState);
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
            System.arraycopy(s.myMap[i], 0, newMap[i], 0, env.sizeY);
        }
        state.myMap = newMap;
        return state;
    }

    @Override
    public Moves bestMove(State state) {
        int depth = 1;
        int loss = -1000;
        int win = 1000;

        State clonedState = cloneState(state);
        do {
            try {
                bestMove = negamaxRoot(clonedState, depth, loss, win);
                depth++;
            } catch (TimeoutException e) {
                System.out.println(e.getMessage());
                break;
            }
        } while (true);
        return bestMove;
    }

    Moves negamaxRoot(State state, int depth, int alpha, int beta) throws TimeoutException {
        int bestVal = 0;
        int v = 0;

        List<Moves> moves = env.legalMoves(state);
        for (Moves m : moves) {
            env.doMove(state, m);
            v = -negamax(state, depth - 1, -beta, -alpha);
            env.undoMove(state, m);
            if (v > bestVal) {
                bestVal = v;
                bestM = m;
                System.out.println(bestM.toString());
                if (v > alpha) {
                    alpha = v;
                }
                if (alpha >= beta) {
                    break;
                }
            }
        }
        return bestM;
    }

    int negamax(State state, int depth, int alpha, int beta) throws TimeoutException {
        if (((System.currentTimeMillis() - startTime) / 1000) >= playclock) {
            throw new TimeoutException("Timed out!");
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
            v = -negamax(state, depth - 1, -beta, -alpha);
            env.undoMove(state, m);
            if (v > bestVal) {
                bestVal = v;
                if (v > alpha) {
                    alpha = v;
                }
                if (alpha >= beta) {
                    break;
                }
            }
        }
        return bestVal;
    }
}