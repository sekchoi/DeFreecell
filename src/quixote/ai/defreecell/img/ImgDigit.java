/**
 * properties file I/O
 */

package quixote.ai.defreecell.img;



public class ImgDigit extends ImgBase {

	@Override
	void loadImages() {
		String []ar = {"A","2","3","4","5",  "6","7","8","9","0",  "J","Q","K",};
		loadImages(ar);
	}


}
