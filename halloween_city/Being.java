package halloween_city;

import util.Helper;
import java.awt.*;

public abstract class Being {
	
	protected int x, y;
	protected int direction;

	public Being(int x, int y, int direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
	}
	
	public abstract void move();
	
}