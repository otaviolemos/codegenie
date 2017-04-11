package br.unifesp.ppgcc.aqexperiment.interfaces.daemon;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.unifesp.ppgcc.aqexperiment.application.AQEService;
import br.unifesp.ppgcc.aqexperiment.infrastructure.util.LogUtils;

public class Main {

	private static long startTime = -1;
	
	public static void main(String[] args) {
		setStartTime();
		
		LogUtils.getLogger().info("");
		LogUtils.getLogger().info("Aplicativo iniciado");
		LogUtils.getLogger().info("");
		
		try {
			@SuppressWarnings("resource")
			ApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");

			LogUtils.getLogger().info("Service");
			AQEService service = (AQEService) ctx.getBean("AQEService");
			service.execute();
		
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