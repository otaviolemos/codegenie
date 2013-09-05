package br.unifesp.ppgcc.aqexperiment.domain;

import java.util.ArrayList;
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

	public AnaliseFunction() {
	}

	public AnaliseFunction(int surveyReponseNumber, String description, int number, int occurences, Long[] relevantsSolrIds) {
		this.surveyNumber = surveyReponseNumber;
		this.description = description;
		this.number = number;
		this.occurences = occurences;
		this.relevantsSolrIds = relevantsSolrIds;
	}

	public void addResponse(SurveyResponse surveyResponse, Execution execution) {

		String methodName = null;
		String returnType = null;
		String params = null;

		int surveyResponseNumber = this.getSurveyNumber();
		switch (surveyResponseNumber) {
		case 1:
			returnType = surveyResponse.getReturn1();
			methodName = surveyResponse.getMethodName1();
			params = surveyResponse.getParams1();
			break;
		case 2:
			returnType = surveyResponse.getReturn2();
			methodName = surveyResponse.getMethodName2();
			params = surveyResponse.getParams2();
			break;
		case 3:
			returnType = surveyResponse.getReturn3();
			methodName = surveyResponse.getMethodName3();
			params = surveyResponse.getParams3();
			break;
		case 4:
			returnType = surveyResponse.getReturn4();
			methodName = surveyResponse.getMethodName4();
			params = surveyResponse.getParams4();
			break;
		case 5:
			returnType = surveyResponse.getReturn5();
			methodName = surveyResponse.getMethodName5();
			params = surveyResponse.getParams5();
			break;
		case 6:
			returnType = surveyResponse.getReturn6();
			methodName = surveyResponse.getMethodName6();
			params = surveyResponse.getParams6();
			break;
		case 7:
			returnType = surveyResponse.getReturn7();
			methodName = surveyResponse.getMethodName7();
			params = surveyResponse.getParams7();
			break;
		case 8:
			returnType = surveyResponse.getReturn8();
			methodName = surveyResponse.getMethodName8();
			params = surveyResponse.getParams8();
			break;
		case 9:
			returnType = surveyResponse.getReturn9();
			methodName = surveyResponse.getMethodName9();
			params = surveyResponse.getParams9();
			break;
		case 10:
			returnType = surveyResponse.getReturn10();
			methodName = surveyResponse.getMethodName10();
			params = surveyResponse.getParams10();
			break;
		case 11:
			returnType = surveyResponse.getReturn11();
			methodName = surveyResponse.getMethodName11();
			params = surveyResponse.getParams11();
			break;
		case 12:
			returnType = surveyResponse.getReturn12();
			methodName = surveyResponse.getMethodName12();
			params = surveyResponse.getParams12();
			break;
		case 13:
			returnType = surveyResponse.getReturn13();
			methodName = surveyResponse.getMethodName13();
			params = surveyResponse.getParams13();
			break;
		case 14:
			returnType = surveyResponse.getReturn14();
			methodName = surveyResponse.getMethodName14();
			params = surveyResponse.getParams14();
			break;
		case 15:
			returnType = surveyResponse.getReturn15();
			methodName = surveyResponse.getMethodName15();
			params = surveyResponse.getParams15();
			break;
		case 16:
			returnType = surveyResponse.getReturn16();
			methodName = surveyResponse.getMethodName16();
			params = surveyResponse.getParams16();
			break;
		case 17:
			returnType = surveyResponse.getReturn17();
			methodName = surveyResponse.getMethodName17();
			params = surveyResponse.getParams17();
			break;
		case 18:
			returnType = surveyResponse.getReturn18();
			methodName = surveyResponse.getMethodName18();
			params = surveyResponse.getParams18();
			break;
		case 19:
			returnType = surveyResponse.getReturn19();
			methodName = surveyResponse.getMethodName19();
			params = surveyResponse.getParams19();
			break;
		case 20:
			returnType = surveyResponse.getReturn20();
			methodName = surveyResponse.getMethodName20();
			params = surveyResponse.getParams20();
			break;
		case 21:
			returnType = surveyResponse.getReturn21();
			methodName = surveyResponse.getMethodName21();
			params = surveyResponse.getParams21();
		}

		this.responses.add(new AnaliseFunctionResponse(methodName, returnType, params, surveyResponse, execution));
	}

	public void setRelevantsFromSingleResult(List<SingleResult> relevants) {
		this.relevants = new ArrayList<SolrResult>();
		for (SingleResult singleResult : relevants)
			this.relevants.add(new SolrResult(singleResult));
	}

	// accessors
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
