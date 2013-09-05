package br.unifesp.ppgcc.aqexperiment.infrastructure.sourcereraqe;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.unifesp.ppgcc.aqexperiment.infrastructure.util.ConfigProperties;

public class AQEApproach {

	private List<Expander> expanders = new ArrayList<Expander>();

	private boolean relaxReturn = false;
	private boolean relaxParams = false;
	
	public AQEApproach(String expanders, boolean relaxReturn, boolean relaxParams) throws Exception {
		this.relaxReturn = relaxReturn;
		this.relaxParams = relaxParams;
		
		String[] splitExpanders = StringUtils.split(expanders, ",");
		
		for(String expanderName : splitExpanders){
			Expander expander = this.getExpander(expanderName);

			if(expander == null)
				throw new Exception("Invalid expander name: " + expanderName);
			
			this.expanders.add(expander);
		}
	}
	
	private Expander getExpander(String expander){
		if(expander != null & StringUtils.trim(expander).equalsIgnoreCase(Expander.WORDNET_EXPANDER))
			return new WordNetExpander();
		if(expander != null & StringUtils.trim(expander).equalsIgnoreCase(Expander.CODE_VOCABULARY_EXPANDER))
			return new CodeVocabularyExpander();
		if(expander != null & StringUtils.trim(expander).equalsIgnoreCase(Expander.TYPE_EXPANDER))
			return new TypeExpander();
		
		return null;
	}
	
	public String getAutoDescription() throws Exception {
		String desc = "";
		
		if(this.relaxReturn)
			desc += "relaxReturn | ";
		if(this.relaxParams)
			desc += "relaxParams | ";
		if(new Boolean(ConfigProperties.getProperty("aqExperiment.moreOneRelevant")))
			desc += "moreOneR | ";
		
		boolean first = true;
		for(Expander expander : expanders){
			if(first){
				desc += expander.getName();
				first = false;
			}else
				desc += ", " + expander.getName();
		}
		
		if("".equals(desc))
			desc += "Without expansion";

		return desc;
	}

	public boolean hasMethodNameExpander(){
		for(Expander expander : expanders){
			if (expander.isMethodNameExpander())
				return true;
		}
		return false;
	}

	public boolean hasParamExpander(){
		for(Expander expander : expanders){
			if (expander.isParamExpander())
				return true;
		}
		return false;
	}

	public boolean hasReturnExpander(){
		for(Expander expander : expanders){
			if (expander.isReturnExpander())
				return true;
		}
		return false;
	}

	public List<Expander> getExpanders() {
		return expanders;
	}

	public boolean isRelaxParams() {
		return relaxParams;
	}

	public boolean isRelaxReturn() {
		return relaxReturn;
	}
	
}
