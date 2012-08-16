package br.unifesp.sjc.dao;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import br.unifesp.sjc.utils.HibernateUtil;
/**
 * Classe que manipula db
 * @author Otavio Lemos
 *
 */
public class RelatedDAO {
	
	private static final String GET_RELATED_BY_WORD = "select word1, word2, type from Pair where word2 = :word or word1 = :word";
	private static final String GET_SYNONYMS_BY_WORD = "select word1, word2, type from Pair where (word2 = :word or word1 = :word) and type = \'s\'";
  private static final String GET_ANTONYMS_BY_WORD = "select word1, word2, type from Pair where (word2 = :word or word1 = :word) and type = \'a\'";
  

	@SuppressWarnings("unchecked")
	public static List<List<String>> getRelated(final String word) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		List<String> synList = new LinkedList<String>();
		List<String> antonymList = new LinkedList<String>();
		
		Query query = session.createQuery(GET_RELATED_BY_WORD);
		query.setParameter("word", word);
		List<Object[]> resultList = query.list();
		
		for(Object[] lo : resultList) {
		  if(((String)lo[2]).equals("s")) {
		    String syn = "";
		    if(((String)lo[1]).equals(word))
		      syn = (String)lo[0];
		    else
		      syn = (String)lo[1];
		    synList.add(syn);
		    addAntonyms(syn, antonymList, session);
		  } else if(((String)lo[2]).equals("a")) {
		    String ant = "";
        if(((String)lo[1]).equals(word)) 
          ant = (String)lo[0];
        else 
          ant = (String)lo[1];
        if(!word.contains(ant))
          antonymList.add(ant);
        addSynonyms(ant, antonymList, session);
		  }
		}
		
    session.close();
    
    List<List<String>> results = new LinkedList<List<String>>();
    
    results.add(synList);
    results.add(antonymList);
    
		return results;
	}


  private static void addSynonyms(String word, List<String> list, Session s) {
    Query query = s.createQuery(GET_SYNONYMS_BY_WORD);  
    query.setParameter("word", word);
    List<Object[]> resultList = query.list();
    
    for(Object[] lo : resultList) {
        if(((String)lo[1]).equals(word))
          list.add((String)lo[0]);
        else
          list.add((String)lo[1]);
    }
  }
  
  private static void addAntonyms(String word, List<String> list, Session s) {
    Query query = s.createQuery(GET_ANTONYMS_BY_WORD);  
    query.setParameter("word", word);
    List<Object[]> resultList = query.list();
    
    for(Object[] lo : resultList) {
        String ant = "";
        if(((String)lo[1]).equals(word))
          ant = ((String)lo[0]);
        else
          ant = ((String)lo[1]);
        if(!word.contains(ant))
          list.add(ant);
    }
  }

}
