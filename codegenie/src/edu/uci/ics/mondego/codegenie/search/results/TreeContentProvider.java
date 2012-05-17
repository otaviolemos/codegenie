/*
 * Created on 24-Aug-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.uci.ics.mondego.codegenie.search.results;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

import edu.uci.ics.mondego.codegenie.search.SearchResultEntryWrapper;

import edu.uci.ics.mondego.codegenie.search.TestDrivenSearchResult;
import edu.uci.ics.mondego.codegenie.search.TreeObjectBase;
import edu.uci.ics.mondego.codegenie.search.TreeParentBase;
import edu.uci.ics.mondego.codegenie.search.Util;

//import edu.uci.ics.mondego.eclipse.search.OperationsWSSearchResult;
//import edu.uci.ics.mondego.eclipse.search.TreeObjectBase;
//import edu.uci.ics.mondego.eclipse.search.TreeParentBase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
	

/**
 * @author ricardo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class TreeContentProvider extends ContentProvider implements ITreeContentProvider
{
	class EntryResultTreeRoot extends TreeParentBase
	{
		
		protected boolean totalCountOn () {return false;};
		protected boolean matchTagOn () {return false;};
		protected String matchKeywordSingular () {return "";};
		protected String matchKeywordPlural () {return  "";};
	}
	
	
	
	protected EntryResultTreeRoot invisibleRoot;

	protected HashMap typeGroups;
	
	protected Object[] resultEntries;

	protected int grouping;
	
	public TreeContentProvider (TDSearchResultPage viewPage)
	{
		super (viewPage);
		
		initTypeGroups();
	}
	
	public void initTypeGroups ()
	{
		typeGroups = new HashMap ();
	}
	
	
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}
	public void dispose() {
	}
	
	public Object[] getElements(Object parent) {

		if (parent instanceof TestDrivenSearchResult)
		{
			//if (invisibleRoot==null) 
			initialize((TestDrivenSearchResult)parent);
			return getChildren(invisibleRoot);
		}
		return getChildren(parent);
	}

	public Object getParent(Object child) {
		if (child instanceof TreeObjectBase) {
			return ((TreeObjectBase)child).getParent();
		}
		return null;
	}

	public Object [] getChildren(Object parent) {
		if (parent instanceof TreeParentBase) {
			return ((TreeParentBase)parent).getChildren();
		}
		return new Object[0];
	}
	public boolean hasChildren(Object parent) {
		if (parent instanceof TreeParentBase) {
			return ((TreeParentBase)parent).hasChildren();
		}
		return false;
	}

	public void elementsChanged(Object[] updatedElements) {
//		if (fResult == null)
//			return;
		int addCount= 0;
		int removeCount= 0;
		TreeViewer viewer = (TreeViewer) getPage().getViewer();
		Set updated= new HashSet();
		Set added= new HashSet();
		Set removed= new HashSet();
		for (int i= 0; i < updatedElements.length; i++) {
			if (getPage().getDisplayedMatchCount(updatedElements[i]) > 0) {
				if (viewer.testFindItem(updatedElements[i]) != null)
					updated.add(updatedElements[i]);
				else
					added.add(updatedElements[i]);
				addCount++;
			} else {
				removed.add(updatedElements[i]);
				removeCount++;
			}
		}


		resultEntries = updatedElements;
		
		
		Object[] addedArray = added.toArray(); 	
		
		for (int i = 0; i < addedArray.length; i++)
		{
			if (addedArray[i] instanceof SearchResultEntryWrapper)
			{			
				TreeObjectBase node = addTreeNode ((SearchResultEntryWrapper)addedArray[i]);
									
				if (node != null)
				{
					addNodeToViewer (viewer, node, (SearchResultEntryWrapper)addedArray[i]);
				}

			
			}
		}

		Object[] removedArray = removed.toArray();
		
		for (int i = 0; i < removedArray.length; i++)
		{
			if (removedArray[i] instanceof SearchResultEntryWrapper)
			{
				invisibleRoot.removeLeaf((SearchResultEntryWrapper) removedArray[i]);
			}
		}

		viewer.remove(removed.toArray());
		
		
		if (addedArray.length > 0 || removedArray.length > 0) 
		{
			viewer.refresh();
		}

		
//		viewer.collapseAll();
		

//		viewer.update(updated.toArray(), new String[] { org.eclipse.jdt.internal.ui.search.SortingLabelProvider.PROPERTY_MATCH_COUNT });
	}


	public void addNodeToViewer (TreeViewer viewer, TreeObjectBase node, SearchResultEntryWrapper entry)
	{
		switch (getPage().getGrouping())
		{
		case TDSearchResultPage.GROUP_BY_TYPE:

			GroupTreeNode group = getGroupTreeNode(entry.getEntry().getEntityTypeId());

			if (viewer.testFindItem(group) == null)
			{
				viewer.add(invisibleRoot, group);
			}

			viewer.add(group, node); 

			break;
			
		case TDSearchResultPage.GROUP_BY_FILE:

			TreeObjectBase parent = node.getParent();
			
			if (parent != null)
			{
				viewer.add (parent, node);
				parent = node.getParent ();
			}
			break;
		}

		
	}
	

	public void reset ()
	{
		invisibleRoot = new EntryResultTreeRoot ();
		typeGroups = new HashMap ();
		
	}
	
	protected void initialize(TestDrivenSearchResult searchResult) 
	{
		reset ();
		
		populateEntries (searchResult);
	}
	
	public GroupTreeNode getGroupTreeNode (int groupType)
	{
		GroupTreeNode gtn = (GroupTreeNode) typeGroups.get (new Integer (groupType));
		
		if (gtn == null)
		{
			gtn = new GroupTreeNode(Entity.TypeEnum.getName(groupType), groupType);
			
			typeGroups.put(new Integer(groupType), gtn);
			
			invisibleRoot.addChild(gtn);
		}
		
		return gtn;
		
	}
	
	public void populateEntries (TestDrivenSearchResult searchResult)
	{
		Object[] rawElements = searchResult.getElements();

		resultEntries = rawElements;
		
		populateEntries (rawElements);
	}
	
	public void populateEntries (Object[] rawElements)
	{
		if (rawElements == null) return;
		
		for (int i= 0; i < rawElements.length; i++) 
		{
			if (getPage().getDisplayedMatchCount(rawElements[i]) > 0)
			{
				if (rawElements[i] instanceof SearchResultEntryWrapper)
				{
					addTreeNode ((SearchResultEntryWrapper)rawElements[i]);					
				}
			}
		}
	}
	
	public TreeObjectBase addTreeNode (SearchResultEntryWrapper entry)
	{
		TreeObjectBase result = null;
		
		switch (getPage().getGrouping())
		{
		case TDSearchResultPage.GROUP_BY_TYPE:
			result = addTreeNodeByType (entry);
			break;
			
		case TDSearchResultPage.GROUP_BY_FILE:
			result = addTreeNodeByFileLocation (entry);
			break;
		}
		
		return result;
	}
	
	public TreeObjectBase addTreeNodeByFileLocation (SearchResultEntryWrapper entry)
	{				
		String wholePath = entry.getEntry().getFilePath();
		
		String[] path = Util.getPathListFromPathString(wholePath);
		
		TreeParentBase parent = invisibleRoot;			
		
		if (path.length != 0) 
		{
			for (int i = 0; i < path.length; i++)
			{
				TreeObjectBase child = parent.getChild(path[i]);
	
				if (child instanceof EntryResultTreeNode) return null;
				
				if (child == null)
				{
					child = new FolderTreeNode (path[i]);
					parent.addChild(child);
				}
				
				parent = (TreeParentBase) child;
			}
		}
		else
		{
			TreeObjectBase child = parent.getChild("none");
			
			if (child instanceof EntryResultTreeNode) return null;
			
			if (child == null)
			{
				child = new FolderTreeNode ("none");
				parent.addChild(child);
			}
			parent = (TreeParentBase) child;
		}
		
		((FolderTreeNode)parent).isFile = true;

		Object obj = parent.getChild(String.valueOf(entry.getEntry().getEntityId()));
		EntryResultTreeNode child = null;
		
		if (obj == null || obj instanceof FolderTreeNode)
		{
			child = new EntryResultTreeNode(entry);
			parent.addChild(child);				
		}
		else
		{
			child = (EntryResultTreeNode) obj;
		}
		
		return child;
	}
	
	public TreeObjectBase addTreeNodeByType (SearchResultEntryWrapper entry)
	{
		EntryResultTreeNode node = new EntryResultTreeNode(entry);
				
		GroupTreeNode group = getGroupTreeNode(entry.getEntry().getEntityTypeId());
		
		if (group != null)
		{
			group.addChild(node);
		}
		
		return node;
	}
	
	public void updateGrouping ()
	{
		initTypeGroups ();
		invisibleRoot = new EntryResultTreeRoot ();		
		populateEntries((TestDrivenSearchResult) getPage().getInput());
		TreeViewer viewer = (TreeViewer) getPage().getViewer();
		viewer.collapseAll();
		viewer.refresh();
		
	}
}