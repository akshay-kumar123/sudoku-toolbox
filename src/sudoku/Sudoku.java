package sudoku;

import sudoku.exception.InvalidGridException;
import sudoku.generator.Difficulty;
import sudoku.generator.Generator;
import sudoku.solver.Solver;
import sudoku.solver.SolverMode;
import sudoku.test.TestGrid;

public class Sudoku {
	
	public Sudoku() {
		testGenerator();
	}

	@SuppressWarnings("unused")
	private void testSolver() {
		long runningTime = 0;
		for (int i = 0; i < 10000; i++) {
			try {
				long startTime = System.nanoTime();
				Solver s = new Solver(new StaticGrid(TestGrid.MEDIUM.getGridString()));
				s.solve(SolverMode.STOP_SECOND_SOLUTION);
				runningTime += (System.nanoTime() - startTime) / 1000;
			} catch (InvalidGridException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Average running time = " + (runningTime / 10000));
	}

	private void testGenerator() {
		Generator gen = Generator.getInstance();
		gen.generateSudoku(Difficulty.EVIL, false);
	}
	
	public static void main(String[] args) {
		new Sudoku();
	}

}
