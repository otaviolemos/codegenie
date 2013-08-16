package br.unifesp.ict.seg.codegenie.preferences;


import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import br.unifesp.ict.seg.codegenie.Activator;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class CodeGeniePreferences extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public CodeGeniePreferences() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("CodeGenie preference page");
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		//add fields
		//file server
		StringFieldEditor fileServer = 
				new StringFieldEditor(PreferenceConstants.FILE_SERVER,
						"File Server",getFieldEditorParent());
		//related words
		StringFieldEditor relatedWordServer = 
				new StringFieldEditor(PreferenceConstants.RELATED_WORD_SERVER,
						"Related Words Server",getFieldEditorParent());
		//slice server
		StringFieldEditor sliceServer = 
				new StringFieldEditor(PreferenceConstants.SLICE_SERVER,
						"Slice Server",getFieldEditorParent());
		//solr server
		StringFieldEditor solrServer = 
				new StringFieldEditor(PreferenceConstants.SOLR_SERVER,
						"Solr server",getFieldEditorParent());
		//annotation package name
		StringFieldEditor annPack = 
				new StringFieldEditor(PreferenceConstants.ANNOTATIONPACKAGE,
						"Annotation package",getFieldEditorParent());
		//annotation package name
		StringFieldEditor annClass = 
				new StringFieldEditor(PreferenceConstants.ANNOTATIONCLASS,
						"Annotation Class",getFieldEditorParent());
		//listen to junit?
		//BooleanFieldEditor listenJUnit = 
		//		new BooleanFieldEditor(PreferenceConstants.LISTEN_JUNIT,
		//				"Listen to JUnit",getFieldEditorParent());
		BooleanFieldEditor ensyn = 
				new BooleanFieldEditor(PreferenceConstants.ENSYN,
						"Use english synonyms",getFieldEditorParent());
		BooleanFieldEditor enant = 
				new BooleanFieldEditor(PreferenceConstants.ENANT,
						"Use english antonyms",getFieldEditorParent());
		BooleanFieldEditor codesyn = 
				new BooleanFieldEditor(PreferenceConstants.CODESYN,
						"Use code synonyms",getFieldEditorParent());
		BooleanFieldEditor codeant = 
				new BooleanFieldEditor(PreferenceConstants.CODEANT,
						"Use code antonyms",getFieldEditorParent());
		addField(fileServer);
		addField(relatedWordServer);
		addField(sliceServer);
		addField(solrServer);
		addField(annPack);
		addField(annClass);
		addField(ensyn);
		addField(enant);
		addField(codesyn);
		addField(codeant);
		//addField(listenJUnit);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
}