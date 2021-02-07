import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class BoardState {
    public HashMap<Point, Integer> board;

    public BoardState(int width, int height) {
        board = new HashMap<>();
        for(int i = 0; i<height; i++) {
            for(int j = 0; j<width; j++) {
                if(i < 2) {
                    Point p = new Point(j + 1,i + 1);
                    board.put(p, 1);
                } else if (i > height - 3) {
                    Point p = new Point(j+1,i+1);
                    board.put(p, 2);
                } else {
                    Point p = new Point(j+1,i+1);
                    board.put(p, 0);
                }
            }
        }
    }
}
