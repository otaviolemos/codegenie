package br.unifesp.ict.seg.codegenie.pool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;

import br.unifesp.ict.seg.codegenie.tmp.Debug;
import br.unifesp.ict.seg.codegenie.tmp.MySingleResult;

public class SearchResultMap {
	
	private static HashMap<Long,ResultInfo> contents = new HashMap<Long,ResultInfo>();
	

	public static void add(Long id, List<MySingleResult> updatedResults,
			IType selection, IJavaProject javap) {
		Debug.debug(SearchResultMap.class,"Mapping id#"+id+" with "+javap.getElementName());
		contents.put(id,new ResultInfo(updatedResults,selection,javap));
	}
	public static IJavaProject getProject(Long id){
		ResultInfo ri = contents.get(id);
		if(ri!=null){
			return ri.javap;
		}
		return null;
	}
	public static IType getTestClass(Long id){
		ResultInfo ri = contents.get(id);
		if(ri!=null){
			return ri.testClass;
		}
		return null;
	}
	
	public static List<Long> getResults(Long id){
		ResultInfo ri = contents.get(id);
		if(ri!=null){
			return ri.solrResults;
		}
		return null;
	}
	
	
	public static class ResultInfo{
		private ArrayList<Long> solrResults = new ArrayList<Long>();
		private IJavaProject javap;
		private IType testClass;
		private ResultInfo(List<MySingleResult> updatedResults,
				IType selection, IJavaProject javap){
			this.javap=javap;
			this.testClass=selection;
			for(MySingleResult sr : updatedResults){
				solrResults.add(sr.getEntityID());
			}
		}
		
	}


	public static void clear() {
		Debug.debug(MethodInterfacePool.class,"crearing pool");
		contents.clear();
	}


}
