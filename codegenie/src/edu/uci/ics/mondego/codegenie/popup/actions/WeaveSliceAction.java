/**
 * @author Otavio Lemos
 */

package edu.uci.ics.mondego.codegenie.popup.actions;

import org.eclipse.jdt.core.*;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.jface.dialogs.InputDialog;

import edu.uci.ics.mondego.codegenie.composition.*;

public class WeaveSliceAction implements IObjectActionDelegate {

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
	public WeaveSliceAction() {
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
		Shell shell = new Shell();
		
		IPackageFragmentRoot selection = 
			(IPackageFragmentRoot) ((TreeSelection)currentSelection).getFirstElement();
		Composition c = new Composition(selection.getJavaProject());
		
		InputDialog id1 = new InputDialog(shell, 
				"Select source folder", "Type the name of the target source folder:", 
				"src", null);
		
		id1.open();
		
		try {
		  c.weave(selection.getElementName(), id1.getValue());
		} catch (Exception e) { 
			
		}

	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.setCurrentSelection(selection);
	}

}
