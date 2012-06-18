/**
 * @author ricardo
 */


package edu.uci.ics.mondego.codegenie.search.results;

import edu.uci.ics.mondego.codegenie.search.TreeParentBase;

public class FolderTreeNode extends TreeParentBase
{
	public boolean isFile;
	
	public FolderTreeNode (String name)
	{
		super(name);	
		isFile = false;
	}
	
	protected boolean matchTagOn () {return false;};
	protected boolean totalCountOn () {return true;};
	protected String matchKeywordSingular () {return "match";};
	protected String matchKeywordPlural () {return  "matches";};
	
}