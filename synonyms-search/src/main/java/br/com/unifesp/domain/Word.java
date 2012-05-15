package br.com.unifesp.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Representação da Palavra
 * @author gustavo.konishi
 *
 */
@Entity
@Table(name="word")
public class Word {

	private Integer id;
	private String value;;
	private Integer userId;

	@Id @GeneratedValue(strategy = GenerationType.AUTO) 
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="text")
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	
	@Column(name="user_id")
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	
	
	
	@Override
	public String toString() {
		return "Word [id=" + id + ", value=" + value + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Word other = (Word) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
