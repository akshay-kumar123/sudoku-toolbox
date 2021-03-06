package sudoku;

import java.util.ArrayList;
import java.util.HashSet;

import sudoku.exception.UnitConstraintException;

public class Unit {
	
	private UnitType type;
	protected ArrayList<Cell> cells = new ArrayList<Cell>(9);

	public Unit(UnitType type) {
		this.type = type;
	}

	
	private void unitComplete() throws UnitConstraintException {
		HashSet<Integer> validationSet = new HashSet<Integer>(9);
		
		for (Cell c : cells) {
			// If validationSet already contains the value of cell c, the grid doesn't respect unit constraints
			if (c.isFilled() && !validationSet.add(c.getValue())) {
				throw new UnitConstraintException("Unit contains two cells with the same value.");
			}

			c.addPeersFromUnit(this);
		}
	}
	
	public void addCell(Cell c) throws UnitConstraintException {
		// Make sure no cell can be added once the unit is complete
		if (cells.size() < 9) {
			cells.add(c);
			if (cells.size() == 9) {
				unitComplete();
			}
		}
	}
	
	public ArrayList<Cell> getCells() {
		return cells;
	}
	
	public UnitType getType() {
		return type;
	}
	
}
