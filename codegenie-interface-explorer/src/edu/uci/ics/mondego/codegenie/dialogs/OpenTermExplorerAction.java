package edu.uci.ics.mondego.codegenie.dialogs;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.actions.ActionDelegate;

public class OpenTermExplorerAction implements 
   IWorkbenchWindowActionDelegate {

  private IWorkbenchWindow window;
  
  public void run(IAction i) {
    TermExplorerDialog dialog = new TermExplorerDialog(new Shell());
    dialog.open();
  }

  public void dispose() {
    // TODO Auto-generated method stub
    
  }

  public void init(IWorkbenchWindow window) {
    this.window = window;
  }

  public void selectionChanged(IAction action, ISelection selection) {
    // TODO Auto-generated method stub
    
  }
  
}
