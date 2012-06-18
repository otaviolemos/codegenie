package edu.uci.ics.mondego.codegenie.search.results;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import edu.uci.ics.mondego.codegenie.search.SearchResultEntryWrapper;
import edu.uci.ics.mondego.codegenie.testing.TestResult;

public class CodeGenieSorter extends ViewerSorter {
	
	static final int jUnit = 1;
	static final int sourcerer = 0;
	static final int time = 2;

	public int compare(Viewer viewer, Object e1, Object e2) {
		double[] property1= getProperty(e1);
		double[] property2= getProperty(e2);
		
		//junit ordering
		if(property1[jUnit] != property2[jUnit]) {
			if (property1[jUnit] < property2[jUnit]) return 1;
			else if (property1[jUnit] > property2[jUnit]) return -1;
			else return 0;
		} else {
			if(property1[jUnit]!= 0) {
				if (property1[time] > property2[time]) return 1;
				else if (property1[time] <= property2[time]) return -1;
			} 
			if (property1[sourcerer] < property2[sourcerer]) return 1;
			else if (property1[sourcerer] > property2[sourcerer]) return -1;
			else return 0;
		}
	}

	protected double[] getProperty(Object element) {
		double[] rank = new double[3];
		rank[0] = 0;
		rank[1] = 0;
		rank[2] = 0;
		double sourcererRank;
		double junitValue;
		double elapsedTime;
		//double coverageValue;
		if (element instanceof SearchResultEntryWrapper) {
			sourcererRank = ((SearchResultEntryWrapper)element).getEntry().getRank();
			junitValue = 0;
			TestResult tr = ((SearchResultEntryWrapper)element).getTestResult();			
			if (tr != null) {
				if (tr.getFailureCount() > 0)
					junitValue = -1;
				else junitValue = 1;
				elapsedTime = tr.getRunTime();
				rank[2] = elapsedTime;
			}
			rank[0] = sourcererRank;
			rank[1] = junitValue;
			return rank;
		}
		return rank; //$NON-NLS-1$
	}
	
	
}
