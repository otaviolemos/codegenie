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

	public List<Term> searchForSynonyms(String word) {
		System.setProperty("wordnet.database.dir", "/Users/otaviolemos/Documents/WordNet-3.0/dict");
		VerbSynset verbSynset;

		if(detectCamel(word))
			word = camelCaseSplit(word);

		WordNetDatabase database = WordNetDatabase.getFileInstance(); 
		Synset[] synsetsV = database.getSynsets(word, SynsetType.VERB);

		List<Term> synonyms = new ArrayList<Term>();
		List<String> synonymStrings = new ArrayList<String>();

		for (int i = 0; i < synsetsV.length; i++) { 
			verbSynset = (VerbSynset)(synsetsV[i]); 
			String[] syns = verbSynset.getWordForms();
			for(int j = 0; j < syns.length; j++) {
				String syn = syns[j];
				if(!synonymStrings.contains(syn) && !syn.equals(word)) {
					if (syn.contains(" ")) 
						syn = camelCaseJoin(syn);
					else 
						synonymStrings.add(syn);
				}
			}
		}

		for(String s : synonymStrings)
			synonyms.add(new Term(s, 0d, 0, false));

		return synonyms;
	}

	private String camelCaseSplit(String word) {
		return word.replaceAll(
				String.format("%s|%s|%s",
						"(?<=[A-Z])(?=[A-Z][a-z])",
						"(?<=[^A-Z])(?=[A-Z])",
						"(?<=[A-Za-z])(?=[^A-Za-z])"
						),
						" "
				);
	}

	private boolean detectCamel(String word) {
		boolean switched = false;
		boolean lastLow = false;
		for(int i = 0; i < word.length(); i++) {
			if(Character.isLowerCase(word.charAt(i)))
				lastLow = true;
			if(Character.isUpperCase(word.charAt(i)) && lastLow) 
				switched = true;
		}
		return switched;
	}

	public static String camelCaseJoin(String s) {
		String result = new String();
		StringTokenizer tkn = new StringTokenizer(s);
		String second;
		while(tkn.hasMoreTokens()) {
			result += tkn.nextToken();
			if(tkn.hasMoreTokens()) {
				second = tkn.nextToken();
				result += second.substring(0,1).toUpperCase();
				result += second.substring(1);
			}
		}
		return result;
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
