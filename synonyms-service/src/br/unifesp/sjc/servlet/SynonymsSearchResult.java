package br.unifesp.sjc.servlet;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * DTO do Synonym
 * @author gustavo.konishi
 *
 */
@XmlRootElement(name="synonymsSearch")
public class SynonymsSearchResult {
	
	
	private List<String> synonyms = new ArrayList<String>();

	@XmlElement(name="synonym")
	public List<String> getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(List<String> synonyms) {
		this.synonyms = synonyms;
	}

	
}
