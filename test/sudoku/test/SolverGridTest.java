package sudoku.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import sudoku.Grid;
import sudoku.solver.SolverGrid;
import sudoku.solver.exception.CandidateNotFoundException;
import sudoku.solver.exception.UnitConstraintException;
import sudoku.solver.exception.ZeroCandidateException;

public class SolverGridTest {

	private SolverGrid grid;

	@Before
	public void setUp() throws Exception {
		grid = new SolverGrid(null, new Grid(TestGrid.VERY_EASY.getGridString()));
	}

	@Test
	public void testIsSolvedFalse() {
		assertFalse(grid.isSolved());
	}

	@Test
	public void testCandidatesInFirstCell() {
		ArrayList<Integer> candidates = new ArrayList<Integer>(2);
		candidates.add(4);
		candidates.add(5);
		assertEquals(candidates, grid.getCell(0, 0).getCandidates());
	}

	@Test
	public void testFillInSingleCandidateCells() throws CandidateNotFoundException, ZeroCandidateException  {
		grid.fillInSingleCandidateCells();
		assertTrue(grid.isSolved());
	}
	
	@Test (expected = UnitConstraintException.class)
	public void testWrongGrid() throws UnitConstraintException, ZeroCandidateException {
		new SolverGrid(null, new Grid(TestGrid.WRONG_GRID.getGridString()));
	}

}
