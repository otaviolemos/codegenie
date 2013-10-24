package br.unifesp.ppgcc.aqexperiment.infrastructure.util;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.StringUtils;

import br.unifesp.ppgcc.aqexperiment.domain.helper.TagCloudMutantQuery;
import br.unifesp.ppgcc.aqexperiment.domain.helper.TagCloudWord;
import br.unifesp.ppgcc.sourcereraqe.infrastructure.JavaTermExtractor;
import br.unifesp.ppgcc.sourcereraqe.infrastructure.RelatedSearchResult;
import edu.uci.ics.sourcerer.services.search.adapter.SearchAdapter;
import edu.uci.ics.sourcerer.services.search.adapter.SearchResult;

@SuppressWarnings("restriction")
public class TagCloudHelper {
	
	private static String relatedWordsServiceUrl = null;;
	
	public static String[] getMethodNameWords(String methodName){
		String names = JavaTermExtractor.getFQNTermsAsString(methodName);
		names = JavaTermExtractor.removeDuplicates(names);
		String[] strTerms = StringUtils.split(names, " ");
		
		return strTerms;
	}
	
	public static List<String> getSynonyms(String word) throws Exception {
		if(relatedWordsServiceUrl == null)
			relatedWordsServiceUrl = ConfigProperties.getProperty("aqExperiment.related-words-service.url");
		
		String url = relatedWordsServiceUrl + "/GetRelated?word=" + word;
		InputStream ins = new URL(url).openStream();
		JAXBContext context = JAXBContext.newInstance(RelatedSearchResult.class);
		Unmarshaller marshaller = context.createUnmarshaller();
		RelatedSearchResult result = (RelatedSearchResult) marshaller.unmarshal(ins);
		
		Set<String> synonyms = new HashSet<String>();
		synonyms.addAll(result.getVerbs());
		synonyms.addAll(result.getNouns());
		synonyms.addAll(result.getAdjectives());

		return new ArrayList<String>(synonyms);
	}

	public static List<TagCloudWord> getTagCloudWordFrequencies(String methodNameWord)throws Exception {
		List<TagCloudWord> tagCloudWordFrequencies = new ArrayList<TagCloudWord>();

		int frequency = TagCloudHelper.getFrequecy(methodNameWord);
		tagCloudWordFrequencies.add(0,new TagCloudWord(methodNameWord, frequency, true));
		
		//Frequency
		List<String> synonyms = getSynonyms(methodNameWord);
		for (String syn : synonyms) {
			if(syn.equalsIgnoreCase(methodNameWord))
				continue;
			
			frequency = TagCloudHelper.getFrequecy(syn);
			tagCloudWordFrequencies.add(new TagCloudWord(syn, frequency));
		}

		Collections.sort(tagCloudWordFrequencies);
		Collections.reverse(tagCloudWordFrequencies);
		
		if(tagCloudWordFrequencies.size() > 4)
			tagCloudWordFrequencies = new ArrayList<TagCloudWord>(tagCloudWordFrequencies.subList(0, 4));
		
		//Rank
		for (int i = 0; i < tagCloudWordFrequencies.size(); i++) {
			tagCloudWordFrequencies.get(i).setRank(i);
		}
		
		return tagCloudWordFrequencies;
	}
	
	public static int getFrequecy(String word) throws Exception {
		SearchAdapter searchAdapter = SearchAdapter.create(ConfigProperties.getProperty("aqExperiment.sourcerer.url"));
		SearchResult searchResult = null;

		String query = "sname_contents:(" + word + ")";
		searchResult = searchAdapter.search(query);
		if (searchResult.getNumFound() == -1)
			throw new Exception("Unable to perform search: " + query);

		return searchResult.getNumFound();
	}
	
	public static List<List<TagCloudWord>> getAllWordsList(String methodName) throws Exception {
		List<List<TagCloudWord>> allWordsList = new ArrayList<List<TagCloudWord>>();
		String[] methodNameWords = TagCloudHelper.getMethodNameWords(methodName);
		for (String methodNameWord : methodNameWords) {
			List<TagCloudWord> words = TagCloudHelper.getTagCloudWordFrequencies(methodNameWord);
			allWordsList.add(0,words);
		}
		
		return allWordsList;
	}

	public static List<TagCloudMutantQuery> getTagCloudMutantQueries(List<List<TagCloudWord>> allWordsList){

		List<TagCloudMutantQuery> tagCloudMutantQueries = getQueries(null, allWordsList);
		
		return tagCloudMutantQueries;
	}

	private static List<TagCloudMutantQuery> getQueries(List<TagCloudWord> mutantParts, List<List<TagCloudWord>> wordsList){
		List<TagCloudMutantQuery> queries = new ArrayList<TagCloudMutantQuery>();
		
		if(mutantParts == null)
			mutantParts = new ArrayList<TagCloudWord>();
		
		for (TagCloudWord word : wordsList.get(0)) {
			if(wordsList.size() > 1){
				mutantParts.add(word);
				List<List<TagCloudWord>> nextWordsList = new ArrayList<List<TagCloudWord>>(wordsList.subList(1, wordsList.size()));
				queries.addAll(getQueries(mutantParts, nextWordsList));
				mutantParts.remove(mutantParts.size()-1);
				continue;
			}else{
				TagCloudMutantQuery query = new TagCloudMutantQuery();
				query.setWords(new ArrayList<TagCloudWord>(mutantParts));
				query.addWord(word);
				if(query.isMutantMethodName())
					queries.add(query);
			}
		}
		
		if(wordsList.size() == 1)
			mutantParts = null;
		
		return queries;
	}
}
