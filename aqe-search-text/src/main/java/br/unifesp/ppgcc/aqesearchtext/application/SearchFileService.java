package br.unifesp.ppgcc.aqesearchtext.application;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;

import br.unifesp.ppgcc.aqesearchtext.domain.SearchResult;
import br.unifesp.ppgcc.aqesearchtext.domain.SearchTerm;
import br.unifesp.ppgcc.aqesearchtext.infrastructure.LogUtils;
import br.unifesp.ppgcc.aqesearchtext.infrastructure.Setup;

public class SearchFileService {

	public List<SearchResult> findOccurrences(File file) {
		List<SearchResult> occurrences = new ArrayList<SearchResult>();
		
		Path path = Paths.get(file.getAbsolutePath());
		int lineNumber = 0;
		try{
			String line = null;
			Scanner scanner =  new Scanner(path, StandardCharsets.UTF_8.name());
			while(scanner.hasNextLine()){
				line = scanner.nextLine();
				this.addLineOccurrences(++lineNumber, line, file, occurrences);
			}
			scanner.close();
		}catch(IOException e){
			LogUtils.getLogger().error("Read file \""+ file.getAbsolutePath() +  "\" error: " + e.getMessage());
			LogUtils.getLogger().error("Line " + (++lineNumber));
		}
		
		return occurrences;
	}
	
	private void addLineOccurrences(int lineNumber, String line, File file, List<SearchResult> occurrences){
		for(SearchTerm searchTerm : Setup.getSearchTerms())
			for(String term : searchTerm.getOrTerms())
				if(StringUtils.indexOfIgnoreCase(line, term) >= 0)
					occurrences.add(new SearchResult(searchTerm, term, file, line, lineNumber));
	}
}
