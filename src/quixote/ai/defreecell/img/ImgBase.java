/**
 * properties file I/O
 */

package quixote.ai.defreecell.img;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;


class ImgMap {
	char name;
	BufferedImage bi;
	
	public ImgMap(char name, BufferedImage bi) {
		this.name = name;
		this.bi   = bi;
	}
}

abstract class ImgBase {

	static String icoPath = "quixote/ai/defreecell/img/";
	static String imgPath = "/" + icoPath;


	ArrayList<ImgMap> items = null;

	
	abstract void loadImages();
	

	String []fileListDir(URI uri) {
		File []files = new File(uri).listFiles();
		//File []files = fil.listFiles();
		if (files == null) {
			return new String[0];
		}

		String names[] = new String[files.length];
		
		for(int i=0; i<files.length; i++) {
			names[i] = files[i].getName();
		}
		return names;
	}


	void loadImages(String []names) {
		//URL uri = new ImgDigit().getClass().getResource(imgPath + "2.png"); 
		//defaultIcon = new ImageIcon(uri, "ico");
		
		items = new ArrayList<ImgMap>();

		URL uri = new ImgDigit().getClass().getResource(imgPath);
		
		try {
			/*String []names;
			if (uri.toURI().toString().toLowerCase().indexOf(".jar") >= 0) {
				//log("scan jar");
				names = fileListJar(uri.toURI());
			} else {
				//log("scan dir");
				names = fileListDir(uri.toURI());
			}*/
			
			for (int i=0; i<names.length; i++) {
				String name = names[i];//.toLowerCase();
				//if (!name.endsWith(".gif") && !name.endsWith(".png") && !name.endsWith(".jpg"))
				//	continue;

				uri = new ImgDigit().getClass().getResource(imgPath + names[i] + ".png");
				BufferedImage img = ImageIO.read(uri);
				ImgMap imap = new ImgMap(name.charAt(0), img);
				//imap.name = name;
				//uri = new ImgDigit().getClass().getResource(imgPath + names[i]);
				//imap.icon = new ImageIcon(uri, "ico");

				items.add(imap);
			}
		} catch (Exception e) {
			log(e);
		}
	}


	public char findImg(BufferedImage bi) {
		if (items == null) {
			loadImages();
		}

		// compare image to image
		float per0 = 0;
		char name = ' ';
		for (int i=0; i<items.size(); i++) {
			ImgMap imap = items.get(i);
			float per = isLike(bi, imap.bi);
			if (per0 < per) {
				per0 = per;
				name = imap.name;
			}
		}
		if (per0 < .7)
			return 0;
		// log(per0);
		return name;
	}
	
	private float isLike(BufferedImage b1, BufferedImage b2) {
		int w = b1.getWidth();
		int h = b1.getHeight();
		if (w != b2.getWidth()) return 0;
		if (h != b2.getHeight()) return 0;
		
		int sum = 0;
		int v1,v2;
		for (int y=0; y<h; y++) {
			for (int x=0; x<w; x++) {
				v1 = b1.getRGB(x, y);
				v2 = b2.getRGB(x, y);
				if (v1==v2)
					sum ++;
			}
		}

		return 1.0f * sum/(w*h);
	}

	
	static protected void log (Object o) { if (o!= null) System.out.println(o.toString()); }
}
