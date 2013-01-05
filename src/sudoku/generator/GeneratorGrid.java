package sudoku.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Stack;

import sudoku.Cell;
import sudoku.DynamicGrid;
import sudoku.StaticGrid;
import sudoku.Unit;
import sudoku.UnitType;
import sudoku.exception.UnitConstraintException;
import sudoku.exception.ZeroCandidateException;

public class GeneratorGrid extends DynamicGrid {

	public final static int PRE_FILL_GIVENS = 3;

	private Difficulty difficulty;
	

	/*
	 * A GeneratorGrid can perform two tasks:
	 * 	- pre-filling an empty grid that can then be used by the Solver to generate a terminal pattern, and
	 * 	- generating a valid Sudoku from a terminal pattern to a specific level of difficulty.
	 * 
	 * The first task is optional as the Solver can easily generate a random terminal pattern from an empty grid by itself.
	 * If pre-filling is performed, then the second task should be performed on a new, separate GeneratorGrid instance.
	 */
	public GeneratorGrid(StaticGrid grid) throws UnitConstraintException, ZeroCandidateException {
		cells = new GeneratorCell[9][9];
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
				GeneratorCell newCell = new GeneratorCell(grid.getCellValue(i, j));
				cells[i][j] = newCell;
				
				// Build units
				rows[i].addCell(newCell);
				cols[j].addCell(newCell);
				boxes[(i / 3) * 3 + (j / 3)].addCell(newCell);
			}
		}
		
		findAllCandidates();
	}
	
	public void preFill() throws ZeroCandidateException {
		for (int i = 0; i < PRE_FILL_GIVENS; i++) {
			Cell c;
			do {
				int x = (int) (Math.random() * 9);
				int y = (int) (Math.random() * 9);
				c = cells[x][y];
			} while (c.isGiven());

			((GeneratorCell) c).chooseRandomCandidate();
		}
	}
	
	public void digHoles(Difficulty difficulty) {
		this.difficulty = difficulty;
		
		// Associate each unit with a counter of the remaining number of givens it contains
		HashMap<Unit, Integer> givensPerUnit = new HashMap<Unit, Integer>(18);
		for (Unit u : units) {
			if (u.getType() != UnitType.BOX) {
				givensPerUnit.put(u, 9);
			}
		}
		
		// Pick the target amount of givens in the grid
		int givensTarget = (int) (Math.random() * (difficulty.getGivensUpperBound() - difficulty.getGivensLowerBound() + 1) + difficulty.getGivensLowerBound());
		System.out.println("Target givens count: " + givensTarget);
		
		// Store all cells in a stack and shuffle
		Stack<Cell> cellsToDig = new Stack<Cell>();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				Cell c = cells[i][j];
				cellsToDig.push(c);
			}
		}
		Collections.shuffle(cellsToDig);
		
		// Pop cells from stack and try to dig them
		while(cellsToDig.size() > givensTarget) {
			GeneratorCell digCell = (GeneratorCell)cellsToDig.pop();
			if (isDiggableCell(digCell, givensPerUnit)) {
				digCell.dig();
			}
		}
	}
	
	private boolean isDiggableCell(Cell cell, HashMap<Unit, Integer> givensPerUnit) {
		for (Unit u : ((GeneratorCell) cell).getParentUnits()) {
			if (u.getType() != UnitType.BOX && givensPerUnit.get(u) <= difficulty.getGivensRowColLowerBound()) {
				return false;
			}
		}
		return true;
	}

}


