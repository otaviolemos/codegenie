package br.unifesp.ict.seg.codegenie.search.solr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaModelMarker;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import br.unifesp.ict.seg.codegenie.Activator;
import br.unifesp.ict.seg.codegenie.pool.MethodInterfacePool;
import br.unifesp.ict.seg.codegenie.search.CGMethodInterface;
import br.unifesp.ict.seg.codegenie.search.relatedwords.RelatedWordUtils;
import br.unifesp.ict.seg.codegenie.tmp.Debug;

/**imported from original codegenie-vocab*/
public class SearchQueryCreator {


	String wantedMethodName;
	String wantedClassName;
	String wantedPackageName;
	String keyInterface;
	String FQNInterface;
	boolean existingClass = false;
	@SuppressWarnings("rawtypes")
	List parameters = new ArrayList();
	ASTNode returnTypeNode = null;
	//pre stores the information for late work
	IType selection;
	String[] query;
	//TODO JAR-AQE
	//String extQuery;
	private long id;
	private CGMethodInterface mi;
	private Boolean isStatic;

	public SearchQueryCreator(IType selection) {
		this.selection = selection;
		this.id = Activator.newID();
	}

	@SuppressWarnings("rawtypes")
	public String[] formQuery() {
		IMarker[] markers = null;
		String message = null;
		int start = 0, end = 0;
		try {
			// // get the first error message through the markers
			// Get the problem messages
			IResource javaSourceFile = selection.getUnderlyingResource();
			markers = javaSourceFile.findMarkers(
					IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER, true,
					IResource.DEPTH_INFINITE);
			// start = Integer.MAX_VALUE;

			/** C�digo novo **/
			start = Integer.MIN_VALUE;
			/**            **/

			int myIndex = -1;

			for (int i = 0; i < markers.length; i++) {
				if (((Integer) markers[i].getAttribute(IMarker.CHAR_START))
						.intValue() > start) {
					start = ((Integer) markers[i]
							.getAttribute(IMarker.CHAR_START)).intValue();
					myIndex = i;
				}

				/** C�digo novo **/
				message = (String) markers[myIndex]
						.getAttribute(IMarker.MESSAGE);
				start = ((Integer) markers[myIndex]
						.getAttribute(IMarker.CHAR_START)).intValue();
				end = ((Integer) markers[myIndex]
						.getAttribute(IMarker.CHAR_END)).intValue();
				/**            **/

				if ((message.endsWith("cannot be resolved"))
						|| (message.endsWith("cannot be resolved to a type"))
						|| (message.indexOf("is undefined for the type") != -1))
					break;
			}

		} catch (Exception e) {
		}
		String myCode = "";
		try {
			myCode = selection.getCompilationUnit().getSource();
		} catch (Exception e) {
		}

		// class does not exist, static method being called
		if (message.endsWith("cannot be resolved")) {
			isStatic = true;
			wantedClassName = message.substring(0,
					message.indexOf("cannot be resolved") - 1);
			wantedMethodName = myCode.substring(end + 1, end
					+ myCode.substring(end).indexOf('('));
		} else if (message.endsWith("cannot be resolved to a type")) {
			// class does not exist, instance method being called
			isStatic = false;
			wantedClassName = message.substring(0,
					message.indexOf("cannot be resolved") - 1);
			String instanceName = "";
			StringTokenizer myTok = new StringTokenizer(myCode);
			while (myTok.hasMoreTokens()) {
				if (myTok.nextToken().equals(wantedClassName)) {
					String possibleInstanceName = myTok.nextToken();
					String nextNextToken = myTok.nextToken();
					if (nextNextToken.equals("=")) {
						instanceName = possibleInstanceName;
						break;
					}
				}
			}

			int invocationStart = myCode.indexOf(instanceName + ".")
					+ instanceName.length() + 1;
			wantedMethodName = myCode.substring(
					invocationStart,
					invocationStart
					+ myCode.substring(
							myCode.indexOf(instanceName + ".")
							+ instanceName.length() + 1)
							.indexOf('('));
		} else if (message.indexOf("is undefined for the type") != -1) {
			// class exists but method does not exist
			wantedMethodName = message.substring(11, message.indexOf('('));
			wantedClassName = message.substring(message
					.indexOf("is undefined for the type") + 26);
			existingClass = false;
		}

		MethodInvocationVisitor mic = new MethodInvocationVisitor();
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setResolveBindings(true);
		parser.setSource(selection.getCompilationUnit());
		CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		cu.accept(mic);

		String myParamKeys = "(";
		String myParamFQNs = "(";
		//String myParamSNs = "(";
		String myParamSNs = "";

		for (Iterator it = parameters.iterator(); it.hasNext();) {
			ITypeBinding itb = ((Expression) it.next()).resolveTypeBinding();
			myParamKeys += itb.getKey();
			myParamFQNs = it.hasNext() ? myParamFQNs + itb.getQualifiedName()
					//+ "," : myParamFQNs + itb.getQualifiedName();
					+ " AND " : myParamFQNs + itb.getQualifiedName();
			//myParamSNs = it.hasNext() ? myParamSNs + itb.getName() + ","
			myParamSNs = it.hasNext() ? myParamSNs + itb.getName() + " AND "
					: myParamSNs + itb.getName();
		}

		myParamKeys += ")";
		myParamFQNs += ")";
		//myParamSNs  += ")";

		String returnKEY = "";
		String returnFQN = "";
		String returnSN = "";

		if (returnTypeNode != null) {
			Expression returnTypeExp = null;
			if (returnTypeNode.getNodeType() == ASTNode.VARIABLE_DECLARATION_FRAGMENT) {
				returnKEY = ((VariableDeclarationFragment) returnTypeNode)
						.resolveBinding().getType().getKey();
				returnFQN = ((VariableDeclarationFragment) returnTypeNode)
						.resolveBinding().getType().getQualifiedName();
				returnSN = ((VariableDeclarationFragment) returnTypeNode)
						.resolveBinding().getType().getName();
			} else if (returnTypeNode.getNodeType() == ASTNode.METHOD_INVOCATION) {
				returnTypeExp = (Expression) ((MethodInvocation) returnTypeNode)
						.arguments().get(0);
				returnKEY = returnTypeExp.resolveTypeBinding().getKey();
				returnFQN = returnTypeExp.resolveTypeBinding()
						.getQualifiedName();
				returnSN = returnTypeExp.resolveTypeBinding().getName();
			} else if (returnTypeNode.getNodeType() == ASTNode.ASSIGNMENT) {
				returnTypeExp = ((Assignment) returnTypeNode).getLeftHandSide();
				returnKEY = returnTypeExp.resolveTypeBinding().getKey();
				returnFQN = returnTypeExp.resolveTypeBinding()
						.getQualifiedName();
				returnSN = returnTypeExp.resolveTypeBinding().getName();
			}
		} else {
			returnKEY = "V";
			returnFQN = "void";
			returnSN = "void";
		}

		keyInterface = wantedMethodName + myParamKeys + returnKEY;

		FQNInterface = returnFQN + " " + wantedMethodName + myParamFQNs;

		try {
			wantedPackageName = selection.getCompilationUnit()
					.getPackageDeclarations()[0].getElementName();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (myParamSNs.equals("("))
			myParamSNs = "";

		String[] ret = { wantedPackageName, wantedClassName, wantedMethodName,
				returnSN, myParamSNs };
		query = ret;
		Debug.debug(getClass(),"wanted package name is: "+wantedPackageName);
		Debug.debug(getClass(),"wanted class name is: "+wantedClassName);
		Debug.debug(getClass(),"wanted method name is: "+wantedMethodName);
		Debug.debug(getClass(),"wanted return is: "+returnSN);
		Debug.debug(getClass(),"wanted params are: "+myParamSNs);
		MethodInterfacePool.add(this);
		return ret;
	}

	public CGMethodInterface getMethodInterface(){
		if(mi==null){
			String params = parameters.toString();
			params = params.substring(1,params.length()-1);
			mi = new CGMethodInterface(id,query[3],query[0]+"."+query[1],query[2],query[4],params.split(", "),isStatic);
		}
		return mi;	
	}

	public boolean doesClassExist() {
		return existingClass;
	}

	public void expandQuery(boolean enSyn, boolean codeSyn, boolean enAnt, boolean codeAnt){
		//query[0] is package name
		//query[1] is class name
		//query[2] is method name
		//query[3] is return type name
		//query[4] is parameters
		query[1] = RelatedWordUtils.getRelatedAsQueryPart(query[1], enSyn, codeSyn, enAnt, codeAnt);
		query[2] = RelatedWordUtils.getRelatedAsQueryPart(query[2], enSyn, codeSyn, enAnt, codeAnt);
		//TODO here comes the AQE jar.
		/*
		SourcererQueryBuilder sqb = new SourcererQueryBuilder(String relatedWordsServerUrl,String expanders,bool relaxReturn,bool relaxParam);
		extQuery = sqb.getSourcererExpandedQuery(String methodName, String returnType, String params);
		 * */
		
	}



	/**Method Invocation Visitor for the tests*/
	class MethodInvocationVisitor extends ASTVisitor {

		public boolean visit(MethodInvocation node) {
			if (node.getName().getIdentifier().equals(wantedMethodName)) {
				parameters = ((MethodInvocation) node).arguments();
				if (node.getParent().getNodeType() == ASTNode.ASSIGNMENT) {
					returnTypeNode = node.getParent();// ((Assignment)node.getParent()).getLeftHandSide();
					return false;
				} else if (node.getParent().getNodeType() == ASTNode.METHOD_INVOCATION
						&& ((MethodInvocation) node.getParent()).getName()
						.getIdentifier().equals("assertEquals")) {
					returnTypeNode = node.getParent(); // (Expression)((MethodInvocation)
					// node).arguments().get(0);
					return false;
				} else if (node.getParent().getNodeType() == ASTNode.VARIABLE_DECLARATION_FRAGMENT) {
					returnTypeNode = node.getParent(); // ((VariableDeclarationFragment)
					// node.getParent()).resolveBinding();
					return false;
				}
			}
			return true;
		}
	}

	public String[] getQuery() {
		return query;
	}

	public Long getID() {
		return id;
	}

	public IType getTestingClass() {
		return this.selection;
	}
}
