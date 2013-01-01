package sudoku.test;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import sudoku.StaticGrid;
import sudoku.generator.GeneratorGrid;
import sudoku.solver.exception.CandidateNotFoundException;
import sudoku.solver.exception.InvalidGridException;
import sudoku.solver.exception.UnitConstraintException;
import sudoku.solver.exception.ZeroCandidateException;

public class GeneratorTest {

	private GeneratorGrid grid;

	@Before
	public void setUp() throws Exception {
		grid = new GeneratorGrid(new StaticGrid());
	}
	
	@Test (expected = UnitConstraintException.class)
	public void testWrongGrid() throws UnitConstraintException, ZeroCandidateException {
		new GeneratorGrid(new StaticGrid(TestGrid.WRONG_GRID.getGridString()));
	}

	@Test
	public void testPreFill() throws CandidateNotFoundException, ZeroCandidateException {
		grid.preFill();
		String preFilledGridString = new StaticGrid(grid).toString();
		
		Matcher m = Pattern.compile("[1-9]").matcher(preFilledGridString);
		int count = 0;
		while (m.find()) {
			count++;
		}
		
		assertEquals(GeneratorGrid.PRE_FILL_GIVENS, count);
	}
	
	@Test
	public void testSolveTerminalPatternRandomness() throws InvalidGridException {
		HashSet<String> set = new HashSet<String>(10);
		for (int i = 0; i < 10; i++) {
			if (!set.add(grid.solveTerminalPattern().toString())) {
				assertFalse(true);
				return;
			}
		}
		assertTrue(true);
	}

}
