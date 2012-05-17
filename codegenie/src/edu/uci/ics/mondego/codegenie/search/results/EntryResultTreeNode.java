/**
 * @author ricardo
 */


package edu.uci.ics.mondego.codegenie.search.results;

import edu.uci.ics.mondego.codegenie.search.TreeObjectBase;

import edu.uci.ics.mondego.codegenie.search.SearchResultEntryWrapper;

public class EntryResultTreeNode extends TreeObjectBase
{
	protected SearchResultEntryWrapper entry = null;
	protected long entityID = 0;
	
	
	public EntryResultTreeNode(SearchResultEntryWrapper entry) 
	{
		super (entry.getEntry().getEntityName());
		
		this.entry = entry;

		entityID = entry.getEntry().getEntityId();
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