package edu.uci.ics.mondego.codegenie.tagclouds;

public class Term implements Comparable<Term> {

	private String term;
	private double weight;
	private int hits;
	private boolean isRemovable;
	
	public Term(String term, double weight,int hits,boolean isRemovable) {
		this.setTerm(term);
		this.setWeight(weight);
		this.setHits(hits);
		this.setRemovable(isRemovable);
	}
	
	public int compareTo(Term that) {
		
		if(this.getWeight() > that.getWeight())
			return -1;
		else if(this.getWeight() < that.getWeight())
			return 1;
		else 
			return 0;
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public boolean isRemovable() {
		return isRemovable;
	}

	public void setRemovable(boolean isRemovable) {
		this.isRemovable = isRemovable;
	}
}
