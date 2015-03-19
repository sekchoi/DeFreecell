/**
 * Tun base class: define logging
 */


package quixote.ai.defreecell;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;


public class Base {
	
	// private Prop prop = new Prop();
	static protected final int NONE		= 0;	// none
	static protected final int ERROR	= 1;	// major
	static protected final int WARNING	= 2;	// brief
	static protected final int INFO		= 3;	// detail
	static protected final int DEBUG 	= 4;	// debug
	
	public int llevel = WARNING;
	
	static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss.SSS ");
	
	static public String getTime() {
		return sdf.format( new Date());
	}

	
	protected void loge(Object obj) {
		if (llevel >= ERROR)
			System.out.println(getTime() + obj.toString()); 
	}

	protected void logw(Object obj) {
		if (llevel >= WARNING)
			System.out.println(getTime() + obj.toString()); 
	}

	protected void logi(Object obj) {
		if (llevel >= INFO)
			System.out.println(obj.toString()); 
	}

	protected void logd(Object obj) {
		if (llevel >= DEBUG)
			System.out.println(obj.toString()); 
	}


	void printSystemProp() {
		Properties props = java.lang.System.getProperties();
		Enumeration<?> en = props.propertyNames();

		// props.list(out) ;
		while (en.hasMoreElements()) {
	        String str = (String)en.nextElement();
	        loge(str + "\t :" + props.getProperty(str));
		}
	}

	

	
}

