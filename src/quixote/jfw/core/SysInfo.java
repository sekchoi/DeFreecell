

package quixote.jfw.core;



public class SysInfo {

	static String	osName;

	static {
		osName = System.getProperty("os.name");
	}
	
	
	public static boolean isWindows() {
		if (osName.toLowerCase().indexOf("windows") >= 0)
			return true;
		return false;
	}
	
	public static String getOsName() {
		return osName;
	}
	
}

