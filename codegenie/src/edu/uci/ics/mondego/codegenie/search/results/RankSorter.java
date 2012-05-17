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


public class RankSorter extends ViewerSorter {
		public int compare(Viewer viewer, Object e1, Object e2) {
			double property1= getProperty(e1);
			double property2= getProperty(e2);
			
			if (property1 < property2) return 1;
			else if (property1 > property2) return -1;
			else return 0;
		}

		protected double getProperty(Object element) {
			if (element instanceof SearchResultEntryWrapper)
				return ((SearchResultEntryWrapper)element).getEntry().getRank();
			return 0; //$NON-NLS-1$
		}
}

