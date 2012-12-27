package sudoku.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import sudoku.test.core.GridTest;

@RunWith(Suite.class)
@SuiteClasses({ GridTest.class })
public class CoreTest {

}
