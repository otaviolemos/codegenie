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
import edu.uci.ics.mondego.search.model.SearchResult;
import edu.uci.ics.mondego.search.model.SearchResultEntry;
import edu.uci.ics.mondego.codegenie.search.SearchResultEntryWrapper;
import edu.uci.ics.mondego.wsclient.common.SearchResultUnMarshaller;
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
		
		CodeGeniePlugin.getPlugin().getStore().getRepositoryStore().clearStore();
		
		SearchQueryCreator sqc = new SearchQueryCreator();
		String[] query = sqc.formQuery(selection);
		
		TestDrivenSearchQuery searchJob = 
			new TestDrivenSearchQuery(query, jp, null, currentSelection);
		NewSearchUI.activateSearchResultView();
		if (NewSearchUI.activateSearchResultView().getActivePage() instanceof TDSearchResultPage) {
			((TDSearchResultPage)NewSearchUI.activateSearchResultView().getActivePage()).refreshButtons();
		}
		NewSearchUI.runQueryInBackground(searchJob);	
		//NewSearchUI.reuseEditor();

		
		//search(query);
		
//		WSGenericSearchQueryParameter gsqp = new WSGenericSearchQueryParameter ();
//		gsqp.setName (SearchQueryDescriptionConstants.KEYWORD);
//		gsqp.setValue (new String[] {selectionString});
//			
//		SearchQueryHandlerClient gsqh = new SearchQueryHandlerClient ();
//		gsqh.put(selectionString, gsqp);
//			
//		SearchQuery searchJob
//		  = new SearchQuery(gsqh.generateWSGenericSearchQuery(1, 10), null);
//			
//		org.eclipse.search.ui.NewSearchUI.activateSearchResultView();
//
//		NewSearchUI.runQuery(searchJob);		
		
//		String body = "public void test() {;}";
//		try {
//		  selection.createMethod(body, null, false, null);
//		} catch(Exception e) {}
		
//		Composition c = new Composition(selection.getJavaProject());
//		
//		try {
//		  c.weave("sliced3", "src");
//		} catch (Exception e) {
//			
//		}
		
//		Shell shell = new Shell();
//		IType selection = (IType) ((TreeSelection)currentSelection).getFirstElement();
//		
//		InputDialog id1 = new InputDialog(shell, 
//				"Select slice", "Type the name of the slice to be added to the project:", 
//				"sliced1", null);
//		
//		InputDialog id2 = new InputDialog(shell, 
//				"Select class", "Type the name of the class to be tested:", 
//				"package.ClassName", null);
//		
//		InputDialog id3 = new InputDialog(shell, 
//				"Select method", "Type the signature of the method to be tested:", 
//				"aMethod(I)Ljava/lang/String;", null);
//		
//		
//		id1.open();		
//			
//		Slice sl1 = new Slice(id1.getValue());
//		
//		try {
//		  sl1.includeInBuild(selection.getJavaProject());
//		} catch (Exception e) {
//			System.err.println(e.getLocalizedMessage());
//			return;
//		}
		
		
//		id2.open();
//		id3.open();
//		
//		
//		JUnitTestRunner tc = new JUnitTestRunner(selection, id2.getValue(), 
//				id3.getValue());
//		
//		try {
//		  tc.runTests();		  
//		} catch (Exception e) {
//			System.err.println(e.getMessage());
//			MessageDialog.openInformation(shell, "Error", "An exception has occured.");
//			return;
//		} finally {
//			try {
//			  sl1.excludeFromBuild(selection.getJavaProject());
//			} catch (Exception e) {
//				System.err.println(e.getMessage());
//				MessageDialog.openInformation(shell, "Error", "An exception has occured.");
//				return;
//			}
//		}
//				
//		MessageDialog.openInformation(shell, "Test", 
//				"Runs: " +  tc.getTestResult().getRunCount() + " " +
//				"Failures: " + tc.getTestResult().getFailureCount() + " " +
//				"Run time: " + tc.getTestResult().getRunTime() + "\r\r" +
//				"Method coverage percentage:" + "\r" +
//				"Nodes - " + tc.getTestResult().getMethodCoverage(0) + "%" + " " +
//				"Edges - " + tc.getTestResult().getMethodCoverage(1) + "%" + " " +
//				"Uses - " + tc.getTestResult().getMethodCoverage(2) + "%" + "\r\r" +
//				"Class coverage percentage:" + "\r" +
//				"Nodes - " + tc.getTestResult().getClassCoverage(0) + "%" + " " +
//				"Edges - " + tc.getTestResult().getClassCoverage(1) + "%" + " " +
//				"Uses - " + tc.getTestResult().getClassCoverage(2) + "%");
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
	
	private void search(String[] query){
		String keywords = query[0] + "&nbsp;" + query[1];
		String queryStr = query[0] + "&nbsp;" + query[1] + "&nbsp;ret:(" + query[2] +
		")" + "&nbsp;args:" + query[3] + ")";
		String pageID = "1";
		String entriesPerPage = "10";
		String sourcererApp = "tagus.ics.uci.edu:8080/sourcerer";
		
		InputStream ins = null;
		try {
		  System.out.println("http://" + sourcererApp + "/ws-search/search?qry=" +
					 queryStr +  "&pid=" +
					 pageID +  "&epp=" +
					 entriesPerPage);
		 ins = new URL("http://" + sourcererApp + "/ws-search/search?qry=" +
									 keywords +  "&pid=" +
									 pageID +  "&epp=" +
									 entriesPerPage +  "&sig=signature").openStream();
		} catch (FileNotFoundException _e) {
			_e.printStackTrace();
		} catch (MalformedURLException _e) {
			_e.printStackTrace();
		} catch (IOException _e) {
			_e.printStackTrace();
		}
		
		SearchResult sr = SearchResultUnMarshaller.unMarshallSearchResults(ins);
		
		// visual testing
		for(SearchResultEntry sre: sr.getEntries()){
			System.out.println(sre.getEntityId() + " | " +
								sre.getRank() + " | " +
								sre.getEntityName() );
		}
		
		try {
			ins.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
