package br.unifesp.ppgcc.aqesearchtext.infrastructure;


import org.apache.log4j.Logger;

/**
 * @author Adriano Carvalho
 * @version 27/12/2007
 */
public final class LogChange {

	public static void log() {

		System.setProperty("log4j.configuration", "log4j.properties");

		Logger LOG = Logger.getLogger(LogChange.class);
		
		LOG.debug("debug");
		LOG.info("info");
		LOG.warn("warn");
		LOG.error("error");
		LOG.fatal("fatal");
	}
}
