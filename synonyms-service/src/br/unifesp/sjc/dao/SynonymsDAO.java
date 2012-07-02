package br.unifesp.sjc.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import br.unifesp.sjc.utils.HibernateUtil;
/**
 * Classe que manipula db
 * @author gustavo.konishi
 *
 */
public class SynonymsDAO {
	
	private static final String GET_SYNONYMS_BY_WORD1 = "select word1 from Pair where word2 = :word and type = \'s\'";
	private static final String GET_SYNONYMS_BY_WORD2 = "select word2 from Pair where word1 = :word and type = \'s\'";
	

	@SuppressWarnings("unchecked")
	public static List<String> getSynonymsByWord(final String word) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		Query query = session.createQuery(GET_SYNONYMS_BY_WORD1);
		query.setParameter("word", word);
		List<String> resultList = query.list();
		HashSet<String> set = new HashSet<String>(resultList);
		
    query = session.createQuery(GET_SYNONYMS_BY_WORD2);
    query.setParameter("word", word);
    resultList = query.list();
    set.addAll(new HashSet<String>(resultList));
		
    session.close();
    
    
		return new ArrayList<String>(set);
	}

}
