package edu.uci.ics.mondego.codegenie.search.results;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModelMarker;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.actions.SelectionDispatchAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.search.ui.text.AbstractTextSearchResult;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchSite;

import edu.uci.ics.mondego.codegenie.composition.Composition;
import edu.uci.ics.mondego.codegenie.composition.SliceOperations;
import edu.uci.ics.mondego.codegenie.search.SearchResultEntryWrapper;
import edu.uci.ics.mondego.codegenie.search.TestDrivenSearchResult;
import edu.uci.ics.mondego.codegenie.search.Util;

import org.eclipse.jdt.junit.launcher.JUnitLaunchShortcut;

public class TestSliceAction extends SelectionDispatchAction {
	
	
	private static ISelection selection;
	
	public TestSliceAction(IWorkbenchSite site) {
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
		
		//ISelection selection = null;
		if (input instanceof TestDrivenSearchResult) {
			selection =  ((TestDrivenSearchResult) input).getSearchQuery().getTestClassSelection();
		} else { 
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
			
//			boolean wovenHere = false;
//			
//			if (!searchResultEntry.isWoven()) {
//				WeaveSliceAction wsa = new WeaveSliceAction(this.getSite());
//				wsa.weave(searchResultEntry);
//				wovenHere = true;
//			}
			
//			IProject myPrj = 
//				((IJavaElement)((IStructuredSelection) selection).toArray()[0]).getJavaProject().getProject();
			
			if (searchResultEntry.isWoven() && selection != null) {
					//&& !hasBuildErrors(myPrj)) {
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						JUnitLaunchShortcut jls = new JUnitLaunchShortcut();
						jls.launch(selection, "run");
					}
				});
			}
			
//			if(wovenHere) {
//			  UnweaveSliceAction uwsa = new UnweaveSliceAction(this.getSite());
//			  uwsa.unweave(searchResultEntry);
//			}
			
			((TDSearchResultPage)NewSearchUI.getSearchResultView().getActivePage()).update(searchResultEntry);
			NewSearchUI.activateSearchResultView();

		}
	}
	
	
	public static boolean hasBuildErrors(IProject prj) {
		IMarker[] markers = null;
		try {
			markers = prj.findMarkers(IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER, 
				false, IResource.DEPTH_INFINITE);
		} catch (CoreException e) {
			return true;
		}
		for(int i=0; i < markers.length; i++) {
			IMarker marker = markers[i];
			if (marker.getAttribute(IMarker.SEVERITY, 0) == IMarker.SEVERITY_ERROR)
				return true;
		}
		return false;
	}
	
}
