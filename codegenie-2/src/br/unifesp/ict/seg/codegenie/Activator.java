package br.unifesp.ict.seg.codegenie;

import org.eclipse.jdt.junit.JUnitCore;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import br.unifesp.ict.seg.codegenie.test.CodeGenieTestListener;
import br.unifesp.ict.seg.codegenie.test.CodeGenieTestRunner;
import br.unifesp.ict.seg.codegenie.tmp.Debug;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin implements IStartup{

	// The plug-in ID
	public static final String PLUGIN_ID = "br.unifesp.ict.seg.codegenie-2"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private static long idStore = 0;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	@Override
	public void earlyStartup() {

		CodeGenieTestListener listener = new CodeGenieTestListener();
		CodeGenieTestRunner.setListener(listener);
		JUnitCore.addTestRunListener(listener);

		Debug.debug(getClass(),"starting codegenie-2.0 plugin");
	}

	public static long newID() {
		return ++idStore;
	}
	public static long lastID(){
		return idStore;
	}

}
