import lib.StdDraw; // used for animating the 8 puzzle with user interactions

import java.awt.Color; // for coloring the board
import java.awt.event.KeyEvent; // for the constants of the keys on the keyboard
import java.util.List; // for storing

//A program that implements the 8 puzzle.
public class EightPuzzle {
	public static void main(String[] args) {
		// StdDraw setup
	    // -----------------------------------------------------------------------
	    // set the size of the canvas (the drawing area) in pixels
	    StdDraw.setCanvasSize(500, 500);
	    // set the range of both x and y values for the drawing canvas
	    StdDraw.setScale(0.5, 3.5);
	    // enable double buffering to animate moving the tiles on the board
	    StdDraw.enableDoubleBuffering();
	    
	    // create a random board for the 8 puzzle (You can also manually enter a board)
	    Board board = new Board();

	    // create a board to represent goal state
	 	Board goal = new Board(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 0 });

	 	// create the solver
	 	Alg solver = new Alg();

	 	// put the board into "not solved" state to represent its color
		int state = Board.STATE_NOT_SOLVED;
		// Print the instructions to console
		System.out.println();
		System.out.println("Welcome To The 8 Puzzle Game");
		System.out.println();
		System.out.println("You can play the game using the arrow keys.");
		System.out.println("Press H to solve the game automatically using misplaced tiles.");
		System.out.println("Press M to solve the game automatically using manhattan distance.");
		System.out.println("Press R to get a new random board.");
		System.out.println("Press G to get a new solvable board.");
		System.out.println("Press F to get a new unsolvable board.");
		System.out.println();
		// The main animation and user interaction loop
	    // -----------------------------------------------------------------------
		while (true) {
			// draw the board, show the resulting drawing and pause for a short time
			StdDraw.clear();
			board.draw(state);
			StdDraw.show();
			StdDraw.pause(100); // 100 ms
			// if the user has pressed the right arrow key on the keyboard
			if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) {
				// not solved state for coloring
				state = Board.STATE_NOT_SOLVED;
				// move the empty cell right
				board.moveRight();
			}
			// if the user has pressed the left arrow key on the keyboard
			if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) {
				// not solved state for coloring
				state = Board.STATE_NOT_SOLVED;
				// move the empty cell left
				board.moveLeft();
			}
			// if the user has pressed the up arrow key on the keyboard
			if (StdDraw.isKeyPressed(KeyEvent.VK_UP)) {
				// not solved state for coloring
				state = Board.STATE_NOT_SOLVED;
				// move the empty cell up
				board.moveUp();
			}
			// if the user has pressed the down arrow key on the keyboard
			if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN)) {
				// not solved state for coloring
				state = Board.STATE_NOT_SOLVED;
				// move the empty cell down
				board.moveDown();
			}
			// if the user has pressed the R key on the keyboard
			if (StdDraw.isKeyPressed(KeyEvent.VK_R)) {
				// not solved state for coloring
				state = Board.STATE_NOT_SOLVED;
				System.out.println("New Board Created!");
				System.out.println();
				// create a new random board
				board = new Board();
			}
			// if the user has pressed the G key on the keyboard
			if (StdDraw.isKeyPressed(KeyEvent.VK_G)) {
				// not solved state for coloring
				state = Board.STATE_NOT_SOLVED;
				System.out.println("New Board Created (Guaranteed Solvable)!");
				System.out.println();
				// create a new random board
				board = new Board();
				// create a board until the board is solvable
				do  {
					board = new Board();
				} while(!Alg.isSolvable(board.getInitialState()));
			}
			// if the user has pressed the F key on the keyboard
			if (StdDraw.isKeyPressed(KeyEvent.VK_F)) {
				// not solved state for coloring
				state = Board.STATE_NOT_SOLVED;
				System.out.println("New Board Created (Guaranteed Unsolvable)!");
				System.out.println();
				// create a new random board
				board = new Board();
				// create a board until the board is unsolvable
				do  {
					board = new Board();
				} while(Alg.isSolvable(board.getInitialState()));
			}
			// if the user has pressed the H key on the keyboard
			if (StdDraw.isKeyPressed(KeyEvent.VK_H)) {
				// not solved state for coloring
				state = Board.STATE_NOT_SOLVED;
				//State that algorithm has been activated
				System.out.println("Algorithm Activated! Using Misplaced Tiles");
				// Check if the board is solvable, proceed if true
				if (Alg.isSolvable(board.getCurrentState())) {
					// State that it is solvable and write "Solving..." on the board while the algorithm calculates the solution
					System.out.println("Solvable!");
					System.out.println();
					StdDraw.setPenColor(Color.WHITE);
					StdDraw.text(2, 2, "Solving...");
					StdDraw.show();		
					// Assign the each solution state to the solutionPath list
					List<Board> solutionPath = solver.solve(board, goal, 0);
					// A loop for drawing each state within the solution path
					for (int i = 0; i < solutionPath.size(); i++) {
						StdDraw.clear();
						solutionPath.get(i).draw(state); // Draw the current step of the solution
						StdDraw.show();
						StdDraw.pause(500); // Pause to visualize the step, adjust the time as needed
					}
					// Change the state to "solved" for coloring the board green and show the goal state since we reached it
					state = Board.STATE_SOLVED;
					board = new Board(goal.getInitialState());
				}
				// if the board is unsolvable, change the state to "unsolvable" for coloring the board red
				else {
					state = Board.STATE_UNSOLVABLE;
					System.out.println("Not solvable.");
					System.out.println();
				}
			}
			// if the user has pressed the H key on the keyboard
			if (StdDraw.isKeyPressed(KeyEvent.VK_M)) {
				// not solved state for coloring
				state = Board.STATE_NOT_SOLVED;
				//State that algorithm has been activated
				System.out.println("Algorithm Activated! Using Manhattan Distance");
				// Check if the board is solvable, proceed if true
				if (Alg.isSolvable(board.getCurrentState())) {
					// State that it is solvable and write "Solving..." on the board while the algorithm calculates the solution
					System.out.println("Solvable!");
					System.out.println();
					StdDraw.setPenColor(Color.WHITE);
					StdDraw.text(2, 2, "Solving...");
					StdDraw.show();		
					// Assign the each solution state to the solutionPath list
					List<Board> solutionPath = solver.solve(board, goal, 1);
					// A loop for drawing each state within the solution path
					for (int i = 0; i < solutionPath.size(); i++) {
						StdDraw.clear();
						solutionPath.get(i).draw(state); // Draw the current step of the solution
						StdDraw.show();
						StdDraw.pause(500); // Pause to visualize the step, adjust the time as needed
					}
					// Change the state to "solved" for coloring the board green and show the goal state since we reached it
					state = Board.STATE_SOLVED;
					board = new Board(goal.getInitialState());
				}
				// if the board is unsolvable, change the state to "unsolvable" for coloring the board red
				else {
					state = Board.STATE_UNSOLVABLE;
					System.out.println("Not solvable.");
					System.out.println();
				}
			}
		}
	}
}