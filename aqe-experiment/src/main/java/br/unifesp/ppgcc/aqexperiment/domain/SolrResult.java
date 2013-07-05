package br.unifesp.ppgcc.aqexperiment.domain;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import edu.uci.ics.sourcerer.services.search.adapter.SingleResult;

@Entity
@Table(name = "solr_result")
public class SolrResult {

	@Id
	@GeneratedValue
	private Long id;

	private Integer rank;
	private BigDecimal score;
	private Long entityID;
	private String fqn;
	private Integer paramCount;
	private String params;
	private String returnFqn;

	public SolrResult() {
	}
	
	public SolrResult(SingleResult singleResult) {
		this.rank = singleResult.getRank();
		this.score = new BigDecimal(singleResult.getScore());
		this.entityID = singleResult.getEntityID();
		this.fqn = singleResult.getFqn();
		this.paramCount = singleResult.getParamCount();
		this.params = singleResult.getParams();
		this.returnFqn = singleResult.getReturnFqn();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SolrResult)
			return this.getEntityID() != null ? this.getEntityID().equals( ((SolrResult)obj).getEntityID() ) : false;
		return super.equals(obj);
	}
	
	//accessors
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public BigDecimal getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	public Long getEntityID() {
		return entityID;
	}

	public void setEntityID(Long entityID) {
		this.entityID = entityID;
	}

	public String getFqn() {
		return fqn;
	}

	public void setFqn(String fqn) {
		this.fqn = fqn;
	}

	public Integer getParamCount() {
		return paramCount;
	}

	public void setParamCount(Integer paramCount) {
		this.paramCount = paramCount;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getReturnFqn() {
		return returnFqn;
	}

	public void setReturnFqn(String returnFqn) {
		this.returnFqn = returnFqn;
	}
}
