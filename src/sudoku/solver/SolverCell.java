package sudoku.solver;

import java.util.ArrayList;

import sudoku.Cell;
import sudoku.solver.exception.ZeroCandidateException;

public class SolverCell extends Cell implements Comparable<SolverCell> {

	private SolverGrid parent;
	
	
	public SolverCell(SolverGrid parent) {
		super();
		this.parent = parent;
	}
	
	public SolverCell(SolverGrid parent, int value) {
		super(value);
		this.parent = parent;
	}
	

	@Override
	protected void candidatesUpdated() throws ZeroCandidateException {
		if (candidates.size() == 1) {
			parent.cellHasSingleCandidate(this);
		} else if (candidates.size() == 0) {
			throw new ZeroCandidateException("Zero candidate left.");
		}
	}
	
	public void resetCell(Integer[] integers) {
		value = 0;
		
		this.candidates = new ArrayList<Integer>(integers.length);
		for (int i = 0; i < integers.length; i++) {
			this.candidates.add(integers[i]);
		}
	}

	
	@Override
	public int compareTo(SolverCell cell2) {
		// Compare by number of candidates
		int c1Count = this.candidates != null ? this.candidates.size() : 0;
		int c2Count = cell2.candidates != null ? cell2.candidates.size() : 0;

		if (c1Count < c2Count) {
			return -1;
		} else if (c1Count > c2Count) {
			return 1;
		} else {
			return 0;
		}
	}

}
