/**
 * @author ricardo
 */

package edu.uci.ics.mondego.codegenie.search.results;

import org.eclipse.jface.action.Action;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.search.ui.text.AbstractTextSearchResult;

import edu.uci.ics.mondego.codegenie.search.TestDrivenSearchResult;
import edu.uci.ics.mondego.codegenie.search.TestDrivenSearchQuery;

public class ShowNextResultsAction extends Action {
	
	TDSearchResultPage page;
	
	public ShowNextResultsAction (TDSearchResultPage page)
	{
		this.page = page;
	}
	
	public void run ()
	{
		AbstractTextSearchResult tmp = page.getInput();
		if (tmp instanceof TestDrivenSearchResult)
		{
			TestDrivenSearchQuery query = (TestDrivenSearchQuery)tmp.getQuery();
			TestDrivenSearchResult result = (TestDrivenSearchResult) tmp;
			
			boolean[] thisQueryType = 
		 	{query.isConsideingrReturnType(), query.isConsideringNames(), query.isConsideringArguments(),
					query.isConsideringMissingClassName()};

			boolean[] lastQueryType = query.getLastQueryType();
			
			if (thisQueryType[0] == lastQueryType[0] &&
				 thisQueryType[1] == lastQueryType[1] &&
				 thisQueryType[2] == lastQueryType[2] &&
				 thisQueryType[3] == lastQueryType[3]) {
				int page = result.getSearchQuery().getCurrentPage();
				result.getSearchQuery().setCurrentPage(page+1);
			}
			
			NewSearchUI.activateSearchResultView();
			NewSearchUI.runQueryInBackground(result.getSearchQuery());
		}	
	}
}
