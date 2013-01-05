package sudoku;

import java.util.ArrayList;

import sudoku.exception.ZeroCandidateException;

public abstract class DynamicGrid {
	
	protected Cell[][] cells;
	protected ArrayList<Unit> units;
	
	
	protected void findAllCandidates() throws ZeroCandidateException {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				cells[i][j].findCandidates();
			}
		}
	}
	

	public int getCellValue(int x, int y) {
		return cells[x][y].getValue();
	}
	
	public Cell getCell(int x, int y) {
		return cells[x][y];
	}
	
	
	public void printDynamicGrid() {
		System.out.println();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (j % 3 == 0) {
					System.out.print(" ");
				}
				Cell c = cells[i][j];
				if (c.isGiven()) {
					System.out.print(c.getValue() + "          ");
				} else {
					String s = c.candidatesToString();
					String s2 = s;
					for (int k = 0; k < (11 - s.length()); k++) {
						s2 += " ";
					}
					System.out.print(s2);
				}
				if (j % 3 == 2) {
					if (j < 6) {
						System.out.print('|');
					} else {
						System.out.println();
					}
				}
			}
			if (i % 3 == 2 && i < 6) {
				System.out.println("----------------------------------+----------------------------------+----------------------------------");
			}
		}
		System.out.println();
	}
	
}
