package edu.uci.ics.mondego.codegenie.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.dialogs.SelectionStatusDialog;

public class TermExplorerDialog extends SelectionStatusDialog  {
  
  Table fTable;
  
  protected TermExplorerDialog(Shell parentShell) {
    super(parentShell);
    this.setTitle("Term Explorer");
  }
  
  public void create() {
//    Text inputText = new Text(this.getContents().getParent(), SWT.BORDER);
//    inputText.setBounds(10, 10, 200, 20);
//    inputText.setTextLimit(100);
//    List resultList = new List(this.getContents().getParent(), SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
//    resultList.setBounds(20, 10, 200, 200);
    super.create();
  }

  @Override
  protected void computeResult() {
    // TODO Auto-generated method stub
    
  }
  
  protected Control createDialogArea(Composite parent) {
    Composite area = (Composite)super.createDialogArea(parent);
//    fContent = new 
//      TypeSelectionComponent(area, SWT.NONE, getMessage());    
    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
    gd.horizontalSpan = 2;
    fTable = new Table(parent, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER | SWT.FLAT | SWT.NONE);
    fTable.setFont(parent.getFont());
    fTable.setLayoutData(gd);
    return area;
  }
  
  
}
