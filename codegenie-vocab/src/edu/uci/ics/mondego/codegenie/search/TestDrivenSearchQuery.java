package edu.uci.ics.mondego.codegenie.search;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.search.ui.text.Match;

import edu.uci.ics.mondego.codegenie.CodeGeniePlugin;
import edu.uci.ics.mondego.codegenie.search.results.TDSearchResultPage;
import edu.uci.ics.sourcerer.services.search.adapter.SearchAdapter;
import edu.uci.ics.sourcerer.services.search.adapter.SearchResult;
import edu.uci.ics.sourcerer.services.search.adapter.SingleResult;
import edu.uci.ics.mondego.codegenie.search.SearchResultEntryWrapper;

import org.eclipse.jdt.core.IJavaProject;

import edu.uci.ics.mondego.codegenie.util.JavaTermExtractor;
import edu.uci.ics.mondego.codegenie.util.RelatedWordUtils;

import edu.uci.ics.mondego.codegenie.preferences.PreferenceConstants;

import org.eclipse.jface.viewers.ISelection;


import java.io.*;

import javax.xml.bind.JAXBException;

import edu.uci.ics.mondego.codegenie.localrepository.RepositoryStore;

public class TestDrivenSearchQuery implements ISearchQuery, Serializable {

  private ISearchResult fResult;
  private String searchLabel = "";
  private boolean hasWoven = false;
  private SearchResult srcResult;
  private IJavaProject project;
  private String[] querySpec;
  private ISelection testClassSelection;
  public int queryID;
  private int currentPage = 1;

  private boolean consideringMissingClassName = true;
  private boolean consideringArguments = true;
  private boolean consideringReturnType = true;
  private boolean consideringNames = true;
  private boolean consideringEnglishSynonyms = true;
  private boolean consideringEnglishAntonyms = true;
  private boolean consideringCodeSynonyms = true;
  private boolean consideringCodeAntonyms = true;


  private boolean[] lastQueryType = 
    {consideringReturnType, consideringNames, consideringArguments, consideringMissingClassName, consideringEnglishSynonyms, consideringCodeSynonyms, consideringEnglishAntonyms, consideringCodeAntonyms};

  public TestDrivenSearchQuery() {
    fResult = null;
    queryID = CodeGeniePlugin.getPlugin().getStore().getNewQueryID();
  }

  public TestDrivenSearchQuery(String[] query, IJavaProject prj, TestDrivenSearchResult fResult,
      ISelection sel) {
    querySpec = query;
    project = prj;
    queryID = CodeGeniePlugin.getPlugin().getStore().getNewQueryID();
    this.fResult = fResult;
    testClassSelection = sel;
  }	

  public int getCurrentPage() {
    return currentPage;
  }

  public void setCurrentPage(int currentPage) {
    this.currentPage = currentPage;
  }

  public String[] getQuerySpec() {
    return querySpec;
  }

  public boolean canRerun() {
    return false;
  }

  public boolean canRunInBackground() {
    return true;
  }

  public String getLabel() {
    updateQueryLabel();
    return searchLabel; 
  }

  public ISearchResult getSearchResult() {
    if (fResult == null) {
      fResult = new TestDrivenSearchResult(this);
    }
    return fResult;
  }

  public IStatus run(IProgressMonitor monitor)
      throws OperationCanceledException {
    TestDrivenSearchResult textResult = (TestDrivenSearchResult)getSearchResult();

    boolean[] thisQueryType = 
      {consideringReturnType, consideringNames, consideringArguments, consideringMissingClassName, consideringEnglishSynonyms, consideringCodeSynonyms, consideringEnglishAntonyms, consideringCodeAntonyms};

    if(thisQueryType[0] != lastQueryType[0] ||
        thisQueryType[1] != lastQueryType[1] ||
        thisQueryType[2] != lastQueryType[2] ||
        thisQueryType[3] != lastQueryType[3] ||
        thisQueryType[4] != lastQueryType[4] ||
        thisQueryType[5] != lastQueryType[5] ||
        thisQueryType[6] != lastQueryType[6] ||
        thisQueryType[7] != lastQueryType[7]) {
      textResult.removeAll();
      setCurrentPage(1);
      queryID = CodeGeniePlugin.getPlugin().getStore().getNewQueryID();
      updateQueryLabel();
      updateLastQueryType();
    }


    srcResult = null;
    String query = getSourcererQuery();

    String url = CodeGeniePlugin.getPlugin().getSourcererURL();
    SearchAdapter s = SearchAdapter.create(url);
    srcResult = s.search(query);


    if(srcResult.getNumFound() == -1) {
      return new Status(IStatus.ERROR, CodeGeniePlugin.PLUGIN_ID, 0, "Unable to perform search.", null);
    }
    
    searchLabel += " - " + srcResult.getNumFound() + " matches in Sourcerer"
        + " (" + srcResult.getLastQueryTime() + "ms)";

    // no results
    
    if(srcResult.getNumFound() == 0) {
      return new Status(IStatus.OK, CodeGeniePlugin.PLUGIN_ID, 0, "ok", null);
    }

    RepositoryStore store = CodeGeniePlugin.getPlugin().getStore().getRepositoryStore();
    if((currentPage-1) * 10 < srcResult.getNumFound()) {
      for(SingleResult sre: srcResult.getResults((currentPage-1) * 10, 10)){
        SearchResultEntryWrapper sr = new SearchResultEntryWrapper(sre);
        Match m = new Match (sr, 0, 1);
        textResult.addMatch(m);
        store.addEntity(sr);
      }
    }
    return new Status(IStatus.OK, CodeGeniePlugin.PLUGIN_ID, 0, "ok", null);
  }

