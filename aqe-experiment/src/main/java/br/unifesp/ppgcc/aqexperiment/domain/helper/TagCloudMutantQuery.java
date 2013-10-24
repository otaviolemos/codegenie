package br.unifesp.ppgcc.aqexperiment.domain.helper;

import java.util.ArrayList;
import java.util.List;

public class TagCloudMutantQuery {

	private List<TagCloudWord> words = new ArrayList<TagCloudWord>();

	public void addWord(TagCloudWord word){
		this.words.add(word);
	}
	
	public boolean isMutantMethodName(){
		for (TagCloudWord word : this.getWords()) {
			if(!word.isOriginalWord())
				return true;
		}
		return false;
	}
	
	public String getMutantMethodName(){
		String mutantMethodName = "";
		for(TagCloudWord word : this.words)
			mutantMethodName += word.getWord();
		return mutantMethodName;
	}
	
	public String getFrequenciesRank(){
		String frequenciesRanking = "";
		boolean first = true;
		for(TagCloudWord word : this.words){
			if(!first){
				frequenciesRanking += ";"+word.getRank();
			}else{
				frequenciesRanking += word.getRank();
				first = false;
			}
		}
		return frequenciesRanking;
	}
	
	public List<TagCloudWord> getWords() {
		return words;
	}

	public void setWords(List<TagCloudWord> words) {
		this.words = words;
	}
}
