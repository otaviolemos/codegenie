/**
 * 
 */
package br.unifesp.ppgcc.aqexperiment.infrastructure;

import org.apache.log4j.Logger;


/**
 * @author Adriano Carvalho
 * @version 27/12/2007
 */
public final class LogUtils {

	public static Logger getLogger(Class<? extends Object> clazz) {

		System.setProperty("log4j.configuration", "log4j.properties");

		Logger LOG = Logger.getLogger(clazz);
		return LOG;
	}

	public static Logger getLogger() {
		Logger LOG = getLogger(LogChange.class);
		return LOG;
	}
}
