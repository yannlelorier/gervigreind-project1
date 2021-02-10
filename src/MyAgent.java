import java.util.ArrayList;
import java.util.HashMap;

public class MyAgent implements Agent
{
    private String role; // the name of this agent's role (white or black)
    private int playclock; // this is how much time (in seconds) we have before nextAction needs to return a move
    private boolean myTurn; // whether it is this agent's turn or not
    private int width, height; // dimensions of the board
    private boolean isWhiteTurn;
    private boolean isTerminalState;
    private Environment env;
    private ArrayList<Node> frontierList;
    private int sizeOfTable = 1000;

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
        frontierList = new ArrayList<Node>();
        // TODO: add your own initialization code here

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
            env.currentState = env.getNextState(env.currentState, lm); // TODO: 1. update your internal world model according to the action that was just executed
        }

        // update turn (above that line it myTurn is still for the previous state)
        myTurn = !myTurn;
        if (myTurn) {
            int depth = 1;

            while (true){ // some loop that goes on until a RuntimeException is thrown.
                try{
                    // do your search here
                    return "noop"; // This has to be changed
                }catch(RuntimeException e){

                }
                depth ++;

            }

            //int alpha = Integer.MAX_VALUE;
            //int beta = -Integer.MAX_VALUE; // you cannot do Integer.Min_Value since if you do -MinValue you will overflow the buffer.
            // TODO: 2. run alpha-beta search to determine the best move
            // look at RandomAgent to understand what to return
            // You should start with something "simple" Like DFS
            // Then go add on it more, For example: DFS -> DFS with iterative deepening
            // -> Minimax with iterative deepening -> Add pruning (alpha-beta search)
            // Remember to always test everything you do as soon as you can do it!!

            //return "noop"; // This has to be changed
        } else {
            return "noop";
        }
    }

    public Node findNextNodeToExpand() {
        if (frontierList.isEmpty()){
            System.out.println("MyAgent : findNextNodeToExpand() -> frontierList is empty");
            return null;
        }
        else{
            // todo
            return null;
        }
    }

    public void expandNode() {
        boolean isTimeUp = true; // of course change this
        if (isTimeUp){
            throw new RuntimeException();
        }
        // for each available move...
        // update frontier list

        // ...

    }


    // is called when the game is over or the match is aborted
    @Override
    public void cleanup() {
        // TODO: cleanup so that the agent is ready for the next match
    }

}
