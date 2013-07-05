package br.unifesp.ppgcc.eaq.infrastructure;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.lang.StringUtils;

public class FileExtensionsFilter implements FileFilter {

	public boolean accept(File file) {

		String fileExtensions;
		try {
			fileExtensions = ConfigProperties.getProperty("textSearch.fileExtensions").trim();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		String[] okFileExtensions = StringUtils.split(fileExtensions, ",");
		
		for (String extension : okFileExtensions) {
			if (file.getName().toLowerCase().endsWith("."+extension)) {
				return true;
			}
		}
		return false;
	}
}