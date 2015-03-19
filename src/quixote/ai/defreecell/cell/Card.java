package quixote.ai.defreecell.cell;


//card가 위치할 자리
public class Card {
	public byte sh;	// 16*1~4 (for Clover Diamond Heart Spade)
	public byte no;	// 1~13 (j q k changed 11,12,13)
	char co ;		// color Red for D,H;  Block C,S

	/**
	 * @param sha_num : {c d h s} & {2~9,0, A,J,Q,K}
	 */
	public Card(String sha_num) {
		char sha = sha_num.charAt(0);
		if (sha>'A') {	// "dJ"  digit format
			if (sha_num.length()>=2)
				init(sha, sha_num.charAt(1));
		} else {		// "43"  byte format
			byte b = toByte(sha_num);		
			this.sh = (byte) (b & 0xf0);
			this.no = (byte) (b & 0xf);
			setColor();	
		}
	}
	
	public Card(byte sh, byte no) {
		this.sh = sh;
		this.no = no;
		setColor();
	}

	public Card clone() {
		return new Card(sh, no);
	}

	byte toByte(String s) {
		try {
			return (byte)Integer.parseInt(s);
		} catch (Exception e) {
		}
		return 0;
	}

	/**
	 * @param sha: c d h s
	 * @param dgt: 2~9,0, A,J,Q,K
	 */
	public Card(char sha, char num) {
		init(sha, num);
	}

	void setColor() {
		//if (sh==0) return;
		co = (sh==0x20 || sh==0x30) ? 'R' : 'B';
	}
	
	void setSha(char sha) {
		switch (sha) {
		case 'c': sh= 0x10; break;
		case 'd': sh= 0x20; break;
		case 'h': sh= 0x30; break;
		default:
		case 's': sh= 0x40; break;
		}
		setColor();
	}
	
	void init(char sha, char num) {
		setSha(sha);
		no = (byte)(num-'0');
		if 		(num =='A') no = 1;
		else if (num =='0') no = 10;
		else if (num =='J') no = 11;
		else if (num =='Q') no = 12;
		else if (num =='K') no = 13;
	}


	/**
	 * Get text to display only number, 0x11~0x4d
	 * @return
	 */
	public byte getByte() {
		return (byte)(sh | no);
	}

		
	/**
	 * Get text to display alpha+digit, ♣A
	 * @return
	 */
	public String dispText() {
		String num = "" + no;
		if 		(no== 1) num="A";
		else if (no==10) num="0";
		else if (no==11) num="J";
		else if (no==12) num="Q";
		else if (no==13) num="K";
		
		String sha = ".";
		if 		(sh==0x10) sha="♣";
		else if (sh==0x20) sha="◇";
		//else if (sh=='h') sha="▽"; 
		//else if (sh=='h') sha="♥"; 
		else if (sh==0x30) sha="♡";
		else if (sh==0x40) sha="♠";
 		return sha + num;
	}
	
	@Override
	public String toString() {
		return dispText() + ' ' + co;
	}
	/**
	 * check for stop position
	 * @param c0
	 * @return
	 */
	public boolean isNext(Card c0) {
		if (no==c0.no+1 && sh==c0.sh)
			return true;
		return false;
	}

	/**
	 * check for deal position
	 * @param card
	 * @return
	 */
	public boolean isPrev(Card c0) {
		if (no==c0.no-1 && co!=c0.co)
			return true;
		return false;
	}
	
}

