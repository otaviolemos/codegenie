package edu.uci.ics.mondego.codegenie.synonyms;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.xml.bind.JAXBException;

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


import edu.uci.ics.mondego.codegenie.tagclouds.Term;
import edu.uci.ics.mondego.codegenie.util.JavaTermExtractor;
/**
 * Tela de busca e edição de sinônimos
 * @author gustavo.konishi
 *
 */
public class SynonymsListEditor {

	private Shell shell;
	private java.util.List<Term> synonyms = new ArrayList<Term>();
	private java.util.List<String> terms = new ArrayList<String>();

	public void createEditorWindow() throws MalformedURLException, IOException, JAXBException {
		synonyms = new ArrayList<Term>();
	    RowData data = new RowData();
		Display display = Display.getDefault();
		shell = new Shell(display);
		final java.util.List<String> toSave = new ArrayList<String>();
		final java.util.List<String> toRemove = new ArrayList<String>();
		RowLayout layout = new RowLayout(SWT.VERTICAL);
		layout.wrap = true;
		layout.fill = true;
		layout.justify = false;
		shell.setLayout(layout);
		shell.setBounds(100, 100,330,570);
		

	    final Text termToSearch = new Text(shell, SWT.SINGLE | SWT.BORDER);
		final Button searchTerm = new Button(shell,SWT.PUSH);
		final List list = new List (shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	    final Text termToAdd = new Text(shell, SWT.SINGLE | SWT.BORDER);
		final Button addTerm = new Button(shell,SWT.PUSH);
		final Button removeButton = new Button(shell,SWT.PUSH);
		final Button saveButton = new Button(shell,SWT.PUSH);
	    termToSearch.setSize(shell.getBounds().width, 20);
	    data = new RowData();
	    data.width = 300;
	    data.height = 20;
	    termToSearch.setLayoutData(data);
	    
	    //CRIA BOTAO
	    data = new RowData();
	    data.width = 200;
	    data.height = 30;
	    searchTerm.setLayoutData(data);
	    searchTerm.setText("Buscar Sinônimos");
	    searchTerm.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent arg0) {
				if(termToSearch.getText().isEmpty())
					return;
				try {
					synonyms = new SynonymsServices().searchForSynonyms(termToSearch.getText());
					terms = JavaTermExtractor.toStringList(synonyms);
				    list.setItems(terms.toArray(new String[0]));
					saveButton.setEnabled(true);
					removeButton.setEnabled(true);
					addTerm.setEnabled(true);
					termToAdd.setEditable(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
		

	    data = new RowData();
	    list.setItems(terms.toArray(new String[0]));
	    data.width = 300;
	    data.height = 300;
	    list.setLayoutData(data);
	    termToAdd.setSize(shell.getBounds().width, 20);
	    data = new RowData();
	    data.width = 300;
	    data.height = 20;
	    termToAdd.setLayoutData(data);
	    termToAdd.setEditable(false);
	    
	    //CRIA BOTA
		addTerm.setSize(200, 30);
	    data = new RowData();
	    data.width = 200;
	    data.height = 30;
	    addTerm.setLayoutData(data);
	    addTerm.setText("Inserir");
	    addTerm.setEnabled(false);
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
		removeButton.setSize(200, 30);
	    data = new RowData();
	    data.width = 200;
	    data.height = 30;
	    removeButton.setLayoutData(data);
	    removeButton.setText("Remover");
	    removeButton.setEnabled(false);
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
	    
    
	    
	    //CRIA BOTAO SAVe
		saveButton.setSize(200, 30);
	    data = new RowData();
	    data.width = 200;
	    data.height = 30;
	    saveButton.setLayoutData(data);
	    saveButton.setText("Salvar");
	    saveButton.setEnabled(false);
	    saveButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent arg0) {
				SynonymsServices delegate = new SynonymsServices();
				delegate.saveSynonyms(termToSearch.getText(), toSave);
				delegate.removeSynonyms(termToSearch.getText(),toRemove);
				//Cria tagcloud depois que salva
//				TagCloudCreator newCloud = new TagCloudCreator();
//				try {
//					newCloud.createTermCloud(word.replace(" ", "%20"));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
				saveButton.setEnabled(false);
				removeButton.setEnabled(false);
				addTerm.setEnabled(false);
				termToAdd.setEditable(false);
			}
			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});

	    shell.setText("Editor de Sinônimos");
		
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
