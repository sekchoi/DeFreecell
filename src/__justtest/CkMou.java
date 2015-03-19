package __justtest;

import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class CkMou {

	public static void main(String[] args) throws Exception{
		dd();
		//rc();
	}

	public static void dd() throws Exception{
		//Thread.sleep(3*1000);
		
	    Robot r = new Robot();
	    //r.mouseMove(35,35);
	    r.mouseMove(35+90,400);
	    int bt = InputEvent.BUTTON2_MASK;
	    r.mousePress( bt );
	    Thread.sleep(200);
	    r.mouseRelease( bt );
	    Thread.sleep(50);
	    
	    r.keyPress(KeyEvent.VK_SPACE);
	    r.keyRelease(KeyEvent.VK_SPACE);
	    
	    r.mousePress( bt );
	    r.mouseRelease( bt );
	    
	    //Thread.sleep(5*1000);
	}


	public static void rc() throws Exception{
	    Robot r = new Robot();
	    r.mouseMove(35,400);
	    int bt = InputEvent.BUTTON2_MASK;
	    r.mousePress( bt );
	    r.mouseRelease( bt );
	    Thread.sleep(50);
	    r.mousePress( bt );
	    r.mouseRelease( bt );
	    Thread.sleep(5*1000);
	  }
}
