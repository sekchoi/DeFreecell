package quixote.ai.defreecell.device;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.text.SimpleDateFormat;

import quixote.ai.defreecell.gui.WinLogging2;



/**
 *  GUI: Mouse, Keyboard
 */

class Device {
	
	static Robot robot;
	static Rectangle rtScreen;	// screen size

	final static SimpleDateFormat fmt = new SimpleDateFormat("mm-dd HH:mm:ss.sss");

	static void n(Object e) { WinLogging2.logn(e.toString()); }

	static {
		try {
			robot = new Robot();
		} catch (Exception e) {
			n(e);
		}
	
	}
	
	public Device() {
		//log("new device");
	}
	
	public short getScrCount() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		return (short)gs.length; 
	}


}
