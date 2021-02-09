import java.util.HashSet;

/**
 * This class holds all information about the state of the environment and the robot
 */
public class State implements Cloneable {
    // TODO: change state according to our needs
    public HashSet<Coordinates> whitePawn;
    public HashSet<Coordinates> blackPawn;
    public HashSet<Coordinates> board;
    public int width, height;

    public State() {
//        whitePawn = new HashSet<Coordinates>();
//        blackPawn = new HashSet<Coordinates>();
        board = new HashSet<Coordinates>();
        width = 3;
        height = 3;
    }


    @SuppressWarnings("unchecked")
    public State clone() {
        State cloned;
        try {
            cloned = (State) super.clone();
            cloned.whitePawn = (HashSet<Coordinates>) whitePawn.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            System.exit(-1);
            cloned = null;
        }
        return cloned;
    }


    public String toString() {
        return "State{ This is the state }";
    }

    public boolean equals(Object o) {
        if (!(o instanceof State)) {
            return false;
        }
        State s = (State) o;
        return false;
    }

    public int hashCode() {
        return whitePawn.hashCode() ^ width ^ height ^ blackPawn.hashCode();
    }
}