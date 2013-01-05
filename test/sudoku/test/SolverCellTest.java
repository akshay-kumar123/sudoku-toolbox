package sudoku.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import sudoku.solver.SolverCell;

public class SolverCellTest {
	
	private SolverCell defaultCell, emptyCell, givenCell;

	@Before
	public void setUp() {
		defaultCell = new SolverCell(null);
		emptyCell = new SolverCell(null, 0);
		givenCell = new SolverCell(null, 3);
	}

	
	/* 
	 * Test method getValue
	 */
	
	@Test
	public void testDefaultCellHasValueZero() {
		assertEquals(0, defaultCell.getValue());
	}

	@Test
	public void testEmptyCellHasValueZero() {
		assertEquals(0, emptyCell.getValue());
	}

	@Test
	public void testGivenCellHasValue() {
		assertEquals(3, givenCell.getValue());
	}
	
	
	/* 
	 * Test method isGiven
	 */
	
	@Test
	public void testIsGivenOnEmptyCell() {
		assertFalse(emptyCell.isGiven());
	}
	
	@Test
	public void testIsGivenOnGivenCell() {
		assertTrue(givenCell.isGiven());
	}

	
	/* 
	 * Test method getCandidates 
	 */

	@Test
	public void testDefaultCellHasCandidatesNotNull() {
		assertNotNull(defaultCell.getCandidates());
	}

	@Test
	public void testEmptyCellHasCandidatesNotNull() {
		assertNotNull(emptyCell.getCandidates());
	}
	
	@Test
	public void testGivenCellHasCandidatesNull() {
		assertNull(givenCell.getCandidates());
	}

}
