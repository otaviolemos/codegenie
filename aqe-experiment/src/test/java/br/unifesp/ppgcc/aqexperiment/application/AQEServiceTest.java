package br.unifesp.ppgcc.aqexperiment.application;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.unifesp.ppgcc.aqexperiment.domain.AnaliseFunction;
import br.unifesp.ppgcc.aqexperiment.domain.AnaliseFunctionResponse;
import br.unifesp.ppgcc.aqexperiment.infrastructure.util.ConfigProperties;

public class AQEServiceTest {

	private static AQEService service;

	@BeforeClass
	public static void testSetup() throws Exception{
		@SuppressWarnings("resource")
		ApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
		service = (AQEService) ctx.getBean("AQEService");
		service.execute();
	}

	@Test
	public void mappingFunctionsTest() throws Exception {
		
		int total21 = 0;

		for(AnaliseFunction function : service.getAnaliseFunctions()){
			for(AnaliseFunctionResponse r : function.getResponses()){
				if(!"Juliano Antunes Guimarães Leite".equals(r.getSurveyResponse().getNome()))
					continue;
				
				int n = function.getSurveyNumber();
				if( n == 1 && (r.getReturnType()+"\t"+r.getMethodName()+"\t"+r.getParams()).equals("void	changePicture	string"))
					total21++;
				else if(n == 2 && (r.getReturnType()+"\t"+r.getMethodName()+"\t"+r.getParams()).equals("String	encryptPasswd	passwd"))
						total21++;
				else if(n == 3 && (r.getReturnType()+"\t"+r.getMethodName()+"\t"+r.getParams()).equals("void	rotatePicture	string"))
					total21++;
				else if(n == 4 && (r.getReturnType()+"\t"+r.getMethodName()+"\t"+r.getParams()).equals("String	bytesToStringHex	bytes"))
					total21++;
				else if(n == 5 && (r.getReturnType()+"\t"+r.getMethodName()+"\t"+r.getParams()).equals("void	unZip	string, string"))
					total21++;
				else if(n == 6 && (r.getReturnType()+"\t"+r.getMethodName()+"\t"+r.getParams()).equals("void	invertString	string"))
					total21++;
				else if(n == 7 && (r.getReturnType()+"\t"+r.getMethodName()+"\t"+r.getParams()).equals("void	highlightPicture	string"))
					total21++;
				else if(n == 8 && (r.getReturnType()+"\t"+r.getMethodName()+"\t"+r.getParams()).equals("void	ScreenCapture	"))
					total21++;
				else if(n == 9 && (r.getReturnType()+"\t"+r.getMethodName()+"\t"+r.getParams()).equals("String	generateDNASequence	string"))
					total21++;
				else if(n == 10 && (r.getReturnType()+"\t"+r.getMethodName()+"\t"+r.getParams()).equals("string	generateDNAReverse	string"))
					total21++;
				else if(n == 11 && (r.getReturnType()+"\t"+r.getMethodName()+"\t"+r.getParams()).equals("void	BlurPocture	string"))
					total21++;
				else if(n == 12 && (r.getReturnType()+"\t"+r.getMethodName()+"\t"+r.getParams()).equals("int	countLine	string"))
					total21++;
				else if(n == 13 && (r.getReturnType()+"\t"+r.getMethodName()+"\t"+r.getParams()).equals("void	 urlEncoder	string"))
					total21++;
				else if(n == 14 && (r.getReturnType()+"\t"+r.getMethodName()+"\t"+r.getParams()).equals("void	 urlDecode	string"))
					total21++;
				else if(n == 15 && (r.getReturnType()+"\t"+r.getMethodName()+"\t"+r.getParams()).equals("void	parseCSV	string"))
					total21++;
				else if(n == 16 && (r.getReturnType()+"\t"+r.getMethodName()+"\t"+r.getParams()).equals("void	zipFile	string, string"))
					total21++;
				else if(n == 17 && (r.getReturnType()+"\t"+r.getMethodName()+"\t"+r.getParams()).equals("void	filterFile	string, string"))
					total21++;
				else if(n == 18 && (r.getReturnType()+"\t"+r.getMethodName()+"\t"+r.getParams()).equals("void	generateMD5	string"))
					total21++;
				else if(n == 19 && (r.getReturnType()+"\t"+r.getMethodName()+"\t"+r.getParams()).equals("void	salvePicture	String, String"))
					total21++;
				else if(n == 20 && (r.getReturnType()+"\t"+r.getMethodName()+"\t"+r.getParams()).equals("void	concatString	List<String>"))
					total21++;
				else if(n == 21 && (r.getReturnType()+"\t"+r.getMethodName()+"\t"+r.getParams()).equals("void	alterStringHifToCamelCase	String"))
					total21++;
			}
		}
		boolean moreOneRelevant = new Boolean(ConfigProperties.getProperty("aqExperiment.moreOneRelevant"));
		assertTrue(moreOneRelevant ? total21 == 12 : total21 == 21);
	}
	
	@Test
	public void responseResultTest() throws Exception {
		AnaliseFunctionResponse response = service.getAnaliseFunctionResponse("Guilherme Moreira", "reverse");
		
		System.out.println("Relevants: "+response.getTotalRelevants());
		System.out.println("Results: "+response.getTotalResults());
		System.out.println("Intersections: "+response.getTotalIntersections());
		System.out.println("Query: "+response.getSourcererQuery());
		
		assertTrue(response != null);
	}
}
