package quixote.ai.defreecell.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

import quixote.ai.defreecell.Constant;
import quixote.ai.defreecell.device.Keyboard;
import quixote.ai.defreecell.device.Mouse;
import quixote.ai.defreecell.game.FindMultiAnswer;
import quixote.ai.defreecell.game.FindWin;
import quixote.jfw.core.Command;

/**
 *  GUI: Chatter main window 
 */

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	// parameters

	final static String WhatsNew = "Ready\n" ;
	
	public final static String Icon = "/quixote/ai/defreecell/img/MainWin.jpg";
	ConfigProp	cfg = null;

	// GUI Components
	JSplitPane		splitV;

	public WinLogging1 pLog = new WinLogging1();
	public WinLogging2 vLog = new WinLogging2();

	FindMultiAnswer findAns = new FindMultiAnswer(this);

	static public Font	defaultFont;
	
	static public Color backColor;
	static public Color editColor;

	public static int debug = 1;
	
	static {
		defaultFont = new Font("굴림체", Font.PLAIN, 12);
		//defaultFont = new Font("맑은 고딕", Font.PLAIN, 12);
		//defaultFont = new Font("새굴림", Font.PLAIN, 12);
		//defaultFont = new Font("Courier New", Font.PLAIN, 12);
		//defaultFont = new Font("FixedSys", Font.PLAIN, 12);

		backColor = new Color(250,250,250);
		editColor = new Color(255,255,255);
	}

	public MainWindow() {
	}


	public void run() {
		cfg = new ConfigProp(Constant.CfgName);
		Dimension siz = Toolkit.getDefaultToolkit().getScreenSize();
		int del = 70;
		if (cfg.getInt("main.win.x", 0) > siz.width-del)
			cfg.put("main.win.x", siz.width-del);
		
		if (cfg.getInt("main.win.y", 0) > siz.height-del)
			cfg.put("main.win.y", siz.height-del);	
		cfg.save();

		setTitle(Constant.Version);

		try { 
			BufferedImage image = ImageIO.read( getClass(). getResource(Icon) ); 
			//setIconImage(image);
			ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
			images.add(image);
			images.add(image);
			images.add(image);
			setIconImages(images);
		} catch (Exception e) { } 

		setBounds(cfg.getInt("main.win.x", 820), cfg.getInt("main.win.y", 0),
				cfg.getInt("main.win.w", 700), cfg.getInt("main.win.h", 575));

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		if (MainWindow.defaultFont != null)
			setFont(defaultFont);

		onCreate();
		setVisible(true);
		
		onReady();
	}


	void onCreate() {
		// 1.1 toolbar
		JToolBar toolBar = new JToolBar("toolBar");
		addToolButton1(toolBar);
		
		add(toolBar, BorderLayout.PAGE_START);

		// 2.1 cr left list

		// 4.1 cr log win

		// 5.1 cr v split
		splitV = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pLog, vLog);
		//splitV.setOneTouchExpandable(true);
		splitV.setDividerLocation(cfg.getInt("main.split.V.loc", 300));
		splitV.setDividerSize(4);
		
		add(splitV);

		// window event
		addWindowListener(new WindowListener() {
			public void windowClosing(WindowEvent e) {
				saveWindowPos();
			}
			public void windowOpened(WindowEvent e) { }
			public void windowIconified(WindowEvent e) { }
			public void windowDeiconified(WindowEvent e) { }
			public void windowDeactivated(WindowEvent e) { }
			public void windowClosed(WindowEvent e) { }
			public void windowActivated(WindowEvent e) { }
		} );
	}


	void onReady() {
		vLog.n(WhatsNew);
		//"Ready");
	}


	class ButtonListener implements ActionListener {
		ButtonListener() { }
		public void actionPerformed(ActionEvent e) {
			onToolMenu(e.getActionCommand().trim());
		}
	}


	void saveWindowPos() {
		cfg.put("main.split.V.loc",  ""+ splitV.getDividerLocation());

		Rectangle rt = getBounds();
		cfg.put("main.win.x",   ""+ rt.x);
		cfg.put("main.win.y",   ""+ rt.y);
		cfg.put("main.win.w",  ""+ rt.width);
		cfg.put("main.win.h", ""+ rt.height);
		
		cfg.save();
	}


	static final String MN_RunPrg	= "Run";
	//static final String MN_FindWin	= "Win";
	static final String MN_FindCard	= "Card";
	
	static final String MN_Card1	= "c1";
	static final String MN_Card2	= "c2";
	static final String MN_Card3	= "c3";
	static final String MN_Card4	= "c4";
	
	static final String MN_Solve	= "Solve";
	static final String MN_Next		= "Next";
	static final String MN_Play		= "Play";
	static final String MN_Debug1	= "D1";
	static final String MN_Debug2	= "D2";
	static final String MN_Debug3	= "D3";
	static final String MN_Debug4	= "D4";

	static final String MN_ClearLog = "Clear";
	static final String MN_Exit 	= "Exit";
	//static final String MN_Check 	= "Check";
		
	void addToolButton1(JToolBar toolBar) {
		// tool button
		ButtonListener btli = new ButtonListener();

		JButton but;
		String [] tool1 = {
			MN_RunPrg,   
			//MN_FindWin,
			MN_FindCard,
			MN_Solve,
			MN_Next,
			MN_Play,
			"",
			MN_Card1,
			MN_Card2,
			MN_Card3,
			"",
			MN_Debug1,
			MN_Debug2,
			MN_Debug3,
			MN_Debug4,
			"", 
			MN_ClearLog, "", 
			//"Logout",
			MN_Exit, 
		};
		
		for (int i=0; i<tool1.length; i++) {
			if (tool1[i].length()<=0) {
				toolBar.addSeparator();		
			} else {
				but = new JButton(tool1[i]);
				but.addActionListener(btli);
				toolBar.add(but);
			}
		}
	}


	void onToolMenu(String txt) {
		if (txt.equals(MN_Exit)) {
			saveWindowPos();
			System.exit(0);
		}
		else if (txt.equals(MN_RunPrg))		onRunPrg();
		
		else if (txt.equals(MN_FindCard))	onFindCard();
		else if (txt.equals(MN_Solve))		onFindAnswer();
		else if (txt.equals(MN_Next))		delayTask("onNextSolve", 50);
		else if (txt.equals(MN_Play))		onPlay();

		else if (txt.equals(MN_Card1))		onLoadCard(1);
		else if (txt.equals(MN_Card2))		onLoadCard(2);
		else if (txt.equals(MN_Card3))		onLoadCard(3);

		else if (txt.equals(MN_Debug1))		onDebug(1);
		else if (txt.equals(MN_Debug2))		onDebug(2);
		else if (txt.equals(MN_Debug3))		onDebug(3);
		else if (txt.equals(MN_Debug4))		onDebug(4);
		
		else if (txt.equals(MN_ClearLog)) 	vLog.clear();
		
		else {
			vLog.n("No tool func: " + txt);
		}
	}

	void onNextSolve() {
		if (findAns.isRunning())
			killSover();
			
		// Press F2 : Start new game
		FindWin findWin = new FindWin();
		Rectangle gwin = findWin.findWin();
		if (gwin == null) {
			vLog.n("No window !");
			return;
		}

		Mouse mou = new Mouse();
		Point p0 = mou.getPoint();
		mou.click(1, gwin.x+2, gwin.y+2);
		
		Keyboard key = new Keyboard();
		key.typeKey(KeyEvent.VK_F2);
		vLog.n("Loading");
		msleep(3500);
		
		vLog.n("Finding");
		findAns.findCard(0);

		onFindAnswer();
		
		// Point p0 = mou.getPoint();
		//mou.click(1, p0.x, p0.y);
		mou.move(p0);
	}
	
	void onRunPrg() {
		vLog.n("Exec " + Constant.PrgName);
		Command cmd = new Command();
		cmd.runPrg(Constant.PrgName);
	}


	void onFindCard() {
		vLog.n("Find Card");
		//new FindCard(this).findCard();
		killSover();
		delayTask("onFindCard2", 50);
	}
	
	void onFindCard2() {
		findAns.findCard(0);
	}

	void onFindAnswer() {
		if (findAns.isRunning()) {
			killSover();
		} else {
			delayTask("onFindAnswer2", 10);
			vLog.n("Solving");
		}
	}

	void onFindAnswer2() {
		findAns.findAnswer();
	}

	void onPlay() {
		killSover();
		delayTask("onPlay2", 100);
	}

	void killSover() {
		if (findAns.isRunning()) {
			findAns.forceStop();
			msleep(500);
		}
	}
	
	void onPlay2() {
		findAns.play();
	}
	
	void onDebug(int lv) {
		debug = lv;
	}

	void onLoadCard(int no) {
		killSover();
		findAns.findCard(no);
	}


	boolean runDelayTask(String task, Object o) {
		boolean rc = true;
		try {
			if 		(task.equals("onFindCard2"))	onFindCard2();
			else if (task.equals("onFindAnswer2"))	onFindAnswer2();
			else if (task.equals("onPlay2"))		onPlay2();
			else if (task.equals("onNextSolve"))	onNextSolve();

			else  rc = false;
		} catch (Exception e) {
		}
		return rc;			
	}


	// delayed task
	void delayTask(String task, long mili) {
		Timer tm = new Timer();
		tm.schedule(new DelayedTask(tm, task), mili);
	}


	Thread prevThread = null;
	
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
				try {
					prevThread.stop();
				} catch (Exception e) {
				}
				vLog.n("kill thread " + prevThread);
			}
			prevThread = Thread.currentThread();
			
			if (!runDelayTask(task, p1))
				vLog.n("!! No task: " + task);
			
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
