package quixote.ai.defreecell.game;

import quixote.ai.defreecell.cell.Card;




// queue move action 
public class Act {

	int fr;		// from cell
	int to;		// to cell
	int cnt;	// move count
	int fri;	// fr index
	int toi;	// to index
	Card c;
	
	public Act(int fr, int to, int cnt,  int fri, int toi, Card c) {
		this.fr = fr;
		this.to = to;
		this.cnt = cnt;
		this.fri = fri;
		this.toi = toi;
		this.c = c;
	}

	public Act(String act) {
		try {
			fr = getCellInx(act.substring(0,2));
			to = getCellInx(act.substring(2,4));
			cnt = frHex(act.charAt(4));
			fri = frHex(act.charAt(5));
			toi = frHex(act.charAt(6));
		} catch (Exception e) {
		}
	}

	public String toText() {
		return getCellStr(fr) + getCellStr(to) + toHex(cnt) + toHex(fri) + toHex(toi) + "_" + c.dispText();
	}
	/**
	 * make action string
	 * @param no cell index 0~15
	 * @return "f1"
	 */
	private String getCellStr(int no) {
		if		(no <8) return "d" + no;
		else if (no <12)return "f" + (no-8);
		else 			return "h" + (no-12);
	}

	/**
	 * get stack 
	 * @param cell: f0 (stack+no)  no:0~7
	 * @return
	 */
	private int getCellInx(String st) throws Exception {
		char s = st.charAt(0);
		int  no = Integer.parseInt(st.substring(1));

		switch (s) {
		case 'd': break;	
		case 'f': no += 8;	break;
		case 'h': no += 12;	break;
		default:
			throw new Exception("No Stack: " + st);
		}

		if (no>=0 && no<16)
			return no;
		
		throw new Exception("Stack inx: " + no);
	}
	

	private char toHex(int no) {
		char ch = (char)no;
		if (no<10)
			ch += '0';
		else
			ch += 'A'-10;
		return ch;
	}

	private int frHex(char ch) {
		int no = ch<='9' ? (ch-'0') : (ch-'A'+10);
		return no;
	}


	
}

