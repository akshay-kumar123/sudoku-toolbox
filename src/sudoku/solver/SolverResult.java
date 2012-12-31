package sudoku.solver;

public enum SolverResult {
	
	// Possible in all solver modes
	NO_SOLUTION,
	
	// Possible in modes DO_NOT_STOP and STOP_SECOND_SOLUTION
	ONE_SOLUTION,
	MULTIPLE_SOLUTIONS,
	
	// Possible in mode STOP_FIRST_SOLUTION
	AT_LEAST_ONE_SOLUTION,
		
}
