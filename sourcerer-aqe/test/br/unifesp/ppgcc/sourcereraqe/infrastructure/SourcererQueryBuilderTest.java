package br.unifesp.ppgcc.sourcereraqe.infrastructure;

import static org.junit.Assert.*;

import org.junit.Test;

public class SourcererQueryBuilderTest {

	@Test
	public void prioritizeOriginalTermsTest() throws Exception{
	  SourcererQueryBuilder sqb = new SourcererQueryBuilder("http://localhost:8080/wordnet-related-service", "WordNet , Type", false, false, false, true, false);	  
	  String query = sqb.getSourcererExpandedQuery("invert", "string", "string");	  
	  //System.out.println(query);	  
	  assertTrue(query.contains("invert^10"));		
	}
	
	@Test
	public void reducedMethodNameTerms() throws Exception {
	  SourcererQueryBuilder sqb = new SourcererQueryBuilder("http://localhost:8080/wordnet-related-service", "WordNet , Type", false, false, false, true, true);	  
	  String query = sqb.getSourcererExpandedQuery("quarter is a big name test", "string", "string");
	  //System.out.println(query);
	  assertFalse(query.contains("a^10"));
	  assertFalse(query.contains("is^10"));
	  assertFalse(query.contains("big^10"));
	}
	
	@Test
	public void reducedMethodNameTerms2() throws Exception {
	  SourcererQueryBuilder sqb = new SourcererQueryBuilder("http://localhost:8080/wordnet-related-service", "WordNet , Type", false, false, false, true, true);	  
	  String query = sqb.getSourcererExpandedQuery("a quarter is a big name test b asd b", "string", "string");
	  //System.out.println(query);
	  assertFalse(query.contains("a^10"));
	  assertFalse(query.contains("is^10"));
	  assertFalse(query.contains("big^10"));
	  assertFalse(query.contains("b^10"));
	  assertFalse(query.contains("asd^10"));
	}
	
	@Test
	public void testTLL() throws Exception {
	  SourcererQueryBuilder sqb = new SourcererQueryBuilder("http://localhost:8080/wordnet-related-service", "TLL , Type", false, false, false, true, true);
	  String query = sqb.getSourcererExpandedQuery("scale", "void", "double");
	  System.out.println(query);
	}
}
