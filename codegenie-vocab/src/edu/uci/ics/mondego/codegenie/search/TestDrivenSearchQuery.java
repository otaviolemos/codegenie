package edu.uci.ics.mondego.codegenie.search;

import java.net.MalformedURLException;
import java.net.URL;
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
import edu.uci.ics.mondego.search.model.SearchResult;
import edu.uci.ics.mondego.search.model.SearchResultEntry;
import edu.uci.ics.mondego.codegenie.search.SearchResultEntryWrapper;
import edu.uci.ics.mondego.wsclient.common.SearchResultUnMarshaller;

import org.eclipse.jdt.core.IJavaProject;

import edu.uci.ics.mondego.codegenie.util.JavaTermExtractor;
import edu.uci.ics.mondego.codegenie.util.SynonymUtils;

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
  private boolean consideringSynonyms = true;

  private boolean[] lastQueryType = 
    {consideringReturnType, consideringNames, consideringArguments, consideringMissingClassName}; 

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
      {consideringReturnType, consideringNames, consideringArguments, consideringMissingClassName};

    if(thisQueryType[0] != lastQueryType[0] ||
        thisQueryType[1] != lastQueryType[1] ||
        thisQueryType[2] != lastQueryType[2] ||
        thisQueryType[3] != lastQueryType[3]) {
      textResult.removeAll();
      setCurrentPage(1);
      queryID = CodeGeniePlugin.getPlugin().getStore().getNewQueryID();
      updateQueryLabel();
      updateLastQueryType();
    }


    srcResult = null;
    String query = getSourcererQuery();

    try
    {
      String url = CodeGeniePlugin.getPlugin().getSourcererURL();


      InputStream ins = new URL("http://" + url + "/ws-search/search?qry=" +
          query.replaceAll(" ", "%20")
          + "&pid=" +
          currentPage +  "&epp=" +
          10 + 
          "&client=codegenie").openStream();

      srcResult = SearchResultUnMarshaller.unMarshallSearchResults(ins);

    } catch (Exception e) {
      return new Status(IStatus.ERROR, CodeGeniePlugin.PLUGIN_ID, 0, "error", null);
    }


    searchLabel += " - " + srcResult.getTotalHitsFound() + " matches in Sourcerer"
        + " (" + srcResult.getTimeTakenMiliSecs() + "ms)";

    // no results
    if(srcResult.getEntries() == null) {
      return new Status(IStatus.OK, CodeGeniePlugin.PLUGIN_ID, 0, "ok", null);
    }

    RepositoryStore store = CodeGeniePlugin.getPlugin().getStore().getRepositoryStore();
    for(SearchResultEntry sre: srcResult.getEntries()){
      SearchResultEntryWrapper sr = new SearchResultEntryWrapper(sre);
      Match m = new Match (sr, 0, 1);
      textResult.addMatch(m);
      store.addEntity(sr);
    }

    return new Status(IStatus.OK, CodeGeniePlugin.PLUGIN_ID, 0, "ok", null);
  }

  public IJavaProject getProject() {
    return project;
  }

  public String getSourcererQuery() {
    String query = "", methodTerms = "", fqnTerms = "", methodSynTerms = "";
    searchLabel = "";

    if (consideringNames) {
      methodTerms = JavaTermExtractor.getFQNTermsAsString(querySpec[2]);
      
      try {
        methodSynTerms = SynonymUtils.getSynonyms(methodTerms);
        fqnTerms += " AND (" + methodSynTerms + ")";
      } catch (MalformedURLException e) {
        System.out.println("Could not search for synonyms: Malformed url.");
      } catch (IOException e) {
        System.out.println("Could not search for synonyms: IO exception.");
      } catch (JAXBException e) {
        System.out.println("Could not search for synonyms: JAXB exception.");
      }
      
      if (consideringMissingClassName) 
        fqnTerms =  JavaTermExtractor.getFQNTermsAsString(querySpec[1]) + 
          " AND (" + methodSynTerms + ")"; 
      
      // TODO: Term or exact?
      
      searchLabel += "Name terms: \'" + fqnTerms + "\' ";
      searchLabel += "Method terms: \'" + methodSynTerms + "\'";

      query += " fqn_contents:(" + fqnTerms + ")";
      
      if(methodSynTerms != "")
        query += " short_name_contents:(" + methodSynTerms + ")";
    }

    if (consideringReturnType) {
      // TODO: Term or exact?
      searchLabel += " Return type: \'" + querySpec[3] + "\' ";
      query += " m_ret_type_contents:(" + querySpec[3].replaceAll("\\[\\]", " ") + ")";
    }

    if (consideringArguments) {
      if(!querySpec[4].equals("")) {
        searchLabel += "Arguments: " + querySpec[4] + ")";
        // TODO: Term or exact?
        //query += " m_args_sname_contents:"+ querySpec[4].replaceAll("\\[\\]", " ") + ")";
        query += " m_sig_args_sname:" + querySpec[4].replaceAll("\\[\\]", "\\\\[\\\\]") + ")";
      }
    }

    return query;
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

  public boolean isConsideingrReturnType() {
    return consideringReturnType;
  }

  public void updateLastQueryType() {
    lastQueryType[0] = consideringReturnType;
    lastQueryType[1] = consideringNames;
    lastQueryType[2] = consideringArguments;
    lastQueryType[3] = consideringMissingClassName; 
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