  public IJavaProject getProject() {
    return project;
  }

  public String getSourcererQuery() {
    String query = "", fqnTerms = "";
    searchLabel = "";

    if (consideringNames) {
      fqnTerms = JavaTermExtractor.getFQNTermsAsString(querySpec[2]);
      if (consideringMissingClassName) 
        fqnTerms += " " + JavaTermExtractor.getFQNTermsAsString(querySpec[1]); 
      fqnTerms = removeDuplicates(fqnTerms);
      fqnTerms = RelatedWordUtils.getRelatedAsQueryPart(fqnTerms, consideringEnglishSynonyms, consideringCodeSynonyms, consideringEnglishAntonyms, consideringCodeAntonyms);
      
      // TODO: Term or exact?
      searchLabel += "Name terms: \'" + fqnTerms + "\' ";
      query = "fqn_contents:(" + fqnTerms + ")";
    }

    if (consideringReturnType) {
      // TODO: Term or exact?
      if(!searchLabel.equals("")) searchLabel += "\n";
      searchLabel += "Return type: \'" + querySpec[3] + "\' ";
      query += " return_fqn_contents:(" + querySpec[3].replaceAll("\\[\\]", " ") + ")";
    }

    if (consideringArguments) {
      if(!querySpec[4].equals("")) {
        if(!searchLabel.equals("")) searchLabel += "\n";
        searchLabel += "Arguments: " + querySpec[4] + ")";
        // TODO: Term or exact?
        //query += " m_args_sname_contents:"+ querySpec[4].replaceAll("\\[\\]", " ") + ")";
        query += " params_snames_exact:\\" + querySpec[4].replaceAll("\\[\\]", "\\\\[\\\\]") + "\\)";
      }
    }
    
    searchLabel = query;

    return query;
  }

  private String removeDuplicates(String fqnTerms) {
    ArrayList<String> as = new ArrayList<String>();
    StringTokenizer stok = new StringTokenizer(fqnTerms);
    String ret = "";
    while(stok.hasMoreTokens()) {
      String s = stok.nextToken();
      if(!as.contains(s)) 
        ret += ret.equals("") ? s : " " + s;
      as.add(s);
    }
    return ret;
  }

  public void updateQueryLabel() {
    getSourcererQuery();
  }

  public void setConsiderArguments(boolean considerArguments) {
    this.consideringArguments = considerArguments;
  }

  public void setConsiderNames(boolean considerNames) {
    this.consideringNames = considerNames;
  }

  public void setConsiderReturnType(boolean considerReturnType) {
    this.consideringReturnType = considerReturnType;		
  }

  public boolean isConsideringArguments() {
    return consideringArguments;
  }

  public boolean isConsideringNames() {
    return consideringNames;
  }

  public boolean isConsideringReturnType() {
    return consideringReturnType;
  }

  public void updateLastQueryType() {
    lastQueryType[0] = consideringReturnType;
    lastQueryType[1] = consideringNames;
    lastQueryType[2] = consideringArguments;
    lastQueryType[3] = consideringMissingClassName; 
    lastQueryType[4] = consideringEnglishSynonyms;
    lastQueryType[5] = consideringCodeSynonyms;
    lastQueryType[6] = consideringCodeAntonyms;
  }

  public boolean[] getLastQueryType() {
    return lastQueryType;
  }

  public boolean isConsideringMissingClassName() {
    return consideringMissingClassName;
  }

  public void setConsiderMissingClassName(boolean consideringMissingClassName) {
    this.consideringMissingClassName = consideringMissingClassName;
  }
  
  public boolean isConsideringEnglishSynonyms() {
    return consideringEnglishSynonyms;
  }

  public void setConsideringEnglishSynonyms(boolean consideringSynonyms) {
    this.consideringEnglishSynonyms = consideringSynonyms;
  }
  
  public boolean isConsideringCodeSynonyms() {
    return consideringCodeSynonyms;
  }

  public void setConsideringCodeSynonyms(boolean consideringSynonyms) {
    this.consideringCodeSynonyms = consideringSynonyms;
  }
  
  public boolean isConsideringCodeAntonyms() {
    return consideringCodeAntonyms;
  }

  public void setConsideringCodeAntonyms(boolean consideringAntonyms) {
    this.consideringCodeAntonyms = consideringAntonyms;
  }
  
  public boolean isConsideringEnglishAntonyms() {
    return consideringEnglishAntonyms;
  }

  public void setConsideringEnglishAntonyms(boolean consideringAntonyms) {
    this.consideringEnglishAntonyms = consideringAntonyms;
  }

  public ISelection getTestClassSelection() {
    return testClassSelection;
  }

  public void setTestClassSelection(ISelection testClassSelection) {
    this.testClassSelection = testClassSelection;
  }

  public boolean hasWoven() {
    return hasWoven;
  }

  public void setHasWoven(boolean hasWoven) {
    this.hasWoven = hasWoven;
  }

}
