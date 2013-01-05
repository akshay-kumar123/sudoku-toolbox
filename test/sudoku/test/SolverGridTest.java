package sudoku.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import sudoku.StaticGrid;
import sudoku.exception.CandidateNotFoundException;
import sudoku.exception.InvalidGridException;
import sudoku.exception.UnitConstraintException;
import sudoku.exception.ZeroCandidateException;
import sudoku.solver.SolverGrid;

public class SolverGridTest {

	private SolverGrid grid;

	@Before
	public void setUp() throws Exception {
		grid = new SolverGrid(null, new StaticGrid(TestGrid.VERY_EASY.getGridString()));
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
		grid.fillInNakedSingleCells();
		assertTrue(grid.isSolved());
	}
	
	@Test (expected = UnitConstraintException.class)
	public void testWrongGrid() throws UnitConstraintException, ZeroCandidateException, InvalidGridException {
		new SolverGrid(null, new StaticGrid(TestGrid.WRONG_GRID.getGridString()));
	}
	
	@Test (expected = InvalidGridException.class)
	public void testWrongGridString() throws UnitConstraintException, ZeroCandidateException, InvalidGridException {
		new SolverGrid(null, new StaticGrid(TestGrid.WRONG_GRID_STRING.getGridString()));
	}
	
	@Test (expected = InvalidGridException.class)
	public void testWrongGridStringLength() throws UnitConstraintException, ZeroCandidateException, InvalidGridException {
		new SolverGrid(null, new StaticGrid(TestGrid.WRONG_GRID_STRING_LENGTH.getGridString()));
	}

}
