package br.unifesp.ict.seg.codegenie;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.internal.util.BundleUtility;
import org.osgi.framework.Bundle;

import br.unifesp.ict.seg.codegenie.tmp.Debug;

@SuppressWarnings("restriction")
public class CodeGenieImages {
	
	public static final String REFRESH = "refresh.png";
	public static final String REMOVE = "remove.png";
	public static final String ADD_N_TEST = "insert.png";
	public static final String RED_IMG = "red_method.gif";
	public static final String YELLOW_IMG = "yellow_method.gif";
	public static final String GREEN_IMG = "methpub_obj.gif";
	public static final String VIEW_CODE = "viewcode.png";
	
	@SuppressWarnings("deprecation")
	public static ImageDescriptor getImageDescriptor(String key){
		URL url = null;
		try {
			url = new URL(Activator.getDefault()//.getStateLocation().toFile().toURI().toURL(),
					.getDescriptor().getInstallURL(),
			        "icons"+File.separator+key);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		
		URL fullPathString = BundleUtility.find(bundle, "icons"
				//+File.separator+"16x16"
				+File.separator+key);
		if(fullPathString==null){
			fullPathString=url;
		}
		Debug.debug(CodeGenieImages.class,"creating image: "+fullPathString);
		return ImageDescriptor.createFromURL(fullPathString);
	}

}
