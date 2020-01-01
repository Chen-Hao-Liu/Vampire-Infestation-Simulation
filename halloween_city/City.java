package halloween_city;

import util.Helper;
import java.awt.Color;
import java.util.ArrayList;

public class City{

	/** Extra FUN
	 * 
	 * This is a option for students who want an extra challenge. 
	 * If you do use the walls you will get extra credit (only if your project works).
	 * 
	 *  walls is a 2D array with an entry for each space in the city.
	 *  If walls[x][y] is true, that means there is a wall in the space.
	 *  else the space is free. Humans, vampires, and priests should never go into spaces that
	 *  have a wall.
	 *
	 */
	private boolean walls[][];
	private int width, height;

	protected Area myMouse = new Area(0,0,false);
	private ArrayList<Human> humans = new ArrayList<Human>();
	private ArrayList<Priest> priests = new ArrayList<Priest>();
	private ArrayList<Vampire> vampires = new ArrayList<Vampire>();
	private boolean addPriest = false;
	private int addX, addY;

	/**
	 * Create a new City and fill it with buildings and people.
	 * @param w width of city
	 * @param h height of city
	 * @param numB number of buildings
	 * @param numP number of people
	 * @param numPriests number of priests
	 * @param numVampire number of vampires
	 * You should modify this function to take the number of:
	 *  vampires,priests, and other creatures too. 
	 */
	public City(int w, int h, int numB, int numP, int numPriest, int numVampire) {
		width = w;
		height = h;
		walls = new boolean[w][h];
		
		randomBuildings(numB);
		populate(numP, numPriest, numVampire);
	}


	/**
	 * Generates numPeople random people distributed throughout the city.
	 * ETRA FUN : People, vampires & other creatures must not be placed inside walls!
	 * You can modify this function to 
	 *
	 * @param numPeople the number of people to generate
	 * 
	 * 
	 */
	private void populate(int numPeople, int numPriest, int numVampire)
	{
		// Generate random Beings around the city
		populateHelper(numPeople, 'h');
		populateHelper(numPriest, 'p');
		populateHelper(numVampire, 'v');
		
	}

	private void populateHelper(int n, char b){
		// Generate num Beings randomly placed around the city
		for(int i=0; i<n; i++){
			//Generates random coordinates
			int x = Helper.nextInt(width);
			int y = Helper.nextInt(height);

			//Checks to see if walls exist at the position generated
			while(walls[x][y] == true){
				//If walls exist, generate another position
				x = Helper.nextInt(width);
				y = Helper.nextInt(height);
			}
             
            //Generate a random direction for Being to face
            int direction = Helper.nextInt(4);  

            //Create valid being
            switch(b){
            	case 'h':
            		humans.add(new Human(x,y,direction));
            		break;
            	case 'p':
            		priests.add(new Priest(x,y,direction));
            		break;
            	case 'v':
            		vampires.add(new Vampire(x,y,direction));
            		break;
            	default:
            		return; 
            }
		}
	}


