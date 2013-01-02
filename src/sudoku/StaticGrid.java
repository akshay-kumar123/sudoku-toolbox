package sudoku;

import java.util.regex.Pattern;

import sudoku.solver.exception.InvalidGridException;

public class StaticGrid {
	
	public static final String EMPTY_GRID = ".................................................................................";
	
	private int[][] gridArray;

	
	public StaticGrid() {
		toArray(EMPTY_GRID);
	}
	
	public StaticGrid(String gridString) throws InvalidGridException {
		// Check length (9x9=81 characters)
		if (gridString.length() != 81) {
			throw new InvalidGridException("Grid string must be 81 characters long");
		}
		
		// Check characters (only dots and digits from 1 to 9)
		if (!Pattern.matches("(?s)^[1-9\\.]+$", gridString)) {
			throw new InvalidGridException("Grid string can only contain dots and digits from 1 to 9");
		}
		
		toArray(gridString);
	}
	
	public StaticGrid(DynamicGrid dGrid) {
		gridArray = new int[9][9];

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				gridArray[i][j] = dGrid.getCellValue(i, j);
			}
		}
	}
	
	
	private void toArray(String grid) {
		gridArray = new int[9][9];
		
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				int index = i * 9 + j;
				String character = grid.substring(index, index + 1);
				if (character.equals(".")) {
					gridArray[i][j] = 0;
				} else {
					gridArray[i][j] = Integer.parseInt(character);
				}
			}
		}
	}
	
	public String toString() {
		String grid = "";
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				int val = gridArray[i][j];
				grid += (val != 0 ? val : ".");
			}
		}
		return grid;
	}
	
	
	public int getCellValue(int x, int y) {
		return gridArray[x][y];
	}
	
	public void setCellValue(int x, int y, int newVal) {
		gridArray[x][y] = newVal;
	}
	
	
	public void printGrid() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (j % 3 == 0) {
					System.out.print(" ");
				}
				System.out.print(cellValueToString(i, j) + " ");
				if (j % 3 == 2) {
					if (j < 6) {
						System.out.print('|');
					} else {
						System.out.println();
					}
				}
			}
			if (i % 3 == 2 && i < 6) {
				System.out.println("-------+-------+-------");
			}
		}
		System.out.println();
	}
	
	private String cellValueToString(int x, int y) {
		int value = gridArray[x][y];
		if (value == 0) {
			return ".";
		} else {
			return String.valueOf(value);
		}
	}
	
}
