package sudoku.solver.exception;

public class CandidateNotFoundException extends Exception {

	private static final long serialVersionUID = 1115575107288080325L;

	public CandidateNotFoundException(String message) {
		super(message);
	}

}
