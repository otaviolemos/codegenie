package br.unifesp.ict.seg.codegenie.util;


import edu.uci.ics.sourcerer.services.search.adapter.SingleResult;
import br.unifesp.ict.seg.codegenie.search.fileserver.FileServerConnector;
import br.unifesp.ict.seg.codegenie.search.solr.MySingleResult;
import br.unifesp.ict.seg.codegenie.tmp.Debug;

public class EntitiesUtil {

	
	public static String shortNamesOnJavaAPI(SingleResult result) {
		String methodSig = toString(result);
		Class<?> c;
		ClassLoader cls = ClassLoader.getSystemClassLoader();
		try {
			String ret = result.getReturnFqn();
			Debug.debug(EntitiesUtil.class,"trying to make a smaller name on: "+ret);
			c = Class.forName(ret,false,cls);
			Debug.debug(EntitiesUtil.class,"\t"+c.getSimpleName());
			methodSig =methodSig.replace(ret, c.getSimpleName());
		} catch (ClassNotFoundException e) {}
		String params = result.getParams();
		if(params.startsWith("(")){
			params = params.replace("(", "");
		}
		if(params.endsWith(")")){
			params = params.replace(")", "");
		}
		String[] classes = params.split(",");
		if(classes!=null){
			for(int i=0;i<classes.length;++i){
				try {
					Debug.debug(EntitiesUtil.class,"trying to make a smaller name on: "+classes[i]);
					c = Class.forName(classes[i],false,cls);
					Debug.debug(EntitiesUtil.class,"\t"+c.getSimpleName());
					if(methodSig.contains(classes[i]))
						methodSig =methodSig.replace(classes[i], c.getSimpleName());
				} catch (ClassNotFoundException e) {
					//e.printStackTrace();
				}
				
			}
		}
		return methodSig;
	}
	
	public static String toString(SingleResult sr){
		String fqn = sr.getFqn();
		String ret = sr.getReturnFqn();
		String params = sr.getParams();
		long eid = sr.getEntityID();
		return ret+" "+fqn+params+" ["+eid+"]";
	}

	public static String getEntitySourceCode(Long entityID) {
		FileServerConnector fsc = new FileServerConnector(entityID,FileServerConnector.ENTITY);
		return new String(fsc.getBytes());
	}

	public static String shortNamesOnJavaAPI(MySingleResult sr) {
		long eid = sr.getEntityID();
		String ret = shortNamesOnJavaAPI(sr.getSingleResult());
		if(ret.endsWith("]")){
			int idx= ret.lastIndexOf("[");
			ret = ret.substring(0,idx);
		}
		return ret+ "["+eid+"]";
	}
	
	

}
