package edu.uci.ics.mondego.codegenie.search.results;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.ui.actions.SelectionDispatchAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.search.ui.text.AbstractTextSearchResult;
import org.eclipse.ui.IWorkbenchSite;

import edu.uci.ics.mondego.codegenie.search.results.EntryResultTreeNode;
import edu.uci.ics.mondego.codegenie.search.SearchResultEntryWrapper;
import edu.uci.ics.mondego.codegenie.search.TestDrivenSearchResult;
import org.eclipse.search.ui.NewSearchUI;

import edu.uci.ics.mondego.codegenie.search.Util;


public class GetSliceAction extends SelectionDispatchAction {

	public GetSliceAction(IWorkbenchSite site) {
		super(site);
	}
	
	public void run(IStructuredSelection selection) {
		run(selection.toArray());
	}
	
	public void run(Object[] elements) 
	{
		if (elements == null) {
			return;
		}
		
		AbstractTextSearchResult input = ((TDSearchResultPage) 
			NewSearchUI.getSearchResultView().getActivePage()).getInput();
		
		String[] query = null;
		IProject project = null;
		if (input instanceof TestDrivenSearchResult) {
			query = ((TestDrivenSearchResult) input).getSearchQuery().getQuerySpec();
			project = ((TestDrivenSearchResult) input).getSearchQuery().getProject().getProject();
			
		}		
		
		for (int i= 0; i < elements.length; i++) {
			Object element = elements[i];
			SearchResultEntryWrapper searchResultEntry = null;
			if (element instanceof SearchResultEntryWrapper) {
				searchResultEntry = (SearchResultEntryWrapper) element;
			} else if (element instanceof EntryResultTreeNode) {
				searchResultEntry = ((EntryResultTreeNode) element).getSearchResultEntry();
			} else {
			    continue;
			}
			if (Util.getSliceFromSearchResult(searchResultEntry, query, project) == null) {
				((TDSearchResultPage)NewSearchUI.getSearchResultView().getActivePage()).update(searchResultEntry);
			}
		}
	}
}
