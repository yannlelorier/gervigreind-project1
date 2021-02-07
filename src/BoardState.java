import java.util.ArrayList;

public class BoardState {
    public ArrayList<State> board;

    public BoardState(int width, int height) {
        board = new ArrayList<>();
        for(int i = 0; i<height; i++) {
            for(int j = 0; j<width; j++) {
                if(i < 2) {
                    State s = new State(j,i,1); // white pawns
                    board.add(s);
                } else if (i > height - 3) {
                    State s = new State(j,i,2); //black pawns
                    board.add(s);
                } else {
                    State s = new State(j,i,0); //there is no pawn on this location
                    board.add(s);
                }
            }
        }
    }
}
