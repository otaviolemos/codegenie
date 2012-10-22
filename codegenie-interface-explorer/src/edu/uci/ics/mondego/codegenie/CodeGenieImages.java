package edu.uci.ics.mondego.codegenie;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;


public class CodeGenieImages {
	
	public static final IPath ICONS_PATH= new Path("$nl$/icons");
	
	public static final String IMG_GREEN= "methpub_obj.gif";
	public static final String IMG_YELLOW="yellow_method.gif";
	public static final String IMG_RED="red_method.gif";
	public static final String IMG_GREY="compare_method.gif";
	public static final String IMG_RETURN_TYPE="return_type_search.gif";
	public static final String IMG_ARGUMENTS="arguments_search.gif";
	public static final String IMG_NAMES="name_search.gif";
	public static final String IMG_CODEGENIE="lamp_icon.gif";
	
	public static Image getImage(String key) {
		ImageDescriptor id = create(key);
		return id.createImage();
	}
	
	public static ImageDescriptor create(String key) {
		IPath path= ICONS_PATH.append(key);
		Bundle bundle = CodeGeniePlugin.getPlugin().getBundle();
		URL url= FileLocator.find(bundle, path, null);
		if (url != null) {
			return ImageDescriptor.createFromURL(url);
		}
		return null;
	}
}
