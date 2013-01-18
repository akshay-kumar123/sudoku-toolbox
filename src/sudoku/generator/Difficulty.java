package sudoku.generator;

public enum Difficulty {
	
	PIECE_OF_CAKE(0, new int[]{43, 48}, 5),
	EASY(1, new int[]{36, 42}, 4),
	MEDIUM(2, new int[]{32, 35}, 3),
	HARD(3, new int[]{28, 31}, 2),
	EVIL(4, new int[]{22, 27}, 0);

	
	private int index;
	private int givensRowColLowerBound;
	private int[] givensInterval;
	
	
	Difficulty(int index, int[] givensInterval, int givensRowColLowerBound) {
		this.index = index;
		this.givensInterval = givensInterval;
		this.givensRowColLowerBound = givensRowColLowerBound;
	}

	
	public int getIndex() {
		return index;
	}
	
	public int getGivensLowerBound() {
		return givensInterval[0];
	}

	public int getGivensUpperBound() {
		return givensInterval[1];
	}

	public int getGivensRowColLowerBound() {
		return givensRowColLowerBound;
	}
	
}
