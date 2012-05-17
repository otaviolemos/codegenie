/**
 * @author ricardo
 */



package edu.uci.ics.mondego.codegenie.search.results;

import edu.uci.ics.mondego.codegenie.search.TreeParentBase;

public class GroupTreeNode extends TreeParentBase
{
	public int type;
	
	public GroupTreeNode (String name, int type)
	{
		super(name);
		
		this.type = type; 
	}
	
	protected boolean matchTagOn () {return true;};
	protected boolean totalCountOn () {return false;};
	protected String matchKeywordSingular () {return "match";};
	protected String matchKeywordPlural () {return  "matches";};
	
}