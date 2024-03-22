import lib.StdDraw; // the StdDraw class in the lib package is used for drawings 

import java.awt.Color; // used for coloring the tile and the number on it
import java.awt.Font; // used for setting the font to show the number on the tile

// A class that is used for modeling the numbered square tiles in the 8 puzzle.
public class Tile {
	// The data fields
	// --------------------------------------------------------------------------
	// the color used for the tile (a static constant shared by all the instances) --Not solved state (Blue)
	private static final Color tileColor = new Color(15, 76, 129);
	// the color used for the number on the tile (a static constant) --Not solved state (Blue)
	private static final Color numberColor = new Color(31, 160, 239);
	// the color used for the box around the tile (a static constant) --Not solved state (Blue)
	private static final Color boxColor = new Color(31, 160, 239);

	// the color used for the tile (a static constant shared by all the instances) --Solved state (Green)
	private static final Color endTileColor = new Color(0, 84, 14); 
	// the color used for the number on the tile (a static constant) --Solved state (Green)
	private static final Color endNumberColor = new Color(0, 145, 24);
	// the color used for the box around the tile (a static constant) --Solved state (Green)
	private static final Color endBoxColor = new Color(0, 145, 24);

	// the color used for the tile (a static constant shared by all the instances) --Unsolvable state (Red)
	private static final Color failTileColor = new Color(92, 0, 0);
	// the color used for the number on the tile (a static constant) --Unsolvable state (Red)
	private static final Color failNumberColor = new Color(158, 0, 0);
	// the color used for the box around the tile (a static constant) --Unsolvable state (Red)
	private static final Color failBoxColor = new Color(158, 0, 0);

	// the line thickness value for the box around the tile (a static constant)
	private static final double lineThickness = 0.01;
	// the font used for the number on the tile (a static constant)
	private static final Font numberFont = new Font("Arial", Font.BOLD, 50);
	private int number; // the number on the tile (an instance variable)

	// A constructor that creates a tile with a given number
	// --------------------------------------------------------------------------
	public Tile(int number) {
		// set the number based on the given value
		this.number = number;
	}

	// The method(s) of the Tile class
	// --------------------------------------------------------------------------
	// A method for drawing the tile centered on a given position (Updated to color based on their state as well)
	public void draw(int posX, int posY, Color tileColor, Color boxColor, Color numberColor) {
		// draw the tile as a filled square
		StdDraw.setPenColor(tileColor);
		StdDraw.filledSquare(posX, posY, 0.5);
		// draw the bounding box of the tile as a square
		StdDraw.setPenColor(boxColor);
		StdDraw.setPenRadius(lineThickness);
		StdDraw.square(posX, posY, 0.5);
		StdDraw.setPenRadius(); // reset pen radius to its default value
		// draw the number on the tile
		StdDraw.setPenColor(numberColor);
		StdDraw.setFont(numberFont);
		StdDraw.text(posX, posY, String.valueOf(number));
	}
	// A method to call the other draw method based on the value of the state to input which colors should be used for that state
	public void draw(int posX, int posY, int state) {
		if (state == Board.STATE_SOLVED) {
			draw(posX, posY, endTileColor, endBoxColor, endNumberColor);
		} 
		else if (state == Board.STATE_NOT_SOLVED) {
			draw(posX, posY, tileColor, boxColor, numberColor);
		} 
		else {
			draw(posX, posY, failTileColor, failBoxColor, failNumberColor);
		}
	}
	// A getter for the number of the tile
	public int getNumber() {
		return number;
	}
}