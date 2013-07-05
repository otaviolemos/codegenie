package br.unifesp.ppgcc.aqexperiment.infrastructure;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.unifesp.ppgcc.aqexperiment.domain.SurveyResponse;
import br.unifesp.ppgcc.aqexperiment.infrastructure.util.ConfigProperties;

@Repository("surveyResponseRepository")
public class SurveyResponseRepository extends BaseRepository<SurveyResponse> {

	public List<SurveyResponse> findAllFromSheet() throws Exception {
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
	
	public List<SurveyResponse> findAll(Date executionTimestamp){
		Criterion c = Restrictions.eq("executionTimestamp", executionTimestamp);
		return super.findByCriteria(c);
	}

}
