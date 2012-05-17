/*
 * Created on 24-Aug-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.uci.ics.mondego.codegenie.search.results;

import org.eclipse.jface.viewers.TableViewer;
import edu.uci.ics.mondego.codegenie.search.TestDrivenSearchResult;
import java.util.HashSet;
import java.util.Set;


/**
 * @author ricardo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TableContentProvider extends ContentProvider  {

	TableContentProvider(TDSearchResultPage page) {
		super (page);
	}
	
	public void elementsChanged(Object[] updatedElements) {
		if (fResult == null)
			return;
		int addCount= 0;
		int removeCount= 0;
		TableViewer viewer = (TableViewer) getPage().getViewer();
		Set<Object> updated= new HashSet<Object>();
		Set<Object> added= new HashSet<Object>();
		Set<Object> removed= new HashSet<Object>();
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
		
		viewer.add(added.toArray());
		viewer.update(updated.toArray(), null);
		viewer.remove(removed.toArray());
	}

	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof TestDrivenSearchResult) {
			Set<Object> filteredElements= new HashSet<Object>();
			Object[] rawElements= ((TestDrivenSearchResult)inputElement).getElements();
			for (int i= 0; i < rawElements.length; i++) {
				if (getPage().getDisplayedMatchCount(rawElements[i]) > 0)
					filteredElements.add(rawElements[i]);
			}
			return filteredElements.toArray();
		}
		return EMPTY_ARR;
	}

}
