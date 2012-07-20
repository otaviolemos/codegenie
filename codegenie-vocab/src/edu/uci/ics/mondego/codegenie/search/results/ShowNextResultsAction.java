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
			TestDrivenSearchQuery query = (TestDrivenSearchQuery) tmp.getQuery();
			TestDrivenSearchResult result = (TestDrivenSearchResult) tmp;
			
			boolean[] thisQueryType = 
		 	{query.isConsideringReturnType(), query.isConsideringNames(), query.isConsideringArguments(),
					query.isConsideringMissingClassName(), query.isConsideringEnglishSynonyms(), query.isConsideringCodeSynonyms(), query.isConsideringAntonyms()};

			boolean[] lastQueryType = query.getLastQueryType();
			
			if (thisQueryType[0] == lastQueryType[0] &&
				 thisQueryType[1] == lastQueryType[1] &&
				 thisQueryType[2] == lastQueryType[2] &&
				 thisQueryType[3] == lastQueryType[3] &&
				 thisQueryType[4] == lastQueryType[4] &&
				 thisQueryType[5] == lastQueryType[5] &&
				 thisQueryType[6] == lastQueryType[6]) {
				int page = result.getSearchQuery().getCurrentPage();
				result.getSearchQuery().setCurrentPage(page+1);
			}
			
			result.setQueryLabel(query.getLabel());
			NewSearchUI.activateSearchResultView();
			NewSearchUI.runQueryInBackground(result.getSearchQuery());
		}	
	}
}