	/**
	 * Generates a random set of numB buildings.
	 *
	 * @param numB the number of buildings to generate
	 */
	private void randomBuildings(int numB) {
		/* Create buildings of a reasonable size for this map */
		int bldgMaxSize = width/6;
		int bldgMinSize = width/50;

		/* Produce a bunch of random rectangles and fill in the walls array */
		for(int i=0; i < numB; i++) {
			int tx, ty, tw, th;
			tx = Helper.nextInt(width);
			ty = Helper.nextInt(height);
			tw = Helper.nextInt(bldgMaxSize) + bldgMinSize;
			th = Helper.nextInt(bldgMaxSize) + bldgMinSize;

			for(int r = ty; r < ty + th; r++) {
				if(r >= height)
					continue;
				for(int c = tx; c < tx + tw; c++) {
					if(c >= width)
						break;
					walls[c][r] = true;
				}
			}
		}
	}

	
	/**
	 * Updates the state of the city for a time step.
	 */
	public void update() {
		// Move humans, vampires, etc
		
		//Delete Chunk if called for
		//First check to see if mouse is in the panel area then check to see if enter was pressed
		if(myMouse.hover && myMouse.enter){
			//Delete a square chunk of the map where the mouse cursor is pointing along with everyone in it
			int x = myMouse.x;
			int y = myMouse.y;

			for(int i=x-8; i<x+9; i++){
				for(int j=y-8; j<y+9; j++){
					if(i>=0 && i<width && j>=0 && j<height){
						if(walls[i][j]){
							walls[i][j] = false;
						}else{
							for(int k=0; k<humans.size(); k++){
								if(i == humans.get(k).x && j == humans.get(k).y){
									humans.remove(k);
								}
							}

							for(int k=0; k<vampires.size(); k++){
								if(i == vampires.get(k).x && j == vampires.get(k).y){
									vampires.remove(k);
								}
							}

							for(int k=0; k<priests.size(); k++){
								if(i == priests.get(k).x && j == priests.get(k).y){
									priests.remove(k);
								}
							}
						}
					}
				}
			}

			myMouse.enter = false;
		}

		//Add Priest For Mouse Click
		if(addPriest){
			//Generate a random direction for Being to face
	        int direction = Helper.nextInt(4);  

	        //Generate new Priest
	        Priest newPriest = new Priest(addX, addY, direction);

	        //Add new Priest to list
	        priests.add(newPriest);

	        //Reset addPriest to false
	        addPriest = false;
		}

		//Human adjacent to vampires
		for(int i=0; i<humans.size(); i++){
			int x = humans.get(i).x;
			int y = humans.get(i).y;
			int direction = humans.get(i).direction;
			
			//If adjacent to vampire, convert to vampire
			if(isAdjacent(x, y, 'v')){
				vampires.add(new Vampire(x, y, direction));
				humans.remove(i);
			}
		}

		//Vampire adjacent to priests
		for(int i=0; i<vampires.size(); i++){
			int x = vampires.get(i).x;
			int y = vampires.get(i).y;
			int direction = vampires.get(i).direction;
			
			//If adjacent to priest, convert to human
			if(isAdjacent(x, y, 'p')){
				humans.add(new Human(x, y, direction));
				vampires.remove(i);
			}
		}

		//Parse through ArrayList of humans
		for(int i=0; i<humans.size(); i++){
			//Acquire the current position of human i
			int x = humans.get(i).x;
			int y = humans.get(i).y; 

			//Increment runBuffer if still running
			if(humans.get(i).runBuffer != 2){
				humans.get(i).runBuffer++;
			}

			//Based on the direction, checks to see if a wall exists before moving
			if(humans.get(i).direction == 0){
				//Checks to see if the next step is within the bounds of the map
				if(x-1 > -1){
					//Checks to see if the next step does not have a wall
					if(walls[x-1][y] != true){
						//Conditions are met, move human
						humans.get(i).move();
					}
				}		 
			}else if(humans.get(i).direction == 1){
				if(x+1 < width){
					if(walls[x+1][y] != true){
						humans.get(i).move();
					}
				}
			}else if(humans.get(i).direction == 2){
				if(y-1 > -1){
					if(walls[x][y-1] != true){
						humans.get(i).move();
					}
				}
			}else{
				if(y+1 < height){
					if(walls[x][y+1] != true){
						humans.get(i).move();
					}
				}
			}

			if(isWithin(x, y, humans.get(i).direction, 10, 'v')){
				//START RUNNING!
				humans.get(i).runBuffer = 0;

				//Switch Direction
				int dir = humans.get(i).direction;
				if(dir == 0){
					humans.get(i).direction = 1;
				}else if(dir == 1){
					humans.get(i).direction = 0;
				}else if(dir == 2){
					humans.get(i).direction = 3;
				}else{
					humans.get(i).direction = 2;
				}
			}else{
				//If human is done running, 10 percent chance for chance for random direction
				if(humans.get(i).runBuffer == 2){
					//Acquire 10 percent chance
					int chance = Helper.nextInt(100);
					if(chance >= 0 && chance <= 9){
						//Random Direction
						humans.get(i).direction = Helper.nextInt(4);
					}
				}
			}
		}

		//Parse through ArrayList of vampires
		for(int i=0; i<vampires.size(); i++){
			//Acquire the current position of vampire i
			int x = vampires.get(i).x;
			int y = vampires.get(i).y; 

			//Based on the direction, checks to see if a wall exists before moving
			if(vampires.get(i).direction == 0){
				//Checks to see if the next step is within the bounds of the map
				if(x-1 > -1){
					//Checks to see if the next step does not have a wall
					if(walls[x-1][y] != true){
						//Conditions are met, move vampire
						vampires.get(i).move();
					}
				}		 
			}else if(vampires.get(i).direction == 1){
				if(x+1 < width){
					if(walls[x+1][y] != true){
						vampires.get(i).move();
					}
				}
			}else if(vampires.get(i).direction == 2){
				if(y-1 > -1){
					if(walls[x][y-1] != true){
						vampires.get(i).move();
					}
				}
			}else{
				if(y+1 < height){
					if(walls[x][y+1] != true){
						vampires.get(i).move();
					}
				}
			}

			if(!isWithin(x,y,vampires.get(i).direction, 10, 'h')){
				//Acquire 20 percent chance
				int chance = Helper.nextInt(100);
				if(chance >= 0 && chance <= 19){
					//Random Direction
					vampires.get(i).direction = Helper.nextInt(4);
				}
			}
		}

		//Parse through ArrayList of priests
		for(int i=0; i<priests.size(); i++){
			//Acquire the current position of priest i
			int x = priests.get(i).x;
			int y = priests.get(i).y; 

			//Acquire %20 chance of splashing holy water
			int chance = Helper.nextInt(100);
			if(chance >= 0 && chance <= 19){
				//Spash holy water
				int dir = priests.get(i).direction;
				if(dir == 0){ //Facing West
					//Parse through Vampires list
					for(int j=0; j<vampires.size(); j++){
						//Acquire x and y coordinates of vampire
						int vx = vampires.get(j).x;
						int vy = vampires.get(j).y; 

						//Only need to check second one because adjacent vampires
						//Have already been vaporized in this time step
						if(vx == x-2 && vy == y){
							vampires.remove(j);
						}
					}
				}else if(dir == 1){ //Facing East
					for(int j=0; j<vampires.size(); j++){
						//Acquire x and y coordinates of vampire
						int vx = vampires.get(j).x;
						int vy = vampires.get(j).y; 

						//Only need to check second one because adjacent vampires
						//Have already been vaporized in this time step
						if(vx == x+2 && vy == y){
							vampires.remove(j);
						}
					}
				}else if(dir == 2){ //Facing South
					for(int j=0; j<vampires.size(); j++){
						//Acquire x and y coordinates of vampire
						int vx = vampires.get(j).x;
						int vy = vampires.get(j).y; 

						if(vx == x && vy == y-2){
							vampires.remove(j);
						}
					}
				}else{ //Facing North
					for(int j=0; j<vampires.size(); j++){
						//Acquire x and y coordinates of vampire
						int vx = vampires.get(j).x;
						int vy = vampires.get(j).y; 

						//Only need to check second one because adjacent vampires
						//Have already been vaporized in this time step
						if(vx == x && vy == y+2){
							vampires.remove(j);
						}
					}
				}
			}

			//Based on the direction, checks to see if a wall exists before moving
			if(priests.get(i).direction == 0){
				//Checks to see if the next step is within the bounds of the map
				if(x-1 > -1){
					//Checks to see if the next step does not have a wall
					if(walls[x-1][y] != true){
						//Conditions are met, move priest
						priests.get(i).move();
					}
				}		 
			}else if(priests.get(i).direction == 1){
				if(x+1 < width){
					if(walls[x+1][y] != true){
						priests.get(i).move();
					}
				}
			}else if(priests.get(i).direction == 2){
				if(y-1 > -1){
					if(walls[x][y-1] != true){
						priests.get(i).move();
					}
				}
			}else{
				if(y+1 < height){
					if(walls[x][y+1] != true){
						priests.get(i).move();
					}
				}
			}

			if(!isWithin(x, y, priests.get(i).direction, 5, 'v')){
				//Acquire 15 percent chance
				chance = Helper.nextInt(100);
				if(chance >= 0 && chance <= 14){
					//Random Direction
					priests.get(i).direction = Helper.nextInt(4);
				}
			}
		}
	}

