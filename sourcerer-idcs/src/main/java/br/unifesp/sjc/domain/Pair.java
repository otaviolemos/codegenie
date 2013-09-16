package br.unifesp.sjc.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="pair")
public class Pair implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  private String word1;
  @Id @Column(name="word1")
  public String getWord1() {
    return word1;
  }
  public void setWord1(String word1) {
    this.word1 = word1;
  }
  
  private String word2;
  @Id @Column(name="word2")
  public String getWord2() {
    return word2;
  }
  public void setWord2(String word2) {
    this.word2 = word2;
  }
  
  private String type;
  @Column(name="type")
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }

  
  @Override
  public String toString() {
    return "Pair [word1=" + word1 + ", word2=" + word2 + " type=" + type + "]";
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + word1.hashCode() + word2.hashCode();
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
    Pair other = (Pair) obj;
    if (word1 == null && word2 == null) {
      if (other.word1 != null || other.word2 != null)
        return false;
    } else if (!word1.equals(other.word1) || !word2.equals(other.word2))
      return false;
    return true;
  }

}
