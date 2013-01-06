package sudoku.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import sudoku.StaticGrid;
import sudoku.generator.Generator;

public class GeneratorTest {

	private Generator gen;

	@Before
	public void setUp() {
		gen = Generator.getInstance();
	}
	
	
	@Test
	public void testGenerateDistinctTerminalPatterns() {
		HashSet<String> set = new HashSet<String>(10);
		for (int i = 0; i < 10; i++) {
			if (!set.add(gen.generateTerminalPattern(false).toString())) {
				assertFalse(true);
				return;
			}
		}
		assertTrue(true);
	}
	
	@Test
	public void testGenerateRandomTerminalPatterns() {
		double tries = 100, targetProb = 1.0 / 9.0, maxDeviation = 0;
		double[] counters = new double[9];
		
		for (int i = 0; i < tries; i++) {
			StaticGrid pattern = gen.generateTerminalPattern(false);
			counters[pattern.getCellValue(8, 8) - 1] += 1;
		}
		
		for (int j = 0; j < 9; j++) {
			double dev = Math.abs(targetProb - counters[j] / tries);
			if (maxDeviation < dev) {
				maxDeviation = dev;
			}
		}
		
		assertTrue(maxDeviation <= targetProb);
	}

}
