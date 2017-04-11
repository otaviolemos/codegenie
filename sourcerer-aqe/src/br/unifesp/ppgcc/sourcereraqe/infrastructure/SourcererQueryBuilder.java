package br.unifesp.ppgcc.sourcereraqe.infrastructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import br.unifesp.ppgcc.sourcereraqe.domain.Expander;

public class SourcererQueryBuilder {

	private AQEApproach aqeApproach;

	public SourcererQueryBuilder(String relatedWordsServiceUrl, String expanders, boolean relaxReturn, boolean relaxParams, boolean contextRelevants, boolean filterMethodNameTermsByParameter, boolean reduceMethodNameTerms) throws Exception {
		aqeApproach = new AQEApproach(relatedWordsServiceUrl, expanders, relaxReturn, relaxParams, contextRelevants, filterMethodNameTermsByParameter, reduceMethodNameTerms);
	}

	public SourcererQueryBuilder() throws Exception {
		aqeApproach = new AQEApproach();
	}

	public String getSourcererExpandedQuery(String methodName, String returnType, String params) throws Exception {

		List<QueryTerm> methodNameTerms = this.getMethodNameTerms(methodName);
		List<QueryTerm> returnTypeTerms = this.getReturnTypeTerms(returnType);
		List<QueryTerm> paramsTerms = this.getParamsTerms(params);

		//Filter method names by parameter
		if(aqeApproach.isFilterMethodNameTermsByParameter())
			methodNameTerms = this.getFilteredMethodNameTermsByParameter(methodNameTerms, paramsTerms);

		//Reduce number of terms
		if(aqeApproach.reduceMethodNameTerms()) 
			methodNameTerms = this.getReducedMethodNameTerms(methodNameTerms);
		
		// EAQ
		for (Expander expander : aqeApproach.getExpanders()) {
			if (expander.isMethodNameExpander())
				for(QueryTerm queryTerm : methodNameTerms)
					expander.expandTerm(queryTerm);

			if (expander.isReturnExpander())
				for(QueryTerm queryTerm : returnTypeTerms)
					expander.expandTerm(queryTerm);

			if (expander.isParamExpander())
				for(QueryTerm queryTerm : paramsTerms)
					expander.expandTerm(queryTerm);
		}

		this.prioritizeOrininalTerms(methodNameTerms);
		String methodPart = this.getMethodNamePart(methodNameTerms);
		String returnTypePart = (aqeApproach.isRelaxReturn() ? "" : this.getReturnTypePart(returnTypeTerms));
		String paramsPart = (aqeApproach.isRelaxParams() ? "" : this.getParamsPart(paramsTerms));

		return (methodPart + returnTypePart + paramsPart).replaceAll("\\[\\]", "\\\\[\\\\]");
	}

	private void prioritizeOrininalTerms(List<QueryTerm> queryTerms){
		for(QueryTerm queryTerm : queryTerms){
			String originalTerm = queryTerm.getExpandedTerms().get(0);
			queryTerm.getExpandedTerms().set(0, originalTerm + "^10");
		}
	}
	
	private List<QueryTerm> getFilteredMethodNameTermsByParameter(List<QueryTerm> methodNameTerms, List<QueryTerm> paramsTerms){
		if (methodNameTerms.size() <= 1 )
			return methodNameTerms;
		
		List<QueryTerm> filteredMethodNameTermsByParameter = new ArrayList<QueryTerm>();
		for (QueryTerm methodQueryTerm : methodNameTerms) {
			boolean useMethodNameTerm = true;
			for (QueryTerm paramTerm : paramsTerms) {
				if (methodQueryTerm.getExpandedTerms().get(0).equalsIgnoreCase(paramTerm.getExpandedTerms().get(0))){
					useMethodNameTerm = false;
					break;
				}
			}
			if(useMethodNameTerm)
				filteredMethodNameTermsByParameter.add(methodQueryTerm);
		}
		return filteredMethodNameTermsByParameter;
	}
	
	private List<QueryTerm> getReducedMethodNameTerms(List<QueryTerm> methodNameTerms) {
		if (methodNameTerms.size() <= 1 )
			return methodNameTerms;
		
		List<QueryTerm> reducedNameTerms = new ArrayList<QueryTerm>(methodNameTerms);
		
		int numOfTermsToBeRemoved = methodNameTerms.size() - aqeApproach.getDesiredNumberOfMethodTerms();
		
		if(numOfTermsToBeRemoved > 0) {
			Map<Integer, Integer> sizeMap = getMapOfTerms(methodNameTerms);
			Iterator<Integer> iter = sizeMap.keySet().iterator();
			int next = iter.next();
			for(int i = 0; i < numOfTermsToBeRemoved; i++) {
				reducedNameTerms.remove(methodNameTerms.get(next));
				if(iter.hasNext())
					next = iter.next();
			}
		}
		return reducedNameTerms;
	}
	
