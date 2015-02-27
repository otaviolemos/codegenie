package br.unifesp.ppgcc.aqexperiment.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.unifesp.ppgcc.aqexperiment.domain.AnaliseFunction;
import br.unifesp.ppgcc.aqexperiment.domain.AnaliseFunctionResponse;
import br.unifesp.ppgcc.aqexperiment.domain.Execution;
import br.unifesp.ppgcc.aqexperiment.domain.SolrResult;
import br.unifesp.ppgcc.aqexperiment.domain.SurveyResponse;
import br.unifesp.ppgcc.aqexperiment.domain.helper.TagCloudMutantQuery;
import br.unifesp.ppgcc.aqexperiment.domain.helper.TagCloudWord;
import br.unifesp.ppgcc.aqexperiment.infrastructure.AnaliseFunctionRepository;
import br.unifesp.ppgcc.aqexperiment.infrastructure.AnaliseFunctionResponseRepository;
import br.unifesp.ppgcc.aqexperiment.infrastructure.ExecutionRepository;
import br.unifesp.ppgcc.aqexperiment.infrastructure.SurveyResponseRepository;
import br.unifesp.ppgcc.aqexperiment.infrastructure.util.ConfigProperties;
import br.unifesp.ppgcc.aqexperiment.infrastructure.util.TagCloudHelper;
import br.unifesp.ppgcc.sourcereraqe.infrastructure.SourcererQueryBuilder;
import edu.uci.ics.sourcerer.services.search.adapter.SearchAdapter;
import edu.uci.ics.sourcerer.services.search.adapter.SearchResult;
import edu.uci.ics.sourcerer.services.search.adapter.SingleResult;

@Service
@Transactional
public class AQEService {

	@Autowired
	private ExecutionRepository executionRepository;

	@Autowired
	private AnaliseFunctionRepository analiseFunctionRepository;

	@Autowired
	private AnaliseFunctionResponseRepository analiseFunctionResponseRepository;

	@Autowired
	private SurveyResponseRepository surveyResponseRepository;
	
	private List<SurveyResponse> surveyResponses;
	private List<AnaliseFunction> analiseFunctions;

	public void execute() throws Exception {

		//Execution
		Execution execution = this.setupExecution();

		for (AnaliseFunction analiseFunction : analiseFunctions) {
			this.process(analiseFunction, execution);
		}
	}

	private Execution setupExecution() throws Exception {
		//Execution
		Execution execution = new Execution(System.currentTimeMillis());
		execution = executionRepository.save(execution);

		// SurveyResponses
		this.persistSurveyResponses(execution);
		surveyResponses = surveyResponseRepository.findAll(execution);

		analiseFunctions = analiseFunctionRepository.findAllValides();
		
		return execution;
	}
	
	private void process(AnaliseFunction analiseFunction, Execution execution) throws Exception {
		this.buildRelevants(analiseFunction);
		this.buildResponses(analiseFunction, execution);
		
		boolean tagCloud = new Boolean(ConfigProperties.getProperty("aqExperiment.tagCloud"));
		if(tagCloud)
			this.processTagCloudResponses(analiseFunction);
		else
			this.processResponses(analiseFunction);

		analiseFunctionRepository.save(analiseFunction);
	}
	
	private void persistSurveyResponses(Execution execution) throws Exception {
		for (SurveyResponse surveyResponse : surveyResponseRepository.findAllFromSheet()) {
			if (!surveyResponse.isValid())
				continue;

			surveyResponse.setExecution(execution);
			surveyResponseRepository.save(surveyResponse);
		}

	}

	private void buildRelevants(AnaliseFunction function) throws Exception {
		SearchAdapter searchAdapter = SearchAdapter.create(ConfigProperties.getProperty("aqExperiment.sourcerer.url"));
		SearchResult searchResult = null;
		for (long entityId : function.getRelevantsSolrIds()) {
			String query = "entity_id:" + entityId;
			searchResult = searchAdapter.search(query);

			if (searchResult.getNumFound() == -1)
				throw new Exception("Unable to perform search: " + query);

			SingleResult singleResult = searchResult.getResults(0, 1).get(0);
			function.getRelevants().add(new SolrResult(singleResult));
		}
	}

	private void buildResponses(AnaliseFunction function, Execution execution) {
		for (SurveyResponse surveyResponse : surveyResponses)
			function.addResponse(surveyResponse, execution);
	}

	private void processResponses(AnaliseFunction function) throws Exception {
		boolean relaxReturn = new Boolean(ConfigProperties.getProperty("aqExperiment.relaxReturn"));
		boolean relaxParams = new Boolean(ConfigProperties.getProperty("aqExperiment.relaxParams"));
		boolean contextRelevants = new Boolean(ConfigProperties.getProperty("aqExperiment.contextRelevants"));
		boolean filterMethodNameTermsByParameter = new Boolean(ConfigProperties.getProperty("aqExperiment.filterMethodNameTermsByParameter"));
		String urlServices = ConfigProperties.getProperty("aqExperiment.related-words-service.url");
		String expanders = ConfigProperties.getProperty("aqExperiment.expanders");
		
		SourcererQueryBuilder sourcererQueryBuilder = new SourcererQueryBuilder(urlServices, expanders, relaxReturn, relaxParams, contextRelevants, filterMethodNameTermsByParameter);
		SearchAdapter searchAdapter = SearchAdapter.create(ConfigProperties.getProperty("aqExperiment.sourcerer.url"));

		for (AnaliseFunctionResponse response : function.getResponses()) {

			this.updateResponseWithSearch(sourcererQueryBuilder, searchAdapter, response, function);
		}
	}

