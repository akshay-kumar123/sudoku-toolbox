package sudoku;

import java.util.ArrayList;

import sudoku.solver.exception.UnitConstraintException;
import sudoku.solver.exception.ZeroCandidateException;

public class DynamicGrid {
	
	protected Cell[][] cells;
	protected ArrayList<Unit> units;
	
	
	public DynamicGrid() {}
	
	public DynamicGrid(Grid grid) throws UnitConstraintException, ZeroCandidateException {
		cells = new Cell[9][9];
		units = new ArrayList<Unit>(27);
		
		// Initialize units
		Unit[] rows = new Unit[9];
		Unit[] cols = new Unit[9];
		Unit[] boxes = new Unit[9];
		
		for (int i = 0; i < 9; i++) {
			rows[i] = new Unit(UnitType.ROW);
			units.add(rows[i]);
			cols[i] = new Unit(UnitType.COLUMN);
			units.add(cols[i]);
			boxes[i] = new Unit(UnitType.BOX);
			units.add(boxes[i]);
		}
		
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				// Build cell
				Cell newCell = new Cell(this, grid.getCellValue(i, j));
				cells[i][j] = newCell;
				
				// Build units
				rows[i].addCell(newCell);
				cols[j].addCell(newCell);
				boxes[(i / 3) * 3 + (j / 3)].addCell(newCell);
			}
		}
		
		findAllCandidates();
	}
	
	
	protected void findAllCandidates() throws ZeroCandidateException {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				cells[i][j].findCandidates();
			}
		}
	}
	
	
	public Grid toGrid() {
		String gridString = "";
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (cells[i][j].isGiven()) {
					gridString += cells[i][j].getValue();
				} else {
					gridString += ".";
				}
			}
		}
		return new Grid(gridString);
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
