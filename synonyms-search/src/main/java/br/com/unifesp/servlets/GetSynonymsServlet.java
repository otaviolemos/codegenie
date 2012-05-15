package br.com.unifesp.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import br.com.unifesp.dao.SynonymsDAO;
import br.com.unifesp.domain.Synonym;
import br.com.unifesp.domain.SynonymDTO;
import br.com.unifesp.domain.SynonymsSearchResult;
/**
 * Servlet simples para obter lista de sinônimos.
 * Palavras composta foram removidas do resultado.
 * @author gustavo.konishi
 *
 */
public class GetSynonymsServlet extends HttpServlet {

	private static final long serialVersionUID = -7125190283545104169L;

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
		SynonymsDAO dao = new SynonymsDAO();
		String toSearch = request.getParameter("word");
		
		List<Synonym> result = dao.getSynonymsByWord(toSearch);
		
		String singleWord = request.getParameter("singleWord");
		if ("1".equals(singleWord)) {
				removeSingleWords(result);
		}
		
		response.setContentType("text/xml");
		try {
			PrintWriter out = response.getWriter();

			SynonymsSearchResult searchResult = new SynonymsSearchResult();
			for (Synonym synonym : result) {
				SynonymDTO dto = new SynonymDTO();
				dto.setUserId(synonym.getUserId());
				dto.setValue(synonym.getValue().toUpperCase());
				searchResult.getSynonyms().add(dto);
			}
			JAXBContext context  = JAXBContext.newInstance(SynonymsSearchResult.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(searchResult, out);
	
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Palavras composta foram removidas.
	 * @param result
	 */
	private void removeSingleWords(List<Synonym> result) {
		for (Iterator iterator = result.iterator(); iterator.hasNext();) {
			Synonym synonym = (Synonym) iterator.next();
			if(synonym.getValue().split(" ").length > 1)
				iterator.remove();
		}
	}
}
