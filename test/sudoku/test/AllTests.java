package sudoku.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ GridTest.class, SolverCellTest.class, SolverGridTest.class, SolverTest.class, GeneratorGridTest.class, GeneratorTest.class})
public class AllTests {

}
