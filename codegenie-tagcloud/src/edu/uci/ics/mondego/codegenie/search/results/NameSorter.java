/*
 * Created on 30-Aug-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.uci.ics.mondego.codegenie.search.results;

/**
 * @author ricardo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import edu.uci.ics.mondego.codegenie.search.SearchResultEntryWrapper;

public class NameSorter extends ViewerSorter {
	public int compare(Viewer viewer, Object e1, Object e2) {
		String property1= getProperty(e1);
		String property2= getProperty(e2);
		return collator.compare(property1, property2);
	}

	protected String getProperty(Object element) {
		if (element instanceof SearchResultEntryWrapper) 
			return ((SearchResultEntryWrapper)element).getEntry().getEntityName();
		return ""; //$NON-NLS-1$
	}
}

