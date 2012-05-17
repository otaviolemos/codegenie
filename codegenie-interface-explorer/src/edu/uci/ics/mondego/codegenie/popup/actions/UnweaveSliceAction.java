/**
 * @author Otavio Lemos
 */

package edu.uci.ics.mondego.codegenie.popup.actions;

import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import edu.uci.ics.mondego.codegenie.composition.Composition;

public class UnweaveSliceAction implements IObjectActionDelegate {

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
	public UnweaveSliceAction() {
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
				"Select name of the slice", "Type the name of the slice to be detached:", 
				"sliced1", null);
		
		id1.open();
		
		try {
		  c.unweave(selection.getElementName(), id1.getValue(), true);
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
