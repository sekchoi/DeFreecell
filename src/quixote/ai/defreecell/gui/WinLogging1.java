package quixote.ai.defreecell.gui;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;


/**
 * GUI: Logging window 
 */

public class WinLogging1 extends JScrollPane {

	private static final long serialVersionUID = 1L;

	JTextArea logWin;
	
	public WinLogging1() {
		init();
	}

	void init() {
		if (MainWindow.defaultFont != null)
			setFont(MainWindow.defaultFont);

		PrintStream prn = new PrintStream(new MyOutputStream());
		System.setOut(prn);

		if (System.getProperty("FromEclipse") == null)
			System.setErr(prn);

		logWin = new JTextArea();
		logWin.setTabSize(4);
		if (MainWindow.defaultFont != null)
			logWin.setFont(MainWindow.defaultFont);
		
		super.setViewportView(logWin);
		//addn("Ready");
	}

	public void add_(String txt) {
		a(txt);
	}
	
	public synchronized void a(Object txt) {
		if (cflag) {
			logWin.setText("");
			cflag = false;
		}
		logWin.append(txt.toString());
		logWin.setCaretPosition( logWin.getDocument().getLength() );		
	}


	public void n(Object txt) {
		a(txt);
		a("\n");
	}
	public void addn_(String txt) {
		n(txt);
	}

	boolean cflag = false;
	public void clear() {
		cflag = true;
		// logWin.setText("");
	}
	

	public static void logn(Object obj) {
		//System.out.println(Base.getTime() + obj.toString()); 
		System.out.println(obj.toString());
	}

	public static void loga(Object obj) {
		//System.out.println(Base.getTime() + obj.toString()); 
		System.out.print(obj.toString());
	}


	class MyOutputStream extends OutputStream {

		ByteBuffer bbuf = ByteBuffer.allocate(8000);

		@Override
		public void write(byte b[], int off, int len) throws IOException {
			a(new String(b, off, len));
		}
	
		@Override
		public void write(int b) {
			try {
				bbuf.put((byte)b);
				
				if (b==13) {
					int ll = bbuf.position();
					byte [] bb = new byte[ll];
					bbuf.rewind();
					bbuf.get(bb, 0, bb.length);
					a(new String(bb));
					//bbuf.rewind();
					bbuf.clear();
				} else {
					bbuf.put((byte)b);
				}
			} catch (Exception e) { }
		}
	}


}
