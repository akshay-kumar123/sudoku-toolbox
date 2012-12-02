package sudoku.generator;

import sudoku.Grid;
import sudoku.solver.Solver;
import sudoku.solver.SolverGoal;
import sudoku.solver.exception.CandidateNotFoundException;
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
	
	
	public Grid generateSudoku(Difficulty difficulty) {
		long startTime = System.nanoTime();

		Grid terminalPattern;
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
			Grid sudoku = digHoles(terminalPattern, difficulty);
			//sudoku.printGrid();
			
			Solver solver = new Solver(sudoku);
			solver.solve(SolverGoal.STOP_SECOND_SOLUTION);
			if (solver.foundSingleSolutions()) {
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
	
	private Grid generateTerminalPattern() {
		try {
			GeneratorGrid genGrid = new GeneratorGrid(new Grid());
			genGrid.preFill();
			Grid terminalPattern = genGrid.solveTerminalPattern();
			if (terminalPattern == null) {
				System.out.println(genGrid.toGrid());
			}
			return terminalPattern;
		} catch (UnitConstraintException | ZeroCandidateException | CandidateNotFoundException e) {
			return null;
		}
	}
	
	private Grid digHoles(Grid terminalPattern, Difficulty difficulty) {
		try {
			GeneratorGrid genGrid = new GeneratorGrid(terminalPattern);
			genGrid.digHoles(difficulty);
			return genGrid.toGrid();
		} catch (UnitConstraintException | ZeroCandidateException e) {
			e.printStackTrace();
			return null;
		}
	}

}
