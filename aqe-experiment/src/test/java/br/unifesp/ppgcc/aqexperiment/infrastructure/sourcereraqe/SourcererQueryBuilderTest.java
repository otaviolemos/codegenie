package br.unifesp.ppgcc.aqexperiment.infrastructure.sourcereraqe;

import static org.junit.Assert.assertTrue; 

import org.junit.Test;

import br.unifesp.ppgcc.aqexperiment.infrastructure.util.ConfigProperties;
import edu.uci.ics.sourcerer.services.search.adapter.SearchAdapter;
import edu.uci.ics.sourcerer.services.search.adapter.SearchResult;

public class SourcererQueryBuilderTest {

	@Test
	public void querySintaxTest() throws Exception {
		String m = "setSize";
		String p = "int, double";
		String r = "void";
		
		boolean relaxReturn = new Boolean(ConfigProperties.getProperty("aqExperiment.relaxReturn"));
		boolean relaxParams = new Boolean(ConfigProperties.getProperty("aqExperiment.relaxParams"));
		SourcererQueryBuilder sourcererQueryBuilder = new SourcererQueryBuilder(ConfigProperties.getProperty("aqExperiment.expanders"), relaxReturn, relaxParams);
		SearchAdapter searchAdapter = SearchAdapter.create(ConfigProperties.getProperty("aqExperiment.sourcerer.url"));
		SearchResult searchResult = null;

		String query = sourcererQueryBuilder.getSourcererExpandedQuery(m, r, p);
		searchResult = searchAdapter.search(query);
		
		System.out.println("Method: " + m);
		System.out.println("Params: " + p);
		System.out.println("Return: " + r);
		System.out.println("Query\n" + query);
		System.out.println("\nReturn: " + searchResult.getNumFound());
		System.out.println("Expanders: " + ConfigProperties.getProperty("aqExperiment.expanders"));
		assertTrue(searchResult.getNumFound() >= -1);
	}
}
