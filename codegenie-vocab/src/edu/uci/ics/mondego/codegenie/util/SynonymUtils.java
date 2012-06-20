package edu.uci.ics.mondego.codegenie.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import edu.uci.ics.mondego.codegenie.synonyms.SynonymsSearchResult;

public class SynonymUtils {
  
  private static String url = "snake.ics.uci.edu:8080";
  
  public static String getSynonyms(String terms) throws MalformedURLException, IOException, JAXBException {
    String ret = "";
    StringTokenizer tkn = new StringTokenizer(terms);
    
    while(tkn.hasMoreTokens()) {
      // iterate through terms searching for a verb
      String tok = tkn.nextToken();
      InputStream ins = new URL(
          "http://" + url + "/synonyms-service/GetSynonyms?word=" + tok).openStream();
      JAXBContext context = JAXBContext
          .newInstance(SynonymsSearchResult.class);
      Unmarshaller marshaller = context.createUnmarshaller();
      SynonymsSearchResult result = (SynonymsSearchResult) marshaller
          .unmarshal(ins);
      
      // if it is a verb, add all synonyms to the terms
      ArrayList<String> v = new ArrayList<String>(result.getVerbs());
      if(ret != "") ret += " OR ";
      ret += tok;
      if(!v.isEmpty()) {
        ret += " OR ";
        for(int i = 0; i < v.size() - 1; i++) 
          ret += v.get(i) + " OR ";
        ret += v.get(v.size()-1);
      }
      
    }
    
    return ret;
  }

}
