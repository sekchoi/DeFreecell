
package quixote.jfw.core;

/**
 * NanoSec 를 측정하기 위한 Timer class
 * 성능을 측정하기 위한 테스트용으로만 사용한다
 */
public class NanoTimer {
	/** 시작 시간 */
	long nano1;
	/** 종료 시간 */
	long nano2;
	
	/**
	 * Class가 시작되면 시작시간을 설정한다
	 */
	public NanoTimer (){
		start();
	}

	/**
	 *  시작시간을 설정한다
	 */
	public void start() {
		nano1 = System.nanoTime();
	}
	
	/**
	 * 종료시간을 설정한다
	 */
	public void stop() {
		nano2 = System.nanoTime();
	}

	/** 
	 * 시간차 mili sec.를 문자열로 얻는다
	 */
	public String getDiff() {
		long diffMili = getDiffMili();
		int mili = (int)(diffMili % 1000);
		int sec = (int)(diffMili / 1000);
		//int min = sec / 60;
		
		//return "" + min + ":"+ sec + "."+ mili;
		return ""
		  +((sec<=9)? "	" : "")	+ sec +	"."
		  +((mili<=99)? "0" : "")
		  +((mili<=9)? "0" : "")	+ mili ;
	}

	/** 
	 * 시간차를 msec 값으로 얻는다
	 */
	public long getDiffMili() {
		return (nano2 - nano1)/1000000;
	}

}
