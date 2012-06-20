package edu.uci.ics.mondego.codegenie.synonyms;

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
public class SynonymsServices {

  private static String url = "snake.ics.uci.edu:8080";

  public List<Term> searchForSynonyms(String word) throws MalformedURLException, IOException, JAXBException {
    InputStream ins = new URL(
        "http://" + url + "/synonyms-service/GetSynonyms?word=" + word).openStream();
    JAXBContext context = JAXBContext
        .newInstance(SynonymsSearchResult.class);
    Unmarshaller marshaller = context.createUnmarshaller();
    SynonymsSearchResult result = (SynonymsSearchResult) marshaller
        .unmarshal(ins);

    if (result == null || (result.getVerbs() == null && result.getNouns() == null))
      return new ArrayList<Term>();

    List<String> verbs = result.getVerbs();
    List<Term> synonyms = new ArrayList<Term>();

    for(String s : verbs)
      synonyms.add(new Term(s, 0d, 0, false));

    return synonyms;
  }
}
