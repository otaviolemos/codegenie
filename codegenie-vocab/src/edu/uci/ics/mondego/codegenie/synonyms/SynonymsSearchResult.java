package edu.uci.ics.mondego.codegenie.synonyms;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * DTO de synonyms
 * @author gustavo
 *
 */
@XmlRootElement(name="synonymsSearch")
public class SynonymsSearchResult {
	
	
	private List<SynonymDTO> synonyms;

	@XmlElement(name="synonym")
	public List<SynonymDTO> getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(List<SynonymDTO> synonyms) {
		this.synonyms = synonyms;
	}

	
}
