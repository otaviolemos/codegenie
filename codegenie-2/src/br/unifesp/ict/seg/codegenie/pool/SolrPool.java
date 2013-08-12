package br.unifesp.ict.seg.codegenie.pool;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import br.unifesp.ict.seg.codegenie.tmp.Debug;
import br.unifesp.ict.seg.codegenie.tmp.MySingleResult;

public class SolrPool {

	private static Map<Long,MySingleResult> contents = new HashMap<Long,MySingleResult>();

	public static void add(Collection<MySingleResult> updatedResults) {
		for(MySingleResult msr : updatedResults){
			add(msr);
		}
		
	}

	public static void add(MySingleResult msr) {
		Debug.debug(SolrPool.class,"finding out if id#"+msr.getEntityID()+" already exists");
		if(!contents.containsKey(msr.getEntityID())){
			Debug.debug(SolrPool.class,"adding id#"+msr.getEntityID());
			contents.put(msr.getEntityID(),msr);
		}
	}
	
	public boolean contains(Long id){
		return contents.containsKey(id);
	}
	
	public static void clear(){
		contents.clear();
	}

	public static MySingleResult[] all(){
		Collection<MySingleResult> values = contents.values();
		return values.toArray(new MySingleResult[values.size()]);
	}
	
	public static MySingleResult[] all(Collection<Long> ids){
		HashSet<MySingleResult> values = new HashSet<MySingleResult>();
		for(Long id : ids){
			values.add(contents.get(id));
		}
		return values.toArray(new MySingleResult[values.size()]);
	}

	public static MySingleResult get(Long eid) {
		return contents.get(eid);
	}
	
}
