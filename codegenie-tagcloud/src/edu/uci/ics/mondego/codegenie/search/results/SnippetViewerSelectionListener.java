package edu.uci.ics.mondego.codegenie.search.results;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.search.ui.text.AbstractTextSearchResult;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import edu.uci.ics.mondego.codegenie.search.SearchResultEntryWrapper;
import edu.uci.ics.mondego.codegenie.search.TestDrivenSearchResult;
import edu.uci.ics.mondego.codegenie.search.Util;
import edu.uci.ics.mondego.codegenie.views.SnippetView;

public class SnippetViewerSelectionListener implements
		ISelectionChangedListener {

	public void selectionChanged(SelectionChangedEvent event) {
		IStructuredSelection sel = (IStructuredSelection)event.getSelection();
		Object o = sel.getFirstElement();
		if (o instanceof SearchResultEntryWrapper) {
			SearchResultEntryWrapper sre = (SearchResultEntryWrapper) o;
			AbstractTextSearchResult input = ((TDSearchResultPage) 
					NewSearchUI.getSearchResultView().getActivePage()).getInput();
			IProject project = ((TestDrivenSearchResult) input).getSearchQuery().getProject().getProject();
			
			String contents = new String();
			try {
				IFile file = Util.getFileFromSearchResult(sre, project);
				if (file != null) {
					BufferedReader d = new BufferedReader(new 
					    InputStreamReader(file.getContents()));
					String line = d.readLine();
					while(line != null) {
						contents += line + "\r\n";
						line = d.readLine();
					}
				} else {
					return;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			
			IWorkbench work = PlatformUI.getWorkbench();
			IViewPart snippetview = work.getActiveWorkbenchWindow().getActivePage()
			.findView("mondego-codegenie.SnippetView");
			if (snippetview == null) {
				try {
					work.getActiveWorkbenchWindow().getActivePage().showView("mondego-codegenie.SnippetView");
				} catch (PartInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				snippetview = work.getActiveWorkbenchWindow().getActivePage()
				.findView("mondego-codegenie.SnippetView");
			}

			((SnippetView) snippetview).setSnippet(contents);

		}
	}
}


