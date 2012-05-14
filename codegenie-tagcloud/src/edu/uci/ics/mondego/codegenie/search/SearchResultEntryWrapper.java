package edu.uci.ics.mondego.codegenie.search;

import edu.uci.ics.mondego.search.model.SearchResultEntry;
import edu.uci.ics.mondego.codegenie.testing.TestResult;

public class SearchResultEntryWrapper {
	
	private SearchResultEntry entry;
	private TestResult testResult = null;
	private boolean woven = false;
	private boolean problemRetrieving = false;
	
	public boolean isWoven() {
		return woven;
	}

	public void setWoven(boolean woven) {
		this.woven = woven;
	}

	public SearchResultEntryWrapper(SearchResultEntry e) {
		entry = e;
	}

	public SearchResultEntry getEntry() {
		return entry;
	}

	public void setEntry(SearchResultEntry entry) {
		this.entry = entry;
	}

	public TestResult getTestResult() {
		return testResult;
	}

	public void setTestResult(TestResult testResult) {
		this.testResult = testResult;
	}

	public boolean hadProblemsRetrieving() {
		return problemRetrieving;
	}

	public void setProblemRetrieving(boolean problemRetrieving) {
		this.problemRetrieving = problemRetrieving;
	}
	
	

}
