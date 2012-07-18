package edu.uci.ics.mondego.codegenie.relatedWords;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import edu.uci.ics.mondego.codegenie.tagclouds.Term;
/**
 * Faz comunicação com servlets de sinônimos
 * @author gustavo
 *
 */
public class RelatedWordsServices {

  private static String url = "snake.ics.uci.edu:8080";

  public List<Term> searchForSynonyms(String word) throws MalformedURLException, IOException, JAXBException {
    InputStream ins = new URL(
        "http://" + url + "/related-words-service/GetRelated?word=" + word).openStream();
    JAXBContext context = JAXBContext
        .newInstance(RelatedSearchResult.class);
    Unmarshaller marshaller = context.createUnmarshaller();
    RelatedSearchResult result = (RelatedSearchResult) marshaller
        .unmarshal(ins);

    if (result == null || (result.getVerbs() == null && result.getNouns() == null))
      return new ArrayList<Term>();

    List<String> verbs = result.getVerbs();
    List<String> codeRelated = result.getCodeRelatedSyns();
    List<Term> synonyms = new ArrayList<Term>();

    for(String s : verbs)
      synonyms.add(new Term(s, 0d, 0, false));
    
    for(String s : codeRelated)
      synonyms.add(new Term(s, 0d, 0, false));

    return synonyms;
  }
}
