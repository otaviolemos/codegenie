package br.unifesp.sjc.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import br.unifesp.ppgcc.sourcereraqe.infrastructure.SourcererQueryBuilder;

public class SearchServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public SearchServlet() {
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String returnType = request.getParameter("return");
		String methodName = request.getParameter("methodName");
		String params = request.getParameter("params");
		String w = request.getParameter("w");
		String c = request.getParameter("c");
		String t = request.getParameter("t");
		
		String relatedWordsServiceUrl = getServletContext().getInitParameter("aqExperiment.related-words-service.url");
        String expanders = w + "," + c + "," + t;
        expanders = StringUtils.replace(expanders, "null,", "");
        expanders = StringUtils.replace(expanders, ",null", "");
        expanders = StringUtils.replace(expanders, "null", "");
		
		String query = "";
		try {
			SourcererQueryBuilder builder = new SourcererQueryBuilder(relatedWordsServiceUrl, expanders, false, false);
			query = builder.getSourcererExpandedQuery(methodName, returnType, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String sourcererUrl = getServletContext().getInitParameter("aqExperiment.sourcerer.url") + "/sorl/select/?q="+query+"&rows=100&indent=on";
		
//		RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
//		dispatcher.forward(request, response);
		
		 String urlWithSessionID = response.encodeRedirectURL(sourcererUrl);
		 response.sendRedirect( urlWithSessionID );
		
	}
}
