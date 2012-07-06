package br.unifesp.sjc.servlet;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * DTO do Synonym
 * @author Otavio Lemos
 *
 */
@XmlRootElement(name="relatedSearch")
public class RelatedSearchResult {
	
	
	private List<String> verbs = new ArrayList<String>();
	private List<String> nouns = new ArrayList<String>();
	private List<String> codeRelatedSyns = new ArrayList<String>();
	private List<String> codeRelatedAntons = new ArrayList<String>();

	@XmlElement(name="codeAntonym")
	public List<String> getCodeRelatedAntons() {
    return codeRelatedAntons;
  }

  public void setCodeRelatedAntons(List<String> codeRelatedAntons) {
    this.codeRelatedAntons = codeRelatedAntons;
  }

  @XmlElement(name="codeSynonym")
	public List<String> getCodeRelatedSyns() {
    return codeRelatedSyns;
  }

  public void setCodeRelatedSyns(List<String> codeRelated) {
    this.codeRelatedSyns = codeRelated;
  }

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
