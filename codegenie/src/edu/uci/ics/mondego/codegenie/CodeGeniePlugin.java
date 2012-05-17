/**
 * @author Otavio Lemos
 */

package edu.uci.ics.mondego.codegenie;

import java.io.File;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.HashMap;

import org.eclipse.core.resources.ISavedState;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import edu.uci.ics.mondego.codegenie.preferences.PreferenceConstants;
import edu.uci.ics.mondego.codegenie.testing.CodeGenieTestRunListener;;

public class CodeGeniePlugin extends AbstractUIPlugin {
	
	private static CodeGeniePlugin plugin;
	private static CodeGeniePluginStore store;
	private static HashMap<String, Long> projectSliceMap = new HashMap<String, Long>
		(149,0.75f);
	private static final String sliceDirName = "sliced";
	
	public static String getSliceDirName() {
		return sliceDirName;
	}
	
	
	//Resource bundle.
	private ResourceBundle resourceBundle;
	
	final static public String PLUGIN_ID = "codegenie";  
	
	public CodeGeniePlugin() {
		super();
		plugin = this;
		try {
			resourceBundle = ResourceBundle.getBundle("codegenie.CodeGeniePluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}
	
	public static String getResourceString(String key) {
		ResourceBundle bundle = CodeGeniePlugin.getPlugin().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}
	
	public static CodeGeniePlugin getPlugin() {
		return plugin;
	}
	
	public CodeGeniePluginStore getStore() {
		return store;
	}
	
	public void start(BundleContext context) throws Exception 
	{
		super.start(context);

		store = new CodeGeniePluginStore();
		ISavedState lastState = ResourcesPlugin.getWorkspace().addSaveParticipant(this, store);

        if (lastState == null)
        {
           return;
        }
        IPath location = lastState.lookup(new Path("save"));

        if (location == null)
        {
           return;
        }
        // the plugin instance should read any important state from the file.
        File f = getStateLocation().append(location).toFile();
        store.readState(f);
        
        org.eclipse.jdt.junit.JUnitCore.addTestRunListener(new CodeGenieTestRunListener());
        getPreferenceStore().setDefault(PreferenceConstants.SOURCERER_SERVER, 
            "tagus.ics.uci.edu:8080/sourcerer");
	}
	
	
	public String getSourcererURL() {
    return getPreferenceStore().getString(PreferenceConstants.SOURCERER_SERVER);
	}

	public static HashMap<String, Long> getProjectSliceMap() {
		return projectSliceMap;
	}

	public static void setProjectSliceMap(HashMap<String, Long> projectSliceMap) {
		CodeGeniePlugin.projectSliceMap = projectSliceMap;
	}

}
