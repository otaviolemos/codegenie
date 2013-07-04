package br.unifesp.ppgcc.eaq.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.unifesp.ppgcc.eaq.domain.AnaliseFunction;
import br.unifesp.ppgcc.eaq.domain.AnaliseFunctionResponse;
import br.unifesp.ppgcc.eaq.domain.Employee;
import br.unifesp.ppgcc.eaq.domain.SurveyResponse;
import br.unifesp.ppgcc.eaq.infrastructure.AnaliseFunctionRepository;
import br.unifesp.ppgcc.eaq.infrastructure.ConfigProperties;
import br.unifesp.ppgcc.eaq.infrastructure.EmployeeRepository;
import br.unifesp.ppgcc.eaq.infrastructure.JavaTermExtractor;
import br.unifesp.ppgcc.eaq.infrastructure.LogUtils;
import br.unifesp.ppgcc.eaq.infrastructure.SurveyResponseRepository;
import edu.uci.ics.sourcerer.services.search.adapter.SearchAdapter;
import edu.uci.ics.sourcerer.services.search.adapter.SearchResult;
import edu.uci.ics.sourcerer.services.search.adapter.SingleResult;


@Service
@Transactional
public class AQEService {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	private List<AnaliseFunction> analiseFunctions = new ArrayList<AnaliseFunction>();
	private List<SurveyResponse> surveyResponses = new ArrayList<SurveyResponse>();

	public void execute() throws Exception {

		List<Employee> employees = employeeRepository.findAll();
		System.out.println(employees.size());
		
		analiseFunctions = new AnaliseFunctionRepository().findAll();
		surveyResponses = new SurveyResponseRepository().findAll();

		for(AnaliseFunction function : this.analiseFunctions){
			this.buildRelevants(function);
			this.buildResponses(function);
			this.processResponses(function);
		}
		
		LogUtils.getLogger().error("Fim execute");
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
		    function.getRelevants().add(singleResult);
	    }
	}
	
	private void buildResponses(AnaliseFunction function){
		for(SurveyResponse surveyResponse : this.surveyResponses)
			function.addResponse(surveyResponse);
	}
	
	private void processResponses(AnaliseFunction function) throws Exception {
		SearchAdapter searchAdapter = SearchAdapter.create(ConfigProperties.getProperty("aqExperiment.sourcererServer"));
	    SearchResult searchResult = null;
		for(AnaliseFunctionResponse response : function.getResponses()){
			List<SingleResult> results = new ArrayList<SingleResult>();
			for(SingleResult relevant :function.getRelevants()){
				String query = this.getSourcererQuery(response.getMethodName(), relevant.getReturnFqn(), relevant.getParams());
				searchResult = searchAdapter.search(query);
				results.addAll(searchResult.getResults(0, 1000));
			}
			response.setResults(results);
			
			if(results.size() > 0)
				this.calculateRelevance(response, function);
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
		for(SingleResult recovered : response.getResults()){
			if(function.getRelevants().contains(recovered))
				intersection++;
		}
		
		response.setPrecision(new Float(intersection / response.getResults().size()));
		response.setRecall(new Float(intersection / function.getRelevants().size()));
	}
}
