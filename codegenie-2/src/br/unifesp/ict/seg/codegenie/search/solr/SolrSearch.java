package br.unifesp.ict.seg.codegenie.search.solr;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.ISelection;

import edu.uci.ics.sourcerer.services.search.adapter.SearchAdapter;
import edu.uci.ics.sourcerer.services.search.adapter.SearchResult;
import edu.uci.ics.sourcerer.services.search.adapter.SingleResult;

import br.unifesp.ict.seg.codegenie.Activator;
import br.unifesp.ict.seg.codegenie.preferences.PreferenceConstants;
import br.unifesp.ict.seg.codegenie.tmp.Debug;

public class SolrSearch {

	private IJavaProject javap;
	private ISelection testClass;
	private String query;
	private Long qid;
	private String server;

	
	public SolrSearch(Long qid,String query,IJavaProject java, ISelection testClass) throws InstantiationException {
		if(query==null || java==null || testClass==null){
			throw new  InstantiationException("args for new "
					+getClass().getSimpleName()+" are: " +query+", "
					+java+", "+testClass);
		}
		this.qid=qid;
		this.query = query;
		this.javap = java;
		this.testClass = testClass;
		this.server = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.SOLR_SERVER);
	}
	
	public Long getQID(){return qid;}
	
	public List<SingleResult> performQuery(){
		SearchAdapter s = SearchAdapter.create(server);
	    SearchResult srcResult = s.search(query);
	    int numFound = srcResult.getNumFound();
	    Debug.debug(getClass(), "Found "+numFound+" results for query");
	    if(numFound>0){
	    	return srcResult.getResults(0, numFound);
	    }
	    return new ArrayList<SingleResult>();
	}

	/**
	 * @return the javap
	 */
	public IJavaProject getJavap() {
		return javap;
	}

	/**
	 * @return the testClass
	 */
	public ISelection getTestClass() {
		return testClass;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String newQ) {
		this.query=newQ;
	}
	
	

}
