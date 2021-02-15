import java.util.HashSet;

public class State implements Cloneable {
    protected short[][] myMap; // 0, 1 or 2 for empty, black or white
    protected boolean isWhiteTurn;

    // this should only be called once each time your player makes a move
    public State() {
        isWhiteTurn = true;
        myMap = new short[2][2];
    }

    // prints out the state as who's turn it is and how the board looks like
    public String toString() {
        System.out.println("myMapLength: " + myMap.length + " , " + myMap[0].length);
        StringBuilder toRet = new StringBuilder("isWhiteTurn: " + isWhiteTurn);
        if (myMap != null && myMap.length > 0 && myMap[0].length > 0){
            toRet.append("\nMap: \n");
            for (int j = myMap[0].length - 1; j >= 0 ; j--){ // for each row, we have to go from up to down

                for (int i = 0; i < myMap.length; i++){ // for each column
                    if (myMap[i][j] == 0){
                        toRet.append("- "); // represents empty space
                    }
                    else if (myMap[i][j] == 1){
                        toRet.append("W "); // represents white piece
                    }
                    else if (myMap[i][j] == 2){
                        toRet.append("B "); // represents black piece
                    }
                    else { // this should never happen, but good to have just in case
                        System.out.println("--Error-- State : toString() -> myMap[i][j] is not 0, 1 or 2");
                    }
                }
                toRet.append("\n");
            }
        }
        else {
            toRet.append("--Error-- State : toString() -> could not print out map");
        }
        return toRet.toString();
    }
}
