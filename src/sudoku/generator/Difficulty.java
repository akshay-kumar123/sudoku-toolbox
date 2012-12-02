package sudoku.generator;

public enum Difficulty {
	
	EXTREMELY_EASY(50, 60, 5),
	EASY(36, 49, 4),
	MEDIUM(32, 35, 3),
	DIFFICULT(28, 31, 2),
	EVIL(22, 27, 0);

	private int givensLowerBound, givensUpperBound, givensRowColLowerBound;
	
	
	Difficulty(int givensLowerBound, int givensUpperBound, int givensRowColLowerBound) {
		this.givensLowerBound = givensLowerBound;
		this.givensUpperBound = givensUpperBound;
		this.givensRowColLowerBound = givensRowColLowerBound;
	}

	
	public int getGivensLowerBound() {
		return givensLowerBound;
	}

	public int getGivensUpperBound() {
		return givensUpperBound;
	}

	public int getGivensRowColLowerBound() {
		return givensRowColLowerBound;
	}
	
}
