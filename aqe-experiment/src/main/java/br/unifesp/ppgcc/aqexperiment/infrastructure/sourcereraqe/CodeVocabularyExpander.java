package br.unifesp.ppgcc.aqexperiment.infrastructure.sourcereraqe;

import java.io.InputStream;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import br.unifesp.ppgcc.aqexperiment.infrastructure.util.ConfigProperties;

@SuppressWarnings("restriction")
public class CodeVocabularyExpander extends Expander {

	public CodeVocabularyExpander() {
		super.setName(CODE_VOCABULARY_EXPANDER);
		super.setMethodNameExpander(true);
		super.setParamExpander(false);
		super.setReturnExpander(false);
	}
	
	public void expandTerm(QueryTerm queryTerm) throws Exception {
		String url = ConfigProperties.getProperty("aqExperiment.related-words-service.url") + "/GetRelated?word=" + queryTerm.getExpandedTerms().get(0);
		InputStream ins = new URL(url).openStream();
		JAXBContext context = JAXBContext.newInstance(RelatedSearchResult.class);
		Unmarshaller marshaller = context.createUnmarshaller();
		RelatedSearchResult result = (RelatedSearchResult) marshaller.unmarshal(ins);

		queryTerm.getExpandedTerms().addAll(result.getCodeRelatedSyns());
		
		queryTerm.getExpandedTermsNot().addAll(result.getCodeRelatedAntons());
	}
}
