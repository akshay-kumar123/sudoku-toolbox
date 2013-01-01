package sudoku.generator;

import sudoku.StaticGrid;
import sudoku.solver.Solver;
import sudoku.solver.SolverMode;
import sudoku.solver.SolverResult;
import sudoku.solver.exception.CandidateNotFoundException;
import sudoku.solver.exception.InvalidGridException;
import sudoku.solver.exception.UnitConstraintException;
import sudoku.solver.exception.ZeroCandidateException;

public class Generator {
	
	private static Generator instance;
	
	public Generator() {}
	
	public static Generator getInstance() {
		if (instance == null) {
			instance = new Generator();
		}
		
		return instance;
	}
	
	
	public StaticGrid generateSudoku(Difficulty difficulty) {
		long startTime = System.nanoTime();

		StaticGrid terminalPattern;
		do {
		/*for (int i = 0; i < 10000; i++) {
			if (i % 500 == 0) {
				System.out.println(i);
			}*/
			terminalPattern = generateTerminalPattern();
			if (terminalPattern == null) {
				System.out.println("Error");
			}
			// Some grids may not have any valid solution, depending on the initial amount of givens
		} while (terminalPattern == null);
		//}

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
	
	private StaticGrid generateTerminalPattern() {
		try {
			GeneratorGrid genGrid = new GeneratorGrid(new StaticGrid());
			genGrid.preFill();
			StaticGrid terminalPattern = genGrid.solveTerminalPattern();
			if (terminalPattern == null) {
				System.out.println(new StaticGrid(genGrid));
			}
			return terminalPattern;
		} catch (UnitConstraintException | ZeroCandidateException | CandidateNotFoundException | InvalidGridException e) {
			return null;
		}
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
