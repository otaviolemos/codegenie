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

public class SearchServlet extends HttpServlet {

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

	public SearchServlet() {
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		String ret = request.getParameter("return");
		String methodName = request.getParameter("methodName");
		String params = request.getParameter("params");
		String wordNet = request.getParameter("wordNet");
		System.out.println("");

	}
}
