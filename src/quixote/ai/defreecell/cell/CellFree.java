package quixote.ai.defreecell.cell;


/**
 * card가 위치할 자리
 * up left 4 position 
 */


public class CellFree extends Cell {

	/**
	 * @param 
	 */
	public CellFree(int aseq, int tseq) {
		super(aseq, tseq, 'f', 1);
	}

	@Override
	public boolean pushable(Card card) {
		if (card == null)
			return false;
		if (cnt == 0)
			return true;
		return false;
	}

	@Override
	public Card popable() {
		if (cnt >0)
			return cards[cnt-1];
		return null;
	}

	@Override
	public void load(String ar) {
		for (int i=0; i<ar.length(); i+=2) {
			add(new Card(ar.substring(i,i+2)));
		}
	}


}

