package quixote.ai.defreecell.game;

import quixote.ai.defreecell.cell.Card;
import quixote.ai.defreecell.cell.Cell;
import quixote.ai.defreecell.cell.CellDeal;
import quixote.ai.defreecell.cell.CellFree;
import quixote.ai.defreecell.cell.CellHome;
import quixote.ai.defreecell.gui.WinLogging2;



/**
 * Freecell game 
 */
/*
	ㅁㅁㅁㅁ  ㅁㅁㅁㅁ : free[4] home[4] 
	 ㅁㅁㅁㅁㅁㅁㅁㅁ  :    deal[8]
	ijkl  mnop 
	 abcdefgh
 */


public class Scene {

	Cell []cell = new Cell[16];
	String actSeq = "";

	int ruleNo = 0;
	static final int [][]Rule = {
		// nHome, nFree, wellOrder, order, LowNum
		{2,5,  40,2,20},
		{20,10,  3,5,12},
		{20,10,  5,3,7},
		{20,10,  7,3,5},
		
		{10,10,  1,1,20},
		{10,5,  1,6,20},
	};
	
	static final int MaxRule = Rule.length;

	
	protected void n(Object e) { WinLogging2.logn(e.toString()); }
	
	public Scene(int ruleNo) {
		this.ruleNo = ruleNo;
		init();
	}

	private void init() {
		int i=0;
		for (; i< 8; i++) cell[i] = new CellDeal(i,i);
		for (; i<12; i++) cell[i] = new CellFree(i,i-8);
		for (; i<16; i++) cell[i] = new CellHome(i,i-12);
	}

	/**
	 * Move multi times
	 * @param acts : "d5f01,d5f11" 
	 * @return
	 */
	public boolean move(String acts) {
		String ar[] = acts.split(",");
		for (int i=0; i<ar.length; i++) {
			moveOne(ar[i]);	
		}
		// 액션그룹을 ; 으로 구분하기 
		actSeq = actSeq.substring(0, actSeq.length()-1) + ";";
		return true;
	}

