import java.util.LinkedList;
import java.util.List;

public class Environment {
    protected int sizeX, sizeY; // does not change
    protected State currentState;
    protected short[][] map;
    public Environment(int w, int h) {
        initFromInput(w, h);
    } // w = column, h = row

    // this does not need to be fast code since it is only run once
    public void initFromInput(int w, int h){
        currentState = new State();
        map = new short[w][h];
        // initialize the map
        for (int i = 0; i < w; i++){ // for each column
            for (int j = 0; j < h; j++){ // for each row
                if (j < 2) { // if first 2 rows we place white
                    map[i][j] = 1; // 1 is for white pieces
                }
                else if (j > (h - 3)) {
                    map[i][j] = 2; // 2 is for black pieces
                }
                else {
                    map[i][j] = 0; // else empty
                }
            }
        }
        currentState.myMap = map;
    }

    // get all moves for current player
    public List<Moves> legalMoves(State state) {
        List<Moves> moves = new LinkedList<Moves>();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (state.isWhiteTurn && map[i][j] == 1 || !state.isWhiteTurn && map[i][j] == 2) {
                    moves.addAll(getMoves(state, i, j));
                }
            }
        }
        return moves;
    }

    // find all moves for piece located at x, y
    public List<Moves> getMoves(State s, int x, int y) {
        List<Moves> moves = new LinkedList<Moves>();
        if (s.isWhiteTurn) {
            if (y + 1 < s.myMap[0].length - 1) {
                //forward
                if (s.myMap[x][y+1] == 0) {
                    moves.add(new Moves(x,y, x, y+1));
                }
                //right diagonal
                if (s.myMap[x+1][y+1]==2 && (x+1 < s.myMap.length-1)) {
                    moves.add(new Moves(x,y,x+1, y+1));
                }
                //left diagonal
                if (s.myMap[x-1][y+1]==2 && (x-1 >= 0)) {
                    moves.add(new Moves(x,y,x-1,y+1));
                }
            }
            
        }else { //black's turn
            if (y - 1 >= 0) {
                //forward
                if (s.myMap[x][y-1] == 0) {
                    moves.add(new Moves(x,y, x, y-1));
                }
                //right diagonal
                if (s.myMap[x+1][y-1]==1 && (x+1 < s.myMap.length-1)) {
                    moves.add(new Moves(x,y,x+1, y-1));
                }
                //left diagonal
                if (s.myMap[x-1][y-1]==1 && (x-1 >= 0)) {
                    moves.add(new Moves(x,y,x-1,y-1));
                }
            }
        }
        return moves;
    }

    public State getNextState(State s, Moves m){
        State c = s.clone();
        // TODO
        if (c.isWhiteTurn) {
            c.myMap[m.x2][m.y2] = 2;
        } else {
            c.myMap[m.x2][m.y2] = 1;
        }
        c.myMap[m.x][m.y] = 0;
        c.isWhiteTurn = !c.isWhiteTurn;
        return c;
    }

    public int eval(State s) {
        // Set your own evaluation here
        // this should not be done prior to State and Environment
        int e = 0;
        // example evaluation
        int blackPieces = 0;
        int whitePieces = 0;

        for (int i = 0; i < s.myMap.length; i++){ // for each column
            for (int j = 0; j < s.myMap[0].length; j++){ // for each row
                if (s.myMap[i][j] == 1){whitePieces++;}
                else if (s.myMap[i][j] == 2){blackPieces++;}
            }
        }
        e = whitePieces - blackPieces;
        if (!s.isWhiteTurn){
            e = -e; // negate the score
        }
        return e;
    }

    // print out current state for this environment. Good for testing.
    public String toString() {
        return currentState.toString();
    }

}
