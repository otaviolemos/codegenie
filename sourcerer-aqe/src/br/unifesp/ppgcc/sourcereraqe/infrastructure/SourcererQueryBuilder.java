package br.unifesp.ppgcc.sourcereraqe.infrastructure;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.unifesp.ppgcc.sourcereraqe.domain.Expander;

public class SourcererQueryBuilder {

	private AQEApproach aqeApproach;

	public SourcererQueryBuilder(String relatedWordsServiceUrl, String expanders, boolean relaxReturn, boolean relaxParams, boolean contextRelevants, boolean filterMethodNameTermsByParameter) throws Exception {
		aqeApproach = new AQEApproach(relatedWordsServiceUrl, expanders, relaxReturn, relaxParams, contextRelevants, filterMethodNameTermsByParameter);
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

		String methodPart = this.getMethodNamePart(methodNameTerms);
		String returnTypePart = (aqeApproach.isRelaxReturn() ? "" : this.getReturnTypePart(returnTypeTerms));
		String paramsPart = (aqeApproach.isRelaxParams() ? "" : this.getParamsPart(paramsTerms));

		return methodPart + returnTypePart + paramsPart;
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
