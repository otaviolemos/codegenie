package edu.uci.ics.mondego.codegenie.synonyms;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * DTO do Synonym
 * @author Otavio Lemos
 *
 */
@XmlRootElement(name="synonymsSearch")
public class SynonymsSearchResult {
	
	
	private List<String> verbs = new ArrayList<String>();
	private List<String> nouns = new ArrayList<String>();

	@XmlElement(name="nounSynonym")
	public List<String> getNouns() {
    return nouns;
  }

  public void setNouns(List<String> nounSynonyms) {
    this.nouns = nounSynonyms;
  }
  
  @XmlElement(name="verbSynonym")
  public List<String> getVerbs() {
		return verbs;
	}

	public void setVerbs(List<String> verbSynonyms) {
		this.verbs = verbSynonyms;
	}

	
}
