package br.unifesp.ppgcc.aqexperiment.interfaces.daemon;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.unifesp.ppgcc.aqexperiment.application.AQEService;
import br.unifesp.ppgcc.aqexperiment.domain.Department;
import br.unifesp.ppgcc.aqexperiment.domain.Employee;
import br.unifesp.ppgcc.aqexperiment.infrastructure.LogUtils;

public class Main {

	private static long startTime = -1;
	
	private EntityManager manager;

	public Main(EntityManager manager) {
		this.manager = manager;
	}

	public static void main(String[] args) {
		setStartTime();
		
		LogUtils.getLogger().info("");
		LogUtils.getLogger().info("Aplicativo iniciado");
		LogUtils.getLogger().info("");
		
		try {
			ApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");

			LogUtils.getLogger().info("Service");
			
			ctx.getBeanDefinitionNames();
			
			AQEService service = (AQEService) ctx.getBean("AQEService");
			service.execute();
			
//			test();
			
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
	
	public static void test() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("persistenceUnit");
		EntityManager manager = factory.createEntityManager();
		Main test = new Main(manager);

		EntityTransaction tx = manager.getTransaction();
		tx.begin();
		try {
			test.createEmployees();
		} catch (Exception e) {
			e.printStackTrace();
		}
		tx.commit();

		test.listEmployees();

		System.out.println(".. done");
	}




	private void createEmployees() {
		int numOfEmployees = manager.createQuery("Select a From Employee a", Employee.class).getResultList().size();
		if (numOfEmployees == 0) {
			Department department = new Department("java");
			manager.persist(department);

			manager.persist(new Employee("Jakab Gipsz",department));
			manager.persist(new Employee("Captain Nemo",department));

		}
	}


	private void listEmployees() {
		List<Employee> resultList = manager.createQuery("Select a From Employee a", Employee.class).getResultList();
		System.out.println("num of employess:" + resultList.size());
		for (Employee next : resultList) {
			System.out.println("next employee: " + next);
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