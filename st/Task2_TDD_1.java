package st;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import st.EntryMap;
import st.TemplateEngine;
import st.SimpleTemplateEngine;

public class Task2_TDD_1 {
	private EntryMap map;
	private TemplateEngine engine;
	// private SimpleTemplateEngine simpleEngine;

	@Before
	public void setUp() throws Exception {
		map = new EntryMap();
		engine = new TemplateEngine();
		// simpleEngine = new SimpleTemplateEngine();
	}
	
	@Test
	// Test X = 0
	public void testZero1(){
		map.store("year", "0 years ago");
		String template = "The year is ${year}.";
		String res = engine.evaluate(template, map, 0);
		assertEquals("The year is 2018.", res);
	}
	
	@Test
	// Test X = 0
	public void testZero2(){
		map.store("year", "in 0 years");
		String template = "The year is still ${year}.";
		String res = engine.evaluate(template, map, 0);
		assertEquals("The year is still 2018.", res);
	}
	
	@Test
	// Test X < 0
	public void testNegative(){
		map.store("year", "-10 years ago");
		String template = "The year is still ${year}!";
		String res = engine.evaluate(template, map, 0);
		assertEquals("The year is still -10 years ago!", res);
	}
	
	@Test
	// Test X years ago
	public void testBefore(){
		map.store("year", "5 years ago");
		String template = "I was still an awkward 15 year old back in ${year}.";
		String res = engine.evaluate(template, map, 0);
		assertEquals("I was still an awkward 15 year old back in 2013.", res);
	}
	
	@Test
	// Test in X years
	public void testAfter(){
		map.store("year", "in 8 years");
		String template = "The average woman in the US gets married at age 28. That means I have until ${year} to find my soulmate. No pressure!";
		String res = engine.evaluate(template, map, 0);
		assertEquals("The average woman in the US gets married at age 28. That means I have until 2026 to find my soulmate. No pressure!", res);
	}
	
	@Test
	// Test base year, X years ago
	public void testBaseYear1(){
		map.store("base_year", "2020");
		map.store("year", "12 years ago");
		String template = "Steve Jobs introduced the iPhone in ${year}.";
		String res = engine.evaluate(template, map, 0);
		assertEquals("Steve Jobs introduced the iPhone in 2008.", res);
	}
	
	@Test
	// Test base year, in X years
	public void testBaseYear2(){
		map.store("base_year", "1998");
		map.store("year", "in 10 years");
		String template = "The US elected its first African-American president in ${year}.";
		String res = engine.evaluate(template, map, 0);
		assertEquals("The US elected its first African-American president in 2008.", res);
	}
	
	@Test
	// Test base year, zero case
	public void testBaseYear3(){
		map.store("base_year", "2000");
		map.store("year", "in 0 years");
		String template = "The turn of the century happened in ${year}.";
		String res = engine.evaluate(template, map, 0);
		assertEquals("The turn of the century happened in 2000.", res);
	}
	
	@Test
	// Test default matching mode
	public void testMatchingMode(){
		map.store("year", "3 years ago");
		map.store("verb", "graduated");
		map.store("location", "Edinburgh");
		String template = "I ${verb} from high school in ${loc ation} in ${YEAR} ${unmatched}.";
		String res = engine.evaluate(template, map, 0);
		assertEquals("I graduated from high school in ${loc ation} in 2015 ${unmatched}.", res);
	}
	
	@Test
	// Test case sensitive, blur search matching mode
	public void testMatchingMode2(){
		map.store("year", "3 years ago");
		map.store("highschool", "North Shore");
		String template = "I ${unmatched_entry} graduated from ${high  school} high school in year ${YEAR}.";
		Integer matchingMode = TemplateEngine.CASE_SENSITIVE | TemplateEngine.BLUR_SEARCH;
		String res = engine.evaluate(template, map, matchingMode);
		assertEquals("I ${unmatched_entry} graduated from North Shore high school in year ${YEAR}.", res);
	}
	
	@Test
	// Test blur search matching mode
	public void testMatchingMode2b(){
		map.store("year", "3 years ago");
		String template = "I graduated high school in ${YeaR }.";
		String res = engine.evaluate(template, map, TemplateEngine.BLUR_SEARCH);
		assertEquals("I graduated high school in 2015.", res);
	}
	
	@Test
	// Test contradictory matching modes
	public void testMatchingMode3(){
		map.store("year", "3 years ago");
		map.store("verb", "graduated");
		String template = "I ${unmatched}${VERB} high school in ${YeaR } .";
		Integer matchingMode = TemplateEngine.DELETE_UNMATCHED | TemplateEngine.KEEP_UNMATCHED | TemplateEngine.BLUR_SEARCH;
		String res = engine.evaluate(template, map, matchingMode);
		assertEquals("I graduated high school in 2015 .", res);
	}
	
	@Test
	// Test delete unmatched, keep unmatched, case sensitive matching modes
	public void testMatchingMode4(){
		map.store("year", "3 years ago");
		map.store("verb", "graduated");
		String template = "I ${VERB}high school in ${YeaR }${unmatched}.";
		Integer matchingMode = TemplateEngine.DELETE_UNMATCHED | TemplateEngine.KEEP_UNMATCHED | TemplateEngine.CASE_SENSITIVE;
		String res = engine.evaluate(template, map, matchingMode);
		assertEquals("I high school in .", res);
	}
}
