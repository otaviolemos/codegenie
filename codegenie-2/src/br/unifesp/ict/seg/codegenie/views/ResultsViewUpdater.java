package br.unifesp.ict.seg.codegenie.views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import edu.uci.ics.sourcerer.services.search.adapter.SingleResult;
import br.unifesp.ict.seg.codegenie.Activator;
import br.unifesp.ict.seg.codegenie.pool.MethodInterfacePool;
import br.unifesp.ict.seg.codegenie.pool.SearchResultMap;
import br.unifesp.ict.seg.codegenie.pool.SlicePool;
import br.unifesp.ict.seg.codegenie.pool.SolrPool;
import br.unifesp.ict.seg.codegenie.popup.actions.SearchAction;
import br.unifesp.ict.seg.codegenie.preferences.PreferenceConstants;
import br.unifesp.ict.seg.codegenie.search.slicer.SliceFile;
import br.unifesp.ict.seg.codegenie.search.slicer.SlicerConnector;
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
		int i=0;
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		int limit = store.getInt(PreferenceConstants.AUTOTEST);
		HashSet<MySingleResult> totest = new HashSet<MySingleResult>();
		for(SingleResult sr : results){
			//long neweid = MySQLQuery.query(MySQLQuery.fixSolr(sr.getFqn(), sr.getParams()));
			//MySingleResult msr = new MySingleResult(sr,neweid);
			MySingleResult msr = new MySingleResult(sr,sr.getEntityID());
			msr.setTestClass(selection);
			updatedResults.add(msr);
			msr.setResultsViewUpdater(this);
			if(i++<limit){
				totest.add(msr);
			}
		}
		//register solr results and this query into the pools
		SolrPool.add(updatedResults);
		SearchResultMap.add(sqc.getID(),updatedResults,selection,searchJob.getJavap());
		//test first elements
		for(MySingleResult msr : totest){
			Long eid = msr.getEntityID();
			Long qid = SearchAction.getCurrentQueryID();
			SlicerConnector sc = new SlicerConnector(eid,qid);
			SliceFile slice = sc.getSlice();
			SlicePool.add(slice, eid, qid);
			slice.setTestClass(msr.getTestClass());
			slice.setMethodInterface(MethodInterfacePool.get(qid));
			try {
				slice.unzip();
				slice.merge();
				slice.saveAndRebuild();
				try{
					slice.createMethod();
				} catch(NullPointerException npe){//bad internet network
					//nothing to do
				}
				slice.saveAndRebuild();
				msr.setWoven();
				slice.runTests(msr);
				//test...
				ResultsView.getCurrent().showMessage("Testing method: "+msr+"\nPlease hit \"ok\" button.");
				SliceFile.removeSlice(eid,msr);
				SlicePool.remove(eid);
			} catch (IOException | CoreException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
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
