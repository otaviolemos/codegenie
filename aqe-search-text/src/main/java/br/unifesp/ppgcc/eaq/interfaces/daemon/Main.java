package br.unifesp.ppgcc.eaq.interfaces.daemon;

import br.unifesp.ppgcc.eaq.application.SearchResultHandler;
import br.unifesp.ppgcc.eaq.application.Service;
import br.unifesp.ppgcc.eaq.infrastructure.LogUtils;

public class Main {

	private static long startTime = -1;
	
	public static void main(String[] args) {
		setStartTime();
		
		LogUtils.getLogger().info("");
		LogUtils.getLogger().info("Aplicativo iniciado");
		LogUtils.getLogger().info("");
		
		try {
			LogUtils.getLogger().info("Service");
			Service service = new Service();
			service.executeSearch();
			SearchResultHandler searchResultHandler = new SearchResultHandler(service.getOccurrrences());
			searchResultHandler.makeXLS();
			searchResultHandler.openXLS();
		} catch (Exception e) {
			LogUtils.getLogger().error(e);
			e.printStackTrace();
		}

		LogUtils.getLogger().info("");
		LogUtils.getLogger().info("Aplicativo finalizado. Tempo de execucao: " + getDuractionTime());
		LogUtils.getLogger().info("");
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static void setStartTime(){
		startTime = System.currentTimeMillis();
	}

	private static String getDuractionTime(){
		long duraction = System.currentTimeMillis() - startTime;
		
		duraction /= 1000;
		if(duraction < 60)
			return duraction + " segundos.";
		
		duraction /= 60;
		return duraction + " minutos.";
	}
}