package edu.uci.ics.mondego.codegenie.synonyms;

import javax.xml.bind.annotation.XmlAttribute;

import com.sun.xml.internal.txw2.annotation.XmlElement;
/**
 * DTO do Synonym
 * @author gustavo.konishi
 */
@XmlElement
public class SynonymDTO {
	
	private String value;
	private Integer userId;

	@XmlAttribute(name="user_id")
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@XmlAttribute(name="value")
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}

