package sudoku.solver;

import java.util.Arrays;

import sudoku.Cell;
import sudoku.Unit;
import sudoku.exception.CandidateNotFoundException;
import sudoku.exception.ZeroCandidateException;

public class SolverUnit extends Unit {
	
	public SolverUnit() {
		super(null);
	}

	
	public boolean hiddenSingles() throws CandidateNotFoundException, ZeroCandidateException {
		boolean lonersCount = false;
		
		int[] candidateCounters = new int[9];
		SolverCell[] candidateOwners = new SolverCell[9];
		Arrays.fill(candidateCounters, 0);
		
		for (Cell c : cells) {
			if (!c.isFilled()) {
				for (Integer candidate : c.getCandidates()) {
					candidateCounters[candidate - 1]++;
					candidateOwners[candidate - 1] = (SolverCell) c;
				}
			}
		}
		
		for (int i = 0; i < 9; i++) {
			if (candidateCounters[i] == 1) {
				lonersCount = true;
				// Do not choose hidden single if cell is already filled, or if it is the only candidate in the cell
				// If it is the only candidate in the cell, then it has already been marked as a naked single and will be processed later 
				if (!candidateOwners[i].isFilled() && !candidateOwners[i].hasSingleCandidate()) {
					candidateOwners[i].chooseCandidate(i + 1);
				}
			}
		}
		
		return lonersCount;
	}

}
