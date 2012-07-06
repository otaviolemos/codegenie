package br.unifesp.sjc.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
/**
 *Classe utilitaria p/ Hibernate
 * @author gustavo.konishi
 *
 */
public class HibernateUtil {  
	   private static final SessionFactory sessionFactory;  
	   static {  
	      sessionFactory = new AnnotationConfiguration().configure()  
	            .buildSessionFactory();  
	   }  
	  
	   public static SessionFactory getSessionFactory() {  
	      return sessionFactory;  
	   }  
	}  