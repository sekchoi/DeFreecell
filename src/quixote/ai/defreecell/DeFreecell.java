/**
 * btp GUI main 
 */

package quixote.ai.defreecell;

import quixote.ai.defreecell.gui.MainWindow;



public class DeFreecell extends Base {

	public static void main(String args[]) {
		DeFreecell tt = new DeFreecell();
		
		tt.run(args);
		return;
	}


	private void run(String args[]) {
		
		logw("");
		logw(Constant.Version + " ... ");
		
		System.setProperty("sun.java2d.d3d", "false");
		
		try {
			MainWindow tun = new MainWindow();
			tun.run();
		}
		catch (Exception e) {
			e.printStackTrace();
			logw(e);
		}
	}

}

