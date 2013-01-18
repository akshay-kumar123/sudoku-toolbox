package sudoku.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import sudoku.StaticGrid;
import sudoku.exception.InvalidGridException;
import sudoku.exception.UnitConstraintException;
import sudoku.exception.ZeroCandidateException;
import sudoku.generator.GeneratorGrid;

public class GridTest {

	private StaticGrid testGrid;
	
	@Before
	public void setUp() throws InvalidGridException {
		testGrid =  new StaticGrid(TestGrid.PIECE_OF_CAKE.getGridString());
	}
	
	@Test
	public void testEmptyGrid() {
		StaticGrid emptyGrid =  new StaticGrid();
		assertEquals(StaticGrid.EMPTY_GRID, emptyGrid.toString());
	}
	
	@Test
	public void testGridFromString() {
		assertEquals(TestGrid.PIECE_OF_CAKE.getGridString(), testGrid.toString());
	}
	
	@Test
	public void testGridFromGeneratorGrid() throws UnitConstraintException, ZeroCandidateException {
		GeneratorGrid dGrid = new GeneratorGrid(testGrid);
		assertEquals(testGrid.toString(), new StaticGrid(dGrid).toString());
	}
	
	@Test
	public void testGetCellValue() {
		assertEquals(3, testGrid.getCellValue(0, 2));
	}
	
	@Test
	public void testSetCellValue() {
		testGrid.setCellValue(0, 0, 4);
		assertEquals(4, testGrid.getCellValue(0, 0));
	}
	
}
