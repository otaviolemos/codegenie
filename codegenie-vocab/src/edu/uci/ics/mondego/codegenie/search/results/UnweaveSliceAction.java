package edu.uci.ics.mondego.codegenie.search.results;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.ui.actions.SelectionDispatchAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.search.ui.text.AbstractTextSearchResult;
import org.eclipse.ui.IWorkbenchSite;

import edu.uci.ics.mondego.codegenie.search.SearchResultEntryWrapper;
import edu.uci.ics.mondego.codegenie.search.TestDrivenSearchQuery;
import edu.uci.ics.mondego.codegenie.search.TestDrivenSearchResult;

import edu.uci.ics.mondego.codegenie.composition.Composition;

public class UnweaveSliceAction extends SelectionDispatchAction {

	public UnweaveSliceAction(IWorkbenchSite site) {
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
			
			if(searchResultEntry.isWoven()) {
				unweave(searchResultEntry);
				if(!searchResultEntry.isWoven()) {
					AbstractTextSearchResult input = ((TDSearchResultPage) 
							NewSearchUI.getSearchResultView().getActivePage()).getInput();
					((TestDrivenSearchQuery)((TestDrivenSearchResult) input).getSearchQuery()).setHasWoven(false);
					((TDSearchResultPage)NewSearchUI.getSearchResultView().getActivePage()).update(searchResultEntry);
				}
			}
		}
	}
	
	public void unweave(SearchResultEntryWrapper searchResultEntry) {
		AbstractTextSearchResult input = ((TDSearchResultPage) 
				NewSearchUI.getSearchResultView().getActivePage()).getInput();
			
			IJavaProject project = null;
			String sourceFolderName = null;
			if (input instanceof TestDrivenSearchResult) {
				project = ((TestDrivenSearchResult) input).getSearchQuery().getProject();
				ISelection selection =  ((TestDrivenSearchResult) input).getSearchQuery().getTestClassSelection();
				IType testType = (IType) ((TreeSelection)selection).getFirstElement();
				sourceFolderName = testType.getAncestor(IJavaElement.PACKAGE_FRAGMENT_ROOT).getElementName();
			} else { 
			  return; 
			}

		
		Composition c = new Composition(project);
		try {
			c.unweave(sourceFolderName, new Long(searchResultEntry.getEntry().getEntityID()).toString(),
					false);
		} catch(Exception e) {
			e.printStackTrace();
		}
		searchResultEntry.setWoven(false);
	}
}
