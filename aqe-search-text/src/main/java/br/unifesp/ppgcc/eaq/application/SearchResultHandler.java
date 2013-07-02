package br.unifesp.ppgcc.eaq.application;

import java.awt.Desktop;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import br.unifesp.ppgcc.eaq.domain.SearchResult;
import br.unifesp.ppgcc.eaq.infrastructure.ConfigProperties;

public class SearchResultHandler {

	private List<SearchResult> searchResults = new ArrayList<SearchResult>();
	private String xlsOutpuPath = "";
	
	public SearchResultHandler(List<SearchResult> searchResults){
		this.searchResults = searchResults;
		try {
			this.xlsOutpuPath = ConfigProperties.getProperty("textSearch.path") + new SimpleDateFormat("yyyy-MM-dd HHmmss").format(System.currentTimeMillis()) + ".xls";
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void print(){
		String separator = "#";

		String header = "\n";
		header += "name" + separator;
		header += "description" + separator;
		header += "term" + separator;
		header += "lineNumber";
		header += "line" + separator;
		header += "file" + separator;
		System.out.println(header);

		for(SearchResult searchResult : this.searchResults){
			String line = "";
			line += searchResult.getSearchTerm().getName() + separator;
			line += searchResult.getSearchTerm().getDescription() + separator;
			line += searchResult.getTerm() + separator;
			line += searchResult.getLineNumber();
			line += searchResult.getLine() + separator;
			line += searchResult.getFile().getAbsolutePath() + separator;
			
			System.out.println(line);
		}
		
		System.out.println("\n");
	}
	
	public void makeXLS() throws Exception {
		WritableWorkbook workbook = Workbook.createWorkbook(new File(xlsOutpuPath));
		WritableSheet sheet = workbook.createSheet("First Sheet", 0);
		
		sheet.addCell(new Label(0, 0, "name"));
		sheet.addCell(new Label(1, 0, "description"));
		sheet.addCell(new Label(2, 0, "term"));
		sheet.addCell(new Label(3, 0, "lineNumber"));
		sheet.addCell(new Label(4, 0, "line"));
		sheet.addCell(new Label(5, 0, "file"));
		
		int line = 1;
		for(SearchResult searchResult : this.searchResults){
			
			sheet.addCell(new Label(0, line, searchResult.getSearchTerm().getName()));
			sheet.addCell(new Label(1, line, searchResult.getSearchTerm().getDescription()));
			sheet.addCell(new Label(2, line, searchResult.getTerm()));
			sheet.addCell(new Label(3, line, searchResult.getLineNumber()+""));
			sheet.addCell(new Label(4, line, searchResult.getLine()));
			sheet.addCell(new Label(5, line, "file://" + searchResult.getFile().getAbsolutePath()));
			
			line++;
		}

		workbook.write(); 
		workbook.close();
	}
	
	public void printXLSLink(){
		String url = "http://google.com";
		String link = "<a href=\"" + url + "\">" + url + "<\\a>";
		System.out.println(link);
	}
	
	public void openXLS() throws Exception {
		//Runtime.getRuntime().exec(ConfigProperties.getProperty("textSearch.path") + "output.xls");
		//Desktop.getDesktop().open(new File(xlsOutpuPath));
		String[] cmdarray=new String[]{"cmd.exe","/c",xlsOutpuPath}; 
		Runtime.getRuntime().exec(cmdarray);
	}
}
