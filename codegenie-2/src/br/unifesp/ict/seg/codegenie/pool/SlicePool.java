package br.unifesp.ict.seg.codegenie.pool;



import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import br.unifesp.ict.seg.codegenie.search.slicer.SliceFile;


public class SlicePool {
	
	//                 qid, {eid}
	protected static HashMap<Long,Set<Long>> map = new HashMap<Long,Set<Long>>();
	//                 eid, SliceFile
	protected static HashMap<Long,SliceFile> contents = new HashMap<Long,SliceFile>();

	public static SliceFile getByQID(long qid) {
		return getByEID(getEID(qid));
	}

	
	public static Long getEID(Long qid){
		Set<Long> set = map.get(qid);
		if(set.size()>0)
			return set.iterator().next();
		return new Long(-1);
	}
	
	public static SliceFile getByEID(Long eid) {
		return contents.get(eid);
	}
	
	public static void add(SliceFile s, Long eid, Long qid){
		contents.put(eid, s);
		if(!map.containsKey(qid)){
			map.put(qid, new HashSet<Long>());
		}
		map.get(qid).add(eid);
	}

	public static void remove(Long eid) {
		contents.remove(eid);
	}
	
}
