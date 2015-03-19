package quixote.ai.defreecell.game;

import java.awt.Rectangle;

import quixote.ai.defreecell.device.Mouse;
import quixote.ai.defreecell.game.ActionList.Action;
import quixote.ai.defreecell.gui.MainWindow;
import quixote.jfw.core.Timer;



/**
 * Process : Find game window 
 * {800, 495}
 */

public class _FindOneAnswer extends FindBase {

	static final int InterMili	= 500;
	
	long nextDisp = 0;		// card disp timer
	
	SceneQueue sque;
	Scene sc0 = null;
	Scene scSave = null;
	Scene scAnswer = null;

	Rectangle mywin;
	Mouse mou = new Mouse();
	
	public boolean isRun = false;
	
	public _FindOneAnswer(MainWindow parent) {
		super(parent);
	}

	public void play() {
		if (scAnswer == null) {
			n("No Answer !");
			return;
		}
		
		n("Play...");
		if (MainWindow.debug <2)
			MainWindow.debug = 2;
		Replay rp = new Replay(parent);
		rp.run(scAnswer);
	}

	int ruleNo = 0;
	public void findAnswer() {
		if (isRun) {
			isRun = false;
			return;
		}
		if (sc0==null) {
			pLog.n("No card !");
			return;
		}
		if (scSave != null) {
			sc0 = scSave;
			scSave = null;
		}
		
		isRun = true;

		n("RuleNo: " + ruleNo);
		sc0.ruleNo = ruleNo;
		findAnswer2();
		//Scene.incRuleNo();
		if (++ruleNo >= Scene.MaxRule)
			ruleNo = 0;	
	}
	
	public void findAnswer2() {

		sque = new SceneQueue();
		sque.push(sc0,1,0);

		pLog.clear();
		pLog.n(sque.toText(false));
		pLog.n(sc0.dispText());

		Timer tm = new Timer();
		Scene rs = find(sque);
		tm.stop();
		if (!isRun) {
			n("Paused! " + tm.getDiff());
			sc0 = sque.last();
		} else if (rs == null) {
			n("No solution! " + tm.getDiff());
		} else {
			scAnswer = rs;
			n("Solved. " + tm.getDiff());
			n(rs.dispText());
			n(rs.actSeq);
		}
		
		isRun = false;
	}

	public void findCard(int no) {
		scSave = null;
		if (no==0) {
			sc0 = new FindCard(parent).findCard();
		} else {
			Scene sc = new Scene(0);
			switch (no) {
			case 1:		sc.loadGame(31); break;
			case 2:		sc.loadGame(32); break;
			case 3:		sc.loadGame(33); break;
			default:	sc = null; break;
			}
			sc0 = sc;
		}
		
		if (sc0 == null) {
			pLog.n("Card error !");
			return;
		}
		pLog.clear();
		pLog.n("");
		pLog.n(sc0.dispText());
	}


	void dispCards(Scene sc, Action act) {
		pLog.clear();
		pLog.n(sque.toText(false));
		n(sque.toText(true));
		//pLog.a("Q:" +sque.size());
		//if (act != null)
		//	pLog.a(" pt:" + act.point);
		//pLog.n("");
		pLog.n(sc.dispText());
	}


	/**
	 * find solution recursively
	 * @param sc
	 * @return
	 */
	Scene find(SceneQueue sque) {
		//int MaxTry = 120 +0; // 105 150;
		int MaxTry = 120 +0; // 105 150;
		
		
		// if focus out, wait
		mywin = parent.getBounds();
		while (
				mou.getPoint().x < mywin.x
				// !mywin.contains(mou.getPoint())
				) {
			msleep(1000);
			a(".");
		}

		if (!isRun) {
			if (scSave == null)
				scSave = sque.last();
			return null;	// stop by forse
		}
		if (sque.size() > MaxTry) return null;
		Scene sc = sque.last(); 
		// n(sc.dispText());
		if (MainWindow.debug >= 2) {
			msleep(MainWindow.debug == 2 ? 10 : 300);
		}

		if (sc.solved()) {
			n(sque.toText(true));
			dispCards(sc, null);
			return sc;
		}

		ActionList al = new ActionList(sc);
		int acnt = al.findActions();
		if (acnt <=0) return null;

		while (al.available()) {
			Action act = al.pop();
			Scene st2 = sc.clone();

			// n(st2.dispText()); n(act.acts);
			
			st2.move(act.acts);

			// Check dup, is overhead 
			// if (sque.has(st2)) continue;
			
			long mili = System.currentTimeMillis();
			if (MainWindow.debug>=2 || nextDisp < mili) {
				nextDisp = mili + InterMili;
				dispCards(st2, act);
			}
			if (MainWindow.debug>=4) {
				scSave = st2;
				isRun = false;
				return null;
			}
			//n("Q:" +sque.size() + " pt:" + act.point);
			// Pause for debug

			if (! sque.push(st2, acnt, acnt-al.size()-1) )
				continue;
			Scene rs = find(sque);
			sque.remove();
			
			if (rs != null)
				return rs;
			if (!isRun)
				return null;	// stop by forse
		}
		return null;
	}
	

	Scene _find2(SceneQueue sque) {
		//Scene s1 = loadCard();
		Scene s1 = sque.last().clone();//loadCard();

		s1.move("d5f01");
		n(s1.dispText());
		s1.move("d5f11");
		n(s1.dispText());

		s1.move("d5h21");
		n(s1.dispText());
		s1.move("d5h31");
		
		n(s1.dispText());
		s1.move("d7h31");
		
		n("End");
		return s1;
	}
	
}
