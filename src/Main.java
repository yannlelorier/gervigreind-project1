import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Main {
	
	/**
	 * starts the game player and waits for messages from the game master <br>
	 * Command line options: [port]
	 */
	public static void main(String[] args){
		try{
			// TODO: put in your agent here
			Agent agent = new MyAgent();
			State bs = new State(8,8);


			/*for(Map.Entry<Point, Integer> entry : bs.board.entrySet()) {
				System.out.println("X: " + entry.getKey().getX() + " | Y: " + entry.getKey().getY() + "|" + " Pawn: " + entry.getValue());
			} */



			int port=4001;
			if(args.length>=1){
				port=Integer.parseInt(args[0]);
			}
			GamePlayer gp=new GamePlayer(port, agent);
			gp.waitForExit();
		}catch(Exception ex){
			ex.printStackTrace();
			System.exit(-1);
		}
	}
}
