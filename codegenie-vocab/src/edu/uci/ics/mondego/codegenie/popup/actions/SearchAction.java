/**
 * @author Otavio Lemos
 */

package edu.uci.ics.mondego.codegenie.popup.actions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.jdt.core.*;

import edu.uci.ics.mondego.codegenie.CodeGeniePlugin;
import edu.uci.ics.mondego.codegenie.search.SearchQueryCreator;
import edu.uci.ics.mondego.codegenie.search.results.TDSearchResultPage;
import edu.uci.ics.sourcerer.services.search.adapter.SearchResult;
import edu.uci.ics.sourcerer.services.search.adapter.SingleResult;
import edu.uci.ics.mondego.codegenie.search.SearchResultEntryWrapper;
import edu.uci.ics.mondego.codegenie.search.*;

public class SearchAction implements IObjectActionDelegate {

	private ISelection currentSelection;
	
	public ISelection getCurrentSelection() {
		return currentSelection;
	}

	public void setCurrentSelection(ISelection currentSelection) {
		this.currentSelection = currentSelection;
	}

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
		//Shell shell = new Shell();
		IType selection = (IType) ((TreeSelection)currentSelection).getFirstElement();
		
		IJavaProject jp = selection.getJavaProject();
		try {
			jp.save(null, true);
			jp.getProject().build(IncrementalProjectBuilder.INCREMENTAL_BUILD, null);
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
		
		//CodeGeniePlugin.getPlugin().getStore().getRepositoryStore().clearStore();
		
		SearchQueryCreator sqc = new SearchQueryCreator();
		String[] query = sqc.formQuery(selection);
		
		TestDrivenSearchQuery searchJob = 
			new TestDrivenSearchQuery(query, jp, null, currentSelection);
		NewSearchUI.activateSearchResultView();
		if (NewSearchUI.activateSearchResultView().getActivePage() instanceof TDSearchResultPage) {
			((TDSearchResultPage)NewSearchUI.activateSearchResultView().getActivePage()).refreshButtons();
		}
		NewSearchUI.runQueryInBackground(searchJob);	
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.setCurrentSelection(selection);
	}
	
	private void getNames(IType type) {
		String method, clazz, pack;
		
	}
}
