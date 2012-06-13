/**
 * @author Rafael B. Januzi
 * @date 01/10/2011
 */

package ict.seg.codegenie.ie.search;

import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.search.ui.NewSearchUI;
import edu.uci.ics.mondego.codegenie.CodeGeniePlugin;
import edu.uci.ics.mondego.codegenie.search.TestDrivenSearchQuery;
import edu.uci.ics.mondego.codegenie.search.results.TDSearchResultPage;


public class IESearch {
	
	/**
	 * query[]:
	 * 
	 * wantedMethodInterface[0] = packageName
	 * wantedMethodInterface[1] = className
	 * wantedMethodInterface[2] = methodName
	 * wantedMethodInterface[3] = returnName
	 * wantedMethodInterface[4] = parametersNames : "(type1,type2"
	 */
	
	/**
	 * boolean[] consideringPartsOfInterface:
	 * 
	 * consideringPartsOfInterface[0] = considerClass;
	 * consideringPartsOfInterface[1] = considerReturn;
	 * consideringPartsOfInterface[2] = considerName;
	 * consideringPartsOfInterface[3] = considerParameters;
	 */

	public static void runSearch(String[] query, IJavaProject jp, boolean[] consideringPartsOfInterface, ISelection currentSelection) {
		
		try {
			jp.save(null, true);
			jp.getProject().build(IncrementalProjectBuilder.INCREMENTAL_BUILD, null);
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
		
		CodeGeniePlugin.getPlugin().getStore().getRepositoryStore().clearStore();
		
		TestDrivenSearchQuery searchJob = new TestDrivenSearchQuery(query, jp, null, currentSelection);
		NewSearchUI.activateSearchResultView();
		
		searchJob.setConsiderMissingClassName(consideringPartsOfInterface[0]);
		searchJob.setConsiderReturnType(consideringPartsOfInterface[1]);
		searchJob.setConsiderNames(consideringPartsOfInterface[2]);
		searchJob.setConsiderArguments(consideringPartsOfInterface[3]);
		
		if (NewSearchUI.activateSearchResultView().getActivePage() instanceof TDSearchResultPage) {
			((TDSearchResultPage)NewSearchUI.activateSearchResultView().getActivePage()).refreshButtons();
		}
		
		NewSearchUI.runQueryInBackground(searchJob);
	}
}
