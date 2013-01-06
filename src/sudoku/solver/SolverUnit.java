package sudoku.solver;

import java.util.ArrayList;
import java.util.Arrays;

import sudoku.Cell;
import sudoku.Unit;
import sudoku.exception.CandidateNotFoundException;
import sudoku.exception.ZeroCandidateException;

public class SolverUnit extends Unit {
	
	private ArrayList<Cell> cells = new ArrayList<Cell>(9);

	
	public SolverUnit() {
		super(null);
	}

	
	public boolean findUnitLoner() throws CandidateNotFoundException, ZeroCandidateException {
		boolean lonersCount = false;
		
		int[] candidateCounters = new int[9];
		Cell[] candidateOwners = new Cell[9];
		Arrays.fill(candidateCounters, 0);
		
		for (Cell c : cells) {
			if (!c.isFilled()) {
				for (int candidate : c.getCandidates()) {
					candidateCounters[candidate - 1]++;
					candidateOwners[candidate - 1] = c;
				}
			}
		}
		
		for (int i = 0; i < 9; i++) {
			if (candidateCounters[i] == 1) {
				lonersCount = true;
				candidateOwners[i].chooseCandidate(i + 1);
			}
		}
		
		return lonersCount;
	}

}
