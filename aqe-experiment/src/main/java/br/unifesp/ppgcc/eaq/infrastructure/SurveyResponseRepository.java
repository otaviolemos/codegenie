package br.unifesp.ppgcc.eaq.infrastructure;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import br.unifesp.ppgcc.eaq.domain.SurveyResponse;

public class SurveyResponseRepository {

	public List<SurveyResponse> findAll() throws Exception {
		File inputWorkbook = new File(ConfigProperties.getProperty("aqExperiment.survey.path"));
		Workbook w;

		w = Workbook.getWorkbook(inputWorkbook);
		Sheet sheet = w.getSheet(0);

		List<SurveyResponse> responses = new ArrayList<SurveyResponse>();
		
		for (int i = 0; i < sheet.getRows(); i++) {
			responses.add(new SurveyResponse(sheet,i));
		}
		return responses;
	}
}
