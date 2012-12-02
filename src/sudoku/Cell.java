package sudoku;

import java.util.ArrayList;
import java.util.Arrays;

import sudoku.solver.SolverGrid;
import sudoku.solver.exception.CandidateNotFoundException;
import sudoku.solver.exception.ZeroCandidateException;

public class Cell implements Comparable<Cell> {
	
	public static final Integer[] CANDIDATES_ARR = new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	public static final ArrayList<Integer> CANDIDATES_SET = new ArrayList<Integer>(Arrays.asList(CANDIDATES_ARR));

	protected DynamicGrid parent;
	protected int value;
	protected ArrayList<Unit> parentUnits;
	protected ArrayList<Cell> peers;
	protected ArrayList<Integer> candidates;
	
	
	public Cell(DynamicGrid parent, int value) {
		this.parent = parent;
		this.value = value;
		parentUnits = new ArrayList<Unit>(3);
		peers = new ArrayList<Cell>(20);
		
		if (!isGiven()) {
			candidates = new ArrayList<Integer>(CANDIDATES_SET);
		}
	}
	
	
	public void addPeersFromUnit(Unit unit) {
		parentUnits.add(unit);
		ArrayList<Cell> cells = unit.getCells();
		for (Cell c : cells) {
			if (!c.equals(this) && !peers.contains(c)) {
				peers.add(c);
			}
		}
	}
	
	public void findCandidates() throws ZeroCandidateException {
		if (!isGiven()) {
			for (Cell c : peers) {
				if (c.isGiven()) {
					candidates.remove((Object)c.getValue());
				}
			}
			candidatesUpdated();
		}
	}
	
	public void chooseCandidate(int val) throws CandidateNotFoundException, ZeroCandidateException {
		value = val;

		if (candidates.contains(val)) {
			candidates = null;
		} else {
			throw new CandidateNotFoundException("Candidate not found.");
		}
		
		for (Cell c : peers) {
			if (!c.isGiven()) {
				c.removeCandidate(value);
			}
		}
	}
	
	public void removeCandidate(int candidate) throws ZeroCandidateException {
		if (candidates.remove((Object)candidate)) {
			candidatesUpdated();
		}
	}
	
	private void candidatesUpdated() throws ZeroCandidateException {
		if (candidates.size() == 1) {
			if (parent != null) {
				((SolverGrid) parent).cellHasSingleCandidate(this);
			}
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
	
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int newVal) {
		value = newVal;
	}
	
	public boolean isGiven() {
		return value != 0;
	}
	
	public ArrayList<Integer> getCandidates() {
		return candidates;
	}
	
	public ArrayList<Unit> getParentUnits() {
		return parentUnits;
	}
	
	
	public String toString() {
		return candidatesToString();
	}
	
	public String candidatesToString() {
		String s = "[";
		for (int c : candidates) {
			s += c;
		}
		s += "]";
		return s;
	}
	
	public String peersToString() {
		String s = "[ ";
		for (Cell c : peers) {
			if (c.isGiven()) {
				s += c.getValue() + " ";
			}
		}
		s += "]";
		return s;
	}

	
	@Override
	public int compareTo(Cell cell2) {
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

