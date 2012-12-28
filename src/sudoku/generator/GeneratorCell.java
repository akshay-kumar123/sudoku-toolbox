package sudoku.generator;

import java.util.ArrayList;

import sudoku.Cell;
import sudoku.Unit;
import sudoku.solver.exception.ZeroCandidateException;

public class GeneratorCell extends Cell {

	public GeneratorCell(int value) {
		super(value);
	}

	
	@Override
	protected void candidatesUpdated() throws ZeroCandidateException {
		if (candidates.size() == 0) {
			throw new ZeroCandidateException("Zero candidate left.");
		}
	}
	
	public ArrayList<Unit> getParentUnits() {
		return parentUnits;
	}

}
