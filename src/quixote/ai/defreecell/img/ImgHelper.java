package quixote.ai.defreecell.img;


/* JpegResizerDemo.java */

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;


public class ImgHelper {

	//static float CompressRate = 0.15f;	// fast
	static float CompressRate = 0.70f;	// normal
	//static float CompressRate = 0.80f;	// good quality
	
	
	static public void saveAs(BufferedImage img, String name) {
		try {
			String type = name.substring(name.length()-3);
			ImageIO.write(img, type, new File(name));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	static public BufferedImage subImageBW(BufferedImage img, int x,int y, int w, int h) {
		try {
			BufferedImage nImg = new BufferedImage(w, h, 
					//BufferedImage.TYPE_BYTE_GRAY
					BufferedImage.TYPE_BYTE_BINARY
					);
	
			Graphics2D g2 = (Graphics2D)nImg.getGraphics();
			g2.drawImage(img, -x,-y, null);
			g2.dispose();
	
			return nImg;
		} catch (Exception e) {
			return null;
		}
	}


}