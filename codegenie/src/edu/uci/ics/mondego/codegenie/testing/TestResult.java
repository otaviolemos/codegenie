/**
 * @author Otavio Lemos
 */

package edu.uci.ics.mondego.codegenie.testing;

public class TestResult  {
	
	private int runCount;
	private int failureCount;
	private long runTime;
	private float[] methodCoverage = new float[criteriaNumber];
	private float[] classCoverage = new float[criteriaNumber];
	
	public static final int NODE = 0;
	public static final int EDGE = 1;
	public static final int USE = 2;
	public static final int criteriaNumber = 3;
	
	public TestResult() {
		runCount = 0;
		failureCount = 0;
		runTime = 0;
		
		for(int i = 0; i < criteriaNumber; i++) {
	       methodCoverage[i] = 0;
	       classCoverage[i] = 0;
	    } 
	}
	
	public void setMethodCoverage(float cov, int criterion) {
		methodCoverage[criterion] = cov;
	}
	
	public void setClassCoverage(float cov, int criterion) {
		classCoverage[criterion] = cov;
	}
	
	public float getMethodCoverage(int criterion) {
		return methodCoverage[criterion];
	}
	
	public float getClassCoverage(int criterion) {
		return classCoverage[criterion];
	}
	
	public long getRunTime() {
		return runTime;
	}

	public void setRunTime(long runTime) {
		this.runTime = runTime;
	}
	
	public int getFailureCount() {
		return failureCount;
	}
	public void setFailureCount(int failureCount) {
		this.failureCount = failureCount;
	}
	public int getRunCount() {
		return runCount;
	}
	public void setRunCount(int runCount) {
		this.runCount = runCount;
	}

}
