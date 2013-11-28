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

	//TODO AQE: remove querySpec
	private String[] querySpec;
	private IJavaProject javap;
	private ISelection testClass;
	private String query;
	private Long qid;
	private String server;

	//TODO AQE: remove constructor
	public SolrSearch(Long qid,String[] query,IJavaProject java, ISelection testClass) throws InstantiationException {
		if(query==null || java==null || testClass==null){
			throw new  InstantiationException("args for new "
					+getClass().getSimpleName()+" are: " +query+", "
					+java+", "+testClass);
		}
		this.qid=qid;
		this.querySpec = query;
		this.javap = java;
		this.testClass = testClass;
		this.server = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.SOLR_SERVER);
	}
	//TODO AQE: new constructor
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
	
	//TODO AQE: remove method
	public void buildQuery(){
		String[][] solrConditions = new String[5][];
		solrConditions[0]=null;
		solrConditions[1]=new String[2];
		solrConditions[1][0]=Solr.FQN_CONTENTS;
		solrConditions[1][1]=Solr.FQN_FRAGMENTS;
		solrConditions[2]=new String[1];
		solrConditions[2][0]=Solr.SNAME_CONTENTS;
		solrConditions[3] = new String[1];
		solrConditions[3][0] = Solr.RETURN_FQN_CONTENTS;
		solrConditions[4]=new String[1];
		solrConditions[4][0]=Solr.PARAMS_FQN_CONTENTS;
		//solrConditions[4][0]=Solr.PARAMS_EXACT;
		this.buildQuery(solrConditions);
	}
	
	//TODO AQE: remove method
	private void buildQuery(String[][] solrConditions){
		Debug.debug(getClass(), "building query with solr conditions...");
		StringBuilder strb = new StringBuilder();
		if(solrConditions!=null){
			//each type (return, fqn, sname, params...)
			for(int i=0;i<solrConditions.length;++i){
				if(solrConditions[i]!=null && querySpec[i]!=null && !querySpec[i].equals("")){
					if(!strb.toString().equals("")){
						strb.append(Solr.AND);
					}
					strb.append("(");
					//each inner type (for example: fqn_frags or fqn_contents
					for(int j=0;j<solrConditions[i].length;++j){
						if(j!=0){
							strb.append(Solr.OR);
						}
						strb.append(solrConditions[i][j]+":("+querySpec[i]+")");
					}
					strb.append(")");
				}
			}
		}
		query = strb.toString();
		Debug.debug(getClass(), query);
	}
	
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
