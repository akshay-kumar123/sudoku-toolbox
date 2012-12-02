package sudoku;

import java.util.ArrayList;
import java.util.HashSet;

import sudoku.solver.exception.UnitConstraintException;

public class Unit {
	
	private UnitType type;
	private ArrayList<Cell> cells = new ArrayList<Cell>(9);

	public Unit(UnitType type) {
		this.type = type;
	}

	
	protected void unitComplete() throws UnitConstraintException {
		HashSet<Integer> testSet = new HashSet<Integer>(9);
		
		for (Cell c : cells) {
			// If testSet already contains the value of cell c, the grid doesn't respect unit constraints
			if (c.isGiven() && !testSet.add(c.getValue())) {
				throw new UnitConstraintException("Unit contains two cells with the same value.");
			}

			c.addPeersFromUnit(this);
		}
	}
	
	public void addCell(Cell c) throws UnitConstraintException {
		cells.add(c);
		if (cells.size() == 9) {
			unitComplete();
		}
	}
	
	public ArrayList<Cell> getCells() {
		return cells;
	}
	
	public UnitType getType() {
		return type;
	}
	
}
