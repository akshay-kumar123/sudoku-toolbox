package sudoku.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import sudoku.Grid;
import sudoku.generator.GeneratorGrid;
import sudoku.solver.exception.UnitConstraintException;
import sudoku.solver.exception.ZeroCandidateException;

public class GridTest {

	private Grid testGrid;
	
	@Before
	public void setUp() {
		testGrid =  new Grid(TestGrid.VERY_EASY.getGridString());
	}
	
	@Test
	public void testEmptyGrid() {
		Grid emptyGrid =  new Grid();
		assertEquals(Grid.EMPTY_GRID, emptyGrid.toString());
	}
	
	@Test
	public void testGridFromString() {
		assertEquals(TestGrid.VERY_EASY.getGridString(), testGrid.toString());
	}
	
	@Test
	public void testGridFromGeneratorGrid() throws UnitConstraintException, ZeroCandidateException {
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
