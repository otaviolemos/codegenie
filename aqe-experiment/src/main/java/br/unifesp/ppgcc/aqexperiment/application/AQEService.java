package br.unifesp.ppgcc.aqexperiment.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
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
import br.unifesp.ppgcc.aqexperiment.infrastructure.util.RelatedWordUtils;
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

    // AQEApproach

    // SurveyResponses
    this.persistSurveyResponses(executionTimestamp);
    List<SurveyResponse> surveyResponses = surveyResponseRepository
        .findAll(executionTimestamp);

    List<AnaliseFunction> analiseFunctions = analiseFunctionRepository
        .findAllHardCode();

    for (AnaliseFunction function : analiseFunctions) {
      this.buildRelevants(function);
      this.buildResponses(surveyResponses, function, executionTimestamp);
      this.processResponses(function);

      analiseFunctionRepository.save(function);
    }
  }

  private void persistSurveyResponses(Date executionTimestamp) throws Exception {
    for (SurveyResponse surveyResponse : surveyResponseRepository
        .findAllFromSheet()) {
      if (!surveyResponse.isValid())
        continue;

      surveyResponse.setExecutionTimestamp(executionTimestamp);
      surveyResponseRepository.save(surveyResponse);
    }

  }

  private void buildRelevants(AnaliseFunction function) throws Exception {
    SearchAdapter searchAdapter = SearchAdapter.create(ConfigProperties
        .getProperty("aqExperiment.sourcererServer"));
    SearchResult searchResult = null;
    for (long entityId : function.getRelevantsSolrIds()) {
      String query = "entity_id:" + entityId;
      searchResult = searchAdapter.search(query);
      if (searchResult.getNumFound() == -1) {
        LogUtils.getLogger().error("Unable to perform search: " + query);
        continue;
      }
      SingleResult singleResult = searchResult.getResults(0, 1).get(0);
      function.getRelevants().add(new SolrResult(singleResult));
    }
  }

  private void buildResponses(List<SurveyResponse> surveyResponses,
      AnaliseFunction function, Date executionTimestamp) {
    for (SurveyResponse surveyResponse : surveyResponses)
      function.addResponse(surveyResponse, executionTimestamp);
  }

  private void processResponses(AnaliseFunction function) throws Exception {
    SearchAdapter searchAdapter = SearchAdapter.create(ConfigProperties
        .getProperty("aqExperiment.sourcererServer"));
    SearchResult searchResult = null;
    for (AnaliseFunctionResponse response : function.getResponses()) {
      List<SingleResult> results = new ArrayList<SingleResult>();

      //for (SolrResult relevant : function.getRelevants()) {
        String query = this.getSourcererQuery(response.getMethodName(),
            response.getReturnType(), response.getParams());
        LogUtils.getLogger().error("Query formed: " + query);
        searchResult = searchAdapter.search(query);
        if (searchResult.getNumFound() == -1) {
          LogUtils.getLogger().error("Unable to perform search: " + query);
          //continue;
        } else {
          LogUtils.getLogger().error("Num. results found: " + searchResult.getNumFound());
          results.addAll(searchResult.getResults(0, searchResult.getNumFound()));
        }
      //}
      response.setResultsFromSingleResult(results);

      this.calculateRecallAndPrecision(response, function);
    }
  }

  private String getSourcererQuery(String methodName, String returnType,
      String params) {
    boolean sourcererLibBug = ")".equals(params); // entityId in ( 5842071 ,
                                                  // 5877324 )

    returnType = StringUtils.replace(returnType, "[", "\\[");
    returnType = StringUtils.replace(returnType, "]", "\\]");
    params = StringUtils.replace(params, "[", "\\[");
    params = StringUtils.replace(params, "]", "\\]");

    boolean aqe = false;
    boolean interfaceDriven = true;
    boolean useOrInSname = false;
    boolean useOrInParamTypes = false;

    String fqnTerms = JavaTermExtractor.getFQNTermsAsString(methodName);

    String query = "";
    if (aqe) {
      fqnTerms = JavaTermExtractor.removeDuplicates(fqnTerms);
      fqnTerms = RelatedWordUtils.getRelatedAsQueryPart(fqnTerms, true, true,
          true, true);
      query += "sname_contents:(" + fqnTerms + ")";
    } else {
      query += "sname_contents:(" + (useOrInSname ? fqnTerms.replace(" ", " OR ") : fqnTerms) + ")";
    }

    // String query = "fqn_contents:("+ fqnTerms + ")";
    

    if (interfaceDriven) {
      query += "\nreturn_sname_contents:( void OR " + returnType + ")";

      if (!"".equals(params) && !sourcererLibBug) {
        // query += "\nparams_snames_exact:" + params;
        // TODO: check if this is correct; adding parameters defined by the
        // subject
        // Parameters will have to be provided as this: String, String, String
        params = params.replace(',', ' ');
        StringTokenizer stokParams = new StringTokenizer(params);
        query += "\nparam_count:" + stokParams.countTokens();
        query += "\nparams_snames_contents:(";
        while (stokParams.hasMoreTokens()) {
          query += stokParams.nextToken();
          if(stokParams.hasMoreTokens()) query += (useOrInParamTypes ? " OR " : " AND ");
        }
        query += ")";
      }
    }
    return query;
  }

  private void calculateRecallAndPrecision(AnaliseFunctionResponse response,
      AnaliseFunction function) {
    
    boolean strictRelevance = true;
    
    int relevantsWithSameInterface = 0;
    
    for (SolrResult relevant : function.getRelevants()) {
      String returnTypeResp = response.getReturnType();
      String returnTypeRel = relevant.getReturnFqn();
      if(!returnTypeResp.equalsIgnoreCase(returnTypeRel))
        continue;
      
        
      String paramsResp = response.getParams();
      String paramsRel = relevant.getParams();
      
      if(paramsRel.length() <= 2 && paramsResp.length() != 0)
        continue;
      
      if(paramsResp.length() == 0 && paramsRel.length() <= 2) {
        relevantsWithSameInterface++;
        continue;
      }
      
      List<String> paramsRelList= new ArrayList<String>(Arrays.asList(paramsRel.substring(1, paramsRel.length()-1).toLowerCase().split(",")));
      for(int i = 0; i < paramsRelList.size(); i++) {
        String p = paramsRelList.get(i);
        int ultimoPonto = p.lastIndexOf('.')+1;
        paramsRelList.set(i, p.substring(ultimoPonto));
      }
        
      List<String> paramsRespList = new ArrayList<String>(Arrays.asList(paramsResp.toLowerCase().split(",")));
      LogUtils.getLogger().info("Resp params: " + paramsRespList.toString());
      LogUtils.getLogger().info("Rel params: " + paramsRelList.toString());
      
      if(!equalLists(paramsRelList, paramsRespList))
        continue;
      else 
        relevantsWithSameInterface++;
    }
    
    int totalRelevants = strictRelevance ? 
        relevantsWithSameInterface : function.getRelevants().size();
    int totalResults = response.getResults().size();
    int totalIntersections = 0;

    for (SolrResult relevant : function.getRelevants()) {
      if (response.getResults().contains(relevant))
        totalIntersections++;
    }

    double recall = totalRelevants == 0 ? 0 : new Double(totalIntersections)
        / new Double(totalRelevants);
    double precision = totalResults == 0 ? 0 : new Double(totalIntersections)
        / new Double(totalResults);

    response.setRecall(recall);
    response.setPrecision(precision);
    response.setTotalRelevants(totalRelevants);
    response.setTotalResults(totalResults);
    response.setTotalIntersections(totalIntersections);
  }
  
  public  boolean equalLists(List<String> one, List<String> two){     
    if (one == null && two == null){
        return true;
    }

    if((one == null && two != null) 
      || one != null && two == null
      || one.size() != two.size()){
        return false;
    }

    //to avoid messing the order of the lists we will use a copy
    //as noted in comments by A. R. S.
    one = new ArrayList<String>(one); 
    two = new ArrayList<String>(two);   

    Collections.sort(one);
    Collections.sort(two);      
    return one.equals(two);
  }
}
