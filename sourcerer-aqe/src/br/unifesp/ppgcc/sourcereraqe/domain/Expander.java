package br.unifesp.ppgcc.sourcereraqe.domain;

import br.unifesp.ppgcc.sourcereraqe.infrastructure.QueryTerm;

public abstract class Expander {

	public static final String WORDNET_EXPANDER = "WordNet";
	public static final String CODE_VOCABULARY_EXPANDER = "CodeVocabulary";
	public static final String TYPE_EXPANDER = "Type";
	public static final String CODE_EXCHANGE_EXPANDER = "CodeExchange";
	public static final String TLL_EXPANDER = "TLL";
	
	private String name;
	private boolean isMethodNameExpander;
	private boolean isParamExpander;
	private boolean isReturnExpander;
	
	public abstract void expandTerm(QueryTerm queryTerm) throws Exception;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMethodNameExpander(boolean isMethodNameExpander) {
		this.isMethodNameExpander = isMethodNameExpander;
	}

	public void setParamExpander(boolean isParamExpander) {
		this.isParamExpander = isParamExpander;
	}

	public void setReturnExpander(boolean isReturnExpander) {
		this.isReturnExpander = isReturnExpander;
	}

	public boolean isMethodNameExpander() {
		return isMethodNameExpander;
	}

	public boolean isParamExpander() {
		return isParamExpander;
	}

	public boolean isReturnExpander() {
		return isReturnExpander;
	}
}
