package quixote.ai.defreecell.game;

import quixote.ai.defreecell.game.ActionList.Action;
import quixote.ai.defreecell.gui.WinLogging2;
import quixote.jfw.core.Timer;



/**
 * Process : Solver 
 */

public class Solver extends Thread {

	private SceneQueue sque;
	Scene sc0 = null;
	Scene scAnswer = null;
	private String perLoop = null;		// loop 진행율 각 위치가 (0~9)
	private String perMade = null;		// game 완성율 시작 0 ~ 마지막9
	Timer tm ;


	private boolean isRun = false;
	boolean forceStop = false;
	
	int ruleNo = 0;

	protected void n(Object e) { WinLogging2.logn(prefix() + e.toString()); }


	public Solver(Scene sc, int ruleNo) {
		this.ruleNo = ruleNo;
		this.sc0 = sc.clone();
		this.sc0.ruleNo = ruleNo;
	}

	public String prefix() {
		return "R" + ruleNo + ": ";
	}
	
	public void forceStop() {
		forceStop = true;
	}

	public boolean isRunning() {
		return isRun;
	}

	// scene shape
	public String dispText() {
		try {
			//if (sque.size()<=0) return "";
			return sque.last().dispText();	
		} catch (Exception e) {
			return "";
		}
	}
	
	public String perMade(boolean full) {
		String txt;
		if (isRun)
			txt = prefix() + sque.perMade(full);
		else
			txt = prefix() + perMade + (scAnswer!=null ? "  " + tm.getDiff() : "");
		return txt ;
	}
	
	public String perLoop(boolean full) {
		String txt;
		if (isRun)
			txt = prefix() + sque.toText(full);
		else
			txt = prefix() + perLoop + (scAnswer!=null ? "  " + tm.getDiff() : "");
		return txt ;
	}

	
	@Override
	public void run() {
		if (isRun) {
			isRun = false;
			return;
		}
		if (sc0==null) {
			n("No card !");
			return;
		}

		isRun = true;

		sque = new SceneQueue();
		sque.push(sc0,1,0);

		tm = new Timer();
		Scene rs = find(sque);
		tm.stop();
		isRun = false;
		
		if (forceStop) {
			n("Stopped! " + tm.getDiff());
			sc0 = sque.last();
		} else if (rs == null) {
			n("No solution! " + tm.getDiff());
		} else {
			scAnswer = rs;
			n("Solved. " + tm.getDiff());
			n(perLoop);
			n(rs.actSeq);
		}

		isRun = false;
	}


	/**
	 * find solution recursively
	 * @param sc
	 * @return
	 */
	Scene find(SceneQueue sque) {
		if (!isRun) return null;	// stop by forse

		Scene sc = sque.last(); 

		if (sc.solved()) {
			perLoop = sque.toText(true);
			perMade = sque.perMade(true);
			return sc;
		}

		int [][]limit = {
				// qsize, makeCount
				//{10, 1}, {20, 3}, {30, 5}, {40, 15}, {60, 20}, {80, 25}, {100, 30}	// 
				//{10, 3}, {20, 5}, {30, 7}, {40, 15}, {60, 20}, {80, 25}, {100, 30}	// 
				//{10, 3}, {20, 5}, {30, 7}, {40, 15}, {60, 20}, {80, 25}	//
				//{10, 3}, {20, 5}, {30, 7}, {40, 15}, {60, 20}, {80, 25}	//
				{15, 5}, {30, 10}, {40, 15}, {60, 20}, {80, 25}	// 매우좋음
		};
		
		// TODO bleak for loosy
		for (int i=0; i<limit.length; i++) {
			if (sque.size() > limit[i][0] && sc.perMade() < limit[i][1])
				return null;
		}
		
		ActionList al = new ActionList(sc);
		int acnt = al.findActions();
		if (acnt <=0) return null;

		while (al.available()) {
			Action act = al.pop();
			Scene st2 = sc.clone();
			
			st2.move(act.acts);

			// Check dup, is overhead 
			if (sque.has(st2))
				continue;
			
			if (! sque.push(st2, acnt, acnt-al.size()-1) )
				continue;
			Scene rs = find(sque);
			sque.remove();
			
			if (rs != null)
				return rs;

			if (forceStop) {
				if (perLoop == null) {
					perLoop = sque.toText(true);
					perMade = sque.perMade(true);
				}
				return null;	// stop by forse
			}
		}
		return null;
	}

}