	/**
	 * Move one time
	 * @param acts : "d5f01" fr to cnt 
	 * @return
	 */
	private boolean moveOne(String act) {
		try {
			actSeq += act +",";

			Act ac = new Act(act);
			Cell fr = cell[ac.fr];
			Cell to = cell[ac.to];
			int cnt = ac.cnt;
			
			// move multi card			
			if (to.pushable(fr.popable(cnt))) {
				to.push(fr, cnt);
				fr.pop(cnt);
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	
	protected Scene clone() {
		Scene s2 = new Scene(ruleNo);
		for (int i=0; i<16; i++) {
			for (int j=0; j<cell[i].cnt; j++) {
				s2.cell[i].add(cell[i].cards[j].clone());
			}
		}
		s2.actSeq = actSeq;
		return s2;
	}


	String dispText() {
		StringBuilder sb = new StringBuilder();
		// free
		int i=8; 
		sb.append("");
		for (; i<12; i++)
			sb.append(cell[i].dispText()).append(' ');

		// home
		sb.append("   ");
		for (; i<16; i++) 
			sb.append(cell[i].dispText()).append(' ');
		sb.append("\n");

		// deal
		for (int j=0; j<17; j++) {
			StringBuilder sb2 = new StringBuilder();
			sb2.append(" ");
			for (i=0; i<8; i++) {
				sb2.append(cell[i].dispText(j)).append(" ");
			}
			String s = sb2.toString();
			if (s.replace(" ", "").length() <=0)
				break;
			sb.append(sb2).append("\n");
		}
		for(int p=sb.length()-1; p>=0; p--) {
			if (sb.charAt(p) == '\n')
				sb.setLength(p);
			else
				break;
		}
		return sb.toString();
	}

	
	String __dispText_b1() {
		StringBuilder sb = new StringBuilder();
		// free
		int i=8; 
		sb.append("Free ");
		for (; i<12; i++)
			sb.append(cell[i].dispText()).append(' ');

		// home
		sb.append("  Home ");
		for (; i<16; i++) 
			sb.append(cell[i].dispText()).append(' ');
		sb.append("\n");

		// deal
		for (i=0; i<8; i++) {
			sb.append("Deal"+i+" ").append(cell[i].dispText()).append("\n");
		}

		return sb.toString();
	}

	
	String saveText() {
		StringBuilder sb = new StringBuilder();
		int i=0;
		// deal
		for (; i<8; i++) {
			sb.append(cell[i].saveText()).append(",");
		}
		// free
		for (; i<12; i++) 
			sb.append(cell[i].saveText()).append(",");
		
		// home
		for (; i<16; i++) 
			sb.append(cell[i].saveText()).append(",");
		
		sb.append('\n');

		return sb.toString();
	}

	
	/**
	 * Get complete score, max: 64 = (h)13*4 + (d)8 + (f)4
	 * @return
	 */
	public int getPoint(Scene st0) {
		int sum = 0;
		// homeCnt
		for (int i=12; i<16; i++) {
			sum += cell[i].cnt * Rule[ruleNo][0];
		}
		
		// freeCnt
		for (int i=0; i<12; i++) {
			if (cell[i].cnt == 0) sum += Rule[ruleNo][1];
		}
	
		sum += getPoint_WellOrdered(st0) *Rule[ruleNo][2];
		sum += getPoint_Ordered(st0) *Rule[ruleNo][3];
		sum += getPoint_LowNum(st0)  *Rule[ruleNo][4];
		return sum;
	}

	// 순서가 13부터 잘 맞을 수록 높은 점수
	public int getPoint_WellOrdered(Scene st0) {
		int sum = 0;
		for (int i=0; i<8; i++) {
			int p0 = st0 .cell[i].wellOrdered();
			int p1 = this.cell[i].wellOrdered();
			int diff = (p1-p0);
			if (sum <diff)
				sum = diff;
		}
		return sum;
	}
	
	// 순서가 잘 맞을 수록 높은 점수
	public int getPoint_Ordered(Scene st0) {
		int sum = 0;
		for (int i=0; i<8; i++) {
			int p0 = st0 .cell[i].ordered();
			int p1 = this.cell[i].ordered();
			int diff = (p1-p0);
			if (sum <diff)
				sum = diff;
		}
		return sum;
	}
	
	// 낮은 수가 짱밖히지 않을 수록 높은 점수
	public int getPoint_LowNum(Scene st0) {
		int sum = 0;
		// home 으로 가야 할 제일 적은 카드 구하기
		int minNo = 13;
		for (int i=12; i<16; i++) {
			if (cell[i].cnt <= 0)
				minNo = 0;
			else if (minNo > cell[i].cards[cell[i].cnt-1].no)
				minNo = cell[i].cards[cell[i].cnt-1].no;
		}
		minNo ++;
		
		// 어느셀인가 ?
		int stNo = -1;
		for (int i=0; i<8; i++) {
			for (int j=0; j<cell[i].cnt; j++) {
				if (cell[i].cards[j].no == minNo) {
					stNo = i;
					break;
				}
			}
			if (stNo>=0) 
				break;
		}
		if (stNo>=0 && st0.cell[stNo].cnt > this.cell[stNo].cnt)
			sum ++;
		return sum;
	}

	public int getScore_b1() {
		int sum = 0;
		for (int i=12; i<16; i++) {
			sum += cell[i].cnt;
		}

		for (int i=0; i<12; i++) {
			if (cell[i].cnt == 0) sum ++;
		}
		return sum;
	}
	
	public boolean isBetter(Scene s2) {
		return actSeq.length() < s2.actSeq.length();
	}


	public boolean isValid() {
		boolean rc = isValidSub();
		n(rc ? "Card is ok." : "Card error!");
		return rc;
	}
	
	private boolean isValidSub() {
		byte []dig = new byte[13];
		byte []sha = new byte[4];

		for (int i=0; i<16; i++) {
			Cell st = cell[i];
			for (int j=0; j<st.cnt; j++) {
				dig[st.cards[j].no -1] ++;
				sha[(st.cards[j].sh>>4) -1] ++;
			}
		}

		for (int i=0; i<dig.length; i++) {
			if (dig[i] != 4) return false;
		}

		for (int i=0; i<sha.length; i++) {
			if (sha[i] != 13) return false;
		}

		return true;
	}
	
	
	public boolean solved() {
		for (int i=12; i<16; i++) {
			if (cell[i].cnt < 13) return false;
		}
		return true;
	}

	/** get free count */
	public int freeCell() {
		int sum = 0;
		for (int i=0; i<12; i++) {
			if (cell[i].cnt <= 0) sum ++;
		}
		return sum;
	}

	/** get made percent */
	public int perMade() {
		int sum = 0;
		for (int i=12; i<16; i++) {
			sum += cell[i].cnt;
		}
		
		return sum;
	}

	
	/**
	 * make action string
	 * @param fr from index 0~15
	 * @param to to index 0~15
	 * @param cnt move count 1~13
	 * @return "f1h11"
	 */
	public String makeAct(int fr, int to, int cnt, Card c) {
		Act ac = new Act(fr, to, cnt, cell[fr].cnt-cnt, cell[to].cnt, c);
		return ac.toText();
	}


	void loadGame(String g) {
		init();
		g = g.replace(" ","").replace("\n","");
		String []ar = g.split(",");
	
		for (int i=0; i<ar.length; i++) {
			if (ar[i].length() > 0)
				cell[i].load(ar[i]);
		}
		isValid();
	}

	/*boolean equals(Scene st2) {
		String s1 = saveText();
		String s2 = st2.saveText();
		return s1.equals(s2);
	}*/
	
	
	
	

	private final static String game11 = "77767574737271,61605958575655,45444342414039,29282726252423,706968676665,545352515049,383736353433,222120191817,,,,,,,,,";	// -3
	private final static String game12 = "77614529766044,28755943277458,42267357412572,56402471553923,705438226953,372168523620,675135196650,341865493317,,,,,,,,,"; // -4

	// Readable format for input
	static final String game21 = 
		"dJ dK s2 c4 s3 d6 s6,"+
		"d2 cK sK c5 d0 s8 c9,"+
		"h9 s9 d9 s0 s4 d8 h2,"+
		"cJ s5 dQ hQ h0 sQ h6,"+
		"d5 dA sJ h4 h8 c6,"+
		"h7 cQ sA cA c2 d3,"+
		"c7 hK hA d4 hJ c8,"+
		"h5 h3 c3 s7 d7 c0," ;

	// Compact format for memory effective
	final static String game22 = "43456620673870,34297721427225,57734174684050,27694460587654,373375525622,552865171835,236149365924,535119713926,,,,,,,,,";
	final static String game23 = "22397717184349,37701944672859,51236840745038,41762542614555,545257562971,276635243420,653336692172,736053755826,,,,,,,,,";
	
	// ok cards
	final static String game31 = /*16920*/ // "19571868376671,59217545517065,42436961745035,73445653262958,527227283840,246022366776,553423773339,251754494120,,,,,,,,,";
			"57175223594170,40443421254250,26277533205551,67375371667636,224939734361,566919723545,186528292458,686077745438,,,,,,,,,";
			//"66584275565528,45516772172961,23254057433877,21744927717335,195065765322,393337364420,265960541868,705224413469,,,,,,,,,";
	final static String game32 = /*26105*/ //"59532842377370,26246925205275,19182365173367,27382234406072,617621506674,495154447135,394177584529,685657365543,,,,,,,,,";
			"38286159412968,74505317397360,22755251217145,35277223544440,761934703769,652543552467,774942182057,335856663626,,,,,,,,,";
	final static String game33 = /*17728*/ // "60717440193854,51436828655933,17187058672472,35532136692377,567361752055,374257222925,764152344950,442639456627,,,,,,,,,";
	/*14081*/ "44402729227541,39652533612119,17515949386028,56547334205257,357168667467,235869245536,504353777037,184245267276,,,,,,,,,";

	//  just testing
	void loadGame(int no) {
		String g;
		switch (no) {
		default:
		case 11: g = game11; break;
		case 12: g = game12; break;
		
		case 21: g = game21; break;
		case 22: g = game22; break;
		case 23: g = game23; break;
		
		// ok card
		case 31: g = game31; break;
		case 32: g = game32; break;
		case 33: g = game22; break;
		}
		loadGame(g);
	}


	public void _check1() {
		loadGame(3);

		//move("d5f01,d5f11");
		//move("d5h21,d5h31");
		n(dispText());
		//n(saveText());
		
		ActionList al = new ActionList(this);
		al.findActions();
		n(al.toString());
	}

	public void _check2() {
		loadGame(1);
		n(dispText());
		n(saveText());
	}
	
/*	public static void main(String[] args) {
		new Scene()._check1();
		//new Scene()._check2();
	}
*/
}

/* OKs
4759
27725
29248
*/
