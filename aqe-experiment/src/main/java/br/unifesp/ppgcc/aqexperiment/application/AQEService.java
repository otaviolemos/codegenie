package br.unifesp.ppgcc.aqexperiment.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
		
		//SurveyResponses
		for(SurveyResponse surveyResponse : surveyResponseRepository.findAllFromSheet()){
			surveyResponse.setExecutionTimestamp(executionTimestamp);
			surveyResponseRepository.save(surveyResponse);
		}
		List<SurveyResponse> surveyResponses = surveyResponseRepository.findAll(executionTimestamp);
		
		List<AnaliseFunction> analiseFunctions = analiseFunctionRepository.findAllHardCode();

		for(AnaliseFunction function : analiseFunctions){
			this.buildRelevants(function);
			this.buildResponses(surveyResponses, function, executionTimestamp);
			this.processResponses(function);

			analiseFunctionRepository.save(function);
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
				results.addAll(searchResult.getResults(0, 1000));
			}
			response.setResultsFromSingleResult(results);
			
			if(results.size() > 0)
				this.calculateRelevance(response, function);
			
			if(response.getPrecision() == null)
				System.out.println("opa");
		}
	}
	
	private String getSourcererQuery(String methodName, String returnType, String params){
		String query = "fqn_contents:("+ JavaTermExtractor.getFQNTermsAsString(methodName) + ")";
		query += "\nreturn_fqn_contents:(" + returnType + ")";
        query += "\nparams_snames_exact:" + params;
		return query;
	}
	
	private void calculateRelevance(AnaliseFunctionResponse response, AnaliseFunction function){
		int intersection = 0;
		for(SolrResult recovered : response.getResults()){
			if(function.getRelevants().contains(recovered))
				intersection++;
		}
		
		response.setPrecision(new BigDecimal(intersection / response.getResults().size()));
		response.setRecall(new BigDecimal(intersection / function.getRelevants().size()));
	}
}
