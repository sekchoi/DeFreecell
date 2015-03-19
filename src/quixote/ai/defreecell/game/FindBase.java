package quixote.ai.defreecell.game;

import java.util.Timer;
import java.util.TimerTask;

import quixote.ai.defreecell.gui.CursorBox;
import quixote.ai.defreecell.gui.MainWindow;
import quixote.ai.defreecell.gui.WinLogging1;
import quixote.ai.defreecell.gui.WinLogging2;



/**
 * Process : Find game window 
 * {800, 495}
 */

public class FindBase {

	CursorBox cBox = new CursorBox();

	MainWindow parent;
	WinLogging1 pLog;
	WinLogging2 vLog;


	public FindBase(MainWindow parent) {
		if (parent != null) {
			this.parent = parent;
			this.pLog = parent.pLog;
			this.vLog = parent.vLog;
		}
	}

	void n(Object o) { vLog.n(o); }
	void a(Object o) { vLog.a(o); }

	// delayed task
	void delayTask(String task, long mili) {
		Timer tm = new Timer();
		tm.schedule(new DelayedTask(tm, task), mili);
	}


	Thread prevThread = null;
	
	boolean runDelayTask(String task, Object o) {
		boolean rc = true;
		return rc;			
	}

	public class DelayedTask extends TimerTask {
		String task;
		Object p1, p2;
		Timer tm;

		public DelayedTask(Timer tm, String task) {
			this.task = task;
			this.tm = tm;
		}
		
		public DelayedTask(Timer tm, String task, Object p1) {
			this.task = task;
			this.p1 = p1;
			this.tm = tm;
		}

		@SuppressWarnings("deprecation")
		@Override public void run() {
			if (prevThread != null) {
				prevThread.stop();
				n("kill thread " + prevThread);
			}
			prevThread = Thread.currentThread();
			
			if (!runDelayTask(task, p1))
				n("!! No task: " + task);
			
			tm.cancel();
			prevThread = null;
		}
	}


	void msleep(long mili) {
		try {
			Thread.sleep(mili);
		} catch (Exception e) { }
	}

}