	private void processTagCloudResponses(AnaliseFunction function) throws Exception{
		SourcererQueryBuilder sourcererQueryBuilder = new SourcererQueryBuilder();
		SearchAdapter searchAdapter = SearchAdapter.create(ConfigProperties.getProperty("aqExperiment.sourcerer.url"));

		for (AnaliseFunctionResponse response : function.getResponses()) {

			//Original
			this.updateResponseWithSearch(sourcererQueryBuilder, searchAdapter, response, function);

			if(response.getRecall() > 0)
				continue;
			
			//MutantResponse
			AnaliseFunctionResponse mutantResponse = new AnaliseFunctionResponse();

			List<List<TagCloudWord>> allWordsList = TagCloudHelper.getAllWordsList(response.getMethodName());
			List<TagCloudMutantQuery> tagCloudMutantQueries = TagCloudHelper.getTagCloudMutantQueries(allWordsList);
			for (TagCloudMutantQuery tagCloudMutantQuery : tagCloudMutantQueries) {
				AnaliseFunctionResponse testResponse = new AnaliseFunctionResponse(response, tagCloudMutantQuery);
				this.updateResponseWithSearch(sourcererQueryBuilder, searchAdapter, testResponse, function);
				
				if(testResponse.getRecall() > mutantResponse.getRecall())
					mutantResponse = new AnaliseFunctionResponse(testResponse);
			}
			
			if(mutantResponse.getRecall() > 0){
				response.setOriginalMethodName(response.getMethodName());
				response.setMethodName(mutantResponse.getMethodName());
				response.setFrequenciesRank(mutantResponse.getFrequenciesRank());
				this.updateResponseWithSearch(sourcererQueryBuilder, searchAdapter, response, function);
			}
		}
	}
	
	private void updateResponseWithSearch(SourcererQueryBuilder sourcererQueryBuilder, SearchAdapter searchAdapter, AnaliseFunctionResponse response, AnaliseFunction function) throws Exception {
		SearchResult searchResult = null;
		List<SingleResult> results = new ArrayList<SingleResult>();

		
		String query = sourcererQueryBuilder.getSourcererExpandedQuery(response.getMethodName(), response.getReturnType(), response.getParams());
		
		searchResult = searchAdapter.search(query);
		if (searchResult.getNumFound() == -1)
			throw new Exception("Unable to perform search: " + query);

		
		response.setSourcererQuery(query);
		results.addAll(searchResult.getResults(0, 35));
		response.setResultsFromSingleResult(results);

		this.calculateRecallAndPrecision(response, function);
	}
	
	private void calculateRecallAndPrecision(AnaliseFunctionResponse response, AnaliseFunction function) throws Exception {

		List<SolrResult> contextRelevants = this.getContextRelevants(response, function);
		
		int totalContextRelevants = contextRelevants.size();
		int totalResults = response.getResults().size();
		int totalIntersections = 0;

		for (SolrResult relevant : contextRelevants) {
			if (response.getResults().contains(relevant))
				totalIntersections++;
		}

		double recall = totalContextRelevants == 0 ? 0 : new Double(totalIntersections) / new Double(totalContextRelevants);
		double precision = totalResults == 0 ? 0 : new Double(totalIntersections) / new Double(totalResults);

		response.setRecall(recall);
		response.setPrecision(precision);
		response.setTotalRelevants(totalContextRelevants);
		response.setTotalResults(totalResults);
		response.setTotalIntersections(totalIntersections);
	}


	private List<SolrResult> getContextRelevants(AnaliseFunctionResponse response, AnaliseFunction function) throws Exception {
		
		//Check if use or not this method
		boolean enable = new Boolean(ConfigProperties.getProperty("aqExperiment.contextRelevants"));
		if(!enable)
			return function.getRelevants();
		
		List<SolrResult> contextRelevants = new ArrayList<SolrResult>();

		for(SolrResult solrResult : function.getRelevants()){
			
			//discard rules
			if(solrResult.isVoidReturn() && !response.isVoidReturn())
				continue;
			if(solrResult.getParamCount() != response.getTotalParams())
				continue;
			
			contextRelevants.add(solrResult);
		}
		
		return contextRelevants;
	}

	public AnaliseFunctionResponse getAnaliseFunctionResponse(String responseName, String responseMethodName){
		for(AnaliseFunction analiseFunction : this.analiseFunctions)
			for(AnaliseFunctionResponse response : analiseFunction.getResponses())
				if(response.getSurveyResponse().getNome().equals(responseName) && response.getMethodName().equals(responseMethodName))
					return response;
		return null;
	}
	
	public List<SurveyResponse> getSurveyResponses() {
		return surveyResponses;
	}

	public List<AnaliseFunction> getAnaliseFunctions() {
		return analiseFunctions;
	}
}
