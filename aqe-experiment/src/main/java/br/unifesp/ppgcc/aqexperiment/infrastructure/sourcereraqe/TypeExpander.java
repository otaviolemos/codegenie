package br.unifesp.ppgcc.aqexperiment.infrastructure.sourcereraqe;

import org.apache.commons.lang.StringUtils;

public class TypeExpander extends Expander {

	public TypeExpander() {
		super.setName(TYPE_EXPANDER);
		super.setMethodNameExpander(false);
		super.setParamExpander(true);
		super.setReturnExpander(false);
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

		//Float types
		else if ("double".equalsIgnoreCase(term))
			this.expandTerm(queryTerm, new String[]{"float","long","int","Integer","short","byte"});
		else if ("float".equalsIgnoreCase(term))
			this.expandTerm(queryTerm, new String[]{"long","int","Integer","short","byte"});
		
		//Integer types
		else if ("long".equalsIgnoreCase(term))
			this.expandTerm(queryTerm, new String[]{"int","Integer","short","byte"});
		else if ("int".equalsIgnoreCase(term) || "Integer".equalsIgnoreCase(term))
			this.expandTerm(queryTerm, new String[]{"short","byte"});
		else if ("short".equalsIgnoreCase(term))
			this.expandTerm(queryTerm, new String[]{"byte"});

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
			this.expandTerm(queryTerm, new String[]{"SortedMap","Hashtable","LinkedHashMap","TreeMap"});
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