	/**
	 * Draw all humans, vampires, and buildings (.
	 */
	public void draw(){
		/* Clear the screen */
		Simulation.dp.clear(Color.BLACK);
		drawWalls();
		
		//Draw the humans
		Simulation.dp.setPenColor(Color.WHITE);
		for(int i=0; i<humans.size(); i++){
			int x = humans.get(i).x;
			int y = humans.get(i).y; 

			Simulation.dp.drawDot(x, y);
		}

		//Draw the vampires
		Simulation.dp.setPenColor(Color.RED);
		for(int i=0; i<vampires.size(); i++){
			int x = vampires.get(i).x;
			int y = vampires.get(i).y; 

			Simulation.dp.drawDot(x, y);
		}

		//Draw the priests
		Simulation.dp.setPenColor(Color.BLUE);
		for(int i=0; i<priests.size(); i++){
			int x = priests.get(i).x;
			int y = priests.get(i).y; 

			Simulation.dp.drawDot(x, y);
		}
	}

	private boolean isAdjacent(int x, int y, char type){
		//Based on type, switch
		switch(type){
			case 'h': //Indicates human
				for(int i=0; i<humans.size(); i++){
					int hx = humans.get(i).x;
					int hy = humans.get(i).y;

					//Checks each direction for adjacency
					if((hx == x && hy == y-1) || (hx == x && hy == y+1) || (hx == x-1 && hy == y) || (hx == x+1 && hy == y)){
						return true;
					}
				}
				return false;
			case 'p'://Indicates priest
				for(int i=0; i<priests.size(); i++){
					int px = priests.get(i).x;
					int py = priests.get(i).y;

					//Checks each direction for adjacency
					if((px == x && py == y-1) || (px == x && py == y+1) || (px == x-1 && py == y) || (px == x+1 && py == y)){
						//System.out.println("Priest");
						return true;
					}
				}
				return false;
			case 'v'://Indicates vampire
				for(int i=0; i<vampires.size(); i++){
					int vx = vampires.get(i).x;
					int vy = vampires.get(i).y;
					
					//Checks each direction for adjacency
					if((vx == x && vy == y-1) || (vx == x && vy == y+1) || (vx == x-1 && vy == y) || (vx == x+1 && vy == y)){
						//System.out.println("Vampire");
						return true;
					}
				}
				return false;
			default:
				return false;
		}
	}


