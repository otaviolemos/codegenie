/**
 * @author Otavio Lemos
 */

package edu.uci.ics.mondego.codegenie.popup.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.JavaCore;

import edu.uci.ics.mondego.codegenie.*;
import edu.uci.ics.mondego.codegenie.composition.SliceOperations;

public class IncludeSliceAction implements IObjectActionDelegate {

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
	public IncludeSliceAction() {
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
		IFile selection = (IFile) ((TreeSelection)currentSelection).getFirstElement();
		
//		getNames(selection);
		
		Shell shell = new Shell();
		
//		InputDialog id1 = new InputDialog(shell, 
//				"Select package", "Type the name of the wanted package:", 
//				"html", null);
//		
//		id1.open();
//		
//		InputDialog id2 = new InputDialog(shell, 
//				"Select class", "Type the name of the wanted class:", 
//				"HTML", null);
//		
//		id2.open();
//		
//		InputDialog id3 = new InputDialog(shell, 
//				"Select source folder", "Type the name of the wanted method:", 
//				"unescape", null);
//		
//		id3.open();
		
		//SearchQueryCreator sqc = new SearchQueryCreator();
		//sqc.formQuery(myTestType)
		
		//String[] names = new String(selection.getName()).replace(".", ":").split(":");
		
		SliceOperations mySlc = new SliceOperations(selection.getName(), JavaCore.create(selection.getProject()));
				
				//substring(0, selection.getName().indexOf(".")),
				
				//id3.getValue(), id2.getValue(), id1.getValue(), JavaCore.create(selection.getProject()));
		try {
		  mySlc.unzipInProject();
		  mySlc.includeInBuild();
		  mySlc.doRenamings();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.setCurrentSelection(selection);
	}
}
