package halloween_city;

import util.DotPanel;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JFrame;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.awt.MouseInfo;
import java.awt.Point;

/*
 * You must add a way to represent humans.  When there is not a vampire  apocalypse occurring, humans
 * should follow these simple rules:
 * 		if (1 in 10 chance):
 * 			turn to face a random direction (up/down/left/right)
 * 		Move in the current direction one space 
 *
 * We will add additional rules for dealing with sighting or running into vampires later.
 */

public class Simulation extends JFrame implements MouseListener, KeyListener{

	private static final long serialVersionUID = -5176170979783243427L;

	/*
	 * The Dot Panel object you will draw to.
	 * NOTE: this is protected static! 
	 */
	protected static DotPanel dp;

	/* Define constants using static final variables */
	public static final int MAX_X = 200; //200
	public static final int MAX_Y = 200; //200
	private static final int DOT_SIZE = 3;
	private static final int NUM_HUMANS = 200;
	private static final int NUM_VAMPIRES = 1;
	private static final int NUM_PRIESTS = 1;
	private static final int NUM_BUILDINGS = 60;

	protected City world;
	/*
	 * This fills the frame with a "DotPanel", a type of drawing canvas that
	 * allows you to easily draw squares to the screen.
	 */
	public Simulation() {
		addMouseListener(this);
		addKeyListener(this);

		this.setSize(MAX_X * DOT_SIZE, MAX_Y * DOT_SIZE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setTitle("Braaiinnnnnsss");

		/* Create and set the size of the panel */
		dp = new DotPanel(MAX_X, MAX_Y, DOT_SIZE);

		/* Add the panel to the frame */
		Container cPane = this.getContentPane();
		cPane.add(dp);
		

		/* Initialize the DotPanel canvas:
		 * You CANNOT draw to the panel BEFORE this code is called.
		 * You CANNOT add new widgets to the frame AFTER this is called.
		 */
		this.pack();
		dp.init();
		dp.clear();
		dp.setPenColor(Color.red);
		this.setVisible(true);

		/* Create our city */
		world = new City(MAX_X, MAX_Y, NUM_BUILDINGS, NUM_HUMANS, NUM_PRIESTS, NUM_VAMPIRES);

		/* This is the Run Loop (aka "simulation loop" or "game loop")
		 * It will loop forever, first updating the state of the world
		 * (e.g., having humans take a single step) and then it will
		 * draw the newly updated simulation. Since we don't want
		 * the simulation to run too fast for us to see, it will sleep
		 * after repainting the screen. Currently it sleeps for
		 * 33 milliseconds, so the program will update at about 30 frames
		 * per second.
		 */
		while(true)
		{
			// Run update rules for world and everything in it
			world.update();
			// Draw to screen and then refresh
			world.draw();
			dp.repaintAndSleep(30);

		}
	}

	public void mouseClicked(MouseEvent e){
		//Mouse Click detected
		System.out.println("Mouse Clicked");

		//Divide by DOT_SIZE in order to scale down raw input position values
		world.addPriestFunc(e.getX()/DOT_SIZE, e.getY()/DOT_SIZE);
	}

	public void mouseEntered(MouseEvent e){
		//Mouse entered area detected
		System.out.println("Mouse Entered");

		//Update boolean
		world.updateMouse(true);
	}


	public void mouseExited(MouseEvent e){
		//Mouse exited area detected
		System.out.println("Mouse Exited");

		//Update boolean
		world.updateMouse(false);
	}

	public void mousePressed(MouseEvent e){
		System.out.println("Mouse Pressed");
	}

	public void mouseReleased(MouseEvent e){
		System.out.println("Mouse Released");
	}

	public void keyPressed(KeyEvent e){
		//Key Pressed Detected
		System.out.println("Key Pressed");

		if(e.getKeyCode() == KeyEvent.VK_SPACE){
			System.out.println("Spacebar Detected");

			//Save state of mouse and keyboard booleans
			boolean hover = world.myMouse.hover;
			boolean enter = world.myMouse.hover;

			world = new City(MAX_X, MAX_Y, NUM_BUILDINGS, NUM_HUMANS, NUM_PRIESTS, NUM_VAMPIRES);
			
			//Restore state of mouse and keyboard booleans
			world.myMouse.hover = hover;
			world.myMouse.enter = enter; 
		}

		if(e.getKeyCode() == KeyEvent.VK_ENTER){
			System.out.println("ENTER Detected");

			//Acquire current mouse pointer position
			Point a = MouseInfo.getPointerInfo().getLocation();

			//Call delete area based on current pointer position adjusted based on DOT_SIZE
			//to accomodate for the scale down from raw input
			world.deleteArea(((int) a.getX()/DOT_SIZE), ((int) a.getY()/DOT_SIZE));
		}
	}

	public void keyReleased(KeyEvent e){
		System.out.println("Key Released");
	}

	public void keyTyped(KeyEvent e){
		System.out.println("Key Typed");
	}

	public static void main(String[] args) {
		/* Create a new GUI window  */
		new Simulation();

	}
}
