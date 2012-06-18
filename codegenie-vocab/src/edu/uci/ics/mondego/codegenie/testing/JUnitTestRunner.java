/**
 * @author Otavio Lemos
 */

package edu.uci.ics.mondego.codegenie.testing;

import java.net.URLClassLoader;

//import org.apache.bcel.util.ClassPath;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IClasspathEntry;
import java.net.URL;
import java.io.*;
import java.lang.reflect.Method;
import org.osgi.framework.*;
import org.eclipse.core.runtime.Path;

//import br.jabuti.project.ClassFile;
//import br.jabuti.project.ClassMethod;
//import br.jabuti.project.Coverage;
//import br.jabuti.project.JabutiProject;
//import br.jabuti.project.TestSet;
//import br.jabuti.util.JUnitLoader;

public class JUnitTestRunner  {
	
	private String testClassName;
	private String baseClassName;
	private String methodName;
	private String[] classPath;
	private IJavaProject project;
	private TestResult testResult;	
	
	
	public JUnitTestRunner (IType myType, String myBaseClassName, String myMethodName)
	{
		this.testClassName = myType.getFullyQualifiedName();
		this.project = myType.getJavaProject();
		this.testResult = new TestResult();
		this.baseClassName = myBaseClassName;
		this.methodName = myMethodName;
		this.classPath = buildClassPath();
	}
	
	private String[] buildClassPath() {
		IClasspathEntry[] cpe = null;
		String projectOPLocation = ""; //the location of the current project output (the "bin" directory)
		
		try {
		  cpe = project.getResolvedClasspath(true);
		  projectOPLocation = project.getOutputLocation().toString();
		} catch(Exception e) { 
		  System.err.println(e.getMessage());
		  return null;
		}
		
		String[] thePrjClasspath = new String[cpe.length+2];
		  //this will be the list of our class paths
		
		IWorkspaceRoot myWorkspaceRoot = ResourcesPlugin.getWorkspace().getRoot();				
		thePrjClasspath[0] = myWorkspaceRoot.getRawLocation().toString()
		  + projectOPLocation + "/";
		
		Bundle bundle = Platform.getBundle("CodeGenie");
		String[] bundlePaths = bundle.getLocation().split("@");
		Path path = new Path(bundlePaths[1] + "CodeGenie_" + 
				bundle.getHeaders().get(Constants.BUNDLE_VERSION) + //gets the current CodeGenie version 
				".jar");
		
		thePrjClasspath[1] = path.makeAbsolute().toString();
		
		for (int i = 2; i < thePrjClasspath.length; i++) {
		    thePrjClasspath[i] = cpe[i-2].getPath().makeAbsolute().toString();		  
		}
		
		return thePrjClasspath;
	}
	

	public String getTestClassName() {
		return testClassName;
	}

	public void setTestClassName(String testClassName) {
		this.testClassName = testClassName;
	}

	public TestResult getTestResult() {
		return testResult;
	}

	public void setTestResult(TestResult testResult) {
		this.testResult = testResult;
	}
	
	public void runTests() throws Exception {
		
		ClassLoader myLoader = getTestClassLoader();
		
		StreamWrapper sw = new StreamWrapper();	
		BufferedOutputStream bos =
            new BufferedOutputStream(sw, 1024);
        PrintStream ps =
            new PrintStream(bos, false);

        // redirect System.out to our print stream to get what the test Runner returns
        OutputStream original = System.out;
        System.setOut(ps);
        Class clas = null;
        Class[] myClasses = new Class[1];
        
		try {
			
		  myClasses[0] = myLoader.loadClass(testClassName);
		  clas = myLoader.loadClass("edu.uci.ics.mondego.codegenie.Runner");		  
		
		} catch (Exception e) {
			System.err.println(e.getMessage());
			throw e;
		}
		
		try {		  
		  Method run = clas.getDeclaredMethod("run", new Class[] { myClasses.getClass() });
		  run.invoke(null, new Object[] { myClasses });
		} catch (Exception e) {
			System.err.println(e.getMessage());
			throw e;
		}
		
		bos.flush();
		
		//redirecting System.out back to original output
		PrintStream ops = new PrintStream(original);
        System.setOut(ops);
        
        String testStr = sw.getOutputStr();
		
        this.testResult.setRunCount(Integer.parseInt(testStr.substring(testStr.indexOf("Runs: ")+6, testStr.indexOf("Failures: "))));
        this.testResult.setFailureCount(Integer.parseInt(testStr.substring(testStr.indexOf("Failures: ")+10, testStr.indexOf("Run time: "))));
        this.testResult.setRunTime(Integer.parseInt(testStr.substring(testStr.indexOf("Run time: ")+10, testStr.length()-1)));
        
        //float[] coverage = new float[6];
        //coverage = getCoveragePercentage();        
        
        //this.testResult.setClassCoverage(coverage[0], TestResult.NODE);
        //this.testResult.setClassCoverage(coverage[1], TestResult.EDGE);
        //this.testResult.setClassCoverage(coverage[2], TestResult.USE);
        //this.testResult.setMethodCoverage(coverage[3], TestResult.NODE);
        //this.testResult.setMethodCoverage(coverage[4], TestResult.EDGE);
        //this.testResult.setMethodCoverage(coverage[5], TestResult.USE);
                
	}
	
