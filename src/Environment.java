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
    public void initFromInput(int w, int h) {
        sizeX = w;
        sizeY = h;
        currentState = new State();
        map = new short[w][h];
        // initialize the map
        for (int i = 0; i < w; i++) { // for each column
            for (int j = 0; j < h; j++) { // for each row
                if (j < 2) { // if first 2 rows we place white
                    map[i][j] = 1; // 1 is for white pieces
                } else if (j > (h - 3)) {
                    map[i][j] = 2; // 2 is for black pieces
                } else {
                    map[i][j] = 0; // else empty
                }
            }
        }
        currentState.myMap = map;
    }

    // get all moves for current player
    public List<Moves> legalMoves(State state) {
        List<Moves> moves = new LinkedList<Moves>();
        // iterate through the list
        for (int i = 0; i < map.length; i++) { // for each column
            for (int j = 0; j < map[0].length; j++) { // for each row
                if (state.isWhiteTurn && map[i][j] == 1) { // if we find a white piece when it is white's turn
                    moves.addAll(getMoves(state, i, j)); // add all moves if any for that piece
                } else if (!state.isWhiteTurn && map[i][j] == 2) { // else if it is black turn and we found a black piece
                    moves.addAll(getMoves(state, i, j)); // add all moves if any for that piece
                }
            }
        }
        return moves;
    }

    // find all moves for piece located at x, y
    public List<Moves> getMoves(State s, int x, int y) {
        List<Moves> moves = new LinkedList<Moves>();
        if (s.isWhiteTurn) { // if it is white we check the row above the piece
            if (y < s.myMap[0].length - 1) { // see if we are not at black's end of the map to avoid null pointers
                // left diagonal
                if (x > 0) { // if we are not at the LEFT end of the map then we check if we can go diagonal left
                    if (s.myMap[x - 1][y + 1] == 2) { // if the top left square is either empty or has black piece (not white piece) we add the move
                        moves.add(new Moves(x, y, x - 1, y + 1));
                    }
                }
                // right diagonal
                if (x < s.myMap.length - 1) { // if we are not at the RIGHT end of the map then we check if we can go diagonal right
                    if (s.myMap[x + 1][y + 1] == 2) { // if the top right square is either empty or has black piece (not white piece) we add the move
                        moves.add(new Moves(x, y, x + 1, y + 1));
                    }
                }
                // forward
                if (s.myMap[x][y + 1] == 0) { // if the square in front of us is empty
                    moves.add(new Moves(x, y, x, y + 1));
                }
            }
        } else {
            if (y > 0) { // see if we are not at white's end of the map to avoid null pointers
                // left diagonal
                if (x > 0) { // if we are not at the LEFT end of the map then we check if we can go diagonal left
                    if (s.myMap[x - 1][y - 1] == 1) { // if the bottom left square is either empty or has white piece (not black piece) we add the move
                        moves.add(new Moves(x, y, x - 1, y - 1));
                    }
                }
                // right diagonal
                if (x < s.myMap.length - 1) { // if we are not at the RIGHT end of the map then we check if we can go diagonal right
                    if (s.myMap[x + 1][y - 1] == 1) { // if the bottom right square is either empty or has white piece (not black piece) we add the move
                        moves.add(new Moves(x, y, x + 1, y - 1));
                    }
                }
                // forward
                if (s.myMap[x][y - 1] == 0) { // if the square below is empty
                    moves.add(new Moves(x, y, x, y - 1));
                }
            }
        }
        return moves;
    }


    public void doMove(State s, Moves m) {
        if (s.isWhiteTurn) {
            if (s.myMap[m.x][m.y] != 1) { // should never happen, means we are not trying to move white piece
                System.out.println("-Error- Environment : doMove() -> c.myMap[" + m.x + "][" + m.y + "] != 1");
            } else {
                s.myMap[m.x2][m.y2] = 1; // place the new piece and (possibly) replace the black piece
                s.myMap[m.x][m.y] = 0; // make the old square empty
            }
        } else {
            if (s.myMap[m.x][m.y] != 2) { // should never happen, means we are not trying to move black piece
                System.out.println("-Error- Environment : doMove() -> c.myMap[" + m.x + "][" + m.y + "] != 2");
            } else {
                s.myMap[m.x2][m.y2] = 2; // place the new piece and (possibly) replace the black piece
                s.myMap[m.x][m.y] = 0; // make the old square empty
            }
        }
        s.isWhiteTurn = !s.isWhiteTurn;
    }

    public void undoMove(State s, Moves m) {
        if (!s.isWhiteTurn) {
            if (s.myMap[m.x2][m.y2] != 1) { // should never happen, means we are not trying to move white piece
                System.out.println("-Error- Environment : undoMove() -> c.myMap[" + m.x2 + "][" + m.y2 + "] != 1");
            } else {
                s.myMap[m.x][m.y] = 1; // place the new piece and (possibly) replace the black piece
                s.myMap[m.x2][m.y2] = 0; // Make it empty square again
                if (m.x != m.x2) {
                    s.myMap[m.x2][m.y2] = 2; // undo the killing
                }
            }
        } else {
            if (s.myMap[m.x2][m.y2] != 2) { // should never happen, means we are not trying to move black piece
                System.out.println("-Error- Environment : undoMove() -> c.myMap[" + m.x2 + "][" + m.y2 + "] != 2");
            } else {
                s.myMap[m.x][m.y] = 2; // place the new piece and (possibly) replace the black piece
                s.myMap[m.x2][m.y2] = 0; // make the old square empty
                if (m.x != m.x2) {
                    s.myMap[m.x2][m.y2] = 1; // make the old square empty
                }
            }
        }
        s.isWhiteTurn = !s.isWhiteTurn;
    }

    public int eval(State s) {
        // Set your own evaluation here
        // this should not be done prior to State and Environment
        int e = 0;
        // example evaluation
        int blackPieces = 0;
        int whitePieces = 0;

        for (int i = 0; i < s.myMap.length; i++) { // for each column
            for (int j = 0; j < s.myMap[0].length; j++) { // for each row
                if (s.myMap[i][j] == 1) {
                    whitePieces++;
                } else if (s.myMap[i][j] == 2) {
                    blackPieces++;
                }
            }
        }
        e = whitePieces - blackPieces;
//        if (!s.isWhiteTurn) {
//            e = -e; // negate the score
//        }
        return e;
    }

    // print out current state for this environment. Good for testing.
    public String toString() {
        return currentState.toString();
    }

    public boolean isTerminalState(State s) {
        // first we check if this is a winning state
        for (int i = 0; i < sizeX; i++) { // for every column
            if (s.myMap[i][0] == 2 || s.myMap[i][sizeY - 1] == 1) {
                return true;
            }
        }
        // then we see if current player has no available moves
        if (legalMoves(s).isEmpty()) {
            //System.out.println("isTerminalState -> legalMoves are empty");
            return true;
        }
        return false; // if not this is not a terminal state

    }

}
