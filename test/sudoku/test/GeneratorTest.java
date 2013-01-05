package sudoku.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import sudoku.generator.Generator;

public class GeneratorTest {

	private Generator gen;

	@Before
	public void setUp() {
		gen = Generator.getInstance();
	}
	
	
	@Test
	public void testGenerateTerminalPatternRandomness() {
		HashSet<String> set = new HashSet<String>(10);
		for (int i = 0; i < 10; i++) {
			if (!set.add(gen.generateTerminalPattern(false).toString())) {
				assertFalse(true);
				return;
			}
		}
		assertTrue(true);
	}

}
