package sudoku.generator;

import java.util.ArrayList;

import sudoku.Cell;
import sudoku.Unit;
import sudoku.exception.CandidateNotFoundException;
import sudoku.exception.ZeroCandidateException;

public class GeneratorCell extends Cell {
	
	private int oldValue = 0;
	

	public GeneratorCell(int value) {
		super(value);
	}
	
	
	public void chooseRandomCandidate() throws ZeroCandidateException {
		try {
			chooseCandidate(candidates.get((int) (Math.random() * candidates.size())));
		} catch (CandidateNotFoundException e) {
			// This should never happen
			System.out.println("Program error: picking a random candidate that does not exist.");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	@Override
	protected void candidatesUpdated() throws ZeroCandidateException {
		if (candidates.size() == 0) {
			throw new ZeroCandidateException("Zero candidate left.");
		}
	}

	
	public void dig() {
		oldValue = value;
		value = 0;
	}
	
	public void reset() {
		value = oldValue;
	}
	
	
	public ArrayList<Unit> getParentUnits() {
		return parentUnits;
	}

}
