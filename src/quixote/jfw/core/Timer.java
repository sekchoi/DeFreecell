

package quixote.jfw.core;



/**
 * MiliSec 를 측정하기 위한 Timer class
 *
 */
public class Timer {

	/** 시작 시간 */
	long mili1;
	
	/** 종료 시간 */
	long mili2;
	
	public Timer (){
		mili1 = System.currentTimeMillis();
	}

	/**
	 * 종료시간을 설정한다
	 */
	public void stop() {
		mili2 = System.currentTimeMillis();
	}
	
	/** 
	 * 시간차 mili sec.를 문자열로 얻는다
	 */
	public String getDiff() {
		
		long diff = mili2 - mili1;
		int mili = (int)(diff % 1000);
		int sec = (int)(diff / 1000);
		int min = sec / 60;
		sec = sec % 60;
		
		return "" + min + ":"+ sec + "."+ mili;
	}

}
