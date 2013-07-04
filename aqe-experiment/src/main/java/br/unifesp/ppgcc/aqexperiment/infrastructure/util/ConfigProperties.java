package br.unifesp.ppgcc.aqexperiment.infrastructure.util;

import java.net.URL;
import java.util.Properties;

public class ConfigProperties {

	private static Properties properties;

	private ConfigProperties() {
	}

	private static void loadProperties() throws Exception {
		properties = new Properties();
        URL url = ClassLoader.getSystemResource("config.properties");
        properties.load(url.openStream());
	}

	private static Properties getProperties() throws Exception {
		loadProperties();
		return properties;
	}
	
	public static String getProperty(String key) throws Exception {
		return getProperties().getProperty(key);
	}
}