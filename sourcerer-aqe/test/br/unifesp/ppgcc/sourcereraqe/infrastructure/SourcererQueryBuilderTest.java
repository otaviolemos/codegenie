package br.unifesp.ppgcc.sourcereraqe.infrastructure;

import static org.junit.Assert.*;

import org.junit.Test;

public class SourcererQueryBuilderTest {

	@Test
	public void prioritizeOriginalTermsTest() throws Exception{
	  SourcererQueryBuilder sqb = new SourcererQueryBuilder("http://localhost:8080/related-words-service", "WordNet , CodeVocabulary , Type", false, false, true, true);	  
	  String query = sqb.getSourcererExpandedQuery("invert", "string", "string");	  
	  System.out.println(query);	  
	  assertTrue(query.contains("invert^10"));
		
	}
}
