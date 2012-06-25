/**
 * @author ricardo
 */

package edu.uci.ics.mondego.codegenie.search;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import edu.uci.ics.sourcerer.services.search.adapter.SingleResult;

import edu.uci.ics.mondego.codegenie.search.results.EntryResultTreeNode;

//import edu.uci.ics.mondego.codegenie.search.results.ResultEntryWrapper;

public abstract class TreeParentBase extends TreeObjectBase 
{
	protected HashMap children;

	protected int totalChildrenCount_;
	
	public TreeParentBase (String name)
	{
		super (name);
		
		reset ();
		
		totalChildrenCount_ = 0;
	}

	public TreeParentBase ()
	{
		super ("");

		reset ();
	}
	
	public void reset ()
	{
		children = new HashMap ();
	}
	
	public String getName() 
	{
		String result = name;
		
		if (hasChildren() && (matchTagOn() || totalCountOn ()))
		{
			int count = 0; 
			
			if (matchTagOn ())
			{
				count = children.size();
			}
			else if (totalCountOn ())
			{
				count = totalChildrenCount_;
			}
			
			result = result + "(" + count;
			
			if (count == 1)
			{
				result = result + ")"; 
			}
			else
			{
				result = result + ")"; 
			}
		}
			
		return result; 
	}
	
	abstract protected boolean totalCountOn ();
	abstract protected boolean matchTagOn ();
	abstract protected String matchKeywordSingular ();
	abstract protected String matchKeywordPlural ();
	
	
	
	public TreeObjectBase getChild (String childName)
	{
		return (TreeObjectBase) children.get(childName);
	}
	
	public void addChild(TreeObjectBase child) {
		
		if (child instanceof EntryResultTreeNode)
		{
			children.put(String.valueOf(((EntryResultTreeNode)child).getEntityID()), child);
		}
		else
		{
			children.put(child.getName(), child);	
		}
		
		child.setParent(this);
		if (!(child instanceof TreeParentBase))
		{
			computeNumberOfChildrenUp();
		}
	}

	public void removeChild(TreeObjectBase child) {
		children.remove(child.getName());
		child.setParent(null);
	}
	
	public boolean removeLeaf (SearchResultEntryWrapper entry)
	{
		TreeObjectBase child = this.getChild(String.valueOf(entry.getEntry().getEntityID()));
		
		if (child != null)
		{
			removeChild(child);
			
			return true;
		}
		else
		{
			TreeObjectBase [] c = getChildren();

			for (int i = 0; i < c.length; i++)
			{
				if (c[i] instanceof TreeParentBase) 
				{
					if (((TreeParentBase)c[i]).removeLeaf(entry)) return true;
				}
			}
		}
		
		return false;		
	}
	
	
	public TreeObjectBase [] getChildren() 
	{
		Collection c = children.values();
		
		Object[] array = c.toArray(new TreeObjectBase[children.size()]);
		
		return (TreeObjectBase []) array;
	}
	public boolean hasChildren() {
		return children.size()>0;
	}
	
	public int childrenCount ()
	{
		return children.size();
	}

	public int totalChildrenCount ()
	{
		return totalChildrenCount_;
	}

	public void computeNumberOfChildrenUp()
	{		
		Collection c = children.values();
		Iterator i = c.iterator();
		totalChildrenCount_ = 0;
		while (i.hasNext()) 
		{
			TreeObjectBase child = (TreeObjectBase) i.next();
			
			if (child instanceof TreeParentBase)
			{
				totalChildrenCount_ = totalChildrenCount_ + ((TreeParentBase)child).totalChildrenCount();
			}
			else
			{
				totalChildrenCount_++;
			}
		}
		if (parent != null) 
		{
			parent.computeNumberOfChildrenUp();
		}
	}
	
	public void computeNumberOfChildrenDown()
	{		
		Collection c = children.values();
		Iterator i = c.iterator();
		totalChildrenCount_ = 0;
		while (i.hasNext()) 
		{
			TreeObjectBase child = (TreeObjectBase) i.next();
			
			if (child instanceof TreeParentBase)
			{			
				((TreeParentBase)child).computeNumberOfChildrenDown();
				totalChildrenCount_ = totalChildrenCount_ + ((TreeParentBase)child).totalChildrenCount();
			}
			else
			{
				totalChildrenCount_++;
			}
		}
	}
	
}
