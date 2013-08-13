package br.unifesp.ict.seg.codegenie.tmp;

import edu.uci.ics.sourcerer.services.search.adapter.SingleResult;

public class MySingleResult {

	protected Long id;
	protected SingleResult sr;

	/**
	 * @return
	 * @see edu.uci.ics.sourcerer.services.search.adapter.SingleResult#getRank()
	 */
	public int getRank() {
		return sr.getRank();
	}

	/**
	 * @return
	 * @see edu.uci.ics.sourcerer.services.search.adapter.SingleResult#getScore()
	 */
	public float getScore() {
		return sr.getScore();
	}

	/**
	 * @return
	 * @see edu.uci.ics.sourcerer.services.search.adapter.SingleResult#getEntityID()
	 */
	public Long getEntityID() {
		return this.id;
	}

	/**
	 * @return
	 * @see edu.uci.ics.sourcerer.services.search.adapter.SingleResult#getFqn()
	 */
	public String getFqn() {
		return sr.getFqn();
	}

	/**
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return sr.hashCode();
	}

	/**
	 * @return
	 * @see edu.uci.ics.sourcerer.services.search.adapter.SingleResult#getParamCount()
	 */
	public int getParamCount() {
		return sr.getParamCount();
	}

	/**
	 * @return
	 * @see edu.uci.ics.sourcerer.services.search.adapter.SingleResult#getParams()
	 */
	public String getParams() {
		return sr.getParams();
	}

	/**
	 * @return
	 * @see edu.uci.ics.sourcerer.services.search.adapter.SingleResult#getReturnFqn()
	 */
	public String getReturnFqn() {
		return sr.getReturnFqn();
	}

	/**
	 * @param obj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if(obj instanceof MySingleResult){
			MySingleResult msr = (MySingleResult) obj;
			return msr.id.equals(id);
		} else if(obj instanceof SingleResult){
			SingleResult msr = (SingleResult) obj;
			return msr.getEntityID().equals(sr.getEntityID());
		}
		return super.equals(obj);
	}

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String retType = sr.getReturnFqn();
		String name = sr.getFqn();
		String params = sr.getParams();
		String idstr = "["+id+"]";
		return retType+" "+name+" "+params+" "+idstr;
	}

	public MySingleResult(SingleResult sr, Long newID) {
		this(sr);
		this.id = newID;
	}
	
	public MySingleResult(SingleResult sr) {
		this.sr = sr;
	}

	public SingleResult getSingleResult() {
		return sr;
	}

	public boolean compareParams(String[] vetParams) {
		String[] thisParams = sr.getParams().substring(1, sr.getParams().length()).split(",");
		if(vetParams.length!=thisParams.length){
			return false;
		}
		for(int i=0;i<thisParams.length;i+=1){
			if(!thisParams[i].contains(vetParams[i]));{
				return false;
			}
		}
		return true;
	}
	

}
