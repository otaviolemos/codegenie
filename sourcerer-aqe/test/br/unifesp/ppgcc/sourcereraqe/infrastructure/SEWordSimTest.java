package br.unifesp.ppgcc.sourcereraqe.infrastructure;

import static org.junit.Assert.*;

import org.junit.Test;
import org.smu.wordsimilarity.WordSimDBFacade;

public class SEWordSimTest {
  
  String inputFile = "SEWordSim-r2.db";
  WordSimDBFacade facade = new WordSimDBFacade(inputFile);

  @Test
  public void test() {
    String inputWord = "compress";
    String stemmedWord = facade.stemWord(inputWord);
    
    //set parameters
    double minSimilarityScore = 0.3;
    int N = 10;
    
  //search and print result
    System.out.print(facade.isInDatabase(stemmedWord) + "\n");
    System.out.print(facade.findMostSimilarWord(stemmedWord) + "\n");
    System.out.print(facade.findMostSimilarWords(stemmedWord, minSimilarityScore) + "\n");
    System.out.print(facade.findTopNWords(stemmedWord, N) + "\n");
  }

}


