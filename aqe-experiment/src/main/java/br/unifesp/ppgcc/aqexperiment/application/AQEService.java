package br.unifesp.ppgcc.aqexperiment.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.unifesp.ppgcc.aqexperiment.domain.AnaliseFunction;
import br.unifesp.ppgcc.aqexperiment.domain.AnaliseFunctionResponse;
import br.unifesp.ppgcc.aqexperiment.domain.SolrResult;
import br.unifesp.ppgcc.aqexperiment.domain.SurveyResponse;
import br.unifesp.ppgcc.aqexperiment.infrastructure.AnaliseFunctionRepository;
import br.unifesp.ppgcc.aqexperiment.infrastructure.AnaliseFunctionResponseRepository;
import br.unifesp.ppgcc.aqexperiment.infrastructure.SurveyResponseRepository;
import br.unifesp.ppgcc.aqexperiment.infrastructure.util.ConfigProperties;
import br.unifesp.ppgcc.aqexperiment.infrastructure.util.JavaTermExtractor;
import br.unifesp.ppgcc.aqexperiment.infrastructure.util.LogUtils;
import edu.uci.ics.sourcerer.services.search.adapter.SearchAdapter;
import edu.uci.ics.sourcerer.services.search.adapter.SearchResult;
import edu.uci.ics.sourcerer.services.search.adapter.SingleResult;


@Service
@Transactional
public class AQEService {

	@Autowired
	private AnaliseFunctionRepository analiseFunctionRepository;
	
	@Autowired
	private AnaliseFunctionResponseRepository analiseFunctionResponseRepository;

	@Autowired
	private SurveyResponseRepository surveyResponseRepository;

	public void execute() throws Exception {
		Date executionTimestamp = new Date(System.currentTimeMillis());
		
		//AQEApproach
		
		//SurveyResponses
		this.persistSurveyResponses(executionTimestamp);
		List<SurveyResponse> surveyResponses = surveyResponseRepository.findAll(executionTimestamp);
		
		List<AnaliseFunction> analiseFunctions = analiseFunctionRepository.findAllHardCode();

		for(AnaliseFunction function : analiseFunctions){
			this.buildRelevants(function);
			this.buildResponses(surveyResponses, function, executionTimestamp);
			this.processResponses(function);

			analiseFunctionRepository.save(function);
		}
	}
	
	private void persistSurveyResponses(Date executionTimestamp) throws Exception {
		for(SurveyResponse surveyResponse : surveyResponseRepository.findAllFromSheet()){
			if(!surveyResponse.isValid())
				continue;

			surveyResponse.setExecutionTimestamp(executionTimestamp);
			surveyResponseRepository.save(surveyResponse);
		}
		
	}
	
	private void buildRelevants(AnaliseFunction function) throws Exception {
		SearchAdapter searchAdapter = SearchAdapter.create(ConfigProperties.getProperty("aqExperiment.sourcererServer"));
	    SearchResult searchResult = null;
	    for(long entityId : function.getRelevantsSolrIds()){
	    	String query = "entity_id:"+entityId;
	    	searchResult = searchAdapter.search(query);
		    if(searchResult.getNumFound() == -1){
				LogUtils.getLogger().error("Unable to perform search: " + query);
		    	continue;
		    }
		    SingleResult singleResult = searchResult.getResults(0, 1).get(0);
		    function.getRelevants().add(new SolrResult(singleResult));
	    }
	}
	
	private void buildResponses(List<SurveyResponse> surveyResponses, AnaliseFunction function, Date executionTimestamp){
		for(SurveyResponse surveyResponse : surveyResponses)
			function.addResponse(surveyResponse, executionTimestamp);
	}
	
	private void processResponses(AnaliseFunction function) throws Exception {
		SearchAdapter searchAdapter = SearchAdapter.create(ConfigProperties.getProperty("aqExperiment.sourcererServer"));
	    SearchResult searchResult = null;
		for(AnaliseFunctionResponse response : function.getResponses()){
			List<SingleResult> results = new ArrayList<SingleResult>();
			for(SolrResult relevant :function.getRelevants()){
				String query = this.getSourcererQuery(response.getMethodName(), relevant.getReturnFqn(), relevant.getParams());
				searchResult = searchAdapter.search(query);
			    if(searchResult.getNumFound() == -1){
					LogUtils.getLogger().error("Unable to perform search: " + query);
			    	continue;
			    }
				results.addAll(searchResult.getResults(0, 1000));
			}
			response.setResultsFromSingleResult(results);
			
			this.calculateRecallAndPrecision(response, function);
		}
	}
	
	private String getSourcererQuery(String methodName, String returnType, String params){
		boolean sourcererLibBug = ")".equals(params); // entityId in ( 5842071 , 5877324 )
		
		returnType = StringUtils.replace(returnType, "[", "\\[");
		returnType = StringUtils.replace(returnType, "]", "\\]");
		params = StringUtils.replace(params, "[", "\\[");
		params = StringUtils.replace(params, "]", "\\]");
		
		String query = "fqn_contents:("+ JavaTermExtractor.getFQNTermsAsString(methodName) + ")";
//		query += "\nreturn_fqn_contents:(" + returnType + ")";
//        if(!"()".equals(params) && !sourcererLibBug)
//        	query += "\nparams_snames_exact:" + params;
		return query;
	}
	
	private void calculateRecallAndPrecision(AnaliseFunctionResponse response, AnaliseFunction function){
		int totalRelevants = function.getRelevants().size();
		int totalResults = response.getResults().size();
		int totalIntersections = 0;
		
		for(SolrResult recovered : response.getResults()){
			if(function.getRelevants().contains(recovered))
				totalIntersections++;
		}
		
		double recall = totalRelevants == 0 ? 0 : new Double(totalIntersections) / totalRelevants;
		double precision = totalResults == 0 ? 0 : new Double(totalIntersections) / totalResults;
		
		response.setRecall(recall);
		response.setPrecision(precision);
		response.setTotalRelevants(totalRelevants);
		response.setTotalResults(totalResults);
		response.setTotalIntersections(totalIntersections);
	}
}
