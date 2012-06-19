package edu.uci.ics.mondego.codegenie.popup.actions;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.xml.bind.JAXBException;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;

import edu.uci.ics.mondego.codegenie.tagclouds.TagCloudCreator;

public class CreateTagCloudAction implements IEditorActionDelegate{

	public void run(IAction action) {
		TagCloudCreator tagCloudCreator = new TagCloudCreator();
		String textSelected="";
		IEditorPart editor =  PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (editor instanceof ITextEditor) {
		  ISelectionProvider selectionProvider = ((ITextEditor)editor).getSelectionProvider();
		  ISelection selection = selectionProvider.getSelection();
		  if (selection instanceof ITextSelection) {
		    ITextSelection textSelection = (ITextSelection)selection;
		    textSelected = textSelection.getText();
		  }
		}
		
	
		try {
//			SynonymsEditor editor2 = new SynonymsEditor();
//			editor2.createEditorWindow(textSelected);
			tagCloudCreator.createTermCloud(textSelected);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void selectionChanged(IAction arg0, ISelection arg1) {
		// TODO Auto-generated method stub
		
	}

	public void setActiveEditor(IAction arg0, IEditorPart arg1) {
		// TODO Auto-generated method stub
		
	}

}
