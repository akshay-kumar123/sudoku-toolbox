package sudoku.solver;

import java.util.ArrayList;

import sudoku.StaticGrid;
import sudoku.solver.exception.*;

public class Solver {

	private StaticGrid grid;
	private SolverGrid solverGrid;

	private SolverResult result;
	private ArrayList<StaticGrid> solutions;
	private long solvingTime;

	
	public Solver(StaticGrid grid) {
		this.grid = grid;
		solutions = new ArrayList<StaticGrid>();
	}
	
	
	public void solve(SolverMode goal) throws InvalidGridException {
		// If solving has already been performed, do not try solving again
		if (solvingPerformed()) {
			return;
		}
		
		// Mark start time
		long startTime = System.nanoTime();
		
		try {
			// Solve
			solverGrid = new SolverGrid(this, grid);
			if (!solverGrid.propagateConstraints()) {
				solverGrid.depthFirstSearch(goal);
			}
			
			if (solutions.size() == 1) {
				if (goal != SolverMode.STOP_FIRST_SOLUTION) {
					result = SolverResult.ONE_SOLUTION;
				} else {
					result = SolverResult.AT_LEAST_ONE_SOLUTION;
				}
			} else if (solutions.size() > 1) {
				result = SolverResult.MULTIPLE_SOLUTIONS;
			} else {
				result = SolverResult.NO_SOLUTION;
			}
		} catch (CandidateNotFoundException | ZeroCandidateException exc) {
			result = SolverResult.NO_SOLUTION;
		} catch (UnitConstraintException exc) {
			result = SolverResult.NO_SOLUTION;
			throw new InvalidGridException("Grid is not valid");
		} finally {
			// Calculate solving time
			solvingTime = (System.nanoTime() - startTime) / 1000000;
		}
	}
	
	
	public void addSolution(StaticGrid validGrid) {
		// Prevent adding solutions after solving has completed
		if (!solvingPerformed()) {
			solutions.add(validGrid);
		}
	}
	
	public SolverResult getResult() {
		return result;
	}
	
	public int getSolutionsCount() {
		return solutions.size();
	}
	
	public ArrayList<StaticGrid> getSolutions() {
		return solutions;
	}

	public boolean solvingPerformed() {
		return result != null;
	}
	
	public long getSolvingTime() {
		return solvingTime;
	}
	
	public String getSolvingTimeToString() {
		return solvingTime + "ms";
	}
	
	
	public void printSolutions() {
		// Display result
		switch (result) {
			case NO_SOLUTION:
				System.out.println("Invalid sudoku grid: no solution.");
				break;
			case ONE_SOLUTION:
				System.out.println("Valid sudoku grid: unique solution found in " + getSolvingTimeToString() + ".");
				break;
			case MULTIPLE_SOLUTIONS:
				System.out.println("Invalid sudoku grid: " + solutions.size() + " solutions found in " + getSolvingTimeToString() + ".");
				break;
			default:
		}
		
		// Display solution(s)
		for (StaticGrid g : solutions) {
			g.printGrid();
		}
	}
	
	
	
}
