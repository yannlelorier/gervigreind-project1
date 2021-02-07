import java.awt.*;

public class State {
    public Point point;
    public int pawn; //0 means that there is no pawn, 1 means white, 2 means black

    public State(int x, int y, int pawn) {
        this.point = new Point(x,y);
        this.pawn = pawn;
    }

    public Point getStateLocation() {
        return point;
    }

    public int getStatePawn() {
        return pawn;
    }

    @Override
    public String toString() {
        return "State{" +
                "point=" + point +
                ", pawn=" + pawn +
                '}';
    }
}
