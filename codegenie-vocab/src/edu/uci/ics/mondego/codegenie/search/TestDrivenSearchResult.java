package edu.uci.ics.mondego.codegenie.search;

import org.eclipse.core.resources.IFile;
import java.io.Serializable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.text.AbstractTextSearchResult;
import org.eclipse.search.ui.text.IEditorMatchAdapter;
import org.eclipse.search.ui.text.IFileMatchAdapter;
import org.eclipse.search.ui.text.Match;
import org.eclipse.ui.IEditorPart;

public class TestDrivenSearchResult extends AbstractTextSearchResult implements
		IEditorMatchAdapter, IFileMatchAdapter, Serializable {

	private TestDrivenSearchQuery fQuery;
	protected String queryLabel;
	
	public boolean isShownInEditor(Match match, IEditorPart editor) {
		return false;
	}
	
	public TestDrivenSearchResult(TestDrivenSearchQuery query) {
		fQuery = query;
		queryLabel = fQuery.getLabel();
	}
	
	public Match[] computeContainedMatches(AbstractTextSearchResult result,
			IEditorPart editor) {
		return null;
	}

	public Match[] computeContainedMatches(AbstractTextSearchResult result,
			IFile file) {
		return null;
	}	
	
	public IFile getFile(Object element) {
		return null;
	}
	
	public ISearchQuery getQuery() {
		return fQuery;
	}
	
	public TestDrivenSearchQuery getSearchQuery() {
		return fQuery;
	}
	
	
	public IEditorMatchAdapter getEditorMatchAdapter() {
		return null;
	}

	public IFileMatchAdapter getFileMatchAdapter() {
		return null;
	}

	public String getLabel() {
		return queryLabel;
	}
	
	public void setQueryLabel(String label) {
	  queryLabel = label;
	}

	public String getTooltip() {
		return "";
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}


}
