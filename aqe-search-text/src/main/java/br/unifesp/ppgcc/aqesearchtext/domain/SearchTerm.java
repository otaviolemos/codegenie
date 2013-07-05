package br.unifesp.ppgcc.eaq.domain;


public class SearchTerm {

	private String name;
	private String description;
	private String[] orTerms;

	public SearchTerm(String name, String description, String[] orTerms){
		this.name = name;
		this.description = description;
		this.orTerms = orTerms;
	}
	
	public SearchTerm(String name, String description, String term){
		this(name, description, new String[]{term});
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String[] getOrTerms() {
		return orTerms;
	}

	public void setOrTerms(String[] orTerms) {
		this.orTerms = orTerms;
	}
	
}
