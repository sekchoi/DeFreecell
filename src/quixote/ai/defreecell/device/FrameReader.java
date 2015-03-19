package quixote.ai.defreecell.device;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;



/**
 *  GUI: Frame Buffer Reader
 */

public class FrameReader extends Device {

	char imgType = 'J';	//  'J':jpg 'P':png 'Z':zip

	int capScreen = 0;

	BufferedImage nowImg;


	Dimension scrDim = null;		// screen size


	public void setImgType(char imgType) {
		this.imgType = imgType;	
	}


	public void setDeviceNo(int val) {
		capScreen = val-1;
		if (capScreen<0 || capScreen >= getScrCount())
			capScreen = 0;
	}


	public Dimension getDim() {
		GraphicsDevice[] gs = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		if (capScreen>= gs.length)
			capScreen= 0;
		
		GraphicsDevice gd = gs[capScreen];
		GraphicsConfiguration[] gc = gd.getConfigurations();
		rtScreen = gc[0].getBounds();
		
		return new Dimension(rtScreen.width, rtScreen.height);		
	}

	
	public BufferedImage capture(Rectangle rt) {
		BufferedImage img = null;
		try {
			img = new Robot().createScreenCapture(rt);
			return img;
			//return ImgHelper.smallImage(img);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


}
