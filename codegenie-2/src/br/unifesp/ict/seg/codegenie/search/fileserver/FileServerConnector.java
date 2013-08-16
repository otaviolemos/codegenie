package br.unifesp.ict.seg.codegenie.search.fileserver;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.eclipse.jface.preference.IPreferenceStore;

import br.unifesp.ict.seg.codegenie.Activator;
import br.unifesp.ict.seg.codegenie.preferences.PreferenceConstants;
import br.unifesp.ict.seg.codegenie.tmp.Debug;

public class FileServerConnector {
	public static final int ENTITY = 2;
	private String fileServer;
	private long ID;
	
	public FileServerConnector(long id, int type){
		if(type==ENTITY){
			IPreferenceStore store = Activator.getDefault().getPreferenceStore();
			fileServer = store.getString(PreferenceConstants.FILE_SERVER)+"/FileServer?entityID=";
		}
		this.ID=id;
	}
	
	
	public byte[] getBytes(){
		URL url;
		long start = System.currentTimeMillis();
		Debug.debug(getClass(),"retrieving URL= "+fileServer+ID);
		try {
			url = new URL(fileServer+ID);
			URLConnection conn = url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String ret = "";
			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				ret+=inputLine+"\n";
			}
			br.close();
			long end = System.currentTimeMillis();
			Debug.debug(getClass(),"took "+(end-start)/1000d+" seconds");
			return ret.getBytes();
		} catch (Exception e) {
			return e.getMessage().getBytes();
		}
	}
	
	public String getSourceCode(){
		return new String(getBytes());
	}
}
