package st;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Task2_Mutation {
	private EntryMap map;
	private TemplateEngine engine;
	private SimpleTemplateEngine simpleEngine;

	@Before
	public void setUp() throws Exception {
		map = new EntryMap();
		engine = new TemplateEngine();
		simpleEngine = new SimpleTemplateEngine();
	}

	@Test
	// mutation 1, 5
	// testing order with BLUR_SEARCH
	public void testBlurSearch() {
		map.store("entry 1", "A");
		map.store("entry2", "B");
		map.store("entry 2", "C");
		map.store("entry3", "D");
		map.store("EnTry2", "E");
		map.delete("entry2");
		String template = "Letters: ${entry1} ${entry2} ${entry3}";
		Integer matchingMode = TemplateEngine.BLUR_SEARCH;
		String result = engine.evaluate(template, map, matchingMode);
		assertEquals("Letters: A C D", result);
	}
	
	@Test 
	// mutation 2
	// no duplicates
	public void TestDuplicates() {
	  	map.store("name", "Alexis");
	  	map.delete("name");
	  	map.store("name", "Alexis");
		String templateString = "${name}";
		assertEquals("Alexis", engine.evaluate(templateString, map, TemplateEngine.DEFAULT));
	}
	
	@Test
	// mutation 3
	// contradictory matching modes
	public void testContradictoryDeleteUnmatched() {
		map.store("firstname", "Thomas");
		map.store("middlename", "Woodrow");
		map.store("surname", "Wilson");
		// DELETE_UNMATCHED, CASE_INSENSITIVE, and BLUR_SEARCH used
		String template = "${FirstName} ${middle_name} ${surname} was a US president.";
		Integer matchingMode = engine.DELETE_UNMATCHED | engine.KEEP_UNMATCHED | engine.BLUR_SEARCH;
		String result = engine.evaluate(template, map, matchingMode);
		assertEquals("Thomas  Wilson was a US president.", result);
	}
	
	@Test
	// mutation 4
	// testing template string boundaries
	public void testTemplateStringBoundary() {
		map.store("name", "Liza");
		map.store("we should try our best for winning the ${competition} cup.", "test");
		map.store("competition", "soccer");
		String template = "I heard that ${name} said: ${we should try our best for winning the ${competition} cup.}";
		Integer matchingMode = engine.DEFAULT;
		String expectedOutput = "I heard that Liza said: ${we should try our best for winning the soccer cup.}";
		String result = engine.evaluate(template, map, matchingMode);
		assertEquals(expectedOutput, result);
	}

	@Test
	// mutation 7
	// test that only existing value pairs can be deleted
	public void testDeleteExisting(){
		map.store("name", "Molly");
		map.delete("my name");		// nothing with pattern "my name" exists in the map
		String res = engine.evaluate("My name is ${my name}.", map, 0);
		assertEquals("My name is ${my name}.", res);
	}
	
	@Test
	// mutation 8
	// test STE case sensitive mode
	public void testCaseSensitive(){
		String template = "My full name is Molly molly Chen.";
		String res = simpleEngine.evaluate(template, "molly", "Moli", SimpleTemplateEngine.CASE_SENSITIVE);
		assertEquals("My full name is Molly Moli Chen.", res);
	}
	
	@Test
	// mutation 9
	public void m9(){
		map.store("first", "bob");
		map.store("last", "  boberson  ");
		String template = "${first} {$last}";
		String res = engine.evaluate(template, map, 0);
		assertEquals("bob    boberson   ", res);
	}
	
	@Test
	// mutation 10
	// prevent recursive replacement
	public void testEvaluate6(){
		String template = "defabc";
		String res = simpleEngine.evaluate(template, "abc", "abcabc", 0);
		assertEquals("defabcabc", res);
	}
	
	/*@Test
	// NOT WORKING
	// testing order with ACCURATE_SEARCH
	public void testAccurateSearch() {
		map.store("entry1", "A");
		map.store("entry2", "B");
		map.store("entry2", "C");
		map.store("entry3", "D");
		map.delete("entry2");
		String template = "Letters: ${entry1} ${entry2} ${entry3}";
		Integer matchingMode = TemplateEngine.ACCURATE_SEARCH;
		String result = engine.evaluate(template, map, matchingMode);
		assertEquals("Letters: A C D", result);
	}*/
	
	//assignment 2 task 1
	// NOT WORKING
    /*@Test
    public void TestTemplateEngineOrder() { //order should remain the same if we delete an entry
    	map.store("Name", "Alexis");
    	map.store("Name", "Erin");
    	map.store("age", "20");
    	map.store("symbol", "?");
    	map.delete("Name");
    	String templateString = "Hello ${name}, is your age ${age} ${symbol} ${other} ${sym   bol}";
    	//String templateString = "Hello ${name}, is your age ${age} ${symbol}";
    	//assertEquals(engine.evaluate(templateString, map, TemplateEngine.DELETE_UNMATCHED|TemplateEngine.KEEP_UNMATCHED),"Hello Erin, is your age 20 ?  ");	
    }*/
}
