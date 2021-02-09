import java.awt.*;
import java.util.Map;
import java.util.Random;

public class MyAgent implements Agent {
    private String role; // the name of this agent's role (white or black)
    private int playclock; // this is how much time (in seconds) we have before nextAction needs to return a move
    private boolean myTurn; // whether it is this agent's turn or not
    private int width, height; // dimensions of the board
    private State state;
    public int xTest = 1;
    public int yTest = 2;

    Random random = new Random();

    public void init(String role, int width, int height, int playclock) {
        this.role = role;
        this.playclock = playclock;
        myTurn = !role.equals("white");
        this.width = width;
        this.height = height;
        state = new State(width, height);
        // TODO: add your own initialization code here
    }

    public String nextAction(int[] lastMove) {
        if (lastMove != null) {
            int x1 = lastMove[0], y1 = lastMove[1], x2 = lastMove[2], y2 = lastMove[3];
            String roleOfLastPlayer;
            if (myTurn && role.equals("white") || !myTurn && role.equals("black")) {
                roleOfLastPlayer = "white";
            } else {
                roleOfLastPlayer = "black";
            }
            System.out.println(roleOfLastPlayer + " moved from " + x1 + "," + y1 + " to " + x2 + "," + y2);
            // TODO: 1. update your internal world model according to the action that was just executed
            Point pointToUpdate = new Point(x1, y1);
//            int pawnState = state.board.get(pointToUpdate);
            //System.out.println("Pawn:" + pawnState);
//            state.board.replace(pointToUpdate, 0);

            Point pointDestination = new Point(x2, y2);
//            state.board.replace(pointDestination, pawnState);

            /*
             *for(Map.Entry<Point, Integer> entry : boardState.board.entrySet()) {
              System.out.println("X: " + entry.getKey().getX() + " | Y: " + entry.getKey().getY() + " |" + " Pawn: " + entry.getValue());
            }
            */

        }

        // update turn (above that line it myTurn is still for the previous state)
        myTurn = !myTurn;
        if (myTurn) {

            // TODO: 2. run alpha-beta search to determine the best move
            // Here we just construct a random move (that will most likely not even be possible),
            // this needs to be replaced with the actual best move.
            xTest = 1;
            if (role.equals("white")) {
                yTest = random.nextInt(height - 1);
                yTest = yTest + 1;
            } else {
                yTest = random.nextInt(height - 1) + 2;
                yTest = yTest - 1;
            }
            return "(move " + xTest + " " + yTest + " " + xTest + " " + yTest + ")";
        } else {
            return "noop";
        }
    }

    @Override
    public void cleanup() {

    }

}
