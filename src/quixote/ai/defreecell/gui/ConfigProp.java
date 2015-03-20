/**
 * properties file I/O
 */

package quixote.ai.defreecell.gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Enumeration;
import java.util.Properties;


public class ConfigProp {

	protected String propName = null;
	protected Properties prop = null;
	//protected File propFile = null;
	
	//protected int loadMethod = 0;	// 0:not check, 1; check every time
	//protected long fileTime = 0;


	protected void log (Object o) { if (o!= null) System.out.println(o.toString()); }


	/**
	 * @param properties file name
	 */
	public ConfigProp(String propName) {
		this.propName = propName;
		//prop = new Properties();
		load();
	}

    
	/**
	 * load properties file
	 */
	public boolean load() {
		prop = new Properties();
		
		File propFile = new File(propName);
		if (!propFile.isFile()) {
			return false;
		}
		
		FileInputStream fin = null;
		try {
			fin = new FileInputStream (propFile.getPath()); 
			prop.load(fin);
			
	    	return true;
		} catch (Exception e) {
			e.printStackTrace();
	    	return false;
		} finally {
			try { if (fin!=null) fin.close(); } catch (Exception e) {}
		}
  	}


	/**
	 * get key value, permit # char
	 */
	public String get0(String key) {
		reload();
    	return (String) prop.get(key);
  	}

	/**
	 * get key value, trim # cahr
	 * @param key
	 * @return
	 */
	public String get(String key) {
		String val = get0(key);
		if (val == null)
			return val;
		
		int pos=val.indexOf('#');
		if (pos>=0)
			val = val.substring(0, pos);
		return val.trim();
  	}

	/**
	 * get key value
	 * if no key, return defVal
	 */
	public String get(String key, String defVal) {
    	String val = get(key);
    	if (val == null)
    		put(key, defVal);
    	return (val == null) ? defVal : val;
  	}


	/**
	 * get int val
	 * if no key, return 0
	 */
	public int getInt(String key, int defVal) {
		String val = get(key);
    	try {
    		int ival = Integer.parseInt(val);
    		return ival;
    	} catch (Exception e) {
    		if (val == null)
        		put(key, ""+defVal);
    		return defVal;
		}
  	}

	/**
	 * get boolean value
	 * if no key, return false
	 */
	public boolean getBoolean(String key) {
    	String val = get(key);
    	if (val != null && val.equalsIgnoreCase("true"))
    		return true;
   		return false;
  	}
	
	/**
	 * dummy function
	 */
	protected boolean reload() {
		return true;
	}
	
	
	/**
	 * set the key value into the properties file
	 */
	public void put(String item, String val) {
		prop.put(item, val);    	
    }

	public void put(String item, long val) {
		prop.put(item, ""+val); 
    }


	// make cfg file
	public boolean save()  {
		StringBuilder sb = new StringBuilder();
		
		Enumeration<?> en = prop.keys();
		while (en.hasMoreElements()) {
			String key = (String)en.nextElement();
			sb.append(key + "=" + get(key) + "\n");
		}
		
		BufferedWriter ou = null;
		try {			
			ou = new BufferedWriter(new FileWriter(propName));
			ou.write(sb.toString());
			ou.close();
		} catch (Exception e) { }
		if (ou!= null) try {ou.close();} catch(Exception ex) {}

    	return true;
    }

    
}

