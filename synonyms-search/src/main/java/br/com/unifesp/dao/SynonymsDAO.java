package br.com.unifesp.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.unifesp.domain.Synonym;
import br.com.unifesp.domain.Word;
import br.com.unifesp.utils.HibernateUtil;
/**
 * Classe que manipula db
 * @author gustavo.konishi
 *
 */
public class SynonymsDAO {

	private static final String GET_SYNONYMS_BY_WORD_LIKE = "select distinct sy from Synonym as sy left join"
			+ " sy.word as w where upper(w.value) like upper(:word)";
	private static final String GET_SYNONYMS_BY_WORD = "select distinct sy from Synonym as sy left join"
			+ " sy.word as w where w.value = :word";

	public void removeSynonym(String word, String[] synonyms) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		Query query = session.createQuery(GET_SYNONYMS_BY_WORD);
		query.setParameter("word", word);
		List<Synonym> synonymsFromDB = query.list();
		for (Iterator iterator = synonymsFromDB.iterator(); iterator.hasNext();) {
			for (int i = 0; i < synonyms.length; i++) {
				String toRemove = synonyms[i];
				Synonym fromDb = (Synonym) iterator.next();
				if (fromDb.getValue().toUpperCase().equals(toRemove)){
						iterator.remove();
						fromDb.setWord(null);
						session.beginTransaction();
						session.save(fromDb);
						session.delete(fromDb);
						session.getTransaction().commit();
				}
			}
		}
	}

	public void saveSynonym(String word, String[] synonym) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		Query query = session.createQuery(GET_SYNONYMS_BY_WORD);
		query.setParameter("word", word);
		List<Synonym> synonymsFromDB = query.list();
		for (int i = 0; i < synonym.length; i++) {
			String synon = synonym[i];
			Synonym syToAdd = new Synonym();
			syToAdd.setValue(synon);
			Word wordToAdd;
			if (synonymsFromDB.size() == 0) {
				wordToAdd = createNewWord(word, session, synonymsFromDB,
						syToAdd);
			} else {
				wordToAdd = synonymsFromDB.get(0).getWord();
			}
			syToAdd.setUserId(1);
			syToAdd.setWord(wordToAdd);
			session.save(syToAdd);
		}

		session.close();
	}

	private Word createNewWord(String word, Session session,
			List<Synonym> synonymsFromDB, Synonym syToAdd) {
		Word wordToAdd = new Word();
		wordToAdd.setValue(word);
		Set<Synonym> set = new HashSet<Synonym>();
		set.add(syToAdd);
		wordToAdd.setUserId(1);
		session.save(wordToAdd);
		synonymsFromDB.add(syToAdd);
		return wordToAdd;
	}

	@SuppressWarnings("unchecked")
	public List<Synonym> getSynonymsByWordWithLike(final String word) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		Query query = session.createQuery(GET_SYNONYMS_BY_WORD_LIKE);
		query.setParameter("word", word);
		List<Synonym> resultList = query.list();

		session.close();
		return resultList;
	}

	@SuppressWarnings("unchecked")
	public List<Synonym> getSynonymsByWord(final String word) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		Query query = session.createQuery(GET_SYNONYMS_BY_WORD);
		query.setParameter("word", word);
		List<Synonym> resultList = query.list();
		HashSet<Synonym> set = new HashSet<Synonym>(resultList);
		session.close();
		return new ArrayList<Synonym>(set);
	}

}
