package quixote.ai.defreecell.cell;


/**
 * card가 위치할 자리
 * down 8 position 
 */


public class CellDeal extends Cell {

	public CellDeal(int aseq, int tseq) {
		super(aseq, tseq, 'd', 13+13);
	}

	@Override
	public boolean pushable(Card card) {
		if (card == null)
			return false;
		if (cnt == 0)
			return true;
		if (card.isPrev(cards[cnt-1]))
			return true;
		return false;
	}

	/**
	 * Card ordered is  13 12 11 10 ...
	 * @return true or false
	 */
	public int wellOrdered() {
		if (cnt<=0) return 0;
		if (cards[0].no != 13) return 0;
		if (cnt==1) 
			return 1;

		for (int i=0; i<cnt-1; i++) {
			if (cards[i].no == cards[i+1].no +1 && cards[i].co != cards[i+1].co)
				continue;
			return 0;
		}
		return cnt;
	}

	/**
	 * Card ordered is  .. 11 10 9 8
	 * @return true or false
	 */
	public int ordered() {
		if (cnt<=0) return 0;
		//if (cnt==1) return 1;

		int sum = 1;
		for (int i=cnt-1; i>0; i--) {
			if (cards[i].no == cards[i-1].no -1 && cards[i].co != cards[i-1].co)
				sum++;
		}
		return sum;
	}

	@Override
	public Card popable() {
		if (cnt>0)
			return cards[cnt-1];
		return null;
	}

	/**
	 * popable n card
	 * @param n
	 * @return first card
	 */
	public Card popable(int n) {
		if (n<0) return null;
		if (n==1) return popable();
		if (n>cnt) return null;
		
		// 연속인가 ?
		for (int i=cnt-n; i<=cnt-1-1; i++) {
			if (cards[i].no == cards[i+1].no +1 && cards[i].co != cards[i+1].co)
				continue;
			return null;
		}
		return cards[cnt-n];
	}
	

	// get card list
	@Override
	public String dispText() {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<cnt; i++) {
			sb.append(cards[i].dispText()).append(' ');
		}
		return sb.toString();
	}

	@Override
	public String saveText() {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<cnt; i++) {
			sb.append(cards[i].getByte());
		}
		return sb.toString();
	}

	/**
	 * ar: 43456620673870  byte format
	 * ar: dJdKs2c4s3d6s6  digit format
	 */
	@Override
	public void load(String ar) {
		for (int i=0; i<ar.length(); i+=2) {
			add(new Card(ar.substring(i,i+2)));
		}
	}


}
