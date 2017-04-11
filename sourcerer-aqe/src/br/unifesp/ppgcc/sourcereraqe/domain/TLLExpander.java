package br.unifesp.ppgcc.sourcereraqe.domain;

import java.util.List;

import org.smu.wordsimilarity.WordSimDBFacade;

import br.unifesp.ppgcc.sourcereraqe.infrastructure.QueryTerm;

public class TLLExpander extends Expander {
  
  private int recommendations = 10;
  String inputFile = "SEWordSim-r2.db";
  WordSimDBFacade facade = new WordSimDBFacade(inputFile);

  public TLLExpander(int rec) {
    super.setName(TLL_EXPANDER);
    super.setMethodNameExpander(true);
    super.setParamExpander(false);
    super.setReturnExpander(false);
    recommendations = rec;
  }

  @Override
  public void expandTerm(QueryTerm queryTerm) throws Exception {
    String stemmedWord = facade.stemWord(queryTerm.getExpandedTerms().get(0));
    List<String> related = facade.findTopNWords(stemmedWord, recommendations);
    if(related != null) 
      queryTerm.getExpandedTerms().addAll(related);
        
  }

}
