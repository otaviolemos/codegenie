package br.unifesp.ppgcc.aqexperiment.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import edu.uci.ics.sourcerer.services.search.adapter.SingleResult;


@Entity
@Table(name = "analise_function_response")
public class AnaliseFunctionResponse {
	
	@Id
	@GeneratedValue
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date executionTimestamp;

	private String methodName;
	
	//adding additional required fields:
	private String returnType;
	private String params;
	

	private Double recall;
	
	@Column(name="precis")
	private Double precision;

	@ManyToOne
	@JoinColumn(name = "surveyResponse")
	private SurveyResponse surveyResponse;
	
	private Integer totalRelevants;
	
	private Integer totalResults;
	
	private Integer totalIntersections;

//	@OneToMany
//	@JoinColumn(name = "analiseFunctionResponse")
	@Transient
	private List<SolrResult> results = new ArrayList<SolrResult>();
	
	public AnaliseFunctionResponse(){
	}
	
	public AnaliseFunctionResponse(String methodName, String returnType, String params, SurveyResponse surveyResponse, Date executionTimestamp) {
		this.executionTimestamp = executionTimestamp;
		this.methodName = methodName;
		this.surveyResponse = surveyResponse;
		// adding return type and params from subject
		this.returnType = returnType;
		this.params = params;
	}
	
	public void setResultsFromSingleResult(List<SingleResult> relevants) {
		this.results = new ArrayList<SolrResult>();
		for(SingleResult singleResult : relevants)
			this.results.add(new SolrResult(singleResult));
	}

	//Accessors
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Double getRecall() {
		return recall;
	}

	public void setRecall(Double recall) {
		this.recall = recall;
	}

	public Double getPrecision() {
		return precision;
	}

	public void setPrecision(Double precision) {
		this.precision = precision;
	}

	public SurveyResponse getSurveyResponse() {
		return surveyResponse;
	}

	public void setSurveyResponse(SurveyResponse surveyResponse) {
		this.surveyResponse = surveyResponse;
	}

	public List<SolrResult> getResults() {
		return results;
	}

	public void setResults(List<SolrResult> results) {
		this.results = results;
	}

	public Date getExecutionTimestamp() {
		return executionTimestamp;
	}

	public void setExecutionTimestamp(Date executionTimestamp) {
		this.executionTimestamp = executionTimestamp;
	}

	public Integer getTotalResults() {
		return totalResults;
	}

	public void setTotalResults(Integer totalResults) {
		this.totalResults = totalResults;
	}

	public Integer getTotalRelevants() {
		return totalRelevants;
	}

	public void setTotalRelevants(Integer totalRelevants) {
		this.totalRelevants = totalRelevants;
	}

	public Integer getTotalIntersections() {
		return totalIntersections;
	}

	public void setTotalIntersections(Integer totalIntersections) {
		this.totalIntersections = totalIntersections;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}
	
}
