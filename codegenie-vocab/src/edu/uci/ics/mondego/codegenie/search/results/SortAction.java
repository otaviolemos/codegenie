/*
 * Created on 29-Aug-2006
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
import org.eclipse.jface.action.Action;
import org.eclipse.swt.custom.BusyIndicator;




public class SortAction extends Action {
	private int fSortOrder;
	private TDSearchResultPage fPage;
	
	public SortAction(String label, TDSearchResultPage page, int sortOrder) {
		super(label);
		fPage= page;
		fSortOrder= sortOrder;
	}

	public void run() {
		BusyIndicator.showWhile(fPage.getViewer().getControl().getDisplay(), new Runnable() {
			public void run() {
				fPage.setSortOrder(fSortOrder);
			}
		});
	}

	public int getSortOrder() {
		return fSortOrder;
	}
}
