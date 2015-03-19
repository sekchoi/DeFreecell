package quixote.ai.defreecell.device;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;




/**
 *  GUI: Mouse
 */

public class Mouse extends Device {


	/**
	 * play mouse event
	 * @param type : 1=press, 0:release, -1:move, -2:wheel
	 * @param v1 : delta-x
	 * @param v2 : delta-y
	 */
	public void mouseEvent(int type, int v1, int v2) {
		switch (type) {
		case 1:
		case 0:
			int btMask = 0;
			if 		(v1==MouseEvent.BUTTON1) btMask = InputEvent.BUTTON1_MASK; 
			else if (v1==MouseEvent.BUTTON2) btMask = InputEvent.BUTTON2_MASK;
			else if (v1==MouseEvent.BUTTON3) btMask = InputEvent.BUTTON3_MASK;
	
			if (type==1)
				robot.mousePress(btMask);
			else
				robot.mouseRelease(btMask);
			break;

		case -1:
			if (rtScreen!=null)
				robot.mouseMove(rtScreen.x+v1, rtScreen.y+v2);
			break;
		case -2:	robot.mouseWheel(v1);	break;
		}
	}


	public void move(Point pt) {
		mouseEvent(-1, pt.x, pt.y);	// move
	}

	public void click(int bt, int x, int y) {
		mouseEvent(-1, x, y);	// move
		mouseEvent(1, bt, 0); // down
		mouseEvent(0, bt, 0); // up
	}

	public void drag(Point fr, Point to) {
		mouseEvent(-1, fr.x, fr.y);	msleep(10);
		mouseEvent(1, 1, 0);		msleep(300);
		
		int dx = (to.x - fr.x);
		int dy = (to.y - fr.y);
		int dt = (int) Math.sqrt(dx*dx + dy*dy) / 50;
		if (dt <3) dt = 3;
		
		dx /= dt;
		dy /= dt;
		
		for (int i=0; i<dt; i++) {
			mouseEvent(-1, fr.x+dx*i, fr.y+dy*i); msleep(50);
		}

		mouseEvent(-1, to.x, to.y);	msleep(300);
		mouseEvent(0, 1, 0);		msleep(10);
	}
	
	public Point getPoint() {
		return MouseInfo.getPointerInfo().getLocation();
	}

	void msleep(long mili) {
		try {
			Thread.sleep(mili);
		} catch (Exception e) { }
	}

}
