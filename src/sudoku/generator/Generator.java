package sudoku.generator;

import sudoku.StaticGrid;
import sudoku.exception.InvalidGridException;
import sudoku.exception.UnitConstraintException;
import sudoku.exception.ZeroCandidateException;
import sudoku.solver.Solver;
import sudoku.solver.SolverMode;
import sudoku.solver.SolverResult;

public class Generator {
	
	public final static int PREFILL_ATTEMPTS = 4;
	
	private static Generator instance;
	
	private Generator() {}
	
	public static Generator getInstance() {
		if (instance == null) {
			instance = new Generator();
		}
		
		return instance;
	}
	
	
	public StaticGrid generateSudoku(Difficulty difficulty, boolean preFill) {
		long startTime = System.nanoTime();

		StaticGrid terminalPattern = generateTerminalPattern(preFill);

		// Validate grid (make sure it has only one solution
		boolean keepGoing = true;
		while(keepGoing) {
			//terminalPattern.printGrid();
			StaticGrid sudoku = digHoles(terminalPattern, difficulty);
			//sudoku.printGrid();
			
			Solver solver = new Solver(sudoku);
			try {
				solver.solve(SolverMode.STOP_SECOND_SOLUTION);
			} catch (InvalidGridException exc) {}
			
			if (solver.getResult() == SolverResult.ONE_SOLUTION) {
				sudoku.printGrid();
				solver.printSolutions();
				keepGoing = false;
			} else {
				System.out.println("Invalid grid");
			}
		}

		// Calculate solving time
		long solvingTime = (System.nanoTime() - startTime) / 1000000;
		System.out.println("Duration: " + solvingTime);
		
		return null;
	}
	
	public StaticGrid generateTerminalPattern(boolean preFill) {
		StaticGrid terminalPattern = null;
		
		while (terminalPattern == null) {
			StaticGrid sourceGrid;
			
			if (preFill) {
				// If the number of cells to be pre-filled is too high, then the pre-filling might fail. 
				// Therefore, only try pre-filling a limited number of times.
				int preFillTries = 0;
				GeneratorGrid genGrid = null;
				
				while (genGrid == null && preFillTries < PREFILL_ATTEMPTS) {
					preFillTries++;
	
					// Initialize a GeneratorGrid from an empty StaticGrid
					try {
						genGrid = new GeneratorGrid(new StaticGrid());
					} catch (UnitConstraintException | ZeroCandidateException e) {
						// This should never happen
						System.out.println("Program error: initializing a DynamicGrid from and empty StaticGrid should not throw any exception.");
						e.printStackTrace();
						System.exit(1);
					}
	
					// Pre-fill the GeneratorGrid
					try {
						genGrid.preFill();
					} catch (ZeroCandidateException e) {
						// Lose GeneratorGrid instance
						genGrid = null;
					}
				}
				
				if (genGrid != null) {
					sourceGrid = new StaticGrid(genGrid);
				}
				else {
					// If the pre-filling failed, use an empty source grid
					sourceGrid = new StaticGrid();
				}
				
				//System.out.println("Final pre-filled grid after " + tries + " attempts");
				//sourceGrid.printGrid();
			}
			else {
				sourceGrid = new StaticGrid();
			}
	
			// Generate terminal pattern by solving source grid and stopping at first solution
			Solver solver = new Solver(sourceGrid);
			try {
				// Using a value GeneratorGrid.PRE_FILL_GIVENS
				solver.solve(SolverMode.STOP_FIRST_SOLUTION);
			} catch (InvalidGridException e) {
				// This should never happen
				System.out.println("Program error: source grid for terminal pattern generation should be valid, as it is either successfully pre-filled or simply empty.");
				e.printStackTrace();
				System.exit(1);
			}
			
			// Test solver's result
			if (solver.getResult() == SolverResult.AT_LEAST_ONE_SOLUTION) {
				// Retrieve the solver's first solution
				terminalPattern = solver.getSolutions().iterator().next();
			}
			else if (solver.getResult() != SolverResult.NO_SOLUTION) {
				// This should never happen
				System.out.println("Program error: solver's result for terminal pattern generation should only be AT_LEAST_ONE_SOLUTION or NO_SOLUTION.");
				System.exit(1);
			}
			
			// If solver's result is NO_SOLUTION, terminalPattern remains null. This should never happen when preFill is false.
			// The probability of a pre-filled grid leading to no solution increases with the number of pre-filled cells it contains.
		}
		//terminalPattern.printGrid();
		return terminalPattern;
	}
	
	private StaticGrid digHoles(StaticGrid terminalPattern, Difficulty difficulty) {
		try {
			GeneratorGrid genGrid = new GeneratorGrid(terminalPattern);
			genGrid.digHoles(difficulty);
			return new StaticGrid(genGrid);
		} catch (UnitConstraintException | ZeroCandidateException e) {
			e.printStackTrace();
			return null;
		}
	}

}
