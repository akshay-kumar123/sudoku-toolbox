package sudoku;

import sudoku.generator.Difficulty;
import sudoku.generator.Generator;

public class Sudoku {
	
	public Sudoku() {
		testGenerator();
	}

	private void testGenerator() {
		Generator gen = Generator.getInstance();
		gen.generateSudoku(Difficulty.DIFFICULT, false);
	}
	
	public static void main(String[] args) {
		new Sudoku();
	}

}
