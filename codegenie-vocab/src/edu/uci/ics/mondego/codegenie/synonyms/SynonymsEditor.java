package edu.uci.ics.mondego.codegenie.synonyms;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.xml.bind.JAXBException;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;

import edu.uci.ics.mondego.codegenie.tagclouds.TagCloudCreator;
import edu.uci.ics.mondego.codegenie.tagclouds.Term;
import edu.uci.ics.mondego.codegenie.util.JavaTermExtractor;
/**
 * Tela de edição de sinônimos
 * @author gustavo.konishi
 */
public class SynonymsEditor {

	private Shell shell;

	//TODO: Código esta replicado aqui e no SynonymListEditor. Muita coisa pode ser reaproveitada
	public void createEditorWindow(final String word) throws MalformedURLException, IOException, JAXBException {
		
		final java.util.List<Term> synonyms = new SynonymsServices().searchForSynonyms(word);
		final java.util.List<String> toSave = new ArrayList<String>();
		final java.util.List<String> toRemove = new ArrayList<String>();
		final java.util.List<String> terms = JavaTermExtractor.toStringList(synonyms);
		Display display = Display.getDefault();
		shell = new Shell(display);
		RowLayout layout = new RowLayout(SWT.VERTICAL);
		layout.wrap = true;
		layout.fill = true;
		layout.justify = false;
		shell.setLayout(layout);
		shell.setBounds(100, 100,330,530);
		final List list = new List (shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	    list.setItems(terms.toArray(new String[0]));
	    RowData data = new RowData();
	    data.width = 300;
	    data.height = 300;
	    list.setLayoutData(data);
	    
	    final Text termToAdd = new Text(shell, SWT.SINGLE | SWT.BORDER);
	    termToAdd.setSize(shell.getBounds().width, 20);
	    data = new RowData();
	    data.width = 300;
	    data.height = 20;
	    termToAdd.setLayoutData(data);
	    
	    //CRIA BOTAO
		final Button addTerm = new Button(shell,SWT.PUSH);
		addTerm.setSize(200, 30);
	    data = new RowData();
	    data.width = 200;
	    data.height = 30;
	    addTerm.setLayoutData(data);
	    addTerm.setText("Adicionar");
	    addTerm.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent arg0) {
				if(termToAdd.getText().isEmpty())
					return;
				try {
					terms.add(termToAdd.getText());
					toSave.add(termToAdd.getText());
				    list.setItems(terms.toArray(new String[0]));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
	    
	    //CRIA BOTAO Remove
		final Button removeButton = new Button(shell,SWT.PUSH);
		removeButton.setSize(200, 30);
	    data = new RowData();
	    data.width = 200;
	    data.height = 30;
	    removeButton.setLayoutData(data);
	    removeButton.setText("Remover");
	    removeButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent arg0) {
				String[] selected = list.getSelection();
				if(!isRemovable(Arrays.asList(selected),synonyms)){
					return;
				}
				for (int i = 0; i < selected.length; i++) {
					toRemove.add(selected[i]);
					terms.remove(selected[i]);
					toSave.remove(selected[i]);
				}
			    list.setItems(terms.toArray(new String[0]));
			}
			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
	    
    
	    
	    //CRIA BOTAO SAVE
		final Button saveButton = new Button(shell,SWT.PUSH);
		saveButton.setSize(200, 30);
	    data = new RowData();
	    data.width = 200;
	    data.height = 30;
	    saveButton.setLayoutData(data);
	    saveButton.setText("Salvar");
	    saveButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent arg0) {
				SynonymsServices delegate = new SynonymsServices();
				delegate.saveSynonyms(word, toSave);
				delegate.removeSynonyms(word,toRemove);
				//Cria tagcloud depois que salva
//				TagCloudCreator newCloud = new TagCloudCreator();
//				try {
//					newCloud.createTermCloud(word.replace(" ", "%20"));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
				shell.close();
			}
			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
	    
	    shell.setText("Sinônimos do termo " + word + "\".");
		
		shell.open();
	}

	
	protected boolean isRemovable(java.util.List<String> toRemove, java.util.List<Term> synonyms) {
		for (Iterator iterator = toRemove.iterator(); iterator.hasNext();) {
			String termToRemove = (String) iterator.next();
			for (Iterator iterator2 = synonyms.iterator(); iterator2.hasNext();) {
				Term term = (Term) iterator2.next();
				if(term.getTerm().equals(termToRemove)&& !term.isRemovable())
					return false;
			}
		}
		return true;
		
	}

}
