package br.com.unifesp.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.unifesp.dao.SynonymsDAO;
import br.com.unifesp.domain.Synonym;

/**
 * Servlet simples para salvar listas de sinônimos
 * @author gustavo.konishi
 *
 */
public class SaveSyServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) {
		SynonymsDAO dao = new SynonymsDAO();
		String word = request.getParameter("word");
		String[] synonyms = request.getParameterValues("synonym");
		
		dao.saveSynonym(word, synonyms);
		
		response.setContentType("text/xml");
	}
}
