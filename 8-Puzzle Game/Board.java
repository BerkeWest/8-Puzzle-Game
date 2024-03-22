import lib.StdDraw; // the StdDraw class in the lib package is used for drawings 

import java.awt.Color; // used for coloring the board
import java.awt.Point; // used for the positions of the tiles and the empty cell

import java.util.ArrayList; // used for improved list interface
import java.util.List; // used for list interface

//A class that is used for modeling the board in the 8 puzzle.
public class Board {
	
	// States of the board that will represent which colors should be used in each state
	public static final int STATE_NOT_SOLVED = -1;
	public static final int STATE_UNSOLVABLE = 1;
	public static final int STATE_SOLVED = 0;
	
	// Data fields: the class variables (actually constants here)
	// --------------------------------------------------------------------------
	// the background color used for the empty cell on the board 
	private static final Color backgroundColor = new Color(145, 234, 255); // for Not solved state
	// the color used for drawing the boundaries around the board
	private static final Color boxColor = new Color(31, 160, 239); // for Not solved state
	
	private static final Color endBgColor = new Color(0, 179, 21); // for Solved state
	private static final Color endBoxColor = new Color(0, 145, 24); // for Solved state
	
	private static final Color failBgColor = new Color(179, 0, 0); // for Unsolvable state
	private static final Color failBoxColor = new Color(158, 0, 0); // for Unsolvable state
	
	// the line thickness value for the boundaries around the board
	// (it is twice the value used for the tiles as only half of it is visible)
	private static final double lineThickness = 0.02;

	// Data fields: the instance variables
	// --------------------------------------------------------------------------
	// a matrix to store the tiles on the board in their current configuration
	private Tile[][] tiles = new Tile[3][3];
	// the row and the column indexes of the empty cell
	private int emptyCellRow, emptyCellCol;
	private int[] initialState; // Represents the starting point of the board
	private int cost; // Cost from the start node to this node.
	private int heuristic; // Heuristic estimate from this node to the goal.
	private Board parent; // The board that represents the one before the move
	
