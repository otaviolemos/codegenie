package br.unifesp.sjc.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import br.unifesp.sjc.dao.RelatedDAO;
import edu.smu.tspell.wordnet.AdjectiveSynset;
import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.VerbSynset;
import edu.smu.tspell.wordnet.WordNetDatabase;
import edu.smu.tspell.wordnet.WordSense;


public class GetRelatedServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int NOUN_SYNS = 0;
	private static final int VERB_SYNS = 1;
	private static final int ADJ_SYNS = 2;
	private static final int CODE_SYNS = 3;
	private static final int NOUN_ANTS = 4;
	private static final int VERB_ANTS = 5;
	private static final int ADJ_ANTS = 6;
	private static final int CODE_ANTS = 7;

	public GetRelatedServlet() {
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
		
		List<String>[] result = getRelated(toSearch);
		
		response.setContentType("text/xml");
		try {
			PrintWriter out = response.getWriter();
			RelatedSearchResult searchResult = new RelatedSearchResult();
			
			for(String s : result[VERB_SYNS])
			  searchResult.getVerbs().add(s);
			
      for(String s : result[NOUN_SYNS])
        searchResult.getNouns().add(s);
      
      for(String s : result[ADJ_SYNS])
        searchResult.getAdjectives().add(s);
      
      for(String s : result[CODE_SYNS])
        searchResult.getCodeRelatedSyns().add(s);
      
      for(String s : result[VERB_ANTS])
        searchResult.getVerbAntonyms().add(s);
      
      for(String s : result[NOUN_ANTS])
        searchResult.getNounAntonyms().add(s);
      
      for(String s : result[ADJ_ANTS])
        searchResult.getAdjectiveAntonyms().add(s);
      
      for(String s : result[CODE_ANTS])
        searchResult.getCodeRelatedAntons().add(s);
			
			JAXBContext context  = JAXBContext.newInstance(RelatedSearchResult.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(searchResult, out);
	
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	private List<String>[] getRelated(String word) {
	  // fist list is the verb synonyms
	  // second list is the noun synonyms
	  // third list is the adjective synonyms
	  // forth list is the code synonyms
	  // fifth list is the verb antonyms
	  // sixth list is the noun antonyms
	  // seventh list is the adjective antonyms
	  // eighth list is the code antonyms
	  
		
		String wordNetDatabasePath = getServletContext().getRealPath("/WEB-INF/classes/wordnet-database");
		System.setProperty("wordnet.database.dir", wordNetDatabasePath);
		VerbSynset verbSynset;
		NounSynset nounSynset;
		AdjectiveSynset adjSynset;

		if(detectCamel(word))
			word = camelCaseSplit(word);

		WordNetDatabase database = WordNetDatabase.getFileInstance(); 
		Synset[] synsetsV = database.getSynsets(word, SynsetType.VERB);
		Synset[] synsetsN = database.getSynsets(word, SynsetType.NOUN);
		Synset[] synsetsA = database.getSynsets(word, SynsetType.ADJECTIVE);

		List<String> verbSyns = new ArrayList<String>();
		List<String> verbAntonyms = new ArrayList<String>();

		for (int i = 0; i < synsetsV.length; i++) { 
			verbSynset = (VerbSynset)(synsetsV[i]); 
			String[] syns = verbSynset.getWordForms();
			for(int j = 0; j < syns.length; j++) {
				String syn = syns[j];
				if(!verbSyns.contains(syn) && !syn.equals(word)) {
				  if (syn.contains(" ")) 
						syn = camelCaseJoin(syn);
				  cleanAdd(verbSyns,syn);
				}
				WordSense[] ants = verbSynset.getAntonyms(syn);
				for(WordSense w : ants)
				  cleanAdd(verbAntonyms, w.getWordForm());
			}
		}
		
		List<String> nounSyns = new ArrayList<String>();
		List<String> nounAntonyms = new ArrayList<String>();

    for (int i = 0; i < synsetsN.length; i++) { 
      nounSynset = (NounSynset)(synsetsN[i]); 
      String[] syns = nounSynset.getWordForms();
      for(int j = 0; j < syns.length; j++) {
        String syn = syns[j];
        if(!nounSyns.contains(syn) && !syn.equals(word)) {
          if (syn.contains(" ")) 
            syn = camelCaseJoin(syn);
          cleanAdd(nounSyns,syn);
        }
        WordSense[] ants = nounSynset.getAntonyms(syn);
        for(WordSense w : ants)
          cleanAdd(nounAntonyms, w.getWordForm());
      }
    }
    
    List<String> adjSyns = new ArrayList<String>();
    List<String> adjAntonyms = new ArrayList<String>();

    for (int i = 0; i < synsetsA.length; i++) { 
      adjSynset = (AdjectiveSynset)(synsetsA[i]); 
      String[] syns = adjSynset.getWordForms();
      for(int j = 0; j < syns.length; j++) {
        String syn = syns[j];
        if(!adjSyns.contains(syn) && !syn.equals(word)) {
          if (syn.contains(" ")) 
            syn = camelCaseJoin(syn);
          cleanAdd(adjSyns,syn);
        }
        WordSense[] ants = adjSynset.getAntonyms(syn);
        for(WordSense w : ants)
          cleanAdd(adjAntonyms, w.getWordForm());
      }
    }
    
    List<String> codeSyns = new ArrayList<String>();
    List<String> codeAntonyms = new ArrayList<String>();
    
    List<List<String>> codeRelations = RelatedDAO.getRelated(word);
    
    codeSyns = codeRelations.get(0);
    codeAntonyms = codeRelations.get(1);
    
    List<String> ret[] = new List[8];
    
    ret[VERB_SYNS] = verbSyns;
    ret[NOUN_SYNS] = nounSyns;
    ret[ADJ_SYNS] = adjSyns;
    ret[CODE_SYNS] = codeSyns;
    ret[VERB_ANTS] = verbAntonyms;
    ret[NOUN_ANTS] = nounAntonyms;
    ret[ADJ_ANTS] = adjAntonyms;
    ret[CODE_ANTS] = codeAntonyms;
    return ret;
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
	
  private static void cleanAdd(List<String> list, String w) {
    if(!w.contains(".") && !list.contains(w))
      list.add(w);
  }


}
