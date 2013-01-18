package sudoku.solver;

import java.util.ArrayList;

import sudoku.StaticGrid;
import sudoku.exception.*;
import sudoku.generator.Difficulty;

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
	

	/* Command line script */
	public static void main(String[] args) {
		// Check at least one argument is provided
		if (args == null || args.length < 1) {
			System.out.println("No grid string provided");
			return;
		}
		
		// Create and print static grid
		StaticGrid grid;
		try {
			grid = new StaticGrid(args[0]);
			grid.printGrid();
		} catch (InvalidGridException e) {
			System.out.println(e.getMessage());
			return;
		}
		
		Solver solver = new Solver(grid);
		try {
			solver.solve(SolverMode.DO_NOT_STOP);
		} catch (InvalidGridException e) {
			System.out.println(e.getMessage());
			return;
		}
		solver.printSolutions();
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
			
			boolean gridChanged, needDPS = true;
			do {
				// Naked singles technique
				solverGrid.nakedSingles();
				
				if (solverGrid.isSolved()) {
					needDPS = false;
					addSolution(new StaticGrid(solverGrid));
					break;
				}

				// Hidden singles technique
				gridChanged = solverGrid.hiddenSingles();
			} while (gridChanged);
			
			if (needDPS) {
				// Depth First Search technique
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
			throw new InvalidGridException("The grid does not respect the rules of Sudoku");
		} finally {
			// Calculate solving time
			solvingTime = (System.nanoTime() - startTime) / 1000000;
		}
	}
	
	public Difficulty determineHumanDifficulty() throws InvalidGridException {
		// Initialise SolverGrid
		try {
			solverGrid = new SolverGrid(this, grid);
		} catch (ZeroCandidateException e) {
			result = SolverResult.NO_SOLUTION;
		} catch (UnitConstraintException e) {
			throw new InvalidGridException("The grid does not respect the rules of Sudoku");
		}
		
		// Start from lowest difficulty level
		Difficulty maxDiff = Difficulty.PIECE_OF_CAKE;
	
		// Solve with human techniques until EVIL difficulty is reached or grid is solved
		while (maxDiff.getIndex() < Difficulty.EVIL.getIndex() && !solverGrid.isSolved()) {
			Difficulty newDiff = solveWithHumanTechniques();

			if (maxDiff.getIndex() < newDiff.getIndex()) {
				maxDiff = newDiff;
			}
		}
		
		return maxDiff;
	}
	
	public Difficulty solveWithHumanTechniques() {
		Difficulty diff = Difficulty.PIECE_OF_CAKE;
		
		try {
			// Always try filling naked singles first
			solverGrid.nakedSingles();
			
			if (solverGrid.hiddenSingles()) {
				diff = Difficulty.EASY;
			}
		} catch (ZeroCandidateException | CandidateNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return diff;
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