	// A constructor that generates a randomized board state
	public Board() {
		this(getRandomState());
	}
	// A constructor that generates a board based on a given array
	public Board(int[] initialState) {
		this.initialState = initialState;

		int arrayIndex = 0; // the index of the current number in the numbers array
		// for each tile in the tile matrix
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				//if it is an empty cell
				if (initialState[arrayIndex] == 0) { 
					// assign the row and the column indexes of the empty cell
					emptyCellRow = row;
					emptyCellCol = col;
					tiles[row][col] = null;
				}
				// create a tile based on the current value of the array
				else {
					tiles[row][col] = new Tile(initialState[arrayIndex]);
				}
	            // increase the array index by 1
				arrayIndex++;
			}
		}
	}
	// A constructor that copies a board to create a new board state
	private Board(Tile[][] tiles, int emptyCellRow, int emptyCellCol, int cost, int heuristic, Board parent) {
		this.tiles = new Tile[3][3];
		// for each tile in the tile matrix
		for (int row = 0; row < tiles.length; row++) {
			for (int col = 0; col < tiles[row].length; col++) {
				// Copy the tile number by using getter method when the tile isn't null
				this.tiles[row][col] = tiles[row][col] == null ? null : new Tile(tiles[row][col].getNumber());
			}
		}
		// copy the remaining values
		this.emptyCellRow = emptyCellRow;
		this.emptyCellCol = emptyCellCol;
		this.cost = cost;
		this.heuristic = heuristic;
		this.parent = parent;
	}
	// A getter for the initial state
	public int[] getInitialState() {
		return initialState;
	}
	// A method that puts the current state into a 1D array
	public int[] getCurrentState() {
		int[] flattenedArray = new int[9]; // A 1D array that stores the current state matrix
		int arrayIndex = 0;
		// for each tile in the tile matrix
		for (int row = 0; row < tiles.length; row++) {
			for (int col = 0; col < tiles[row].length; col++) {
				// Add the tiles number in the array, then increase the index (Store the null tile as 0)
				flattenedArray[arrayIndex++] = tiles[row][col] != null ? tiles[row][col].getNumber() : 0;
			}
		}
		return flattenedArray;
	}
	// A method that creates a new state based on each operation on the current state
	public static Board getNewState(Board currentState, String operation) {
		// Create a copy of the current state
		Board newState = new Board(currentState.tiles, currentState.emptyCellRow, currentState.emptyCellCol,
				currentState.cost, currentState.heuristic, currentState);
		// change the current state based on each operation
		switch (operation) {
		case "up":
			if (newState.moveUp())
				return newState;
		case "down":
			if (newState.moveDown())
				return newState;
			break;
		case "left":
			if (newState.moveLeft())
				return newState;
			break;
		case "right":
			if (newState.moveRight())
				return newState;
			break;
		}
		return null;
	}
	// A getter for parent
	public Board getParent() {
		return parent;
	}
	// A setter for parent
	public void setParent(Board parent) {
		this.parent = parent;
	}
	// A method that returns a list of all possible neighbors
	public List<Board> getNeighbors() {
		List<Board> neighbors = new ArrayList<>();
		for (String operation : new String[] { "up", "down", "left", "right" }) {
			Board newState = getNewState(this, operation);
			if (newState != null) {
				neighbors.add(newState);
			}
		}
		return neighbors;
	}
	// A method to calculate heuristic value of a board
	public int calculateHeuristic(Board goal, int n) {
		if (n==0) {
			int heuristic = 0;
			// for each tile in the tile matrix
			for (int row = 0; row < tiles.length; row++) {
				for (int col = 0; col < tiles[row].length; col++) {
					// Increase the heuristic if only one of the goal or the board is on empty tile
					if (tiles[row][col] == null || goal.tiles[row][col] == null && tiles[row][col] != goal.tiles[row][col]) {
						heuristic++;
					}
					// Increase the heuristic if the tile numbers of the goal and the board don't match in the same spot
					else if (tiles[row][col].getNumber() != goal.tiles[row][col].getNumber()) {
						heuristic++;
					}
				}
			}
			return heuristic;
		}
		else if (n==1) {
				// for each tile in the tile matrix
				 for (int i = 0; i < tiles.length; i++) { 
					 for (int j = 0; j < tiles[i].length; j++) { 
						 Tile tile = tiles[i][j]; 
						 if (tile != null) { 
							 int tileNumber = tile.getNumber();
							 int goalRow = (tileNumber - 1) / goal.tiles.length;
							 int goalCol = (tileNumber - 1) % goal.tiles[0].length;
							 heuristic += Math.abs(i - goalRow) + Math.abs(j - goalCol); } } } 
				 return heuristic; 
				}
		else
			return 0;
	}
	// The method(s) of the Board class
	// --------------------------------------------------------------------------
	// An inner method that randomly reorders the elements in a given int array.
	private static void randomShuffling(int[] array) {
		// loop through all the elements in the array
		for (int i = 0; i < array.length; i++) {
			// create a random index in the range [0, array.length - 1]
			int randIndex = (int) (Math.random() * array.length);
			// swap the current element with the randomly indexed element
			if (i != randIndex) {
				int temp = array[i];
				array[i] = array[randIndex];
				array[randIndex] = temp;
			}
		}
	}
	// A method that generates a random state
	private static int[] getRandomState() {
		int[] numbers = new int[9];
		for (int i = 0; i < 9; i++)
			numbers[i] = i;
		randomShuffling(numbers);
		return numbers;
	}

	// A method for moving the empty cell right
	public boolean moveRight() {
		// the empty cell cannot go right if it is already at the rightmost column
		if (emptyCellCol == 2)
			return false; // return false as the empty cell cannot be moved
		// replace the empty cell with the tile on its right
		tiles[emptyCellRow][emptyCellCol] = tiles[emptyCellRow][emptyCellCol + 1];
		tiles[emptyCellRow][emptyCellCol + 1] = null;
		// update the column index of the empty cell
		emptyCellCol++;
		// return true as the empty cell is moved successfully
		return true;
	}

	// A method for moving the empty cell left
	public boolean moveLeft() {
		// the empty cell cannot go left if it is already at the leftmost column
		if (emptyCellCol == 0)
			return false; // return false as the empty cell cannot be moved
		// replace the empty cell with the tile on its left
		tiles[emptyCellRow][emptyCellCol] = tiles[emptyCellRow][emptyCellCol - 1];
		tiles[emptyCellRow][emptyCellCol - 1] = null;
		// update the column index of the empty cell
		emptyCellCol--;
		// return true as the empty cell is moved successfully
		return true;
	}

	// A method for moving the empty cell up
	public boolean moveUp() {
		// the empty cell cannot go up if it is already at the topmost row
		if (emptyCellRow == 0)
			return false; // return false as the empty cell cannot be moved
		// replace the empty cell with the tile above it
		tiles[emptyCellRow][emptyCellCol] = tiles[emptyCellRow - 1][emptyCellCol];
		tiles[emptyCellRow - 1][emptyCellCol] = null;
		// update the row index of the empty cell
		emptyCellRow--;
		// return true as the empty cell is moved successfully
		return true;
	}

	// A method for moving the empty cell down
	public boolean moveDown() {
		// the empty cell cannot go down if it is already at the bottommost row
		if (emptyCellRow == 2)
			return false; // return false as the empty cell cannot be moved
		// replace the empty cell with the tile below it
		tiles[emptyCellRow][emptyCellCol] = tiles[emptyCellRow + 1][emptyCellCol];
		tiles[emptyCellRow + 1][emptyCellCol] = null;
		// update the row index of the empty cell
		emptyCellRow++;
		// return true as the empty cell is moved successfully
		return true;
	}

	// A method for drawing the board by using the StdDraw library
	public void draw(int state) {
		// clear the drawing canvas using the background color (changes based on state)
		if (state == STATE_SOLVED)
			StdDraw.clear(endBgColor);
		else if (state == STATE_NOT_SOLVED)
			StdDraw.clear(backgroundColor);
		else
			StdDraw.clear(failBgColor);
		// for each tile in the tile matrix
		for (int row = 0; row < 3; row++)
			for (int col = 0; col < 3; col++) {
				// skip the empty cell
				if (tiles[row][col] == null)
					continue;
				// get the position of the tile based on its indexes
				// by using the getTilePosition method defined below
				Point tilePosition = getTilePosition(row, col);
				// draw the tile centered on its position and state
				tiles[row][col].draw(tilePosition.x, tilePosition.y, state);

			}
		// draw the box around the board (color changes based on state)
		if (state == STATE_SOLVED)
			StdDraw.setPenColor(endBoxColor);
		else if (state == STATE_NOT_SOLVED)
			StdDraw.setPenColor(boxColor);
		else
			StdDraw.setPenColor(failBoxColor);
		StdDraw.setPenRadius(lineThickness);
		StdDraw.square(2, 2, 1.5);
		StdDraw.setPenRadius(); // reset pen radius to its default value
	}

	// An inner method that returns the position of the tile on the board
	// with the given row and column indexes
	private Point getTilePosition(int rowIndex, int columnIndex) {
		// convert the indexes to the positions in StdDraw
		int posX = columnIndex + 1, posY = 3 - rowIndex;
		return new Point(posX, posY);
	}
	// A getter that gives the cost
	public int getCost() {
		return cost;
	}
	// A setter that sets the cost
	public void setCost(int cost) {
		this.cost = cost;
	}
	// A getter that gives heuristic
	public int getHeuristic() {
		return heuristic;
	}
	// A setter that sets heuristic
	public void setHeuristic(int heuristic) {
		this.heuristic = heuristic;
	}
	// A method to check if two boards are equal to each other

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Board board = (Board) o;
	    // for each tile in the tile matrix
		for (int row = 0; row < tiles.length; row++) {
			for (int col = 0; col < tiles[row].length; col++) {
				Tile thisTile = this.tiles[row][col];
				Tile otherTile = board.tiles[row][col];
				//Checks if each of the tiles are the same or not
				if ((thisTile == null && otherTile != null) || (thisTile != null && otherTile == null)
						|| (thisTile != null && otherTile != null && thisTile.getNumber() != otherTile.getNumber())) {
					return false;
				}
			}
		}
		return true;
	}

}