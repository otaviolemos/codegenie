package br.unifesp.ict.seg.codegenie.popup.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import br.unifesp.ict.seg.codegenie.tmp.MySQLQuery;

import edu.uci.ics.sourcerer.services.search.adapter.SingleResult;

import br.unifesp.ict.seg.codegenie.pool.MethodInterfacePool;
import br.unifesp.ict.seg.codegenie.pool.SearchResultMap;
import br.unifesp.ict.seg.codegenie.pool.SlicePool;
import br.unifesp.ict.seg.codegenie.pool.SolrPool;
import br.unifesp.ict.seg.codegenie.search.solr.MySingleResult;
import br.unifesp.ict.seg.codegenie.search.solr.SearchQueryCreator;
import br.unifesp.ict.seg.codegenie.search.solr.SolrSearch;
import br.unifesp.ict.seg.codegenie.tmp.Debug;
import br.unifesp.ict.seg.codegenie.views.ResultsView;


public class SearchAction implements IObjectActionDelegate {

	private static Long lastQID;
	private ISelection selection;
	
	/**
	 * Constructor for Action1.
	 */
	public SearchAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		MethodInterfacePool.clear();
		SearchResultMap.clear();
		SlicePool.clear();
		SolrPool.clear();
		IType selection = (IType) ((TreeSelection)this.selection).getFirstElement();
		IJavaProject javap = selection.getJavaProject();
		try {
			javap.save(null, true);
			javap.getProject().build(IncrementalProjectBuilder.INCREMENTAL_BUILD, null);
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
		//given the IType that is selected, build the solr query
		SearchQueryCreator sqc = new SearchQueryCreator(selection);
		sqc.formQuery();
		sqc.getMethodInterface();
		sqc.expandQuery(true, true,true,true);
		lastQID = sqc.getID();
		String[] query = sqc.getQuery();
		if(query == null){
			Debug.errDebug(SearchAction.this.getClass(), "Unable to build Solr query");
			return;
		}
		
		Debug.debug(SearchAction.this.getClass(), "Query params are:");
		for(int i=0;i<query.length;++i){
			Debug.debug(SearchAction.this.getClass(), "\t"+i+" => "+query[i]);	
		}
		SolrSearch searchJob=null;
		try {
			searchJob = new SolrSearch(query,javap,this.selection);
		} catch (InstantiationException e) {
			e.printStackTrace();
			return;
		}
		searchJob.buildQuery();
		List<SingleResult> results = searchJob.performQuery();
		//TODO remove
		Debug.debug(getClass(), "updating solr results...");
		List<MySingleResult> updatedResults = new ArrayList<MySingleResult>();
		for(SingleResult sr : results){
			long neweid = MySQLQuery.query(MySQLQuery.fixSolr(sr.getFqn(), sr.getParams()));
			MySingleResult msr = new MySingleResult(sr,neweid);
			msr.setTestClass(selection);
			updatedResults.add(msr);
		}
		//register solr results and this query into the pools
		SolrPool.add(updatedResults);
		SearchResultMap.add(sqc.getID(),updatedResults,selection,javap);
		
		
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

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	public static Long getCurrentQueryID() {
		return lastQID;
	}

}
