package quixote.ai.defreecell.game;

import java.awt.Point;
import java.awt.Rectangle;

import quixote.ai.defreecell.device.Keyboard;
import quixote.ai.defreecell.device.Mouse;
import quixote.ai.defreecell.gui.MainWindow;



/**
 * Process : Find game window 
 * {800, 495}
 */

public class Replay extends FindBase {

	Scene st = null;

	Rectangle gwin;
	Mouse mou;
	Keyboard key;
	
	GameImage gimg = null;

	public Replay(MainWindow parent) {
		super(parent);
	}


	public void run(Scene stAnswer) {
		st = stAnswer;

		// st.actSeq = st.actSeq.replace(';', ',');
		String ar[] = st.actSeq.split(";");

		// 1. find window
		FindWin findWin = new FindWin();
		gwin = findWin.findWin();
		if (gwin == null) {
			n("No window !");
			return;
		}
		
		// 2. play actions 
		mou = new Mouse();
		key = new Keyboard();
		gimg = new GameImage();

		msleep(300);
		mou.click(1, gwin.x+2, gwin.y+2);

		for (int i=0; i<ar.length; i++) {
			a(" " +(i+1)+ "/"+ (ar.length) +": ");
			if (!runOne(st, ar[i]))
				break;
			n("");
			//if (i >= 45 + 500) { n("Stopped"); break; }
		}
	}


	public boolean runOne(Scene st, String acts) {
		
		String ar[] = acts.split(",");

		for (int i=0; i<ar.length; i++) {
			a(ar[i] + " ");
			if (!gwin.contains(mou.getPoint())) { n("Stopped"); return false; }

			if (ar[i].charAt(2) != 'h') {
				msleep(300 +200*MainWindow.debug);
				play(ar[i]);
				continue;
			} else {
				mou.click(3, gwin.x+2, gwin.y+2);

				// skip auto move
				for (i++; i<ar.length && ar[i].charAt(2) == 'h'; i++) {
					a(ar[i] + " ");
					if (!gwin.contains(mou.getPoint())) { n("Stopped"); return false; }
					msleep(130 +100*MainWindow.debug);
				}
				i--;
			}
		}
		return true;
	}

	
	// focus is in me
	boolean checkFocus() {
		return parent.isActive(); // parent.isFocused();
	}

	private void play(String acts) {
		Act act = new Act(acts);
		Point fr = gimg.getCardPoint(act.fr, act.fri, act.fri+act.cnt);
		Point to = gimg.getCardPoint(act.to, act.toi, act.toi);
		
		mou.drag(fr, to);

		/*
		mou.move(fr);	 msleep(10);
		key.typeEsc();	 msleep(10);
		key.typeSpace(); msleep(100 *WindowMain.debug);
		mou.move(to);	 msleep(200 *WindowMain.debug); 
		key.typeSpace(); msleep(100);
		*/
	}
}

