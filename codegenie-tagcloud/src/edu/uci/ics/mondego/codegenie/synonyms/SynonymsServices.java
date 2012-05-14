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

import edu.uci.ics.mondego.codegenie.tagclouds.Term;
/**
 * Faz comunicação com servlets de sinônimos
 * @author gustavo
 *
 */
public class SynonymsServices {

	public List<Term> searchForSynonyms(String word)
			throws MalformedURLException, IOException, JAXBException {
		InputStream ins = new URL(
				"http://localhost:8080/SynonymsSearch/GetSynonyms?word=" + word
						+ "&singleWord=1").openStream();
		JAXBContext context = JAXBContext
				.newInstance(SynonymsSearchResult.class);
		Unmarshaller marshaller = context.createUnmarshaller();
		SynonymsSearchResult result = (SynonymsSearchResult) marshaller
				.unmarshal(ins);
		if (result == null || result.getSynonyms() == null)
			return new ArrayList<Term>();
		List<Term> synonyms = new ArrayList<Term>();
		for (Iterator iterator = result.getSynonyms().iterator(); iterator
				.hasNext();) {
			SynonymDTO synonym = (SynonymDTO) iterator.next();
			boolean removable = synonym.getUserId().equals(1);
			synonyms.add(new Term(synonym.getValue(), 0d, 0, removable));
		}
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
