package halloween_city;

import util.Helper;
import java.awt.*;

public class Priest extends Being {
	
	public Priest(int x, int y, int direction) {
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