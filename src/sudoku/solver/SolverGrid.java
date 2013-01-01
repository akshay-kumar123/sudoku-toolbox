package sudoku.solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Stack;

import sudoku.Cell;
import sudoku.DynamicGrid;
import sudoku.StaticGrid;
import sudoku.Unit;
import sudoku.solver.exception.CandidateNotFoundException;
import sudoku.solver.exception.UnitConstraintException;
import sudoku.solver.exception.ZeroCandidateException;

public class SolverGrid extends DynamicGrid {
	
	private static final boolean STOP_DPS = false, CONTINUE_DPS = true;

	private Solver solver;
	private Stack<SolverCell> singleCandidateCells = new Stack<SolverCell>();
	
	
	public SolverGrid(Solver solver, StaticGrid grid) throws UnitConstraintException, ZeroCandidateException {
		cells = new SolverCell[9][9];
		units = new ArrayList<Unit>(27);
		
		this.solver = solver;
		
		// Initialize units
		SolverUnit[] rows = new SolverUnit[9];
		SolverUnit[] cols = new SolverUnit[9];
		SolverUnit[] boxes = new SolverUnit[9];
		
		for (int i = 0; i < 9; i++) {
			rows[i] = new SolverUnit();
			units.add(rows[i]);
			cols[i] = new SolverUnit();
			units.add(cols[i]);
			boxes[i] = new SolverUnit();
			units.add(boxes[i]);
		}
		
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				// Build cell
				SolverCell newCell = new SolverCell(this, grid.getCellValue(i, j));
				cells[i][j] = newCell;
				
				// Build units
				rows[i].addCell(newCell);
				cols[j].addCell(newCell);
				boxes[(i / 3) * 3 + (j / 3)].addCell(newCell);
			}
		}
		
		findAllCandidates();
	}
	
	
	public void cellHasSingleCandidate(SolverCell c) {
		if (!singleCandidateCells.contains(c)) {
			singleCandidateCells.push(c);
		}
	}
	
	public void fillInSingleCandidateCells() throws CandidateNotFoundException, ZeroCandidateException {
		//System.out.println("Filling in single-candidate cells...");
		//printParsedGrid();
		while (!singleCandidateCells.empty()) {
			SolverCell c = singleCandidateCells.pop();
			c.chooseCandidate(c.getCandidates().iterator().next());
		}
	}
	
	
	public boolean propagateConstraints() throws CandidateNotFoundException, ZeroCandidateException {
		fillInSingleCandidateCells();
		
		boolean gridChanged;
		do {
			if (isSolved()) {
				solver.addSolution(new StaticGrid(this));
				// Grid is solved
				return true;
			}

			gridChanged = false;
			
			//System.out.println("Looking for loners...");
			for (Unit u : units) {
				if (((SolverUnit) u).findUnitLoner()) {
					//System.out.println("Loner chosen");
					//printParsedGrid();
					fillInSingleCandidateCells();
					gridChanged = true;
				}
			}
		} while (gridChanged);

		// Grid is not solved yet
		return false;
	}
	
	public boolean depthFirstSearch(SolverMode goal) {
		// Sort unfilled cells by number of candidates and create a map of the cells and their candidates for backtracking
		PriorityQueue<Cell> cellsForDPS = new PriorityQueue<Cell>();
		HashMap<Cell, Integer[]> backtrackMap = new HashMap<Cell, Integer[]>();
		prepareForDPS(cellsForDPS, backtrackMap);

		if (cellsForDPS.size() > 0) {
			// Retrieve cell with lowest number of candidates, as well as its candidates
			Cell dpsCell = cellsForDPS.poll();
			ArrayList<Integer> dpsCandidates = dpsCell.getCandidates();
			
			if (goal == SolverMode.STOP_FIRST_SOLUTION) {
				// Shuffle the candidates to increase randomness
				Collections.shuffle(dpsCandidates);
			}
			
			// Choose each candidate
			for (Integer i : dpsCandidates) {
				try {
					dpsCell.chooseCandidate(i);
					// Fill in any single-candidate cell before pursuing
					fillInSingleCandidateCells();
					if (depthFirstSearch(goal) == STOP_DPS) {
						return STOP_DPS;
					}
				} catch (CandidateNotFoundException | ZeroCandidateException e) {}

				backtrack(backtrackMap);
				singleCandidateCells = new Stack<SolverCell>();
			}
		} else {
			solver.addSolution(new StaticGrid(this));
			int solutionsCount = solver.getSolutionsCount();
			if (solutionsCount == 1 && goal == SolverMode.STOP_FIRST_SOLUTION || solutionsCount == 2 && goal == SolverMode.STOP_SECOND_SOLUTION) {
				return STOP_DPS;
			} else {
				backtrack(backtrackMap);
			}
		}
		
		return CONTINUE_DPS;
	}
	
	private void prepareForDPS(PriorityQueue<Cell> cellsForDPS, HashMap<Cell, Integer[]> backtrackMap) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (!cells[i][j].isGiven()) {
					Cell c = cells[i][j];
					cellsForDPS.add(c);
					backtrackMap.put(c, c.getCandidates().toArray(new Integer[0]));
				}
			}
		}
	}
	
	private void backtrack(HashMap<Cell, Integer[]> backtrackMap) {
		//System.out.println("Backtracking...");
		for (Cell c : backtrackMap.keySet()) {
			((SolverCell) c).resetCell((backtrackMap.get(c)));
		}
	}
	
	
	public boolean isSolved() {
		boolean solved = true;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (!cells[i][j].isGiven()) {
					solved = false;
				}
			}
		}
		return solved;
	}
	
}
