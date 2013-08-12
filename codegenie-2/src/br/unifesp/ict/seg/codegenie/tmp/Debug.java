package br.unifesp.ict.seg.codegenie.tmp;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Debug {

	private static boolean debug = true;
	
	public static void debug(Class<?> class1, String string) {
		if(debug){
			String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
			System.out.println("[" + timeStamp
					+" @ "+class1.getSimpleName()+"]: "+string);
		}
	}
	public static void errDebug(Class<?> class1, String string) {
		if(debug){
			String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
			System.err.println("[" + timeStamp
					+" @ "+class1.getSimpleName()+"]: "+string);
		}
	}


}
