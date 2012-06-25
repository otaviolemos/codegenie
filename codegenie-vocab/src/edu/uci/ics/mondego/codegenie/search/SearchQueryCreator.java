/**
 * @author Otavio Lemos
 */

package edu.uci.ics.mondego.codegenie.search;

import org.eclipse.jdt.core.*;
import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.dom.*;
import java.util.StringTokenizer;
import java.util.*;

public class SearchQueryCreator {

	String wantedMethodName;
	String wantedClassName;
	String wantedPackageName;
	String keyInterface;
	String FQNInterface;
	List parameters = new ArrayList();
	ASTNode returnTypeNode = null;

	public String[] formQuery(IType myTestType) {
		IMarker[] markers = null;
		String message = null;
		int start = 0, end = 0;
		try {
			// // get the first error message through the markers
			// Get the problem messages
			IResource javaSourceFile = myTestType.getUnderlyingResource();
			markers = javaSourceFile.findMarkers(
					IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER, true,
					IResource.DEPTH_INFINITE);
			// start = Integer.MAX_VALUE;

			/** Código novo **/
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

				/** Código novo **/
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
			// message = (String)
			// markers[myIndex].getAttribute(IMarker.MESSAGE);
			// start = ((Integer)
			// markers[myIndex].getAttribute(IMarker.CHAR_START)).intValue();
			// end = ((Integer)
			// markers[myIndex].getAttribute(IMarker.CHAR_END)).intValue();
		} catch (Exception e) {
		}
		String myCode = "";
		try {
			myCode = myTestType.getCompilationUnit().getSource();
		} catch (Exception e) {
		}

		// class does not exist, static method being called
		if (message.endsWith("cannot be resolved")) {
			wantedClassName = message.substring(0,
					message.indexOf("cannot be resolved") - 1);
			wantedMethodName = myCode.substring(end + 1, end
					+ myCode.substring(end).indexOf('('));
		} else if (message.endsWith("cannot be resolved to a type")) {
			// class does not exist, instance method being called
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
//			myCode.substring(
//					start + wantedClassName.length() + 1,
//					start
//							+ wantedClassName.length()
//							+ 1
//							+ myCode.substring(
//									start + wantedClassName.length() + 1)
//									.indexOf(" ="));
			
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
		}

		MethodInvocationVisitor mic = new MethodInvocationVisitor();
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setResolveBindings(true);
		parser.setSource(myTestType.getCompilationUnit());
		CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		cu.accept(mic);

		String myParamKeys = "(";
		String myParamFQNs = "(";
		String myParamSNs = "(";

		for (Iterator it = parameters.iterator(); it.hasNext();) {
			ITypeBinding itb = ((Expression) it.next()).resolveTypeBinding();
			myParamKeys += itb.getKey();
			myParamFQNs = it.hasNext() ? myParamFQNs + itb.getQualifiedName()
					+ "," : myParamFQNs + itb.getQualifiedName();
			myParamSNs = it.hasNext() ? myParamSNs + itb.getName() + ","
					: myParamSNs + itb.getName();
		}

		myParamKeys += ")";
		myParamFQNs += ")";

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
			wantedPackageName = myTestType.getCompilationUnit()
					.getPackageDeclarations()[0].getElementName();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (myParamSNs.equals("("))
			myParamSNs = "";

		String[] ret = { wantedPackageName, wantedClassName, wantedMethodName,
				returnSN, myParamSNs };

		return ret;

	}

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

}
