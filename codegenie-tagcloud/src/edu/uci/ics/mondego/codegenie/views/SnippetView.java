package edu.uci.ics.mondego.codegenie.views;

import javax.swing.Scrollable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.ui.part.ViewPart;

public class SnippetView extends ViewPart {

    private Text text;
	
    public SnippetView() {
        super();
    }
    
	public void createPartControl(Composite parent) {
        text = new Text(parent, SWT.V_SCROLL|SWT.MULTI|SWT.WRAP);
        text.setText("No snippet to show.");
        text.setBackground(
        		Display.getDefault().getSystemColor(SWT.COLOR_WHITE)
        );
	}
	
	public void setFocus() {
        text.setFocus();
	}
	
	public void setSnippet(String str) {
		text.setText(str);
	}
}
