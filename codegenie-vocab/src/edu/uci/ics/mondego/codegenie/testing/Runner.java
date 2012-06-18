/**
 * @author Otavio Lemos
 */

package edu.uci.ics.mondego.codegenie.testing;

import org.junit.runner.Result;
import org.junit.runner.JUnitCore;

public class Runner {
	
	public static void run(Class[] classes) {
		Result result = null;
		result = JUnitCore.runClasses(classes);
		System.out.print("Runs: " + result.getRunCount() + 
				"Failures: " + result.getFailureCount() + 
				"Run time: " + result.getRunTime());
	}

}
