package halloween_city;

import util.Helper;
import java.awt.*;

public class Human extends Being {
	//When a human sees a vampire, turn the opposite direction and run 2 squares
	//This variable keeps track of squares ran
	protected int runBuffer = 2;
	
	public Human(int x, int y, int direction) {
		super(x, y, direction);
	}
	
	public void move() {
		if(direction == 0){
			x--; //0 indicates facing West
		}else if(direction == 1){
			x++; //1 indicates facing East
		}else if(direction == 2){
			y--; //2 indicates facing South
		}else{
			y++; //3 indicates facing North
		}
	}
}