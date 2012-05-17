package edu.uci.ics.mondego.codegenie.testing;

import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.junit.model.ITestElement;
import org.eclipse.jdt.junit.model.ITestRunSession;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.IProject;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import edu.uci.ics.mondego.codegenie.search.SearchResultEntryWrapper;
import edu.uci.ics.mondego.codegenie.search.results.TDSearchResultPage;
import edu.uci.ics.mondego.codegenie.testing.TestResult;
import edu.uci.ics.mondego.codegenie.CodeGeniePlugin;
import org.eclipse.swt.widgets.Display;

import org.eclipse.search.ui.text.Match;

public class CodeGenieTestRunListener extends TestRunListener {
	
	private int failureCount = 0;
	private static long id;
	private int testCount = 0;
	private String projectName = null;
	private long elapsedTime = 0;

	public void testFailed(int status, String testId, String testName,
			String trace) {
		failureCount++;
	}

	public void sessionFinished(ITestRunSession session) {
	  if (!session.getProgressState().equals(ITestElement.ProgressState.COMPLETED))
	    return;
    Long id = CodeGeniePlugin.getProjectSliceMap().get(this.projectName);
    CodeGenieTestRunListener.id = id;

    if (id != null) {
      TestResult t = new TestResult();
      t.setFailureCount(failureCount);
      t.setRunCount(testCount);
      t.setRunTime(elapsedTime);
      
      SearchResultEntryWrapper sre = CodeGeniePlugin.getPlugin().getStore().getRepositoryStore().getEntry(
              CodeGenieTestRunListener.id);
      sre.setTestResult(t);
      CodeGeniePlugin.getPlugin().getStore().getRepositoryStore().changeEntry(id, sre);
      
      Display.getDefault().syncExec(new Runnable() {
                 public void run() {
                   SearchResultEntryWrapper sre = CodeGeniePlugin.getPlugin().getStore().getRepositoryStore().getEntry(
                    CodeGenieTestRunListener.id);
                   ((TDSearchResultPage)NewSearchUI.getSearchResultView().getActivePage()).update(sre);
                 }
          });
    }
    
    // re-initialize fields
    failureCount = 0;
    testCount = 0;
    projectName = null;
    elapsedTime = 0;
	}

	public void sessionStarted(ITestRunSession session) {
		failureCount = 0;
		this.testCount = 0;
	}
	
	public void testCaseStarted(ITestCaseElement tc) {
	  this.testCount++;

	  if (projectName == null) {
      String testClassName = tc.getTestClassName();
      IWorkspaceRoot myWorkspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
      IJavaProject[] myPrjs = null;
      try {
        myPrjs = JavaCore.create(myWorkspaceRoot).getJavaProjects();
      } catch (JavaModelException e1) {
        e1.printStackTrace();
      }
      
      for(int i = 0; i < myPrjs.length; i++) {
        try {
          IType myType = myPrjs[i].findType(testClassName);
          if(myType != null && myType.exists()) {
            this.projectName = myType.getJavaProject().getProject().getName();
            break;
          }
        } catch (JavaModelException e1) {
          e1.printStackTrace();
        }
      }
    }
	  
	}
	
	 public void testCaseFinished(ITestCaseElement testCaseElement) {
	   if (testCaseElement.getTestResult(false).equals(ITestCaseElement.Result.FAILURE))
	     failureCount++;
	 }

}
