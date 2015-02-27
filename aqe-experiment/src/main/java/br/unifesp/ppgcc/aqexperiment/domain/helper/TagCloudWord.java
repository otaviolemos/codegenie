package br.unifesp.ppgcc.aqexperiment.domain.helper;

public class TagCloudWord implements Comparable<TagCloudWord>{

	private String word;
	private int frequency;
	private int rank;
	private boolean originalWord;

	public TagCloudWord(String word, int frequency, boolean originalWord) {
		this.word = this.getUpperCaseFirstChar(word);
		this.frequency = frequency;
		this.rank = 0;
		this.originalWord = originalWord;
	}
	
	public TagCloudWord(String word, int frequency) {
		this.word = this.getUpperCaseFirstChar(word);
		this.frequency = frequency;
		this.originalWord = false;
	}

	private String getUpperCaseFirstChar(String str){
		if(str == null || "".equals(str))
			return str;
		return (""+str.charAt(0)).toUpperCase() + str.substring(1); 
	}
	
	public void setWord(String word) {
		this.word = this.getUpperCaseFirstChar(word);
	}

	public int compareTo(TagCloudWord o) {
		if(this.isOriginalWord())
			return 1;
		if(o.isOriginalWord())
			return -1;
		
		if (this.getFrequency() < o.getFrequency())
            return -1;

		if (this.getFrequency() > o.getFrequency())
            return 1;

        return 0;
     }
	
	public String getWord() {
		return word;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	public boolean isOriginalWord() {
		return originalWord;
	}
	public void setOriginalWord(boolean originalWord) {
		this.originalWord = originalWord;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
}
