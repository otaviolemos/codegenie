package br.unifesp.ict.seg.codegenie.search.slicer;



import org.eclipse.jface.preference.IPreferenceStore;

import br.unifesp.ict.seg.codegenie.Activator;
import br.unifesp.ict.seg.codegenie.preferences.PreferenceConstants;

public class SliceAddedAnn {

	Long eid;
	
	public SliceAddedAnn(Long eid) {
		this.eid=eid;
	}
	
	public String toString(){
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String clazz = store.getString(PreferenceConstants.ANNOTATIONCLASS);
		String ret = "@"+clazz+"(entityID="+eid+")"+System.lineSeparator();
		return ret;
	}
	
	public String fqn(){
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String pck = store.getString(PreferenceConstants.ANNOTATIONPACKAGE);
		String clazz = store.getString(PreferenceConstants.ANNOTATIONCLASS);
		return pck+"."+clazz;
	}
	
	public String getImport(){
		return "import "+fqn()+";"+System.lineSeparator();
	}

	
	
}