	public LinkedHashMap<Integer, Integer> sortHashMapByValues(
	        HashMap<Integer, Integer> passedMap) {
	    List<Integer> mapKeys = new ArrayList<>(passedMap.keySet());
	    List<Integer> mapValues = new ArrayList<>(passedMap.values());
	    Collections.sort(mapValues);
	    Collections.sort(mapKeys);

	    LinkedHashMap<Integer, Integer> sortedMap =
	        new LinkedHashMap<>();

	    Iterator<Integer> valueIt = mapValues.iterator();
	    while (valueIt.hasNext()) {
	        Integer val = valueIt.next();
	        Iterator<Integer> keyIt = mapKeys.iterator();

	        while (keyIt.hasNext()) {
	            Integer key = keyIt.next();
	            Integer comp1 = passedMap.get(key);
	            Integer comp2 = val;

	            if (comp1.equals(comp2)) {
	                keyIt.remove();
	                sortedMap.put(key, val);
	                break;
	            }
	        }
	    }
	    return sortedMap;
	}
	
	private Map<Integer, Integer> getMapOfTerms(List<QueryTerm> methodNameTerms) {
		HashMap<Integer,Integer> sizeMap = new HashMap<Integer, Integer>();
		for(int i = 0; i < methodNameTerms.size(); i++)  
			sizeMap.put(i, methodNameTerms.get(i).getExpandedTerms().get(0).length());
		
		sizeMap = sortHashMapByValues(sizeMap);
		return sizeMap;
	}

	private List<QueryTerm> getMethodNameTerms(String methodName){
		String names = JavaTermExtractor.getFQNTermsAsString(methodName);
		names = JavaTermExtractor.removeDuplicates(names);
		String[] strTerms = StringUtils.split(names, " ");
		
		List<QueryTerm> terms = new ArrayList<QueryTerm>();
		
		for(String term : strTerms){
			terms.add(new QueryTerm(term));
		}
		
		return terms;
	}
	
	private List<QueryTerm> getReturnTypeTerms(String returnType){
		
		List<QueryTerm> terms = new ArrayList<QueryTerm>();
		if(!StringUtils.isBlank(returnType))
			terms.add(new QueryTerm(StringUtils.trim(returnType)));
		
		return terms;
	}
	
	private List<QueryTerm> getParamsTerms(String params){
		String[] strTerms = StringUtils.split(StringUtils.trim(params),  ",");
		
		List<QueryTerm> terms = new ArrayList<QueryTerm>();
		
		for(String term : strTerms){
			terms.add(new QueryTerm(StringUtils.trim(term)));
		}
		
		return terms;
	}
	
	private String getMethodNamePart(List<QueryTerm> methodTerms) throws Exception {

		if(methodTerms.size() == 0)
			return "";

		String query = "";
		query += "sname_contents:(" + this.getSourcererQueryPart(methodTerms) + ")";
		
		return query;
	}

	private String getReturnTypePart(List<QueryTerm> returnTypeTerms) {

		if(returnTypeTerms.size() == 0)
			return "";

		String query = "";
		query += "\nreturn_sname_contents:(" + this.getSourcererQueryPart(returnTypeTerms) + ")";
		
		return query;
	}

	private String getParamsPart(List<QueryTerm> paramsTerms) {

		boolean sourcererLibBug = paramsTerms.size() == 1 && ")".equals(paramsTerms.get(0)); // entityId in ( 5842071 , 5877324 )
		if (paramsTerms.size() == 0 || sourcererLibBug)
			return "";

		String query = "";
		query += "\nparam_count:" + paramsTerms.size();
		query += "\nparams_snames_contents:(" + this.getSourcererQueryPart(paramsTerms) + ")";

		return query;
	}
	
	private String getSourcererQueryPart(List<QueryTerm> queryTerms) {
		String query = "";
		boolean firstTerm = true;
		for(QueryTerm queryTerm : queryTerms){

			if(!firstTerm)
				query += " AND ";
			else
				firstTerm = false;
			
			boolean firstExpanded = true;
			for(String expandedTerm : queryTerm.getExpandedTerms()){
				if(firstExpanded){
					query += "( " + expandedTerm;
					firstExpanded = false;
				} else
					query += " OR " + expandedTerm;
			}
			if(queryTerm.getExpandedTerms().size() > 0)
				query += " )";
			

			firstExpanded = true;
			for(String expandedTerm : queryTerm.getExpandedTermsNot()){
				if(firstExpanded){
					query += " AND !( " + expandedTerm;
					firstExpanded = false;
				} else
					query += " OR " + expandedTerm;
			}
			if(queryTerm.getExpandedTermsNot().size() > 0)
				query += " )";
		}

		return query;
	}

}
