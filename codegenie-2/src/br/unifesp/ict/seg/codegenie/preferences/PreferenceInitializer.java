package br.unifesp.ict.seg.codegenie.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import br.unifesp.ict.seg.codegenie.Activator;

import static br.unifesp.ict.seg.codegenie.preferences.PreferenceConstants.*;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(FILE_SERVER, "http://127.0.0.1:8080");
		//store.setDefault(LISTEN_JUNIT, true);
		store.setDefault(RELATED_WORD_SERVER, "http://snake.ics.uci.edu:8080");
		store.setDefault(SLICE_SERVER, "http://127.0.0.1:8080");
		store.setDefault(SOLR_SERVER, "http://127.0.0.1:8080");
		store.setDefault(ANNOTATIONPACKAGE, "slice.annotation");
		store.setDefault(ANNOTATIONCLASS, "FromSlice");
		store.setDefault(ENSYN, true);
		store.setDefault(CODESYN, true);
		store.setDefault(ENANT, true);
		store.setDefault(CODEANT, true);
		store.setDefault(AUTOTEST, 5);
	}

}
