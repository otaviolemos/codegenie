package br.unifesp.ict.seg.codegenie.search.solr;

import org.eclipse.jdt.core.IType;

import br.unifesp.ict.seg.codegenie.views.ResultsViewUpdater;

import edu.uci.ics.sourcerer.services.search.adapter.SingleResult;

public class MySingleResult {

	protected Long id;
	protected SingleResult sr;
	private IType testClass;
	private int failures=-1;
	private int errors=-1;
	private int success=-1;
	private boolean woven;
	private ResultsViewUpdater resultsViewUpdater;

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
		String ret =  idstr + " " +retType+" "+name+" "+params;
		if(failures!=-1){
			ret+=" Failures="+failures+", Errors="+errors+", Success="+success;
		}
		if(woven){
			ret+=" - currrently woven";
		} else {
			ret+=" - currrently unwoven";
		}
		return ret;
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
		for(int i=0;i<thisParams.length;i++){
			if(!thisParams[i].contains(vetParams[i])){
				return false;
			}
		}
		return true;
	}
	
	
	public void setTestClass(IType selection) {
		testClass = selection;
	}
	
	public IType getTestClass(){return testClass;}

	public void setResults(int failuresCount, int errorCount, int successCount) {
		this.failures = failuresCount;
		this.errors = errorCount;
		this.success = successCount;
		
	}

	public int getSuccess() {
		return this.success;
	}
	
	public int getTotal(){
		return success+errors+failures;
	}

	public void setUnWoven() {
		this.woven=false;		
	}
	public void setWoven() {
		this.woven=true;		
	}

	public void setResultsViewUpdater(ResultsViewUpdater resultsViewUpdater) {
		this.resultsViewUpdater = resultsViewUpdater;
	}
	
	public ResultsViewUpdater getResultsViewUpdater(){return this.resultsViewUpdater;}

	public boolean isWoven() {
		return this.woven;
	}
	

}
