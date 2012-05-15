package br.com.unifesp.domain;

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
	
	
	private List<SynonymDTO> synonyms = new ArrayList<SynonymDTO>();

	@XmlElement(name="synonym")
	public List<SynonymDTO> getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(List<SynonymDTO> synonyms) {
		this.synonyms = synonyms;
	}

	
}
