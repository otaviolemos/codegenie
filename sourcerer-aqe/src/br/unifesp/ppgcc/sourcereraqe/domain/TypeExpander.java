package br.unifesp.ppgcc.sourcereraqe.domain;

import org.apache.commons.lang.StringUtils;

import br.unifesp.ppgcc.sourcereraqe.infrastructure.QueryTerm;

public class TypeExpander extends Expander {

	public TypeExpander() {
		super.setName(TYPE_EXPANDER);
		super.setMethodNameExpander(false);
		super.setParamExpander(true);
		super.setReturnExpander(true);
	}
	
	public void expandTerm(QueryTerm queryTerm){
		
		if(StringUtils.isBlank(queryTerm.getExpandedTerms().get(0)))
			return;
		
		//Reference: page 3 from http://www.cse.ohio-state.edu/~rountev/421/lectures/lecture02.pdf 
		String term = queryTerm.getExpandedTerms().get(0);

		//Primitive Casts
		if("char".equalsIgnoreCase(term))
			this.expandTerm(queryTerm, new String[]{"Character"});
		else if("Character".equalsIgnoreCase(term))
			this.expandTerm(queryTerm, new String[]{"char"});
		if("int".equalsIgnoreCase(term))
			this.expandTerm(queryTerm, new String[]{"Integer"});
		else if("Integer".equalsIgnoreCase(term))
			this.expandTerm(queryTerm, new String[]{"int"});

		//Numeric types
		if (StringUtils.indexOf(",double,float,long,int,Integer,short,byte,", ","+term+",") > -1)
			this.expandTerm(queryTerm, new String[]{"double","float","long","int","Integer","short","byte"});
		
		//Generic types
		else if (useGeneric(term))
			this.expandTerm(queryTerm, new String[]{this.removeGeneric(term)});
		
		//Colletcion Types
		this.expandCollection(queryTerm, term);
		if(useGeneric(term))
			this.expandCollection(queryTerm, this.removeGeneric(term));

	}

	private void expandCollection(QueryTerm queryTerm, String term){
		//Reference: http://sammy-woo.blogspot.com.br/2011/07/java-collection-hierarchy.html
		if ("Collection".equalsIgnoreCase(term))
			this.expandTerm(queryTerm, new String[]{"Set","List","Queue","SortedSet","HashSet","LinkedHashSet","TreeSet","ArrayList","Vector","LinkedList","PriorityQueue"});
		else if ("Set".equalsIgnoreCase(term))
			this.expandTerm(queryTerm, new String[]{"SortedSet","HashSet","LinkedHashSet"});
		else if ("SortedSet".equalsIgnoreCase(term))
			this.expandTerm(queryTerm, new String[]{"TreeSet"});
		else if ("List".equalsIgnoreCase(term))
			this.expandTerm(queryTerm, new String[]{"ArrayList","Vector","LinkedList"});
		else if ("Queue".equalsIgnoreCase(term))
			this.expandTerm(queryTerm, new String[]{"LinkedList","PriorityQueue"});
		else if ("Map".equalsIgnoreCase(term))
			this.expandTerm(queryTerm, new String[]{"SortedMap","Hashtable","LinkedHashMap","HashMap","TreeMap"});
		else if ("SortedMap".equalsIgnoreCase(term))
			this.expandTerm(queryTerm, new String[]{"TreeMap"});
	}
	
	private boolean useGeneric(String term) {
		int open = StringUtils.indexOf(term, "<");
		int close = StringUtils.indexOf(term, ">");
		return (open + 1) < close;
	}

	private String removeGeneric(String term){
		term = StringUtils.substring(term, 0, StringUtils.indexOf(term, "<"));
		term = StringUtils.trim(term);
		return term;
	}
	
	private void expandTerm(QueryTerm queryTerm, String[] expandedTerms) {
		for(String term : expandedTerms)
			queryTerm.getExpandedTerms().add(term);
	}
}
