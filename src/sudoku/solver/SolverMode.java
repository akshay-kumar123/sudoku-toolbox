package sudoku.solver;

public enum SolverMode {
	
	// Find all solutions
	DO_NOT_STOP,
	
	// Generate terminal pattern
	STOP_FIRST_SOLUTION,
	
	// Prove or disprove that a grid has a unique solution
	STOP_SECOND_SOLUTION
	
}
