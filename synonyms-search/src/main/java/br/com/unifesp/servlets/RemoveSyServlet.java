package br.com.unifesp.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.unifesp.dao.SynonymsDAO;

public class RemoveSyServlet extends HttpServlet {

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
		
		dao.removeSynonym(word, synonyms);
	}
}
