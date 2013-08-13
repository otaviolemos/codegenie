package br.unifesp.ict.seg.codegenie.test;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.junit.launcher.JUnitLaunchShortcut;
import org.eclipse.jface.viewers.StructuredSelection;

import br.unifesp.ict.seg.codegenie.search.solr.MySingleResult;
import br.unifesp.ict.seg.codegenie.tmp.Debug;


public class CodeGenieTestRunner {

	private IType test;
	private static CodeGenieTestListener listener;

	public static CodeGenieTestListener getListener(){
		return listener;
	}

	public CodeGenieTestRunner(IType testClass) {
		this.test = testClass;
	}

	public void runTest(MySingleResult sr) {
		IJavaElement jtest = test.getPrimaryElement();
		StructuredSelection iss = new StructuredSelection(jtest);
		JUnitLaunchShortcut js = new JUnitLaunchShortcut();
		listener.setObservers(sr);
		js.launch(iss, "run");
		Debug.debug(getClass(),"junit launched");
	}

	public static void setListener(CodeGenieTestListener listener) {
		CodeGenieTestRunner.listener = listener;
	}

	

}
