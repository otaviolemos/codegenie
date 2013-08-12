package br.unifesp.ict.seg.codegenie.pool;

import java.util.HashMap;
import java.util.Map;

import br.unifesp.ict.seg.codegenie.search.CGMethodInterface;
import br.unifesp.ict.seg.codegenie.search.solr.SearchQueryCreator;
import br.unifesp.ict.seg.codegenie.tmp.Debug;

public class MethodInterfacePool {

	private static Map<Long,SearchQueryCreator> contents = new HashMap<Long,SearchQueryCreator>();
	
	public static void add(SearchQueryCreator searchQueryCreator) {
		if(!contents.containsKey(searchQueryCreator.getID())){
			contents.put(searchQueryCreator.getID(),searchQueryCreator);
		}
	}

	public static void clear() {
		Debug.debug(MethodInterfacePool.class,"crearing pool");
		contents.clear();		
	}

	public static CGMethodInterface get(Long qid) {
		return contents.get(qid).getMethodInterface();
	}

}
