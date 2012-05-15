package br.com.unifesp;

import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import br.com.unifesp.dao.SynonymsDAO;
import br.com.unifesp.domain.Synonym;
import br.com.unifesp.domain.Word;
import br.com.unifesp.utils.HibernateUtil;
/**
 * Classe de teste para funcionalidade de busca de sinÃ´nimos
 * @author gustavo.konishi
 *
 */
public class App 
{
	public static void main(final String[] args) {
			Scanner in = new Scanner(System.in);
			SynonymsDAO dao = new SynonymsDAO();
			while(true){
			  System.out.println("Palavra: ");
			  String toSearch = in.nextLine();
			  List<Synonym> result = dao.getSynonymsByWord(toSearch);
			  System.out.println("Sinônimos:\n");
			  for (Synonym synonym : result) {
				  System.out.println(synonym.getValue());
			  }
			}
	}

}
