/**
 * @author ricardo
 */

package edu.uci.ics.mondego.codegenie.search;

public abstract class TreeObjectBase {

	protected String name;
	protected TreeObjectBase parent = null;
	
	public TreeObjectBase(String name) {
		this.name = name;
	}
	
	
	public String getName() {
		return name;
	}
	public void setParent(TreeObjectBase parent) {
		this.parent = parent;
	}

	
	public TreeObjectBase getParent() {
		return parent;
	}
	public String toString() {
		return getName();
	}
	
	abstract public void computeNumberOfChildrenUp();


}