	//Checks to see if a certain being is within range based on the direction, current coordinates, and range
	private boolean isWithin(int x, int y, int direction, int range, char type){
		//Based on the being indicated, switch
        switch(type){
        	case 'h': //h indicates human
        		for(int i=0; i<humans.size(); i++){
        			int hx = humans.get(i).x;
        			int hy = humans.get(i).y;
        			
        			//Checks direction
        			if(direction == 0){
        				if(y == hy){
        					//Checks to see if in range
        					if(hx < x && hx >= x-range){
        						//System.out.println("A");
        						return true;
        					}
        				}
        			}else if(direction == 1){
        				if(y == hy){
        					if(hx > x && hx <= x+range){
        						//System.out.println("B");
        						return true;
        					}
        				}
        			}else if(direction == 2){
        				if(x == hx){
        					if(hy < y && hy >= y-range){
        						//System.out.println("C");
        						return true;
        					}
        				}
        			}else{
        				if(x == hx){
        					if(hy > y && hy <= y+range){
        						//System.out.println("D");
        						return true;
        					}
        				}
        			}
        		}

        		return false;
        	case 'p': //p indicates priest
        		for(int i=0; i<priests.size(); i++){
        			int px = priests.get(i).x;
        			int py = priests.get(i).y;
        			
        			//Checks direction
        			if(direction == 0){
        				if(y == py){
        					//Checks to see if in range
        					if(px < x && px >= x-range){
        						return true;
        					}
        				}
        			}else if(direction == 1){
        				if(y == py){
        					if(px > x && px <= x+range){
        						return true;
        					}
        				}
        			}else if(direction == 2){
        				if(x == px){
        					if(py < y && py >= y-range){
        						return true;
        					}
        				}
        			}else{
        				if(x == px){
        					if(py > y && py <= y+range){
        						return true;
        					}
        				}
        			}
        		}

        		return false; 
        	case 'v': //v indicates vampire
        		for(int i=0; i<vampires.size(); i++){
        			int vx = vampires.get(i).x;
        			int vy = vampires.get(i).y;
        			
        			//Checks direction
        			if(direction == 0){
        				if(y == vy){
        					//Checks to see if in range
        					if(vx < x && vx >= x-range){
        						//System.out.println("A");
        						return true;
        					}
        				}
        			}else if(direction == 1){
        				if(y == vy){
        					if(vx > x && vx <= x+range){
        						//System.out.println("B");
        						return true;
        					}
        				}
        			}else if(direction == 2){
        				if(x == vx){
        					if(vy < y && vy >= y-range){
        						//System.out.println("C");
        						return true;
        					}
        				}
        			}else{
        				if(x == vx){
        					if(vy > y && vy <= y+range){
        						//System.out.println("D");
        						return true;
        					}
        				}
        			}
        		}

        		return false;
        	default:
        		return false; 
        }
	}

	//Adds a priest when mouse is clicked
	public void addPriestFunc(int x, int y){
		try{
			//If walls are not in the way
			if(!walls[x][y]){
				//Update global vampire coordinates to be added
				addX = x;
				addY = y;
				//Give signal to add Vampire
				addPriest = true;
			}else{
				System.out.println("CANNOT ADD PRIEST DUE TO WALL, PLEASE TRY AGAIN");
			}
		}catch(Exception e){
			System.out.println("Exception Caught: ");
			System.out.println(e);
		}
	}

	//When user cursor enters the area
	public void updateMouse(boolean flag){
		if(flag){
			//If cursor is in the panel area
			myMouse.hover = true;
		}else{
			//If cursor is not in the panel area
			myMouse.hover = false;
		}
	}

	//delete the selected area of the mouse cursor
	public void deleteArea(int x, int y){
		//Give the signal that enter was indeed pressed
		myMouse.enter = true;

		//Update coordinates of mouse
		myMouse.x = x;
		myMouse.y = y;
	}

	/**
	 * EXTRA FUN function... not used unless you are going for that
	 * Draw the buildings.
	 * First set the color for drawing, then draw a dot at each space
	 * where there is a wall.
	 */
	private void drawWalls() {
		Simulation.dp.setPenColor(Color.DARK_GRAY);
		for(int r = 0; r < height; r++)
		{
			for(int c = 0; c < width; c++)
			{
				if(walls[c][r])
				{
					Simulation.dp.drawDot(c, r);
				}
			}
		}
	}

}
