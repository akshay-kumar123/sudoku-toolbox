package sudoku;

import java.util.ArrayList;
import java.util.Arrays;

import sudoku.solver.exception.CandidateNotFoundException;
import sudoku.solver.exception.ZeroCandidateException;

public abstract class Cell {
	
	public static final Integer[] CANDIDATES_ARR = new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	public static final ArrayList<Integer> CANDIDATES_SET = new ArrayList<Integer>(Arrays.asList(CANDIDATES_ARR));

	protected int value;
	protected ArrayList<Unit> parentUnits;
	protected ArrayList<Cell> peers;
	protected ArrayList<Integer> candidates;
	
	
	public Cell(int value) {
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
	
	protected abstract void candidatesUpdated() throws ZeroCandidateException;
	
	
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
	
}

