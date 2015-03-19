package quixote.ai.defreecell.game;

import java.awt.image.BufferedImage;

import quixote.ai.defreecell.gui.MainWindow;
import quixote.ai.defreecell.img.ImgDigit;
import quixote.ai.defreecell.img.ImgShape;


/**
 * Process : Find card position 
 * {801, 497}
 */

public class FindCard extends FindBase {

	Scene st = new Scene(0);
	
	ImgDigit imgDig = new ImgDigit();
	ImgShape imgSha = new ImgShape();
	
	GameImage gi = new GameImage();
	
	public Scene findCard() {
		// 1. get Game Window
		gi = new GameImage();
		if (gi.bi==null) return null;
		
		// 2. get card positions
		//st.loadAlphaGame1();
		String game = recognizeCard();
		n(game);
		st.loadGame(game);
		//st.isValid();

		pLog.clear();
		pLog.n(st.dispText());
		n(st.saveText());
		return st;
	}

	
	// 카드 인식
	String recognizeCard() {
		StringBuilder sb = new StringBuilder();
		BufferedImage bb;
		
		// find deal cards
		for(int x=0; x<8; x++) {
			int maxy = 32; // x<4 ? 7 : 6;
			
			for(int y=0; y<maxy; y++) {
				bb = gi.getDigImgDeal(x,y);
				//ImgHelper.saveAs(bb, x+"_"+y+".png");
				char dig = imgDig.findImg(bb);
				if (dig == 0) // no card is in
					break;
				
				bb = gi.getShaImgDeal(x,y);
				//ImgHelper.saveAs(bb, x+"_"+y+".png");
				char sha = imgSha.findImg(bb);
				if (sha == 0) // no card is in
					break;
				
				sb.append(" " + sha + "" + dig);
			}
			sb.append(",\n");
			//n("");
		}

		// find free, home cards
		for(int x=0; x<8; x++) {
			for(int y=0; y<1; y++) {
				bb = gi.getDigImgFree(x);
				//ImgHelper.saveAs(bb, x+"_d"+".png");
				char dig = imgDig.findImg(bb);
				if (dig == 0) // no card is in
					break;
				
				bb = gi.getShaImgFree(x);
				//ImgHelper.saveAs(bb, x+"_s"+".png");
				char sha = imgSha.findImg(bb);
				if (sha == 0) // no card is in
					break;
				sb.append("" + sha + "" + dig);
			}
			sb.append(",");
			if (x==3) sb.append("  ");
			//n("");
		}
		sb.append("\n");

		return sb.toString();
	}


	public FindCard(MainWindow parent) {
		super(parent);
	}

}
