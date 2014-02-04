package br.unifesp.ppgcc.aqexperiment.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.unifesp.ppgcc.aqexperiment.infrastructure.util.ConfigProperties;
import br.unifesp.ppgcc.sourcereraqe.infrastructure.AQEApproach;

@Entity
@Table(name = "execution")
public class Execution {

	@Id
	@GeneratedValue
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date executionTimestamp;

	private String autoDescription;
	private Integer number = 0;

	public Execution() {
	}

	public Execution(long executionTimestamp) throws Exception {
		this.executionTimestamp = new Date(executionTimestamp);

		boolean tagCloud = new Boolean(ConfigProperties.getProperty("aqExperiment.tagCloud"));
		boolean relaxReturn = new Boolean(ConfigProperties.getProperty("aqExperiment.relaxReturn"));
		boolean relaxParams = new Boolean(ConfigProperties.getProperty("aqExperiment.relaxParams"));
		boolean contextRelevants = new Boolean(ConfigProperties.getProperty("aqExperiment.contextRelevants"));
		boolean filterMethodNameTermsByParameter = new Boolean(ConfigProperties.getProperty("aqExperiment.filterMethodNameTermsByParameter"));
		String urlServices = ConfigProperties.getProperty("aqExperiment.related-words-service.url");
		String expanders = ConfigProperties.getProperty("aqExperiment.expanders");

		AQEApproach aqeApproach = tagCloud ? new AQEApproach() : new AQEApproach(urlServices, expanders, relaxReturn, relaxParams, contextRelevants, filterMethodNameTermsByParameter);
		this.autoDescription = aqeApproach.getAutoDescription();
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getExecutionTimestamp() {
		return executionTimestamp;
	}
	
	public void setExecutionTimestamp(Date executionTimestamp) {
		this.executionTimestamp = executionTimestamp;
	}
	
	public String getAutoDescription() {
		return autoDescription;
	}
	
	public void setAutoDescription(String autoDescription) {
		this.autoDescription = autoDescription;
	}
	
	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}
}
