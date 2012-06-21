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
  
  public static String getSynonymsAsQueryPart(String terms) throws MalformedURLException, IOException, JAXBException {
    String ret = "";
    StringTokenizer tkn = new StringTokenizer(terms);
    
    while(tkn.hasMoreTokens()) {
      // iterate through terms
      String tok = tkn.nextToken();
      InputStream ins = new URL(
          "http://" + url + "/synonyms-service/GetSynonyms?word=" + tok).openStream();
      JAXBContext context = JAXBContext
          .newInstance(SynonymsSearchResult.class);
      Unmarshaller marshaller = context.createUnmarshaller();
      SynonymsSearchResult result = (SynonymsSearchResult) marshaller
          .unmarshal(ins);
      
      ArrayList<String> v = new ArrayList<String>(result.getVerbs());
      ArrayList<String> n = new ArrayList<String>(result.getNouns());
      
      if(ret != "") ret += " AND ";
      ret += "(" + tok;
      
      if(!v.isEmpty()) {
        ret += " OR ";
        for(int i = 0; i < v.size() - 1; i++) 
          ret += v.get(i) + " OR ";
        ret += v.get(v.size()-1) + ")";
      } else {
        if(!n.isEmpty()) {
          ret += " OR ";
          for(int i = 0; i < n.size() - 1; i++) 
            ret += n.get(i) + " OR ";
          ret += n.get(n.size()-1) + ")";
        } else {
          ret += ")";
        }
      }
      
    }
    
    return ret;
  }

}
