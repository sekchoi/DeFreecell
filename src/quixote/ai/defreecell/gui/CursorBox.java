/**
 *  GUI: Monitoring main window 
 */


package quixote.ai.defreecell.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Box;
import javax.swing.JToolBar;
import javax.swing.JWindow;


public class CursorBox extends JWindow {

	private static final long serialVersionUID = 1L;

	
	Box winRight;
	
	static public Font	defaultFont;
	
	int iconW = 100;
	int iconH = 100;

	public CursorBox() {
		if (CursorBox.defaultFont != null)
			setFont(defaultFont);

		onCreate();
		
		setAlwaysOnTop(true);
		setOpacity(0.60f);
		setVisible(true);

		// onReady();
	}


	void moveIcon(int dx, int dy) {
		n("move " + dx + "," + dy);
		Rectangle rt = getBounds();

		setBounds(rt.x+dx, rt.y+dy, iconW, iconH);
	}


	void setBound(Rectangle rt) {
		setBounds(rt);
	}


	void onCreate() {
	    
		// 1.1 toolbar
		JToolBar toolBar = new JToolBar("toolBar") {

			private static final long serialVersionUID = 1L;

			int x1 = 100;
			int y1 = 100;
			boolean pressed = false;
			
			@Override
		    protected void processMouseMotionEvent(MouseEvent me) {
	    		//n("move " + me.toString());
				int id = me.getID();
				if (pressed && id == MouseEvent.MOUSE_DRAGGED) {
					int x2 = me.getXOnScreen();
					int y2 = me.getYOnScreen();
		    		
					moveIcon(x2-x1, y2-y1);
					x1 = x2;
					y1 = y2;
		    	//} else if (!isBarMode) {
		    	//	resizeWindow(true);
		    	}
		    }


		    @Override
		    protected void processMouseEvent(MouseEvent me) {
		    	if (me.getButton() != 1) return;
		    	
		    	int id = me.getID();
		    	if (id == MouseEvent.MOUSE_PRESSED) {
		    		pressed = true;
		    		x1 = me.getXOnScreen();
		    		y1 = me.getYOnScreen();
		    		n("pressed on " + x1+","+y1);
		    	} else if (id == MouseEvent.MOUSE_RELEASED) {
		    		n("release");
		    		pressed = false;
		    	}
		    }
		    
		};
		
		toolBar.setFloatable(false);

		add(toolBar, BorderLayout.PAGE_START);

		winRight = Box.createVerticalBox();
		
		// 4.1 cr log win
		//add(winRight);

		addWindowListener(new WindowListener() {
			
			public void windowClosing(WindowEvent e) {
				closeWindow();
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
		n("Ready");
	}

	
	Color defMenuBack = null;

	class ButtonListener implements ActionListener {
		ButtonListener() { }
		public void actionPerformed(ActionEvent e) {
			// onToolMenu(e.getActionCommand().trim());
		}
	}

	public void hideWin(long mili) {
		new Timer().schedule(new TaskHide(), mili);
	}


	public class TaskHide extends TimerTask {
		public TaskHide() {}

		@Override public void run() {
			closeWindow();
		}
	}

	private void closeWindow() {
		this.dispose();
	}


	void n(Object e) { WinLogging2.logn(e.toString()); }


}

