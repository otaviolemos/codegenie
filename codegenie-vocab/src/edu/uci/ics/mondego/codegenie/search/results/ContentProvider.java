/*
 * Created on 30-Aug-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.uci.ics.mondego.codegenie.search.results;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import edu.uci.ics.mondego.codegenie.search.TestDrivenSearchResult;


/**
 * @author ricardo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class ContentProvider implements IStructuredContentProvider {

	
	protected final Object[] EMPTY_ARR= new Object[0];
	protected TestDrivenSearchResult fResult;
	protected TDSearchResultPage fPage;
	
	public ContentProvider (TDSearchResultPage page)
	{
		this.fPage = page;
	}
	
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		initialize((TestDrivenSearchResult) newInput);
	}
	
	protected void initialize(TestDrivenSearchResult result) {
		fResult= result;
	}
		
	public void dispose() {
		// nothing to do
	}

	TDSearchResultPage getPage() {
		return fPage;
	}

	public void clear()
	{
		getPage().getViewer().refresh();
	}

	abstract public void elementsChanged(Object[] updatedElements);		
	
	public Object[] getElements(Object inputElement) {
		// TODO Auto-generated method stub
		return null;
	}
}
