package quixote.ai.defreecell.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import quixote.ai.defreecell.device.FrameReader;
import quixote.ai.defreecell.gui.WinLogging2;


/**
 * Process : Find game window 
 * {800, 495}
 */

public class FindWin {

	Dimension scr = null;		// screen size
	BufferedImage bi;		// full screan
	
	final int BlockSize	= 4;
	final int MinWidth	= 700;//790;
	final int MinHeight	= 400;//480;


	protected void n(Object e) { WinLogging2.logn(e.toString()); }
	protected void a(Object e) { WinLogging2.loga(e.toString()); }
	
	public Rectangle findWin() {
		// find dimemsoin
		FrameReader	fbr = new FrameReader();
		fbr.setDeviceNo(1);
		scr = fbr.getDim();

		bi = fbr.capture(new Rectangle(0,0, scr.width,scr.height));
		
		Rectangle gwin = new Rectangle(0,0, 100,100);	// find windows

		// scan start point 
		Point pt = scanBlock();
		if (pt == null) {
			n("Not found !");
			return null;
		}

		// scan exact window
		n("Found " + pt.x + "," + pt.y);
		int del;
		del = getXLen(pt.x, pt.y, -1);
		gwin.x = pt.x + del;
		
		del = getYLen(pt.x, pt.y, -1);
		gwin.y = pt.y + del;

		del = getXLen(gwin.x, gwin.y, +1);
		gwin.width = del;// - pt.x +1;
		
		del = getYLen(gwin.x, gwin.y, +1);
		gwin.height = del;// - pt.y +1;

		//cBox.setBound(gwin);
		//cBox.hideWin(50);

		return gwin;
	}


	// get green x-line length
	int getXLen(int x0, int y, int del) {
		int x=x0+del;
		for (; x>=0 && x<scr.width-del; x+= del) {
			if (! isGreen(x,y, del,del))
				break;
		}
		return x-x0+1;
	}
	
	// get green x-line length
	int getYLen(int x, int y0, int del) {
		int y=y0+del;
		for (; y>=0 && y<scr.height-del; y+= del) {
			if (! isGreen(x,y, del,del))
				break;
		}
		return y-y0+1;
	}

	
	// scan left-upper pointer, in block unit
	Point scanBlock() {
		a("scan y=");
		for (int y=0; y<scr.height-BlockSize; y+= BlockSize) {
			a(y + " ");
			for (int x=0; x<scr.width-BlockSize; x+= BlockSize) {
				//a(x + " ");	
				// check green color
				if (isGreen(x,y, BlockSize, BlockSize)) {
					if (isXLong(x,y, MinWidth) && isYLong(x,y, MinHeight))
						return new Point(x,y);
				}
			}
		}
		return null;
	}
	
	// get green x-line length
	boolean isXLong(int x0, int y, int max) {
		for (int x=x0; x<x0+max; x+= BlockSize) {
			if (! isGreen(x,y, BlockSize,BlockSize))
				return false;
		}
		return true;
	}
	
	// get green x-line length
	boolean isYLong(int x, int y0, int max) {
		for (int y=y0; y<y0+max; y+= BlockSize) {
			if (! isGreen(x,y, BlockSize,BlockSize))
				return false;
		}
		return true;
	}


	boolean isGreen(int x0, int y0, int w, int h) {
		if (w<0) w=-w;
		if (h<0) h=-h;
		try {
			int sum = 0;
			for (int y=y0; y<y0+h; y++) {
				for (int x=x0; x<x0+w; x++) {
					Color co = new Color(bi.getRGB(x, y));
					// check more green
					if (co.getGreen() > 60 && co.getGreen() > co.getBlue() && co.getGreen() > co.getRed())
						sum ++;
					else
						break;
				}
			}
			if (sum >= w*h)
				return true;
		} catch (Exception e) {
		}
		
		return false;
	}


	
}
