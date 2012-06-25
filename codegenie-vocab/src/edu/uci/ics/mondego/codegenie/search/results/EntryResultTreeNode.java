/**
 * @author ricardo
 */


package edu.uci.ics.mondego.codegenie.search.results;

import edu.uci.ics.mondego.codegenie.search.TreeObjectBase;

import edu.uci.ics.mondego.codegenie.search.SearchResultEntryWrapper;
import edu.uci.ics.mondego.codegenie.util.JavaTermExtractor;

public class EntryResultTreeNode extends TreeObjectBase
{
	protected SearchResultEntryWrapper entry = null;
	protected long entityID = 0;
	
	
	public EntryResultTreeNode(SearchResultEntryWrapper entry) 
	{
		super (JavaTermExtractor.getNameAndParams(entry.getEntry()));
		
		this.entry = entry;

		entityID = entry.getEntry().getEntityID();
	}

	public SearchResultEntryWrapper getSearchResultEntry() {
		return entry;
	}
	
	public void setSearchResultEntry(SearchResultEntryWrapper searchResultEntry) {
		this.entry = searchResultEntry;
	}

	public void computeNumberOfChildrenUp() {};
	
	public long getEntityID ()
	{
		return entityID;
	}

}