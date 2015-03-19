
package quixote.jfw.core;


/**
 * Cached properties file I/O
 */
public class CachedProp extends FileProp {

	protected int loadMethod = 1;	// 0:1 time, 1; check every time

	// private void log (Object o) { if (o!= null) System.out.println(o.toString()); }


	/**
	 * @param properties file name
	 */
	public CachedProp(String propName) {
		super(propName);
		loadMethod = 1;
	}

	
	/**
	 * if file is changed then reload
	 */
	protected boolean reload() {
		if (loadMethod > 0) {
			// log("loading");
			if (isChanged())
				return load();
		}
		return true;
	}


	/**
	 * check if file is chaged
	 */
	public boolean isChanged() {
		if (propFile == null)
			return true;
		
		return (propFile ==null || fileTime != propFile.lastModified());
	}


}

