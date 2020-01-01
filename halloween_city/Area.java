package halloween_city;

import util.Helper;
import java.awt.*;

public class Area {
	protected int x, y;
	
	//If mouse if hovering over panel
	protected boolean hover = false;

	//If enter was pressed
	protected boolean enter = false;
	
	public Area(int x, int y, boolean hover) {
		this.x = x;
		this.y = y;
		this.hover = hover;
	}

}