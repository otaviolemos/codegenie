package br.unifesp.ict.seg.codegenie.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IType;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import edu.uci.ics.sourcerer.services.search.adapter.SingleResult;
import br.unifesp.ict.seg.codegenie.pool.SearchResultMap;
import br.unifesp.ict.seg.codegenie.pool.SolrPool;
import br.unifesp.ict.seg.codegenie.search.solr.MySingleResult;
import br.unifesp.ict.seg.codegenie.search.solr.SearchQueryCreator;
import br.unifesp.ict.seg.codegenie.search.solr.SolrSearch;
import br.unifesp.ict.seg.codegenie.tmp.Debug;
import br.unifesp.ict.seg.codegenie.tmp.MySQLQuery;

public class ResultsViewUpdater {

	private SolrSearch searchJob;
	private SearchQueryCreator sqc;

	public ResultsViewUpdater(SolrSearch searchJob,SearchQueryCreator sqc) {
		this.sqc = sqc;
		this.searchJob = searchJob;
	}

	public void makeQueryAndUpdateView() {
		//MethodInterfacePool.clear();
		SearchResultMap.clear();
		//SlicePool.clear();
		SolrPool.clear();
		IType selection = sqc.getTestingClass();
		List<SingleResult> results = searchJob.performQuery();
		Debug.debug(getClass(), "updating solr results...");
		List<MySingleResult> updatedResults = new ArrayList<MySingleResult>();
		for(SingleResult sr : results){
			long neweid = MySQLQuery.query(MySQLQuery.fixSolr(sr.getFqn(), sr.getParams()));
			MySingleResult msr = new MySingleResult(sr,neweid);
			msr.setTestClass(selection);
			updatedResults.add(msr);
			msr.setResultsViewUpdater(this);
		}
		//register solr results and this query into the pools
		SolrPool.add(updatedResults);
		SearchResultMap.add(sqc.getID(),updatedResults,selection,searchJob.getJavap());
		
		
		//force showing the code genie view
		IWorkbench work = PlatformUI.getWorkbench();
		//bring view to the front
		try {
			work.getActiveWorkbenchWindow()
			.getActivePage()
			.showView(ResultsView.ID);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		//get ResultsView
		ResultsView view = (ResultsView) work.getActiveWorkbenchWindow()
				.getActivePage().findView(ResultsView.ID);
		view.refresh();
	}

	public void updateQuery() {
		String newQ = ResultsView.getCurrent().
				showInput("New Query", searchJob.getQuery());
		searchJob.setQuery(newQ);
	}

}
