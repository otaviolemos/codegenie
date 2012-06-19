package edu.uci.ics.mondego.codegenie.synonyms;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.VerbSynset;
import edu.smu.tspell.wordnet.WordNetDatabase;
import edu.uci.ics.mondego.codegenie.tagclouds.Term;
/**
 * Faz comunicação com servlets de sinônimos
 * @author gustavo
 *
 */
public class SynonymsServices {

	public List<Term> searchForSynonyms(String word)
			throws MalformedURLException, IOException, JAXBException {
		
		
		System.setProperty("wordnet.database.dir", "/Users/otaviolemos/Documents/WordNet-3.0/dict");
		NounSynset nounSynset; 
		VerbSynset verbSynset;

		WordNetDatabase database = WordNetDatabase.getFileInstance(); 
		Synset[] synsetsN = database.getSynsets(word, SynsetType.NOUN); 
		Synset[] synsetsV = database.getSynsets(word, SynsetType.VERB); 
		List<Term> synonyms = new ArrayList<Term>();
		List<String> synonymStrings = new ArrayList<String>();
		
		for (int i = 0; i < synsetsN.length; i++) { 
		    nounSynset = (NounSynset)(synsetsN[i]); 
		    String syn = nounSynset.getWordForms()[0];
		    if(!synonymStrings.contains(syn) && !syn.equals(word))
		    	synonymStrings.add(syn);
		}
		
		for (int i = 0; i < synsetsV.length; i++) { 
		    verbSynset = (VerbSynset)(synsetsV[i]); 
		    String syn = verbSynset.getWordForms()[0];
		    if(!synonymStrings.contains(syn) && !syn.equals(word))
		    	synonymStrings.add(syn);
		}
		
		for(String s : synonymStrings)
			synonyms.add(new Term(s, 0d, 0, false));
		
		return synonyms;
	}

	public void removeSynonyms(String word, java.util.List<String> synonyms) {
		String url = "http://localhost:8080/SynonymsSearch/RemoveSynonym?word="
				+ word;
		for (Iterator iterator = synonyms.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			url = url.concat("&synonym=" + string);
		}
		try {
			InputStream ins = new URL(url).openStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveSynonyms(String word, java.util.List<String> synonyms) {
		String url = "http://localhost:8080/SynonymsSearch/SaveSynonym?word="
				+ word;
		for (Iterator iterator = synonyms.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			url = url.concat("&synonym=" + string);
		}
		try {
			InputStream ins = new URL(url).openStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
