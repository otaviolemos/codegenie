package br.unifesp.ppgcc.eaq.application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.unifesp.ppgcc.eaq.domain.SearchResult;
import br.unifesp.ppgcc.eaq.infrastructure.ConfigProperties;
import br.unifesp.ppgcc.eaq.infrastructure.FileExtensionsFilter;
import br.unifesp.ppgcc.eaq.infrastructure.LogUtils;


public class Service {

	private List<SearchResult> occurrences = new ArrayList<SearchResult>();

	private FileExtensionsFilter filter = new FileExtensionsFilter();
	private SearchFileService searchFileService = new SearchFileService();
	
	public void executeSearch() throws Exception {
		File file = new File(ConfigProperties.getProperty("textSearch.path"));
		
		if(!file.isDirectory())
			throw new Exception("Invalid folder: " + file.getAbsolutePath());
		
		this.processFolder(file.listFiles());
	}
	
	private void processFolder(File[] files) {
		for (File file : files) {
			if(file.isDirectory() && !this.isIgnoredFolder(file)){
				LogUtils.getLogger().info("Folder: " + file.getAbsolutePath());
				this.processFolder(file.listFiles());
			} else{
				this.processFile(file);
			}
		}
	}

	private boolean isIgnoredFolder(File file){

		if(!file.isDirectory())
			return false;

		String ignoredFolders;
		try {
			ignoredFolders = ConfigProperties.getProperty("textSearch.ignoreFolders.paths").trim();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		String[] ignoredFoldersArray = StringUtils.split(ignoredFolders, ",");
		
		for (String ignoredFolder : ignoredFoldersArray) {
			File testFile = new File(ignoredFolder);
			if (file.getAbsolutePath().equals(testFile.getAbsolutePath())) {
				return true;
			}
		}
		return false;

	}
	
	private void processFile(File file) {
		if(!this.filter.accept(file))
			return;

		this.occurrences.addAll(searchFileService.findOccurrences(file));
	}

	public List<SearchResult> getOccurrrences(){
		return this.occurrences;
	}
}
