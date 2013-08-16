package br.unifesp.ict.seg.codegenie.test;

import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.junit.model.ITestRunSession;
import org.eclipse.jdt.junit.model.ITestElement.Result;

import br.unifesp.ict.seg.codegenie.search.solr.MySingleResult;
import br.unifesp.ict.seg.codegenie.tmp.Debug;
import br.unifesp.ict.seg.codegenie.views.ResultsView;

public class CodeGenieTestListener extends TestRunListener{


	private int failure=0;
	private int error=0;
	private int success=0;
	private MySingleResult sr;

	public int getFailuresCount(){
		return failure;
	}
	public int getErrorCount(){
		return error;
	}
	public int getSuccessCount(){
		return success;
	}

	public void setObservers(MySingleResult sr){
		this.sr = sr;
	}

	@Override
	public void sessionFinished(ITestRunSession session) {
		Debug.debug(getClass(),"session finished");
		if(sr!=null){
			sr.setResults(getFailuresCount(),getErrorCount(),getSuccessCount());
		}
		ResultsView.getCurrent().canRefresh();
		super.sessionFinished(session);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.junit.TestRunListener#sessionStarted(org.eclipse.jdt.junit.model.ITestRunSession)
	 */
	@Override
	public void sessionStarted(ITestRunSession session) {
		Debug.debug(getClass(),"session started");
		reset();
		super.sessionStarted(session);
	}

	private void reset() {
		error=0;
		failure=0;
		success=0;
		ResultsView.getCurrent().cantRefresh();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.junit.TestRunListener#testCaseFinished(org.eclipse.jdt.junit.model.ITestCaseElement)
	 */
	@Override
	public void testCaseFinished(ITestCaseElement testCaseElement) {
		super.testCaseFinished(testCaseElement);
		if(testCaseElement.getTestResult(true).equals(Result.FAILURE)){
			failure++;
		} else if(testCaseElement.getTestResult(true).equals(Result.ERROR)){
			error++;
		} else if(testCaseElement.getTestResult(true).equals(Result.OK)){
			success++;
		}
	}

}
