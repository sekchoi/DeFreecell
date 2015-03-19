package quixote.ai.defreecell.cell;


/**
 * card가 위치할 자리
 */
/*
ㅁㅁㅁㅁ  ㅁㅁㅁㅁ : free[4] home[4] 
 ㅁㅁㅁㅁㅁㅁㅁㅁ  :    deal[8] 
*/

public abstract class Cell {
	char type = 0;	// d:deal  f=free  h:home  
	int  tseq = 0;	// # seq in same type 
	//char name; 
	
	public Card [] cards;
	public int cnt = 0;


	public Cell(int aseq, int tseq, char type, int size) {
		//this.name = (char)('a' + aseq);
		this.tseq = tseq;
		this.type = type;
		cards = new Card[size];
	}


	abstract public boolean pushable(Card card);
	abstract public Card popable();
	abstract public void load(String ar);

	/**
	 * Card ordered is  13 12 11 10 ...
	 * @return true or false
	 */
	public int wellOrdered() {
		return (cnt==1) ? 1 : 0;
	}

	/**
	 * Card ordered is  .. 11 10 9 8
	 * @return true or false
	 */
	public int ordered() {
		return (cnt==1) ? 1 : 0;
	}

	
	/**
	 * popable n card
	 * @param n
	 * @return first card
	 */
	public Card popable(int n) {
		if (n==1) return popable();
		return null;
	}

	
	/**
	 * push by force for init
	 * @param card
	 */
	public void add(Card card) {
		if (cnt < cards.length)
			cards[cnt ++] = card;
	}

	public boolean push(Cell fr, int cn2) {
		if (pushable(fr.cards[fr.cnt -cn2])) {
			for (int i=fr.cnt -cn2; i<fr.cnt; i++)
				cards[cnt ++] = fr.cards[i];
			return true;
		}
		return false;
	}


	public Card pop(int cn2) {
		if (popable(cn2) == null)
			return null;
		Card c = cards[cnt-cn2];
		cnt -= cn2;
		return c;
	}


	// get text to display, alpha+digit[]
	public String dispText() {
		if (cnt <= 0)
			return "__ ";
		return cards[cnt-1].dispText();
	}

	// get text to display, alpha+digit[]
	public String dispText(int i) {
		if (i >= cnt)
			return "   ";
		return cards[i].dispText();
	}
	
	// get text to save, number[]
	public String saveText() {
		if (cnt <= 0)
			return "";
		return "" + cards[cnt-1].getByte();
	}


	public String toString() {
		return type + "" + tseq + " cnt:" + cnt;
	}
	
}
