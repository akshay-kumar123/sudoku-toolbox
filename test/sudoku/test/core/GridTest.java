package sudoku.test.core;

import static org.junit.Assert.*;

import org.junit.Test;

import sudoku.Grid;
import sudoku.test.AllTests;

public class GridTest {

	@Test
	public void newEmptyGrid() {
		assertEquals(Grid.EMPTY_GRID, new Grid().toString());
	}
	
	@Test
	public void newGrid() {
		assertEquals(AllTests.TEST_GRIDS[0], new Grid(AllTests.TEST_GRIDS[0]).toString());
	}
	
	@Test
	public void getCellValue() {
		assertEquals(3, new Grid(AllTests.TEST_GRIDS[0]).getCellValue(0, 2));
	}
	
	@Test
	public void setCellValue() {
		Grid g = new Grid(AllTests.TEST_GRIDS[0]);
		g.setCellValue(0, 0, 4);
		assertEquals(4, g.getCellValue(0, 0));
	}
	
}
