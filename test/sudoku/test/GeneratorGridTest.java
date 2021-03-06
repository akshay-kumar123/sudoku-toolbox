package sudoku.test;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import sudoku.StaticGrid;
import sudoku.exception.InvalidGridException;
import sudoku.exception.UnitConstraintException;
import sudoku.exception.ZeroCandidateException;
import sudoku.generator.GeneratorGrid;
import sudoku.generator.GeneratorGridException;

public class GeneratorGridTest {
	
	@Test (expected = UnitConstraintException.class)
	public void testWrongGrid() throws UnitConstraintException, ZeroCandidateException, InvalidGridException {
		new GeneratorGrid(new StaticGrid(TestGrid.WRONG_GRID.getGridString()));
	}

	@Test
	public void testPreFill() throws UnitConstraintException, ZeroCandidateException, GeneratorGridException {
		// Pre-fill an empty grid
		GeneratorGrid grid = new GeneratorGrid(new StaticGrid());
		grid.preFill();
		
		// Test that the resulting grid has the right number of givens
		String preFilledGridString = new StaticGrid(grid).toString();
		
		Matcher m = Pattern.compile("[1-9]").matcher(preFilledGridString);
		int count = 0;
		while (m.find()) {
			count++;
		}
		
		assertEquals(GeneratorGrid.PRE_FILL_GIVENS, count);
	}
	
	@Test
	public void testIsEmptyGrid() throws UnitConstraintException, ZeroCandidateException {
		GeneratorGrid grid = new GeneratorGrid(new StaticGrid());
		assertEquals(0, grid.getGivensCount());
	}

	@Test (expected = GeneratorGridException.class)
	public void testPreFillNonEmptyGrid() throws UnitConstraintException, ZeroCandidateException, InvalidGridException, GeneratorGridException {
		GeneratorGrid grid = new GeneratorGrid(new StaticGrid(TestGrid.EASY.getGridString()));
		grid.preFill();
	}

}
