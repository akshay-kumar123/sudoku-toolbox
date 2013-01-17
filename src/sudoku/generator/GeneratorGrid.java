package sudoku.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import sudoku.Cell;
import sudoku.DynamicGrid;
import sudoku.StaticGrid;
import sudoku.Unit;
import sudoku.UnitType;
import sudoku.exception.InvalidGridException;
import sudoku.exception.UnitConstraintException;
import sudoku.exception.ZeroCandidateException;
import sudoku.solver.Solver;
import sudoku.solver.SolverMode;
import sudoku.solver.SolverResult;

public class GeneratorGrid extends DynamicGrid {

	public final static int PRE_FILL_GIVENS = 3;

	//private Difficulty difficulty;
	private int givensTarget;
	//private HashMap<Unit, Integer> givensPerUnit;
	

	/*
	 * A GeneratorGrid can perform two tasks:
	 * 	- pre-filling an empty grid that can then be used by the Solver to generate a terminal pattern, and
	 * 	- pruning a terminal pattern to generate a Sudoku with a specific level of difficulty.
	 * 
	 * The first task is optional as the Solver can easily generate a random terminal pattern from an empty grid by itself.
	 * If pre-filling is performed, though, then the pruning should be performed on a new instance of GeneratorGrid.
	 */
	public GeneratorGrid(StaticGrid grid) throws UnitConstraintException, ZeroCandidateException {
		cells = new GeneratorCell[9][9];
		units = new ArrayList<Unit>(27);
		
		// Initialise units
		Unit[] rows = new Unit[9];
		Unit[] cols = new Unit[9];
		Unit[] boxes = new Unit[9];
		
		for (int i = 0; i < 9; i++) {
			rows[i] = new Unit(UnitType.ROW);
			units.add(rows[i]);
			cols[i] = new Unit(UnitType.COLUMN);
			units.add(cols[i]);
			boxes[i] = new Unit(UnitType.BOX);
			units.add(boxes[i]);
		}
		
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				// Build cell
				GeneratorCell newCell = new GeneratorCell(grid.getCellValue(i, j));
				cells[i][j] = newCell;
				
				// Build units
				rows[i].addCell(newCell);
				cols[j].addCell(newCell);
				boxes[(i / 3) * 3 + (j / 3)].addCell(newCell);
			}
		}
		
		findAllCandidates();
	}
	
	public void preFill() throws ZeroCandidateException, GeneratorGridException {
		if (getGivensCount() > 0) {
			throw new GeneratorGridException("Pre-filling can only be performed on an empty grid.");
		}
		
		for (int i = 0; i < PRE_FILL_GIVENS; i++) {
			Cell c;
			do {
				int x = (int) (Math.random() * 9);
				int y = (int) (Math.random() * 9);
				c = cells[x][y];
			} while (c.isFilled());

			((GeneratorCell) c).chooseRandomCandidate();
		}
	}
	
	public StaticGrid prune(Difficulty difficulty) throws GeneratorGridException {
		if (getGivensCount() < 81) {
			throw new GeneratorGridException("Pruning can only be performed on a terminal pattern.");
		}
		
		//this.difficulty = difficulty;

		// Pick the target amount of givens in the grid
		givensTarget = (int) (Math.random() * (difficulty.getGivensUpperBound() - difficulty.getGivensLowerBound() + 1) + difficulty.getGivensLowerBound());
		System.out.println("Target number of givens: " + givensTarget);
		
		return pruneHelper(81);
	}
	
	int minAchieved = 81;
	
	private StaticGrid pruneHelper(int remainingGivens) {
		// Return grid if target number of givens is reached (grid will propagate and end the recursion)
		if (remainingGivens == givensTarget) {
			return new StaticGrid(this);
		}
		
		// If target number of givens hasn't been reached, try digging each of the remaining cells
		// Store remaining given cells in a stack and shuffle to ensure randomness
		Stack<Cell> cellsToDig = new Stack<Cell>();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				Cell c = cells[i][j];
				if (c.isFilled()) {
					cellsToDig.push(c);
				}
			}
		}
		Collections.shuffle(cellsToDig);
		
		// Pop cells from stack and try to dig them
		while(!cellsToDig.isEmpty()) {
			GeneratorCell cell = (GeneratorCell) cellsToDig.pop();
			
			// Dig cell
			cell.dig();
			
			// Solve resulting grid and test for unique solution
			Solver s = new Solver(new StaticGrid(this));
			try {
				s.solve(SolverMode.STOP_SECOND_SOLUTION);
			} catch (InvalidGridException e) {
				// This should never happen
				System.out.println("Program error: pruning cells from a terminal pattern should not lead to an invalid grid.");
				System.exit(1);
			}

			// If unique solution, recurse; otherwise, undig cell and continue loop
			if (s.getResult() == SolverResult.ONE_SOLUTION) {
				// Recurse on the dug grid
				StaticGrid validSudoku = pruneHelper(remainingGivens - 1);
				
				// If a grid was returned by the recursive call, it means a valid solution was found
				// Propagate the solution to parent recursive calls to end the recursion
				if (validSudoku != null) {
					return validSudoku;
				}
			}
			
			// Digging the cell hasn't returned a valid Sudoku grid; backtrack by undigging the cell
			cell.reset();
		}

		// Cannot remove any more givens: none of the remaining given cells leads to a valid Sudoku
		// Return current grid, which is a valid solution (grid will be propagated by parent recursive calls until recursion ends)
		return new StaticGrid(this);
	}
	
	
	/*
	 * Used to validate the grid
	 */
	public int getGivensCount() {
		int count = 0;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (cells[i][j].isFilled()) {
					count++;
				}
			}
		}
		return count;
	}

}


