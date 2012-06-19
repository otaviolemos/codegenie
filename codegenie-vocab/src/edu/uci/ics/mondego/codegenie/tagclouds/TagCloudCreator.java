package edu.uci.ics.mondego.codegenie.tagclouds;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.zest.cloudio.TagCloud;
import org.eclipse.zest.cloudio.TagCloudViewer;

import edu.uci.ics.mondego.codegenie.CodeGeniePlugin;
import edu.uci.ics.mondego.codegenie.synonyms.SynonymsServices;
import edu.uci.ics.mondego.codegenie.util.JavaTermExtractor;
import edu.uci.ics.mondego.search.model.SearchResult;
import edu.uci.ics.mondego.search.model.SearchResultEntry;
import edu.uci.ics.mondego.wsclient.common.SearchResultUnMarshaller;
/**
 * Cria tela com nuvem de termos.
 * 
 * @author gustavo
 *
 */
public class TagCloudCreator {

	private static final int MIN_WEIGHT = 1;
	private static final int MAX_WEIGHT = 6;
	private String url = CodeGeniePlugin.getPlugin().getSourcererURL();
	private static final int MAX_RESULTS = 100;
	private Shell shell;

	
	/**
	 * Busca palavra no sourcerer
	 * @param word
	 * @return
	 */
	private SearchResult searchInSourcerer(String word) {
		InputStream ins = null;
		try {
			ins = new URL("http://" + url + "/ws-search/search?qry="
//					+ "contents:" + word.replaceAll(" ", "%20") + ")%20"
//					+ "%20OR%20short_name:(" + word.replaceAll(" ", "%20")
					// changing the search based on fqns to only the method name
					+ "short_name:(" + word.replaceAll(" ", "%20")
					+ ")%20" + "&pid=" + 1 + "&epp=" + MAX_RESULTS + "&client=ne")
					.openStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		SearchResult srcResult = SearchResultUnMarshaller
				.unMarshallSearchResults(ins);
		return srcResult;
	}

	private void addTerms(String fqn,List<Term> synonyms) {
		String justFqn = JavaTermExtractor.removeMethodArguments(fqn);

		String[] untokenizedTerms = justFqn.split("[.]");
		double weight = MAX_WEIGHT;
		for (int i = 0; i < untokenizedTerms.length; i++) {
			for (Iterator iterator = synonyms.iterator(); iterator.hasNext();) {
				Term synonym = (Term) iterator.next();
				if (untokenizedTerms[i].contains(
						synonym.getTerm().toUpperCase())) {
					synonym.setWeight(synonym.getWeight() + weight);
					synonym.setHits(synonym.getHits() + 1);
				}
			}
			if (weight > MIN_WEIGHT)
				weight--;
		}

	}
	
	private void drawCloud(final String word,List<Term> synonyms){
		Display display = Display.getDefault();
		shell = new Shell(display);
		RowLayout layout = new RowLayout(SWT.VERTICAL);
		layout.wrap = true;
		layout.fill = true;
		layout.justify = false;
		shell.setLayout(layout);
		TagCloud cloud = new TagCloud(shell, SWT.NONE);
		RowData data = new RowData();
		data.width = 500;
		data.height = 500;
		cloud.setLayoutData(data);
		shell.setBounds(100, 100, 530, 630);
		cloud.setBounds(0,0, shell.getBounds().width, shell.getBounds().height-30);
	    final TagCloudViewer viewer = new TagCloudViewer(cloud);
	    
	    viewer.setContentProvider(new IStructuredContentProvider() {
	           public void dispose() { }
	           public void inputChanged(Viewer viewer, Object oldInput,
	                   Object newInput) {}
	           public Object[] getElements(Object inputElement) {
	               return ((List<?>)inputElement).toArray();
	           }
		});
	    
		Collections.sort(synonyms);
	    double maxWeight = synonyms.get(0).getWeight();
	    
	    viewer.setLabelProvider(new TermLabelProvider(cloud.getFont(),maxWeight));
	 
		viewer.setInput(synonyms);
		
		//CRIA INPUT
		final Text inputSelected = new Text(shell, SWT.SINGLE | SWT.BORDER);
		final Button replaceButton = new Button(shell,SWT.PUSH);
	    inputSelected.setSize(shell.getBounds().width, 20);
	    data = new RowData();
	    data.width = 500;
	    data.height = 20;
	    inputSelected.setLayoutData(data);
	    viewer.addSelectionChangedListener(new ISelectionChangedListener() {
	    	public void selectionChanged(SelectionChangedEvent event) {
	    		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
	    		if(selection.isEmpty()){
	    			inputSelected.setText("");
	    			replaceButton.setEnabled(false);
	    			return;
	    		}
    			replaceButton.setEnabled(true);
	    		String terms = "";
	    		for (Iterator iterator = selection.toList().iterator(); iterator
						.hasNext();) {
					Term selectedTerm = (Term) iterator.next();
					terms += selectedTerm.getTerm()+" ";
				}
				inputSelected.setText(terms.substring(0,terms.length()-1));
	    	}
	    });
	    
	    //CRIA BOTAO
		final Button createOtherCloud = new Button(shell,SWT.PUSH);
		createOtherCloud.setSize(200, 30);
	    data = new RowData();
	    data.width = 200;
	    data.height = 30;
	    createOtherCloud.setLayoutData(data);
	    createOtherCloud.setText("Create tag cloud");
	    createOtherCloud.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent arg0) {
				if(inputSelected.getText().isEmpty())
					return;
				TagCloudCreator newCloud = new TagCloudCreator();
				try {
					newCloud.createTermCloud(inputSelected.getText().replace(" ", "%20"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
	    
	    replaceButton.setEnabled(false);
			replaceButton.setSize(200, 30);
		    data = new RowData();
		    data.width = 200;
		    data.height = 30;
		    replaceButton.setLayoutData(data);
		    replaceButton.setText("Replace term");
		    replaceButton.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent arg0) {
					IEditorPart editor =  PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
					if (editor instanceof ITextEditor) {
					  ISelectionProvider selectionProvider = ((ITextEditor)editor).getSelectionProvider();
					  ISelection selection = selectionProvider.getSelection();
					  if (selection instanceof ITextSelection) {
					    ITextSelection textSelection = (ITextSelection)selection;
					  try {
						((ITextEditor) editor).getDocumentProvider().getDocument(editor.getEditorInput()).replace(textSelection.getOffset(),textSelection.getLength(), inputSelected.getText().toLowerCase());
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
					  }
					}
					shell.close();
				}
				public void widgetDefaultSelected(SelectionEvent event) {
				}
			});

	    
	    shell.setText("Tag cloud for \"" + word + "\".");
		
		shell.open();
	}
	

	public void createTermCloud(String word) throws MalformedURLException, IOException, JAXBException {
		SynonymsServices sySearch = new SynonymsServices();
		List<Term> synonyms = sySearch.searchForSynonyms(word);
		if (synonyms.size() == 0) {
			//TODO: Tratar quando não encontrar sinonimo.
			return;
		}
		synonyms.add(new Term(word, 0,0, false));
		SearchResult sr = searchInSourcerer(JavaTermExtractor.toSearchQueryFormat(synonyms));
		if (sr.getEntries() != null) {
			for (SearchResultEntry sre : sr.getEntries())
				addTerms(sre.getEntityName(),synonyms);
		}
		drawCloud(word, synonyms);
		
	}

}
