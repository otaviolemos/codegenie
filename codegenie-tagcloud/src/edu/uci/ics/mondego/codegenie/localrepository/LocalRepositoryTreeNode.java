/**
 * @author ricardo
 */

package edu.uci.ics.mondego.codegenie.localrepository;

import edu.uci.ics.mondego.codegenie.search.TreeParentBase;


public class LocalRepositoryTreeNode extends TreeParentBase {

	public boolean isFile = false; 
	
	public LocalRepositoryTreeNode() 
	{
		super("");
	}	

	public LocalRepositoryTreeNode(String name) 
	{
		super(name);
	}	
	
	protected boolean matchTagOn () {return false;};
	protected boolean totalCountOn () {return true;};
	protected String matchKeywordSingular () {return "";};
	protected String matchKeywordPlural () {return  "";};
	

}
