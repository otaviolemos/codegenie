package br.unifesp.ppgcc.aqesearchtext.domain;

import java.io.File;

public class SearchResult {
	
	private SearchTerm searchTerm;
	private String term;
	private File file;
	private String line;
	private int lineNumber;

	public SearchResult(SearchTerm searchTerm, String term, File file, String line, int lineNumber){
		this.searchTerm = searchTerm;
		this.term = term;
		this.file = file;
		this.line = line;
		this.lineNumber = lineNumber;
	}
	
	public SearchTerm getSearchTerm() {
		return searchTerm;
	}
	public void setSearchTerm(SearchTerm searchTerm) {
		this.searchTerm = searchTerm;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
}
