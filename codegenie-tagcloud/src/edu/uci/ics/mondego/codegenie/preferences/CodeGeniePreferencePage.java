package edu.uci.ics.mondego.codegenie.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import edu.uci.ics.mondego.codegenie.CodeGeniePlugin;

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

public class CodeGeniePreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public CodeGeniePreferencePage() {
		super(GRID);
		setPreferenceStore(CodeGeniePlugin.getPlugin().getPreferenceStore());
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
    
    StringFieldEditor sourcererAddress = new StringFieldEditor(
        PreferenceConstants.SOURCERER_SERVER, 
        "&Sourcerer Server", 
        getFieldEditorParent());
    
    addField(sourcererAddress);
		
//		addField(
//			new BooleanFieldEditor(
//				PreferenceConstants.RETURN_TYPE,
//				"&Return type",
//				getFieldEditorParent()));
//		
//		addField(new RadioGroupFieldEditor(
//				PreferenceConstants.RET_TERM_EXACT,
//			"Priceseness for the return type",
//			1,
//			new String[][] { { "&Term", "term" }, {
//				"&Exact", "exact" }
//		}, getFieldEditorParent()));
//		
//
//		addField(
//				new BooleanFieldEditor(
//					PreferenceConstants.NAME,
//					"&Name",
//					getFieldEditorParent()));
//		
//		addField(new RadioGroupFieldEditor(
//				PreferenceConstants.NAME_TERM_EXACT,
//			"Priceseness for the name",
//			1,
//			new String[][] { { "&Term", "term" }, {
//				"&Exact", "exact" }
//		}, getFieldEditorParent()));
//
//
//		addField(
//				new BooleanFieldEditor(
//					PreferenceConstants.ARGUMENTS,
//					"&Arguments",
//					getFieldEditorParent()));
//				
//		addField(new RadioGroupFieldEditor(
//				PreferenceConstants.ARGS_TERM_EXACT,
//			"Priceseness for the arguments",
//			1,
//			new String[][] { { "&Term", "term" }, {
//				"&Exact", "exact" }
//		}, getFieldEditorParent()));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
//  
//  public void performDefaults() {
//    this.getPreferenceStore().setToDefault(PreferenceConstants.SOURCERER_SERVER);
//  }
	
}