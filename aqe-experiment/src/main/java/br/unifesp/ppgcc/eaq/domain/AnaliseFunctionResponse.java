package br.unifesp.ppgcc.eaq.domain;

import java.util.ArrayList;
import java.util.List;

import edu.uci.ics.sourcerer.services.search.adapter.SingleResult;


public class AnaliseFunctionResponse {
	
	private String methodName;
	private Float recall;
	private Float precision;

	private SurveyResponse surveyResponse;
	
	private List<SingleResult> results = new ArrayList<SingleResult>();
	
	public AnaliseFunctionResponse(String methodName, SurveyResponse surveyResponse) {
		this.methodName = methodName;
		this.surveyResponse = surveyResponse;
	}
	
	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Float getRecall() {
		return recall;
	}

	public void setRecall(Float recall) {
		this.recall = recall;
	}

	public Float getPrecision() {
		return precision;
	}

	public void setPrecision(Float precision) {
		this.precision = precision;
	}

	public SurveyResponse getSurveyResponse() {
		return surveyResponse;
	}

	public void setSurveyResponse(SurveyResponse surveyResponse) {
		this.surveyResponse = surveyResponse;
	}

	public List<SingleResult> getResults() {
		return results;
	}

	public void setResults(List<SingleResult> results) {
		this.results = results;
	}
}
