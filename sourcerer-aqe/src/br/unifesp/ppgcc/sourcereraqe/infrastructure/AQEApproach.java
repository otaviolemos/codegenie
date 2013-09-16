package br.unifesp.ppgcc.sourcereraqe.infrastructure;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.unifesp.ppgcc.sourcereraqe.domain.CodeVocabularyExpander;
import br.unifesp.ppgcc.sourcereraqe.domain.Expander;
import br.unifesp.ppgcc.sourcereraqe.domain.TypeExpander;
import br.unifesp.ppgcc.sourcereraqe.domain.WordNetExpander;

public class AQEApproach {

	private List<Expander> expanders = new ArrayList<Expander>();

	private boolean relaxReturn = false;
	private boolean relaxParams = false;
	private String relatedWordsServiceUrl;
	
	public AQEApproach(String relatedWordsServiceUrl, String expanders, boolean relaxReturn, boolean relaxParams) throws Exception {
		this.relaxReturn = relaxReturn;
		this.relaxParams = relaxParams;
		this.relatedWordsServiceUrl = relatedWordsServiceUrl;
		
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
			return new WordNetExpander(this.relatedWordsServiceUrl);
		if(expander != null & StringUtils.trim(expander).equalsIgnoreCase(Expander.CODE_VOCABULARY_EXPANDER))
			return new CodeVocabularyExpander(this.relatedWordsServiceUrl);
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
		
		boolean first = true;
		for(Expander expander : expanders){
			if(first){
				desc += expander.getName();
				first = false;
			}else
				desc += ", " + expander.getName();
		}
		
		if("".equals(desc) || desc.endsWith(" | "))
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
