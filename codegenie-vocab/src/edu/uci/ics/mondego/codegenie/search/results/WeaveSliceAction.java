package edu.uci.ics.mondego.codegenie.search.results;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.ui.actions.SelectionDispatchAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.search.ui.text.AbstractTextSearchResult;
import org.eclipse.ui.IWorkbenchSite;

import edu.uci.ics.mondego.codegenie.composition.SliceOperations;
import edu.uci.ics.mondego.codegenie.composition.Composition;
import edu.uci.ics.mondego.codegenie.search.SearchResultEntryWrapper;
import edu.uci.ics.mondego.codegenie.search.TestDrivenSearchQuery;
import edu.uci.ics.mondego.codegenie.search.TestDrivenSearchResult;
import edu.uci.ics.mondego.codegenie.search.Util;

public class WeaveSliceAction extends SelectionDispatchAction {

	public WeaveSliceAction(IWorkbenchSite site) {
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
			
			AbstractTextSearchResult input = ((TDSearchResultPage) 
					NewSearchUI.getSearchResultView().getActivePage()).getInput();

			TestDrivenSearchQuery query = (TestDrivenSearchQuery)((TestDrivenSearchResult) input).getSearchQuery();
			
			if (!searchResultEntry.isWoven() && !query.hasWoven()) {
				weave(searchResultEntry);
				if(searchResultEntry.isWoven()) {
					((TDSearchResultPage)NewSearchUI.getSearchResultView().getActivePage()).update(searchResultEntry);
					query.setHasWoven(true);
				}
			}
		}
	}
	
	public void weave(SearchResultEntryWrapper searchResultEntry) {
	  
	  TDSearchResultPage searchPage = ((TDSearchResultPage) 
        NewSearchUI.getSearchResultView().getActivePage());
	  
		AbstractTextSearchResult input = searchPage.getInput();
		
		IType testType = null;
			
			String[] query = null;
			IProject project = null;
			String sourceFolderName = null;
			if (input instanceof TestDrivenSearchResult) {
				query = ((TestDrivenSearchResult) input).getSearchQuery().getQuerySpec();
				project = ((TestDrivenSearchResult) input).getSearchQuery().getProject().getProject();
				ISelection selection =  ((TestDrivenSearchResult) input).getSearchQuery().getTestClassSelection();
				testType = (IType) ((TreeSelection)selection).getFirstElement();
				sourceFolderName = testType.getAncestor(IJavaElement.PACKAGE_FRAGMENT_ROOT).getElementName();
			} else { 
			  return; 
			}
			
		IFile sliceFile = 
			Util.getSliceFromSearchResult(searchResultEntry, query, project);
		
		if (sliceFile == null) {
			searchPage.update(searchResultEntry);
			return;
		}

		
		IJavaProject jprj = JavaCore.create(project);
		
		SliceOperations so = new SliceOperations(sliceFile.getName(), jprj, searchResultEntry.getEntry());
		
		
		try {
			so.unzipInProject();
			so.includeInBuild();
			so.doRenamings();
			Composition c = new Composition(jprj, testType, so.getToImport());
	    c.setExistingClass(searchPage.isExistingClass());
			c.weave(so.getName(), sourceFolderName);
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		searchResultEntry.setWoven(true);
	}
	
}
