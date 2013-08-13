package br.unifesp.ict.seg.codegenie.search;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

import br.unifesp.ict.seg.codegenie.search.solr.Solr;

public class CGMethodInterface {

	private Long queryid;
	private String returnFQN;
	private String parentFqn;
	private String methodname;
	private String[] paramsTypes;
	private IType parent;
	private IMethod method;
	private Boolean isStatic;

	public CGMethodInterface(Long queryid, String returnFqn, String parentFqn, String methodName,
			String paramsTypes,String[] paramsNames,Boolean isStatic) {
		this.queryid = queryid;
		this.returnFQN = returnFqn;
		this.parentFqn = parentFqn;
		this.methodname = methodName;
		this.paramsTypes = paramsTypes.split(Solr.AND);
		this.isStatic=isStatic;
	}

	/**
	 * @return the returnFQN
	 */
	public String getReturnFQN() {
		return returnFQN;
	}

	/**
	 * @return the parentFqn
	 */
	public String getParentFqn() {
		return parentFqn;
	}

	/**
	 * @return the methodname
	 */
	public String getMethodname() {
		return methodname;
	}

	/**
	 * @return the paramsTypes
	 */
	public String[] getParamsTypes() {
		return paramsTypes;
	}

	public Long getQueryID() {
		return queryid;
	}

	public boolean equalsParams(String[] parameterTypes) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not implemented yet");
		//return false;
	}

	public void restore() {
		// TODO Auto-generated method stub
		
	}

	public void setParent(IType t) {
		this.parent = t;
		
	}

	public void setMethod(IMethod method) {
		this.method = method;
	}

	public boolean getisStatic() {
		return isStatic;
	}

}
