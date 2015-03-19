package quixote.ai.defreecell.device;

import java.awt.event.KeyEvent;

import quixote.jfw.core.Command;



/**
 *  GUI: Keyboard
 */

public class Keyboard extends Device {
	
	
	public final static int SKEY_START	= 0x10000+1;
	public final static int SKEY_CAD	= SKEY_START +1;
	public final static int SKEY_KO_EN	= SKEY_START +2;
	public final static int SKEY_TEXT	= SKEY_START +3;
	public final static int SKEY_END	= SKEY_START +20;
	

	public void specialKey(int skey) {
		n("recv SKEY: " + skey);
		if (skey == SKEY_CAD) {
			n( "start vchatService 1" );
			Command cmd = new Command();
			cmd.runPrg("net start vchatService");
			n( "start vchatService 2" );
		} if (skey == SKEY_KO_EN) {
			try {
				Command cmd = new Command();
				cmd.runPrg("vchatService koen");
			} catch (Exception e) {
				n("excep");
				e.printStackTrace();
			} catch (Error e) {
				n("error");
				e.printStackTrace();
			}
		}
	}
	

	public void keyEvent(int code, int press) {
		if (code ==0) {
			n("key c:" + code + " " + press + " ignored!");
			return;
		}
		// n("key c:" + code + " " + press);
		try {
			if (press==1)
				robot.keyPress(code);
			else
				robot.keyRelease(code);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void checkToggle() {
		// find foreground window
		Command cmd = new Command();
		cmd.runPrg("vchatService koen");
		n("toggle koen");
	}

	public void typeKey(int key) {
		robot.keyPress(key);
		robot.keyRelease(key);
	}
	
	public void typeEsc() {
		typeKey(KeyEvent.VK_ESCAPE);
	}

	public void typeSpace() {
		typeKey(KeyEvent.VK_SPACE);
	}
	
	/*
	static final Character.Subset[] korSet = { Character.UnicodeBlock.HANGUL_SYLLABLES };
	
	static void tougleKorean(java.awt.Window comp) {
		// change 한/영
		InputContext ime = comp.getInputContext();
			//java.awt.im.InputContext.getInstance();
		
		n( ime.isCompositionEnabled() + " " + ime.getLocale() );
		ime.setCharacterSubsets(ime.isCompositionEnabled() ? null : korSet);
		n( ime.isCompositionEnabled() + " " + ime.getLocale() );
	}
*/
	

}
