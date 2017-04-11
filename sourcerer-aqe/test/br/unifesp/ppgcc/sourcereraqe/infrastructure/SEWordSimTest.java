package br.unifesp.ppgcc.sourcereraqe.infrastructure;


import org.junit.Test;
import org.smu.wordsimilarity.WordSimDBFacade;

public class SEWordSimTest {
  
  String inputFile = "SEWordSim-r2.db";
  WordSimDBFacade facade = new WordSimDBFacade(inputFile);

  @Test
  public void test() {
    String inputWord = "resize";
    String stemmedWord = facade.stemWord(inputWord);
    
    //set parameters
    double minSimilarityScore = 0.3;
    int N = 6;
    
    //search and print result
    System.out.print(facade.isInDatabase(stemmedWord) + "\n");
    System.out.print(facade.findMostSimilarWord(stemmedWord) + "\n");
    System.out.print(facade.findMostSimilarWords(stemmedWord, minSimilarityScore) + "\n");
    System.out.print(facade.findTopNWords(stemmedWord, N) + "\n");
    
  }

}


