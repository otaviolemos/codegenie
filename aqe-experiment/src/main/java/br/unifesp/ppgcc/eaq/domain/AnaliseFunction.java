package br.unifesp.ppgcc.eaq.domain;

import java.util.ArrayList;
import java.util.List;

import edu.uci.ics.sourcerer.services.search.adapter.SingleResult;


public class AnaliseFunction {

	private String description;
	private int number;
	private int surveyNumber;
	private int occurences;
	private Long[] relevantsSolrIds;
	
	private List<SingleResult> relevants = new ArrayList<SingleResult>();
	private List<AnaliseFunctionResponse> responses = new ArrayList<AnaliseFunctionResponse>();

	public AnaliseFunction(String description, int number, int surveyNumber, int occurences, Long[] relevantsSolrIds) {
		this.description = description;
		this.number = number;
		this.surveyNumber = surveyNumber;
		this.occurences = occurences;
		this.relevantsSolrIds = relevantsSolrIds;
	}
	
	public void addResponse(SurveyResponse surveyResponse) {
		
//		if(!"15 + 6".equals(surveyResponse.getQuestoes()) && !"21".equals(surveyResponse.getQuestoes()))
		if(!"15 + 6".equals(surveyResponse.getQuestoes()) && !"21".equals(surveyResponse.getQuestoes()))
			return;
		
		String methodName = null;
		if(this.getNumber() == 18)
			methodName = surveyResponse.getMethodName1();
		else if(this.getNumber() == 23)
			methodName = surveyResponse.getMethodName2();
		else if(this.getNumber() == 21)
			methodName = surveyResponse.getMethodName3();
		else if(this.getNumber() == 19)
			methodName = surveyResponse.getMethodName4();
		else if(this.getNumber() == 32)
			methodName = surveyResponse.getMethodName5();
		else if(this.getNumber() == 35)
			methodName = surveyResponse.getMethodName6();
		else if(this.getNumber() == 9)
			methodName = surveyResponse.getMethodName7();
		else if(this.getNumber() == 10)
			methodName = surveyResponse.getMethodName8();
		else if(this.getNumber() == 14)
			methodName = surveyResponse.getMethodName9();
		else if(this.getNumber() == 22)
			methodName = surveyResponse.getMethodName10();
		else if(this.getNumber() == 24)
			methodName = surveyResponse.getMethodName11();
		else if(this.getNumber() == 30)
			methodName = surveyResponse.getMethodName12();
		else if(this.getNumber() == 17)
			methodName = surveyResponse.getMethodName13();
		else if(this.getNumber() == 5)
			methodName = surveyResponse.getMethodName14();
		else if(this.getNumber() == 33)
			methodName = surveyResponse.getMethodName15();
		else if(this.getNumber() == 20)
			methodName = surveyResponse.getMethodName16();
		else if(this.getNumber() == 16)
			methodName = surveyResponse.getMethodName17();
		else if(this.getNumber() == 11)
			methodName = surveyResponse.getMethodName18();
		else if(this.getNumber() == 1)
			methodName = surveyResponse.getMethodName19();
		else if(this.getNumber() == 13)
			methodName = surveyResponse.getMethodName20();
		else if(this.getNumber() == 34)
			methodName = surveyResponse.getMethodName21();
		
		this.responses.add(new AnaliseFunctionResponse(methodName, surveyResponse));
	}

	//accessors
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getSurveyNumber() {
		return surveyNumber;
	}
	public void setSurveyNumber(int surveyNumber) {
		this.surveyNumber = surveyNumber;
	}
	public int getOccurences() {
		return occurences;
	}
	public void setOccurences(int occurences) {
		this.occurences = occurences;
	}
	public Long[] getRelevantsSolrIds() {
		return relevantsSolrIds;
	}
	public void setRelevantsSolrIds(Long[] relevantsSolrIds) {
		this.relevantsSolrIds = relevantsSolrIds;
	}
	public List<SingleResult> getRelevants() {
		return relevants;
	}
	public void setRelevants(List<SingleResult> relevants) {
		this.relevants = relevants;
	}
	public List<AnaliseFunctionResponse> getResponses() {
		return responses;
	}
	public void setResponses(List<AnaliseFunctionResponse> responses) {
		this.responses = responses;
	}
}
