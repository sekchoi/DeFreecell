package quixote.ai.defreecell.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import quixote.ai.defreecell.device.FrameReader;
import quixote.ai.defreecell.gui.WinLogging2;
import quixote.ai.defreecell.img.ImgHelper;


/**
 * Process : Find card position 
 * {801, 497}
 */

public class GameImage {

	// deal cards
	static final Point 		dealDgtPt	= new Point(67,153);	// 수자 위치
	static final Dimension	dealDgtBox	= new Dimension(9,11);	// 수자 크기
	static final Point		dealShaPt	= new Point(67,153+14);	// 모양 위치
	static final Dimension	dealShaBox	= new Dimension(9,6);	// 모양 크기
	static final Dimension	dealDelta	= new Dimension(86,23);	// delta
	static final int 		dealDx[]	= {0,0,1,1,  3,2,1,0};	// delta x

	// free, home cards
	static final Point 		freeDgtPt	= new Point(23,26);		// 수자 위치
	static final Dimension	freeDgtBox	= new Dimension(9,11);	// 수자 크기
	static final Point		freeShaPt	= new Point(23,26+14);	// 모양 위치
	static final Dimension	freeShaBox	= new Dimension(9,6);	// 모양 크기
	static final Dimension	freeDelta	= new Dimension(91,0);	// delta
	static final int 		freeDx[]	= {0,0,0,0,  54,54,54,54};	// delta x

	
	BufferedImage bi;	// game image 	
	Rectangle gwin;		// game image

	protected void n(Object e) { WinLogging2.logn(e.toString()); }

	
	public GameImage() {
		if (findWin())
			capWin();
	}

	boolean findWin() {
		gwin = new FindWin().findWin();
		if (gwin == null) {
			n("No window !");
			bi = null;
			return false;
		}
		return true;
	}

	/**
	 * Capture game window
	 */
	void capWin() {
		FrameReader	fbr = new FrameReader();
		bi = fbr.capture(gwin);
		//n(bi);
		//ImgHelper.saveAs(bi, "game.png");
	}


	// Get deal image in x,y
	BufferedImage getDigImgDeal(int x, int y) {
		return ImgHelper.subImageBW(bi,
				dealDgtPt.x + dealDelta.width*x + dealDx[x],
				dealDgtPt.y + dealDelta.height*y,
				dealDgtBox.width, dealDgtBox.height);
	}
	
	// Get deal shape in x,y
	BufferedImage getShaImgDeal(int x, int y) {
		return ImgHelper.subImageBW(bi,
				dealShaPt.x + dealDelta.width*x + dealDx[x],
				dealShaPt.y + dealDelta.height*y,
				dealShaBox.width, dealShaBox.height);
	}

	// Get free digit in x,y
	BufferedImage getDigImgFree(int x) {
		int y = 0;
		return ImgHelper.subImageBW(bi,
				freeDgtPt.x + freeDelta.width*x + freeDx[x],
				freeDgtPt.y + freeDelta.height*y,
				freeDgtBox.width, freeDgtBox.height);
	}
	
	// Get free image in x,y
	BufferedImage getShaImgFree(int x) {
		int y = 0;
		return ImgHelper.subImageBW(bi,
				freeShaPt.x + freeDelta.width*x + freeDx[x],
				freeShaPt.y + freeDelta.height*y,
				freeShaBox.width, freeShaBox.height);
	}

	
	/**
	 * Get card position
	 * @param cno
	 * @param inx
	 * @return
	 */
	Point getCardPoint(int cno, int inx, int cnt) {
		Point pt = new Point();
		if (cno >=8) {
			cno -=8;
			pt = new Point(freeDgtPt);
			pt.x += (cno * freeDelta.width) + freeDx[cno];
		} else {
			int dely = dealDelta.height;
			if (cnt >= 4)
				dely = getDealDeltaY(cno);	// 간격이 좁아졌을 때 구하기
			pt = new Point(dealDgtPt);
			pt.x += (cno * dealDelta.width) + dealDx[cno];
			pt.y += dely * inx;	// cnt 반영
		}			
		pt.x += gwin.x + freeDelta.width/2;
		pt.y += gwin.y + 7;
		return pt;
	}

	static final Point 		dealCheckPt	= new Point(125,150);	// 카드 간격 확인용
	
	/**
	 * Deal cell의 card 높이 차이 구하기 
	 */
	public int getDealDeltaY(int cno) {
		capWin();
		int dy = dealDelta.height;
		if (bi==null)
			return dy;
		
		// capture
		int y = dealCheckPt.y;
		int x = dealCheckPt.x + (cno * dealDelta.width) + dealDx[cno];
		
		if (!isGray(x, y))
			return dy;
		
		for (int i=1; i<dy; i++) {
			if (!isWhite(x, y+i)) {
				// n("del y: " + i);
				return i;
			}
		}
		return dy;
	}

	
	boolean isWhite(int x, int y) {
		Color co = new Color(bi.getRGB(x, y));
		//n(x+"," + y +": " + co.getRed() + " " + co.getGreen() + " " +co.getBlue() + " ");
		if (co.getRed() >240 && co.getRed() >240 && co.getRed() >240)
			return true;
		return false;
	}
	
	boolean isGray(int x, int y) {
		Color co = new Color(bi.getRGB(x, y));
		//n(x+"," + y +": " + co.getRed() + " " + co.getGreen() + " " +co.getBlue() + " ");
		if (co.getRed() < 170 && co.getRed() < 170 && co.getRed() < 170)
			return true;
		return false;
	}
	
	
	// just test
	public void check1() {
		int dy = 0;
		dy = getDealDeltaY(0); n(dy);
		dy = getDealDeltaY(1); n(dy);
	}

	
	public static void main(String[] args) {
		GameImage gimg = new GameImage(); 
		gimg.check1();
	}

	
}
