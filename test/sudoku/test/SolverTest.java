package sudoku.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sudoku.StaticGrid;
import sudoku.exception.InvalidGridException;
import sudoku.solver.Solver;
import sudoku.solver.SolverMode;
import sudoku.solver.SolverResult;

public class SolverTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testSolveEasyGrid() throws InvalidGridException {
		Solver s = new Solver(new StaticGrid(TestGrid.EASY.getGridString()));
		s.solve(SolverMode.DO_NOT_STOP);
		assertTrue(s.getResult() == SolverResult.ONE_SOLUTION);
	}

	@Test
	public void testSolveMediumGrid() throws InvalidGridException {
		Solver s = new Solver(new StaticGrid(TestGrid.MEDIUM.getGridString()));
		s.solve(SolverMode.DO_NOT_STOP);
		assertEquals("356719824248563197791284653635972481827146935419835276962457318583621749174398562", s.getSolutions().get(0).toString());
	}

	@Test
	public void testSolveDifficultGrid() throws InvalidGridException {
		Solver s = new Solver(new StaticGrid(TestGrid.DIFFICULT.getGridString()));
		s.solve(SolverMode.DO_NOT_STOP);
		assertTrue(s.getSolvingTime() < 30);
	}

	@Test
	public void testSolveEvilGrid() throws InvalidGridException {
		Solver s = new Solver(new StaticGrid(TestGrid.EVIL.getGridString()));
		s.solve(SolverMode.DO_NOT_STOP);
		assertTrue(s.getSolvingTime() < 50);
	}

	@Test
	public void testSolveMultipleSolutionsGrid() throws InvalidGridException {
		Solver s = new Solver(new StaticGrid(TestGrid.MULTIPLE_SOLUTIONS.getGridString()));
		s.solve(SolverMode.DO_NOT_STOP);
		assertEquals(5, s.getSolutionsCount());
	}

	@Test
	public void testSolveLotsOfSolutionsGrid1() throws InvalidGridException {
		Solver s = new Solver(new StaticGrid(TestGrid.LOTS_OF_SOLUTIONS.getGridString()));
		s.solve(SolverMode.STOP_FIRST_SOLUTION);
		assertEquals(SolverResult.AT_LEAST_ONE_SOLUTION, s.getResult());
	}

	@Test
	public void testSolveLotsOfSolutionsGrid2() throws InvalidGridException {
		Solver s = new Solver(new StaticGrid(TestGrid.LOTS_OF_SOLUTIONS.getGridString()));
		s.solve(SolverMode.STOP_SECOND_SOLUTION);
		assertEquals(SolverResult.MULTIPLE_SOLUTIONS, s.getResult());
	}

	@Test
	public void testSolveNoSolutionGrid() throws InvalidGridException {
		Solver s = new Solver(new StaticGrid(TestGrid.NO_SOLUTION.getGridString()));
		s.solve(SolverMode.DO_NOT_STOP);
		assertEquals(SolverResult.NO_SOLUTION, s.getResult());
	}

}
