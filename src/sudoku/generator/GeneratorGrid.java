package sudoku.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Stack;

import sudoku.Cell;
import sudoku.DynamicGrid;
import sudoku.Grid;
import sudoku.Unit;
import sudoku.UnitType;
import sudoku.solver.Solver;
import sudoku.solver.SolverMode;
import sudoku.solver.exception.CandidateNotFoundException;
import sudoku.solver.exception.UnitConstraintException;
import sudoku.solver.exception.ZeroCandidateException;

public class GeneratorGrid extends DynamicGrid {

	private final static int GIVENS = 3;
	
	private Difficulty difficulty;
	
	
	public GeneratorGrid() {}

	public GeneratorGrid(Grid grid) throws UnitConstraintException, ZeroCandidateException {
		super(grid);
	}
	
	public void preFill() throws CandidateNotFoundException, ZeroCandidateException {
		for (int i = 0; i < GIVENS; i++) {
			Cell c;
			do {
				int x = (int) (Math.random() * 9);
				int y = (int) (Math.random() * 9);
				c = cells[x][y];
			} while (c.isGiven());
			
			ArrayList<Integer> candidates = c.getCandidates();
			int index = (int) (Math.random() * candidates.size());
			c.chooseCandidate((Integer)candidates.toArray()[index]);
		}
	}
	
	public Grid solveTerminalPattern() {
		Solver solver = new Solver(toGrid());
		solver.solve(SolverMode.STOP_FIRST_SOLUTION);
		
		ArrayList<Grid> solutions = solver.getSolutions();
		if (solutions.size() > 0) {
			return solutions.get(0);
		} else {
			return null;
		}
	}
	
	public void digHoles(Difficulty difficulty) {
		this.difficulty = difficulty;
		
		// Associate each unit with a counter of the remaining number of givens it contains
		HashMap<Unit, Integer> givensPerUnit = new HashMap<Unit, Integer>(18);
		for (Unit u : units) {
			if (u.getType() != UnitType.BOX) {
				givensPerUnit.put(u, 9);
			}
		}
		
		// Pick the target amount of givens in the grid
		int givensTarget = (int) (Math.random() * (difficulty.getGivensUpperBound() - difficulty.getGivensLowerBound() + 1) + difficulty.getGivensLowerBound());
		System.out.println("Target givens count: " + givensTarget);
		
		// Store all cells in a stack and shuffle
		Stack<Cell> cellsToDig = new Stack<Cell>();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				Cell c = cells[i][j];
				cellsToDig.push(c);
			}
		}
		Collections.shuffle(cellsToDig);
		
		// Pop cells from stack and try to dig them
		while(cellsToDig.size() > givensTarget) {
			Cell digCell = cellsToDig.pop();
			if (isDiggableCell(digCell, givensPerUnit)) {
				digCell.setValue(0);
			}
		}
	}
	
	private boolean isDiggableCell(Cell cell, HashMap<Unit, Integer> givensPerUnit) {
		for (Unit u : cell.getParentUnits()) {
			if (u.getType() != UnitType.BOX && givensPerUnit.get(u) <= difficulty.getGivensRowColLowerBound()) {
				return false;
			}
		}
		return true;
	}

}


