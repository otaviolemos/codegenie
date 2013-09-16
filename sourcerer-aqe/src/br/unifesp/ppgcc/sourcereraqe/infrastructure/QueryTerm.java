package br.unifesp.ppgcc.sourcereraqe.infrastructure;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class QueryTerm {

	private List<String> expandedTerms = new ArrayList<String>();
	private List<String> expandedTermsNot = new ArrayList<String>();

	public QueryTerm(String term) {
		
		term = StringUtils.replace(term, "[", "\\[");
		term = StringUtils.replace(term, "]", "\\]");
		term = term.replace(',', ' ');
		
		expandedTerms.add(term);
	}

	public List<String> getExpandedTerms() {
		return expandedTerms;
	}

	public void setExpandedTerms(List<String> expandedTerms) {
		this.expandedTerms = expandedTerms;
	}
	
	public List<String> getExpandedTermsNot() {
		return expandedTermsNot;
	}
	
	public void setExpandedTermsNot(List<String> expandedTermsNot) {
		this.expandedTermsNot = expandedTermsNot;
	}
}
