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



public class GroupAction extends Action {
	private int fGrouping;
	private TDSearchResultPage fPage;
	
	public GroupAction(String label, String tooltip, TDSearchResultPage page, int grouping) {
		super(label);
		setToolTipText(tooltip);
		fPage= page;
		fGrouping= grouping;
	}

	public void run() {
		fPage.setGrouping(fGrouping);
	}

	public int getGrouping() {
		return fGrouping;
	}
}

