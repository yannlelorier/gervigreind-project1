import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Environment {
    protected int sizeX, sizeY;
    protected State currentState;
    protected short[][] map;

    public Environment(int w, int h) {
        initFromInput(w, h);
    }

    public void initFromInput(int w, int h) {
        sizeX = w;
        sizeY = h;
        currentState = new State();
        map = new short[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (j < 2) {
                    map[i][j] = 1;
                } else if (j > (h - 3)) {
                    map[i][j] = 2;
                } else {
                    map[i][j] = 0;
                }
            }
        }
        currentState.myMap = map;
    }

    public List<Moves> legalMoves(State state) {
        List<Moves> moves = new LinkedList<Moves>();
        if (state.isWhiteTurn) {
            int w = state.myMap.length;
            int h = state.myMap[0].length;
            for (int i = 0; i < w; i++) {
                for (int j = 0; j < h; j++) {
                    if (state.myMap[i][j] == 1) {
                        moves.addAll(getMoves(state, i, j));
                    }
                }
            }
        } else {
            int w = state.myMap.length;
            int h = state.myMap[0].length;
            for (int i = w - 1; 0 <= i; i--) {
                for (int j = h - 1; 0 <= j; j--) {
                    if (state.myMap[i][j] == 2) {
                        moves.addAll(getMoves(state, i, j));
                    }
                }
            }
        }
        return moves;
    }

//    for (int i = 0; i < w; i++) {
//        for (int j = 0; j < h; j++) {
//            if (state.myMap[i][j] == 2) {
//                moves.addAll(getMoves(state, i, j));
//            }
//        }
//    }

    public List<Moves> getMoves(State s, int x, int y) {
        List<Moves> moves = new LinkedList<Moves>();
        if (s.isWhiteTurn) {
            if (y < s.myMap[0].length - 1) {
                // left diagonal
                if (x > 0) {
                    if (s.myMap[x - 1][y + 1] == 2) {
                        moves.add(new Moves(x, y, x - 1, y + 1));
                    }
                }
                // right diagonal
                if (x < s.myMap.length - 1) {
                    if (s.myMap[x + 1][y + 1] == 2) {
                        moves.add(new Moves(x, y, x + 1, y + 1));
                    }
                }
                // forward
                if (s.myMap[x][y + 1] == 0) {
                    moves.add(new Moves(x, y, x, y + 1));
                }
            }
        } else {
            if (y > 0) {
                // left diagonal
                if (x > 0) {
                    if (s.myMap[x - 1][y - 1] == 1) {
                        moves.add(new Moves(x, y, x - 1, y - 1));
                    }
                }
                // right diagonal
                if (x < s.myMap.length - 1) {
                    if (s.myMap[x + 1][y - 1] == 1) {
                        moves.add(new Moves(x, y, x + 1, y - 1));
                    }
                }
                // forward
                if (s.myMap[x][y - 1] == 0) {
                    moves.add(new Moves(x, y, x, y - 1));
                }
            }
        }
        return moves;
    }


    public void doMove(State s, Moves m) {
        if (s.isWhiteTurn) {
            if (s.myMap[m.x][m.y] != 1) {
                System.out.println("-Error- Environment : doMove() -> c.myMap[" + m.x + "][" + m.y + "] != 1");
            } else {
                s.myMap[m.x2][m.y2] = 1;
                s.myMap[m.x][m.y] = 0;
            }
        } else {
            if (s.myMap[m.x][m.y] != 2) {
                System.out.println("-Error- Environment : doMove() -> c.myMap[" + m.x + "][" + m.y + "] != 2");
            } else {
                s.myMap[m.x2][m.y2] = 2;
                s.myMap[m.x][m.y] = 0;
            }
        }
        s.isWhiteTurn = !s.isWhiteTurn;
    }

    public void undoMove(State s, Moves m) {
        if (!s.isWhiteTurn) {
            if (s.myMap[m.x2][m.y2] != 1) {
                System.out.println("-Error- Environment : undoMove() -> c.myMap[" + m.x2 + "][" + m.y2 + "] != 1");
            } else {
                s.myMap[m.x][m.y] = 1;
                s.myMap[m.x2][m.y2] = 0;
                if (m.x != m.x2) {
                    s.myMap[m.x2][m.y2] = 2;
                }
            }
        } else {
            if (s.myMap[m.x2][m.y2] != 2) {
                System.out.println("-Error- Environment : undoMove() -> c.myMap[" + m.x2 + "][" + m.y2 + "] != 2");
            } else {
                s.myMap[m.x][m.y] = 2;
                s.myMap[m.x2][m.y2] = 0;
                if (m.x != m.x2) {
                    s.myMap[m.x2][m.y2] = 1;
                }
            }
        }
        s.isWhiteTurn = !s.isWhiteTurn;
    }

    public int eval(State s) {
        int e = 0;
        int blackPieces = 0;
        int whitePieces = 0;

        for (int i = 0; i < s.myMap.length; i++) {
            for (int j = 0; j < s.myMap[0].length; j++) {
                if (s.myMap[i][j] == 1) {
                    whitePieces++;
                } else if (s.myMap[i][j] == 2) {
                    blackPieces++;
                }
            }
        }
        e = whitePieces - blackPieces;
        return e;
    }

    public String toString() {
        return currentState.toString();
    }

    public boolean isTerminalState(State s) {
        for (int i = 0; i < sizeX; i++) { // for every column
            if (s.myMap[i][0] == 2 || s.myMap[i][sizeY - 1] == 1) {
                return true;
            }
        }

        if (legalMoves(s).isEmpty()) {
            return true;
        }
        return false; // if not this is not a terminal state

    }

}
