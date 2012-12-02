package sudoku;

public class Grid {
	
	private static final String EMPTY_GRID = ".................................................................................";
	
	private String grid;
	private int[][] gridArray;

	
	public Grid() {
		this.grid = EMPTY_GRID;
		toArray();
	}
	
	public Grid(String grid) {
		this.grid = grid;
		toArray();
	}
	
	
	private void toArray() {
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
	
	public int getCellValue(int x, int y) {
		return gridArray[x][y];
	}
	
	public void setCellValue(int x, int y, int newVal) {
		gridArray[x][y] = newVal;
	}
	
	
	private String cellValueToString(int x, int y) {
		int value = gridArray[x][y];
		if (value == 0) {
			return ".";
		} else {
			return String.valueOf(value);
		}
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
	
	public String toString() {
		return grid;
	}
	
}
