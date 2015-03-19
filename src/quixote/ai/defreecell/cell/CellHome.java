package quixote.ai.defreecell.cell;


/**
 * card가 위치할 자리
 * up right 4 position 
 */

public class CellHome extends Cell {

	public CellHome(int aseq, int tseq) {
		super(aseq, tseq, 'h', 13);
	}

	@Override
	public boolean pushable(Card card) {
		if (card == null)
			return false;
		if (cnt <= 0) {
			if (card.no == 1)
				return true;
			else
				return false;	
		} else if (card.isNext(cards[cnt-1])) {
			return true;
		}
		return false;
	}


	@Override
	public Card popable() {
		return null;
	}


	@Override
	public void load(String ar) {
		Card c = new Card(ar.substring(0,0+2));

		for (int i=1; i<=c.no; i++) {
			add(new Card("" + (c.sh | i)));
		}
	}


}
