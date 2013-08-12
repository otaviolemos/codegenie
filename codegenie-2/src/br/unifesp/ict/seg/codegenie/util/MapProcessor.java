package br.unifesp.ict.seg.codegenie.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.JavaModelException;

import br.unifesp.ict.seg.codegenie.tmp.Debug;


public  class MapProcessor<T extends IMember> {

	public static enum Action {REPLACE, KEEP};

	private HashMap<String, T> toAdd = new HashMap<String, T>();

	/**if the @param target contains one element in @param source, then renames it*/
	public MapProcessor(T[] source, T[] target, Action a){
		HashMap<String, T> tfieldmap = new HashMap<String,T>();
		for(T f : target){
			tfieldmap.put(getName(f), f);
		}
		toAdd = new HashMap<String,T>();
		for(T f : source){
			if(tfieldmap.containsKey(getName(f))){
				//rename
				if(a.equals(Action.REPLACE)){
					toAdd.put(getName(f), f);
					T f1 = tfieldmap.get(getName(f));
					equalsAction(f,f1);
				}
			} else {
				toAdd.put(getName(f), f);
			}
		}
	}

	public Map<String, T> getToAdd(){return toAdd;}

	public String getName(T t){
		return t.getElementName();
	}

	public void equalsAction(T src, T target){
		try {
			target.rename("original_copy_"+target.getElementName(), false, null);
		} catch (JavaModelException e) {
			Debug.errDebug(MapProcessor.class,"Could not rename method: "+getName(src));
		}
	}
}
