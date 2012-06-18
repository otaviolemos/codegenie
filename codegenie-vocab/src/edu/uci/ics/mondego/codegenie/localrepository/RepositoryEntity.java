/**
 * @author ricardo
 */

package edu.uci.ics.mondego.codegenie.localrepository;

import java.io.Serializable;
import org.eclipse.core.resources.IFile;
import edu.uci.ics.mondego.codegenie.search.SearchResultEntryWrapper;

public class RepositoryEntity implements Serializable {

	static final long serialVersionUID = 1;
	
	public SearchResultEntryWrapper searchResultEntry;
	
	public boolean downloadedIntoLocalRepository;
	
	public IFile localRepositoryFile;

	public SearchResultEntryWrapper getSearchResultEntry() {
		return searchResultEntry;
	}

	public void setSearchResultEntry(SearchResultEntryWrapper searchResultEntry) {
		this.searchResultEntry = searchResultEntry;
	} 
}
