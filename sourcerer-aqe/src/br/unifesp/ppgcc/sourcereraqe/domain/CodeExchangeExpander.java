package br.unifesp.ppgcc.sourcereraqe.domain;

import edu.uci.ics.codeexchange.recommendations.RecommendationUtil;
import br.unifesp.ppgcc.sourcereraqe.infrastructure.QueryTerm;

public class CodeExchangeExpander extends Expander {
  
  private int recommendations = 10;

  public CodeExchangeExpander(int rec) {
    super.setName(CODE_EXCHANGE_EXPANDER);
    super.setMethodNameExpander(true);
    super.setParamExpander(false);
    super.setReturnExpander(false);
    recommendations = rec;
  }
  
  @Override
  public void expandTerm(QueryTerm queryTerm) throws Exception {
    queryTerm.getExpandedTerms().addAll(RecommendationUtil.getKeywordRecommendations(recommendations, queryTerm.getExpandedTerms().get(0)));
  }

}
