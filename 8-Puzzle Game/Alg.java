import java.util.*; //used to import necessary classes from existing packages and use them

public class Alg {
	// Algorithm that creates the solution
	public List<Board> solve(Board initial, Board goal,int n) {
		// Create a priority queue for open set and compare them by the sum of cost + heuristic
		PriorityQueue<Board> openSet = new PriorityQueue<>(
				Comparator.comparingInt(b -> b.getCost() + b.getHeuristic()));

		// Set for the closed set to track already evaluated nodes
		Set<Board> closedSet = new HashSet<>();

		// Set the cost and heuristics of initial state
		initial.setCost(0);
		initial.setHeuristic(initial.calculateHeuristic(goal, n));

		// Add the initial state to the open set
		openSet.add(initial);
		// A loop that continues till the open set is empty
		while (!openSet.isEmpty()) {
			Board current = openSet.poll(); // Node in open set having the lowest f(n)

			// 	Reconstructs the path if the current node is goal node
			if (current.equals(goal)) {
				return reconstructPath(current); 
			}
			// Add the current board to closed list
			closedSet.add(current);

			// A loop that searches for each neighbor of the current node
			for (Board neighbor : current.getNeighbors()) {
				if (closedSet.contains(neighbor)) {
					continue; // Ignore the neighbor which is already evaluated
				}
				// Calculate the distance from start to neighbor
				int gScore = current.getCost() + 1;
				// When a new node is discovered, add it to open list
				if (!openSet.contains(neighbor)) {
					openSet.add(neighbor);
				} 
				// If gScore is larger than previous cost, ignore this because it is not a better path
				else if (gScore >= neighbor.getCost()) {
					continue;
				}
				// This path is the best one so record its values
				neighbor.setParent(current);
				neighbor.setCost(gScore);
				neighbor.setHeuristic(neighbor.calculateHeuristic(goal,n));
			}
		}

		return null; // When no solution is found
	}

	// Reconstructs the solution path
	private List<Board> reconstructPath(Board current) {
		List<Board> totalPath = new ArrayList<>();
		// A loop to fill path until there are no current
		while (current != null) {
			totalPath.add(current);
			current = current.getParent(); // Move backwards from goal to initial state
		}
		Collections.reverse(totalPath); // Reverse the path to start from the initial state
		return totalPath;
	}
	// A method to calculate if a board is solvable
	public static boolean isSolvable(int[] initialState) {
		int inversion = 0;
		for (int i = 0; i < initialState.length; i++) { // Index of initial state
			for (int j = i + 1; j < initialState.length; j++) { // Indexes of the steps after the current one
				// Increases inversion if a tile has a smaller number after them (not counting 0)
				if (initialState[i] > initialState[j] && initialState[j] != 0) {
					inversion++;
				}
			}
		}
		// The board is solvable if the inversion is even
		return inversion % 2 == 0;
	}
}