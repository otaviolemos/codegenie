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

import edu.uci.ics.mondego.codegenie.relatedWords.RelatedSearchResult;

public class RelatedWordUtils {

  private static String url = "snake.ics.uci.edu:8080";

  public static String getRelatedAsQueryPart(String terms, boolean enSyn, boolean codeSyn, boolean enAnt, boolean codeAnt) {
    String ret = "";
    StringTokenizer tkn = new StringTokenizer(terms);
    
    if(!enSyn && !codeSyn && !codeAnt && !enAnt) return terms;
    
    try {

    while(tkn.hasMoreTokens()) {
      // iterate through terms
      String tok = tkn.nextToken();
      InputStream ins = new URL(
          "http://" + url + "/related-words-service/GetRelated?word=" + tok).openStream();
      JAXBContext context = JAXBContext
          .newInstance(RelatedSearchResult.class);
      Unmarshaller marshaller = context.createUnmarshaller();
      RelatedSearchResult result = (RelatedSearchResult) marshaller
          .unmarshal(ins);

      ArrayList<String> v = new ArrayList<String>(result.getVerbs());
      ArrayList<String> n = new ArrayList<String>(result.getNouns());
      ArrayList<String> a = new ArrayList<String>(result.getNouns());
      ArrayList<String> va = new ArrayList<String>(result.getVerbAntonyms());
      ArrayList<String> na = new ArrayList<String>(result.getNounAntonyms());
      ArrayList<String> aa = new ArrayList<String>(result.getAdjectiveAntonyms());
      ArrayList<String> cs = new ArrayList<String>(result.getCodeRelatedSyns());
      ArrayList<String> ca = new ArrayList<String>(result.getCodeRelatedAntons());

      if(enSyn) {

        if(!ret.equals("")) ret += " AND ";
        ret += "(" + tok;

        if(!v.isEmpty()) {
          ret += " OR ";
          for(int i = 0; i < v.size() - 1; i++) 
            ret += v.get(i) + " OR ";
          ret += v.get(v.size()-1);
        } else if(!n.isEmpty()) {
            ret += " OR ";
            for(int i = 0; i < n.size() - 1; i++) 
              ret += n.get(i) + " OR ";
            ret += n.get(n.size()-1);
        } else {
          if(!a.isEmpty()) {
            ret += " OR ";
            for(int i = 0; i < a.size() - 1; i++) 
              ret += a.get(i) + " OR ";
            ret += a.get(a.size()-1);
          }
        }
      }
      
      if(codeSyn) {
        if(!enSyn) {
          if(!ret.equals("")) ret += " AND ";
          ret += "(" + tok;
        }
        if(!cs.isEmpty()) {
          ret += " OR ";
          for(int i = 0; i < cs.size() - 1; i++) 
            ret += cs.get(i) + " OR ";
          ret += cs.get(cs.size()-1);
        }
      }
      
      if(enSyn || codeSyn)
        ret += ")";
      
      if(enAnt) {
        if(!v.isEmpty()) {
          if(!va.isEmpty()) {
            if(!ret.equals("")) 
              ret += " AND ";
            else
              ret = tok + " AND ";
            ret += "!(";
            for(int i = 0; i < va.size() - 1; i++) 
              ret += va.get(i) + " OR ";
            ret += va.get(va.size()-1) + ")";
          }
        } else if(!na.isEmpty()) {
            if(!ret.equals("")) 
              ret += " AND ";
            else
              ret = tok + " AND ";
            ret += "!(";
            for(int i = 0; i < na.size() - 1; i++) 
              ret += na.get(i) + " OR ";
            ret += na.get(na.size()-1) + ")";
        } else {
          if(!aa.isEmpty()) {
            if(!ret.equals("")) 
              ret += " AND ";
            else
              ret = tok + " AND ";
            ret += "!(";
            for(int i = 0; i < aa.size() - 1; i++) 
              ret += aa.get(i) + " OR ";
            ret += aa.get(aa.size()-1) + ")";
          }
        }
      }

      if(codeAnt) {
        if(!ca.isEmpty()) {
          if(!ret.equals("")) 
            ret += " AND ";
          else
            ret = tok + " AND ";
          ret += "!(";
          for(int i = 0; i < ca.size() - 1; i++) 
            ret += ca.get(i) + " OR ";
          ret += ca.get(ca.size()-1) + ")";
        }
      }
    }
    
    } catch (MalformedURLException e) {
      System.out.println("Could not search for related words: Malformed url.");
      ret = terms;
    } catch (IOException e) {
      System.out.println("Could not search for related words: IO exception.");
      ret = terms;
    } catch (JAXBException e) {
      System.out.println("Could not search for related words: JAXB exception.");
      ret = terms;
    }

    if(ret.equals("")) 
      ret = terms;
    
    return ret;
  }

}
