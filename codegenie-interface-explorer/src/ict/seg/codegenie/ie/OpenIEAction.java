/**
 * @author Rafael B. Januzi
 * @date 11/10/2011
 */

package ict.seg.codegenie.ie;

import ict.seg.codegenie.ie.userInterface.InterfaceExplorerUserInterface;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import edu.uci.ics.mondego.codegenie.search.SearchQueryCreator;


public class OpenIEAction implements IObjectActionDelegate{
	
	private ISelection currentSelection;
	
	public ISelection getCurrentSelection() {
		return currentSelection;
	}

	public void setCurrentSelection(ISelection currentSelection) {
		this.currentSelection = currentSelection;
	}
	
	public void selectionChanged(IAction action, ISelection selection) {
		this.setCurrentSelection(selection);
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub
	}
	
	public OpenIEAction() {
		super();
	}
	
	public void run(IAction action) {
		
		IType selection = (IType) ((TreeSelection)currentSelection).getFirstElement();
		
		IJavaProject jp = selection.getJavaProject();
		
		SearchQueryCreator sqc = new SearchQueryCreator();
		String[] wantedMethodInterface = sqc.formQuery(selection);
		
		/**
		 * wantedMethodInterface[0] = packageName
		 * wantedMethodInterface[1] = className
		 * wantedMethodInterface[2] = methodName
		 * wantedMethodInterface[3] = returnName
		 * wantedMethodInterface[4] = parametersNames : "(type1,type2"
		 */
		
		InterfaceExplorerUserInterface ie = new InterfaceExplorerUserInterface();
		
		ie.openIE(wantedMethodInterface[1], wantedMethodInterface[3], wantedMethodInterface[2], wantedMethodInterface[4].substring(1), jp, currentSelection);
		
	}


}
