package sudoku.solver;

import java.util.ArrayList;

import sudoku.Grid;
import sudoku.solver.exception.*;

public class Solver {

	private Grid grid;
	private SolverGrid solverGrid;
	private ArrayList<Grid> solutions = new ArrayList<Grid>();
	private long solvingTime;

	
	public Solver(Grid grid) {
		this.grid = grid;
	}
	
	
	public void solve(SolverGoal goal) {
		// Mark start time
		long startTime = System.nanoTime();
		
		try {
			// Solve
			solverGrid = new SolverGrid(this, grid);
			if (!solverGrid.propagateConstraints()) {
				solverGrid.depthFirstSearch(goal);
			}
		} catch (CandidateNotFoundException | ZeroCandidateException exc) {
			System.out.println("Grid has no solution.");
		} catch (UnitConstraintException exc) {
			System.out.println("Grid is not valid.");
		}

		// Calculate solving time
		solvingTime = (System.nanoTime() - startTime) / 1000000;
	}
	
	
	public void addSolution(Grid validGrid) {
		solutions.add(validGrid);
	}
	
	public int getSolutionsCount() {
		return solutions.size();
	}
	
	public ArrayList<Grid> getSolutions() {
		return solutions;
	}
	
	public long getSolvingTime() {
		return solvingTime;
	}
	
	public String getSolvingTimeString() {
		return solvingTime + "ms";
	}
	
	public boolean foundSingleSolutions() {
		return solutions.size() == 1;
	}
	
	
	public void printSolutions() {
		if (solutions.size() > 0) {
			// Display result
			if (foundSingleSolutions()) {
				System.out.println("Valid sudoku grid: unique solution found in " + getSolvingTimeString() + ".");
			} else {
				System.out.println("Invalid sudoku grid: " + solutions.size() + " solutions found in " + getSolvingTimeString() + ".");
			}
			
			// Display solution(s)
			for (Grid g : solutions) {
				g.printGrid();
			}
		} else {
			System.out.println("Invalid sudoku grid: no solution.");
		}
	}
	
}
