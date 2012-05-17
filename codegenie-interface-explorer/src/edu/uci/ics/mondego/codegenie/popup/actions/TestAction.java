/**
 * @author Otavio Lemos
 */

package edu.uci.ics.mondego.codegenie.popup.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.jdt.core.*;
import org.eclipse.jface.viewers.TreeSelection;
import edu.uci.ics.mondego.codegenie.testing.*;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;

import org.eclipse.jdt.junit.launcher.JUnitLaunchShortcut;

public class TestAction implements IObjectActionDelegate {

		private ISelection currentSelection;
		
		public ISelection getCurrentSelection() {
			return currentSelection;
		}

		public void setCurrentSelection(ISelection currentSelection) {
			this.currentSelection = currentSelection;
		}

		/**
		 * Constructor for Action1.
		 */
		public TestAction() {
			super();
		}

		/**
		 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
		 */
		public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		}

		/**
		 * @see IActionDelegate#run(IAction)
		 */
		public void run(IAction action) {
//			Shell shell = new Shell();
			JUnitLaunchShortcut jls = new JUnitLaunchShortcut();
			
			jls.launch(currentSelection, "run");
			
			
////			InputDialog id2 = new InputDialog(shell, 
////			"Select class", "Type the name of the class to be tested:", 
////			"package.ClassName", null);
////	
////			InputDialog id3 = new InputDialog(shell, 
////			"Select method", "Type the signature of the method to be tested:", 
////			"aMethod(I)Ljava/lang/String;", null);
////
//			IType selection = 
//				(IType) ((TreeSelection)currentSelection).getFirstElement();
////			
////			id2.open();
////			id3.open();
//			
//			JUnitTestRunner tc = new JUnitTestRunner(selection, 
//					"package.ClassName", "aMethod(I)Ljava/lang/String;");
//					//id2.getValue(), 
//					//id3.getValue());
//
//			try {
//				tc.runTests();		  
//			} catch (Exception e) {
//				System.err.println(e.getMessage());
//			}
//			
//			MessageDialog.openInformation(shell, "Test", 
//			"Runs: " +  tc.getTestResult().getRunCount() + " " +
//			"Failures: " + tc.getTestResult().getFailureCount() + " " +
//			"Run time: " + tc.getTestResult().getRunTime() + "\r\r" +
//			"Method coverage percentage:" + "\r" +
//			"Nodes - " + tc.getTestResult().getMethodCoverage(0) + "%" + " " +
//			"Edges - " + tc.getTestResult().getMethodCoverage(1) + "%" + " " +
//			"Uses - " + tc.getTestResult().getMethodCoverage(2) + "%" + "\r\r" +
//			"Class coverage percentage:" + "\r" +
//			"Nodes - " + tc.getTestResult().getClassCoverage(0) + "%" + " " +
//			"Edges - " + tc.getTestResult().getClassCoverage(1) + "%" + " " +
//			"Uses - " + tc.getTestResult().getClassCoverage(2) + "%");

			
		}

		/**
		 * @see IActionDelegate#selectionChanged(IAction, ISelection)
		 */
		public void selectionChanged(IAction action, ISelection selection) {
			this.setCurrentSelection(selection);
		}

}