	protected URLClassLoader getTestClassLoader() {
		URL[] myURLList = new URL[classPath.length]; 
		
		for (int i = 0; i < myURLList.length; i++) {
			try {
			  myURLList[i] = new URL("file:/" + classPath[i]);
			} catch(Exception e) {}
		}
		
		return new URLClassLoader(myURLList);
	}
	
	
//	public float[] getCoveragePercentage() {
//		// pre-conditions: 1) Class named baseClassName was compiled
//		//                    and the .class is in the classpath
//		//				   2) Class named junitClassName must be a valid JUnit
//		//                    compiled class and the .class is in the classpath
//		
//		// This method returns the All-nodes coverage percentage
//		// for method named methodName in a class named baseClassName.
//		// It uses the JaBUTi tool to do so. The steps
//		// that must be followed are:
//		// 1) create a JaBUTi project with baseClassName
//		// 2) import test cases to that project
//		// 3) get coverage report for all-nodes for method named
//		//    methodName 
//		
//		float[] percentage = new float[6];
//		String prjFile = "tmpJBTProj4CodeGenie";
//		// the name of the project file must be unique
//		// -> other solution - create the project file only temporarily,
//		//    to get the coverage. We must delete it in the
//		//    end of this method
//		
//		String myCP = "";
//		
//		for (int i = 0; i < classPath.length-1; i++)
//			myCP = myCP.concat(classPath[i].replace('/', '\\') + ";" );
//	    myCP = myCP.concat(classPath[classPath.length-1].replace('/', '\\'));
//		
//		
//		System.setProperty("java.class.path", System.getProperty("java.class.path")
//				+ ";" + myCP);
//		
//		// This is to make our classes visible by adding the 
//		// userDirectory\temp to the java class path
//		
//		String classPath = System.getProperty("java.class.path");
//			
//		// 1) Creating the JaBUTi project
//		
//		JabutiProject jbtProject = null;
//        
//        try {        
//        	jbtProject = new JabutiProject( baseClassName, classPath); 
//        	// try to create a new JaBUTi project for the baseClass
//        } catch (Exception e)
//        { 	
//        	System.err.println( "Error creating the JaBUTi project " + prjFile );
//        	e.getMessage();
//        }
//        
//        
//        jbtProject.addInstr(baseClassName);
//        // sets the base class to be instrumented
//        // -> Make sure whether it can be always the baseClassName
//        
//        jbtProject.rebuild();
//        // Rebuilds all the project's info, including the classes
//		// to be instrumented
//        
//		jbtProject.setProjectFile( new File( prjFile ) );
//		//  Setting the project file name
//		
//		TestSet.initialize( jbtProject, jbtProject.getTraceFileName() );
//		// TestSet reads a trace file and stores all the test cases into test 
//		// cases objects.
//				
//		
//		// 2) Importing the test cases
//		
//		ClassPath cp = new ClassPath(classPath);
//		
//		File tcFile = null; // the test case class file 
//		
//		try {
//		  tcFile = new File(cp.getPath(testClassName.replace(".", "//") + ".class"));
//		  // tries to create a File object for the junit class
//		} catch(IOException ioe) {
//			System.err.println( "Error while loading the test case class " + testClassName );
//        	ioe.getMessage();
//		}
//		
//		java.util.Vector v = new java.util.Vector();		
//		v.add(testClassName);		
//		v.addAll(JUnitLoader.testCaseNames(tcFile));
//		// prepares the vector for loadTestCase 
//		// first item must be junit class name
//		// following items are test mehods' names - retrieved by testCaseNames
//		
//		JUnitLoader.loadTestCase(jbtProject, tcFile, v);			
//		// 3) Getting coverage for method
//		
//		ClassFile cf = jbtProject.getClassFile(baseClassName);
//		// creates a JaBUTi class file from base class
//		
//		ClassMethod cm = cf.getMethod(methodName);
//		// creates a JaBUTi method file from methodName
//		
//		Coverage allCNodes = cf.getClassFileCoverage(br.jabuti.criteria.Criterion.PRIMARY_NODES);
//		Coverage allMNodes = cm.getClassMethodCoverage(br.jabuti.criteria.Criterion.PRIMARY_NODES);
//		Coverage allCEdges = cf.getClassFileCoverage(br.jabuti.criteria.Criterion.PRIMARY_EDGES);
//		Coverage allMEdges = cm.getClassMethodCoverage(br.jabuti.criteria.Criterion.PRIMARY_EDGES);
//		Coverage allCUses = cf.getClassFileCoverage(br.jabuti.criteria.Criterion.PRIMARY_USES);
//		Coverage allMUses = cm.getClassMethodCoverage(br.jabuti.criteria.Criterion.PRIMARY_USES);
//				
//		percentage[0] = allCNodes.getPercentage();
//		percentage[1] = allCEdges.getPercentage();
//		percentage[2] = allCUses.getPercentage();
//		percentage[3] = allMNodes.getPercentage();
//		percentage[4] = allMEdges.getPercentage();
//		percentage[5] = allMUses.getPercentage();
//		
//		// See if we need to delete any temporary files created
//		
//		return percentage;
//	}


	public IJavaProject getProject() {
		return project;
	}

	public void setProject(IJavaProject project) {
		this.project = project;
	}
	
	private class StreamWrapper extends OutputStream
	   {
	      private String outputStr;
	      
	      public String getOutputStr() {
	    	  return outputStr;
	      }
	 
	      public StreamWrapper()
	      {
	         outputStr = "";
	      }
	 
	      public void write(int b) throws IOException
	      {
	         if (b < 0)
	            outputStr = outputStr.concat("?"); //Replace invalid char with a question mark 
	         else
	         {
	            char[] chars = Character.toChars(b);
	            for (int i = 0; i < chars.length; i++)
	               outputStr = outputStr.concat(Character.toString(chars[i]));
	         }
	      }
	 
	      public void write(byte[] b) throws IOException
	      {
	         for (int i = 0; i < b.length; i++)
	            write(b[i]);
	      }
	 
	      public void write(byte[] b, int off, int len) throws IOException
	      {
	         for (int i = off; i < off + len; i++)
	            write(b[i]);
	      }
	   }
     }
