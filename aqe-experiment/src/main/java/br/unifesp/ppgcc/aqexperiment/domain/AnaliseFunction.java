package br.unifesp.ppgcc.aqexperiment.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import edu.uci.ics.sourcerer.services.search.adapter.SingleResult;


@Entity
@Table(name = "analise_function")
public class AnaliseFunction {

	@Id
	@GeneratedValue
	private Long id;

	private String description;
	private int number;
	private int surveyNumber;
	private int occurences;
	private Long[] relevantsSolrIds;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "analiseFunction")
	private List<SolrResult> relevants = new ArrayList<SolrResult>();

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "analiseFunction")
	private List<AnaliseFunctionResponse> responses = new ArrayList<AnaliseFunctionResponse>();

	public AnaliseFunction(){
	}

	public AnaliseFunction(String description, int number, int surveyNumber, int occurences, Long[] relevantsSolrIds) {
		this.description = description;
		this.number = number;
		this.surveyNumber = surveyNumber;
		this.occurences = occurences;
		this.relevantsSolrIds = relevantsSolrIds;
	}
	
	public void addResponse(SurveyResponse surveyResponse, Date executionTimestamp) {
		
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
			//methodName = surveyResponse.getMethodName15();
			methodName = surveyResponse.getMethodName16();
		else if(this.getNumber() == 20)
			//methodName = surveyResponse.getMethodName16();
			methodName = surveyResponse.getMethodName17();
		else if(this.getNumber() == 16)
			//methodName = surveyResponse.getMethodName17();
			methodName = surveyResponse.getMethodName18();
		else if(this.getNumber() == 11)
			//methodName = surveyResponse.getMethodName18();
			methodName = surveyResponse.getMethodName19();
		else if(this.getNumber() == 1)
			//methodName = surveyResponse.getMethodName19();
			methodName = surveyResponse.getMethodName20();
		else if(this.getNumber() == 13)
			methodName = surveyResponse.getMethodName20();
		else if(this.getNumber() == 34)
			methodName = surveyResponse.getMethodName21();
		
		this.responses.add(new AnaliseFunctionResponse(methodName, surveyResponse, executionTimestamp));
	}

	public void setRelevantsFromSingleResult(List<SingleResult> relevants) {
		this.relevants = new ArrayList<SolrResult>();
		for(SingleResult singleResult : relevants)
			this.relevants.add(new SolrResult(singleResult));
	}

	//accessors
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public List<SolrResult> getRelevants() {
		return relevants;
	}
	public void setRelevants(List<SolrResult> relevants) {
		this.relevants = relevants;
	}
	public List<AnaliseFunctionResponse> getResponses() {
		return responses;
	}
	public void setResponses(List<AnaliseFunctionResponse> responses) {
		this.responses = responses;
	}
}
