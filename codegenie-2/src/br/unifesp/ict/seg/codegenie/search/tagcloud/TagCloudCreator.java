package br.unifesp.ict.seg.codegenie.search.tagcloud;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.eclipse.jface.preference.IPreferenceStore;
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

import br.unifesp.ict.seg.codegenie.Activator;
import br.unifesp.ict.seg.codegenie.preferences.PreferenceConstants;
import br.unifesp.ict.seg.codegenie.search.relatedwords.RelatedWordsServices;
import edu.uci.ics.sourcerer.services.search.adapter.SearchResult;
import edu.uci.ics.sourcerer.services.search.adapter.SingleResult;
import edu.uci.ics.sourcerer.services.search.adapter.SearchAdapter;

/**
 * Cria tela com nuvem de termos.
 * 
 * @author gustavo
 * 
 */
public class TagCloudCreator {

  private static final int MIN_WEIGHT = 1;

  private static final int MAX_WEIGHT = 6;


  private static final int MAX_RESULTS = 100;

  private Shell shell;

  
	/**
	 * Busca palavra no sourcerer
	 * 
	 * @param word
	 * @return
	 */
	private SearchResult searchInSourcerer(String word) {
		String query = "sname_contents:(" + word + ")";
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    String url = store.getString(PreferenceConstants.SOLR_SERVER);
		SearchAdapter s = SearchAdapter.create(url);
		SearchResult srcResult = s.search(query);
		return srcResult;
	}

  private void addTerms(String fqn, List<Term> synonyms) {
    String justFqn = JavaTermExtractor.removeMethodArguments(fqn);

    String[] untokenizedTerms = justFqn.split("[.]");
    double weight = MAX_WEIGHT;
    for (int i = 0; i < untokenizedTerms.length; i++) {
      for (Iterator<Term> iterator = synonyms.iterator(); iterator.hasNext();) {
        Term synonym = (Term) iterator.next();
        if (untokenizedTerms[i].toUpperCase().contains(
            synonym.getTerm().toUpperCase())) {
          synonym.setWeight(synonym.getWeight() + weight);
          synonym.setHits(synonym.getHits() + 1);
        }
      }
      if (weight > MIN_WEIGHT)
        weight--;
    }

  }

  private void drawCloud(final String word, List<Term> synonyms) {
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
    shell.setBounds(100, 100, 510, 620);
    cloud.setBounds(0, 0, shell.getBounds().width,
        shell.getBounds().height - 30);
    final TagCloudViewer viewer = new TagCloudViewer(cloud);

    viewer.setContentProvider(new IStructuredContentProvider() {
      public void dispose() {
      }

      public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
      }

      public Object[] getElements(Object inputElement) {
        return ((List<?>) inputElement).toArray();
      }
    });

    Collections.sort(synonyms);
    double maxWeight = synonyms.get(0).getWeight();

    viewer.setLabelProvider(new TermLabelProvider(cloud.getFont(), maxWeight));

    viewer.setInput(synonyms);

    // CRIA INPUT
    final Text inputSelected = new Text(shell, SWT.SINGLE | SWT.BORDER);
    final Button replaceButton = new Button(shell, SWT.PUSH);
    inputSelected.setSize(shell.getBounds().width, 20);
    data = new RowData();
    data.width = 500;
    data.height = 20;
    inputSelected.setLayoutData(data);
    viewer.addSelectionChangedListener(new ISelectionChangedListener() {
      public void selectionChanged(SelectionChangedEvent event) {
        IStructuredSelection selection = (IStructuredSelection) viewer
            .getSelection();
        if (selection.isEmpty()) {
          inputSelected.setText("");
          replaceButton.setEnabled(false);
          return;
        }
        replaceButton.setEnabled(true);
        String terms = "";
        
        for (Iterator<Term> iterator = selection.toList().iterator(); iterator
            .hasNext();) {
          Term selectedTerm = (Term) iterator.next();
          terms += selectedTerm.getTerm() + " ";
        }
        inputSelected.setText(terms.substring(0, terms.length() - 1));
      }
    });

    // CRIA BOTAO
    final Button createOtherCloud = new Button(shell, SWT.PUSH);
    createOtherCloud.setSize(200, 30);
    data = new RowData();
    data.width = 200;
    data.height = 30;
    createOtherCloud.setLayoutData(data);
    createOtherCloud.setText("Create tag cloud");
    createOtherCloud.addSelectionListener(new SelectionListener() {

      public void widgetSelected(SelectionEvent arg0) {
        if (inputSelected.getText().isEmpty())
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
        IEditorPart editor = PlatformUI.getWorkbench()
            .getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        if (editor instanceof ITextEditor) {
          ISelectionProvider selectionProvider = ((ITextEditor) editor)
              .getSelectionProvider();
          ISelection selection = selectionProvider.getSelection();
          if (selection instanceof ITextSelection) {
            ITextSelection textSelection = (ITextSelection) selection;
            try {
              ((ITextEditor) editor)
                  .getDocumentProvider()
                  .getDocument(editor.getEditorInput())
                  .replace(textSelection.getOffset(),
                      textSelection.getLength(),
                      inputSelected.getText().toLowerCase());
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

  public void createTermCloud(String word) throws MalformedURLException,
      IOException, JAXBException {
    String[] words = { word };
    RelatedWordsServices sySearch = new RelatedWordsServices();
    
    if (detectCamel(word))
      words = camelCaseSplit(word);

    List<Term> synonyms = new ArrayList<Term>();
    
    for(String s : words) { 
       synonyms.addAll(sySearch.searchForSynonyms(s));
       synonyms.add(new Term(s, 0, 0, false));
    }
    
    if (synonyms.size() == 0) {
      // TODO: Tratar quando não encontrar sinonimo.
      return;
    }
    
    SearchResult sr = searchInSourcerer(JavaTermExtractor
        .toSearchQueryFormat(synonyms));
    
    if (sr.getNumFound() != 0) {
      for (SingleResult sre : sr.getResults(0, MAX_RESULTS - 1))
        addTerms(JavaTermExtractor.getNameAndParams(sre), synonyms);
    }
    
    drawCloud(word, synonyms);

  }

  private String[] camelCaseSplit(String word) {
    String wordWithSpaces = word.replaceAll(String.format("%s|%s|%s",
        "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])",
        "(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
    return wordWithSpaces.split(" ");
  }

  private boolean detectCamel(String word) {
    boolean switched = false;
    boolean lastLow = false;
    for (int i = 0; i < word.length(); i++) {
      if (Character.isLowerCase(word.charAt(i)))
        lastLow = true;
      if (Character.isUpperCase(word.charAt(i)) && lastLow)
        switched = true;
    }
    return switched;
  }

  
  
}
