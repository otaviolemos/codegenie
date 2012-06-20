package br.unifesp.sjc.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.VerbSynset;
import edu.smu.tspell.wordnet.WordNetDatabase;


public class GetSynonymsServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GetSynonymsServlet() {
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) {
		String toSearch = request.getParameter("word");
		
		List<String> result = getSynonyms(toSearch);
		
		response.setContentType("text/xml");
		try {
			PrintWriter out = response.getWriter();

			SynonymsSearchResult searchResult = new SynonymsSearchResult();
			for (String synonym : result) 
				searchResult.getSynonyms().add(synonym);
			JAXBContext context  = JAXBContext.newInstance(SynonymsSearchResult.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(searchResult, out);
	
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	private List<String> getSynonyms(String word) {
		System.setProperty("wordnet.database.dir", "/home/sourcerer/WordNet");
		VerbSynset verbSynset;

		if(detectCamel(word))
			word = camelCaseSplit(word);

		WordNetDatabase database = WordNetDatabase.getFileInstance(); 
		Synset[] synsetsV = database.getSynsets(word, SynsetType.VERB);

		List<String> synonymStrings = new ArrayList<String>();

		for (int i = 0; i < synsetsV.length; i++) { 
			verbSynset = (VerbSynset)(synsetsV[i]); 
			String[] syns = verbSynset.getWordForms();
			for(int j = 0; j < syns.length; j++) {
				String syn = syns[j];
				if(!synonymStrings.contains(syn) && !syn.equals(word)) {
					if (syn.contains(" ")) 
						syn = camelCaseJoin(syn);
					else 
						synonymStrings.add(syn);
				}
			}
		}
	
		return synonymStrings;
	}

	private String camelCaseSplit(String word) {
		return word.replaceAll(
				String.format("%s|%s|%s",
						"(?<=[A-Z])(?=[A-Z][a-z])",
						"(?<=[^A-Z])(?=[A-Z])",
						"(?<=[A-Za-z])(?=[^A-Za-z])"
						),
						" "
				);
	}

	private boolean detectCamel(String word) {
		boolean switched = false;
		boolean lastLow = false;
		for(int i = 0; i < word.length(); i++) {
			if(Character.isLowerCase(word.charAt(i)))
				lastLow = true;
			if(Character.isUpperCase(word.charAt(i)) && lastLow) 
				switched = true;
		}
		return switched;
	}

	public static String camelCaseJoin(String s) {
		String result = new String();
		StringTokenizer tkn = new StringTokenizer(s);
		String second;
		while(tkn.hasMoreTokens()) {
			result += tkn.nextToken();
			if(tkn.hasMoreTokens()) {
				second = tkn.nextToken();
				result += second.substring(0,1).toUpperCase();
				result += second.substring(1);
			}
		}
		return result;
	}


}
