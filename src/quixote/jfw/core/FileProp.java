
package quixote.jfw.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.util.Properties;


/**
 * properties file I/O
 *
 */
public class FileProp {

	protected String propName = null;
	public Properties prop = null;
	protected File propFile = null;
	
	protected int loadMethod = 0;	// 0:not check, 1; check every time
	protected long fileTime = 0;


	protected void log (Object o) { if (o!= null) System.out.println(o.toString()); }


	/**
	 * @param properties file name
	 */
	public FileProp(String propName) {
		this.propName = propName;
		//prop = new Properties();
		load();
	}

	public boolean isloaded() {
		return propFile != null;
	}
	
    /**
	 * get fullpath of the properties file
	 * @param prop
	 * @return
	 */
    public static String getPropPath(String propName) {
    	// search file path
    	File propFile = new File(propName);
		if (propFile.isFile())
			return propName;
    	
    	// search in classparh
    	URL url = new DummyClass().getClass().getResource("/" + propName);
		if (url != null)
			return url.getFile();
		return null;
    }
    
	/**
	 * load properties file
	 */
	public boolean load() {
		//log("loading ...");
		
		prop = new Properties();
		
		propFile = new File(propName);
		if (!propFile.isFile()) {
			URL url1 = getClass().getResource("/"+ propName);
			if (url1 != null)
				propFile = new File(url1.getPath());
			else
				propFile = null;
		}
		
		if (propFile == null) {
			System.out.println(propName + " not found1 !");
			new Exception(propName + " not found1 !").printStackTrace();
			return false;
		}

		FileInputStream fin = null;
		try {
			fin = new FileInputStream (propFile.getPath()); 
			prop.load(fin);

			fileTime = propFile.lastModified();
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
    public boolean put(String item, String val) {

    	//String filePathRead  = new WizApp().getFilepath("/"+ WizApp.appPropName);
    	String filePathRead  = propFile.getPath();
		String filePathWrite = filePathRead + ".new";

		BufferedReader in = null;
		BufferedWriter ou = null;
		try {			
			in = new BufferedReader(new FileReader(filePathRead));
			ou = new BufferedWriter(new FileWriter(filePathWrite));
	
			String buf;
			boolean found =false;
	
			while (in.ready()) {
				buf = in.readLine().trim();
				if (buf.startsWith(item)) {
					found = true;
					buf = item+ "=" + val;
				}			
				ou.write(buf + "\n");
			}
			if (!found) {
				ou.write(item+ "=" + val + "\n");
			}
			in.close();
			ou.close();
			
			// 기존 properties ?��?��??? ?��?��?���? ?���? 만든 properties ?��?��?�� 기존 ?��름으�? rename ?��?��.
			File fe1 = new File(filePathRead);
			fe1.delete();
	
			File fe2 = new File(filePathWrite);
			fe2.renameTo(fe1);
			fe2 = null ;
			fe1 = null;
		} catch (Exception e) { 
		}
		if (in!= null) try {in.close();} catch(Exception ex) {}
		if (ou!= null) try {ou.close();} catch(Exception ex) {}

    	return true;
    }


    /**
     * get jar path of the class
     */
    public static String getJarpath(String cls) {
    	cls = cls.replace('.', '/').trim();
    	cls = "/" + cls + ".class";
		URL url = new DummyClass().getClass().getResource(cls);
		if (url == null)
			return null;

		cls = url.getFile();
		if (cls.startsWith("file"))
			cls = cls.substring(5, cls.indexOf("!"));
		else
			cls = cls.substring(0);
		return cls;
    }

    
}

