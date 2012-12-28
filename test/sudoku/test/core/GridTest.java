package sudoku.test.core;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import sudoku.Grid;
import sudoku.generator.GeneratorGrid;
import sudoku.solver.exception.UnitConstraintException;
import sudoku.solver.exception.ZeroCandidateException;
import sudoku.test.AllTests;

public class GridTest {

	private static Grid emptyGrid, testGrid;
	
	@BeforeClass
	public static void setUpBeforeClass() {
		emptyGrid =  new Grid();
		testGrid =  new Grid(AllTests.TEST_GRIDS[0]);
	}
	
	@Test
	public void testEmptyGridInit() {
		assertEquals(Grid.EMPTY_GRID, emptyGrid.toString());
	}
	
	@Test
	public void testGridInitFromString() {
		assertEquals(AllTests.TEST_GRIDS[0], testGrid.toString());
	}
	
	@Test
	public void testGridInitFromGeneratorGrid() throws UnitConstraintException, ZeroCandidateException {
		GeneratorGrid dGrid = new GeneratorGrid(testGrid);
		assertEquals(testGrid.toString(), new Grid(dGrid).toString());
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
