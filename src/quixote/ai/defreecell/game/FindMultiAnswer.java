package quixote.ai.defreecell.game;

import java.awt.Rectangle;

import quixote.ai.defreecell.device.Mouse;
import quixote.ai.defreecell.gui.MainWindow;



/**
 * Process : Find game window 
 * {800, 495}
 */

public class FindMultiAnswer extends FindBase {

	static final int TimerSec= 1*60 + 0;

	
	long nextDisp = 0;		// card disp timer
	
	Scene sc0 = null;
	Scene scAnswer = null;	// first solution

	Solver []solver = null;
	
	Rectangle mywin;
	Mouse mou = new Mouse();
	
	private boolean isRun = false;

	public FindMultiAnswer(MainWindow parent) {
		super(parent);
	}

	public boolean isRunning() {
		return isRun; 
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

	public void findAnswer() {
		if (isRun) {
			forceStop();
			return;
		}
		if (sc0==null) {
			pLog.n("No card !");
			return;
		}
		
		scAnswer = null;
		isRun = true;
		
		Thread th = new Thread() {
			@Override
			public void run() {
				findAnswer2();
			}
		};
		th.start();
	}

	
	public void findAnswer2() {
		// create
		solver = new Solver[Scene.MaxRule];
		for (int i=0; i<solver.length; i++) {
			solver[i] = new Solver(sc0, i);
		}

		// start
		for (int i=0; i<solver.length; i++) {
			solver[i].start();
		}

		// wait solutions
		long mili0 = System.currentTimeMillis();
		long sec = 0;
		for(;;) {
			long sec2 = (System.currentTimeMillis() - mili0)/1000;
			if (sec2 > TimerSec) break;
			msleep(500);
			dispSolvers(sec2);
			/*if (sec != sec2) {
				sec = sec2;
				a(sec + " ");
				if (sec % 30 == 0)
					n("");
			}*/

			int cnt = 0;
			for (int i=0; i<solver.length; i++) {
				if (!solver[i].isRunning())
					cnt ++;
			}
			// TODO cut fast solution
			//if (cnt >= 3)
			if (cnt * 100 / solver.length >= 50) 
				break;
		}
		
		forceStop();

		//wait all stop
		for (int j=0; j<30; j++) {
			msleep(100);
			int i=0;
			for (i=0; i<solver.length; i++) {
				if (solver[i].isRunning())
					break;
			}
			if (i>=solver.length)
				break;
		}

		dispSolvers(sec);

		// select best solution
		for (int i=0; i<solver.length; i++) {
			n(solver[i].perLoop(true));
			n(solver[i].perMade(true));

			if (solver[i].scAnswer != null) {
				if (scAnswer == null || solver[i].scAnswer.isBetter(scAnswer)) {
					scAnswer = solver[i].scAnswer;
				}
			}
		}

		isRun = false;
	}
	
	public void forceStop() {
		isRun = false;
		for (int i=0; i<solver.length; i++) {
			solver[i].forceStop();
		}
		// msleep(1000);
	}


	void dispSolvers(long sec) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<solver.length; i++) {
			if (solver[i].isRunning()) {
				sb.append(solver[i].perMade(true)).append("\n");
				//sb.append(solver[i].perLoop(true)).append("\n");
			}
		}

		for (int i=0; i<solver.length; i++) {
			if (solver[i].isRunning()) {
				sb.append(solver[i].dispText()).append("\n" + sec + "secs");
				break;
			}
		}
		if (sb.length() > 0) {
			pLog.clear();
//			pLog.a("        1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890\n");
			pLog.a("                 1         2         3         4         5         6         7         8         9         \n");
			pLog.a(sb.toString());
		}
	}


	public void findCard(int no) {
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

}
