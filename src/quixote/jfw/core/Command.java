
package quixote.jfw.core;



class ProcThread extends Thread {
	String cmd = "";


	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public void run() {
		try {
			Process	prc_pp = Runtime.getRuntime().exec(cmd);
			prc_pp.waitFor();
			//new Log().log("exec(" +cmd + ") ok " + prc_pp);
		}
		catch (Exception ex) {
			System.out.println("exec(" +cmd + ") err=" + ex.toString());
		}
	}
}


public class Command {

	int	logLevel = 1;	// 0:none, 1:brief, 2:detail
	//Log log = new Log();



	//	reindex not indexed item master
	public boolean runPrg(String prg) {
		ProcThread th = new ProcThread();
		th.setCmd(prg);
		th.start();
		return true;
	}

}

