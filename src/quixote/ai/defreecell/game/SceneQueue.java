package quixote.ai.defreecell.game;

import java.util.ArrayList;
import java.util.List;


/**
 * Scene 를 Recursive loop 돌리기 위한 구조
 *
 */

public class SceneQueue {

	class SQueue {
		Scene sc;
		String stxt;
		int max;
		int cur;
		
		public SQueue(Scene sc, int max, int cur) {
			this.sc = sc.clone();
			this.max = max;
			this.cur = cur;
			stxt = sc.saveText();
		}
	}

	
	List<SQueue> list = new ArrayList<SQueue>();

	//static final int MaxSize = 120 +0; // 105 150;
	static final int MaxSize = 100 +20; // 105 150;

	boolean isFull() {
		return size() > MaxSize;
	}
	
	// Check dup
	boolean has(Scene sc) {
		String s2 = sc.saveText();
		for (int i=0; i<list.size(); i++) {
			if (list.get(i).stxt.equals(s2))
				return true;
		}
		return false;
	}

	
	/**
	 * push stage
	 * @param sc : current stage
	 * @param max: 
	 * @param inx: 
	 * @return
	 */
	boolean push(Scene sc, int max, int cur) {
		if (isFull()) return false;
		if (has(sc))  return false;
		list.add(new SQueue(sc, max, cur));
		//list2.add(sc.saveText());
		return true;
	}

	void remove() {
		int cnt = list.size()-1;
		if (cnt<0)
			return;
		list.remove(cnt);
	}

	public int size() {
		return list.size();
	}

	public Scene last() {
		int cnt = list.size()-1;
		if (cnt<0)
			return null;
		return list.get(cnt).sc;
	}


	/**
	 * Display percent
	 * @return
	 */
	public String toText(boolean full) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%3d ", list.size()-1));
		int i=1;
		for (; i<list.size(); i++) {
			SQueue sq = list.get(i);
			sb.append((int)(10.0 * sq.cur / sq.max));
			if (!full && i>60)
				break;
		}

		if (!full && i>60) {
			SQueue sq = list.get(list.size()-1);
			sb.append("..." + (int)(10.0 * sq.cur / sq.max));
		}			
		return sb.toString();
	}

	public String perMade(boolean full) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%3d ", list.size()-1));
		int i=1;
		try {
			for (; i<list.size(); i++) {
				SQueue sq = list.get(i);
				if (sq==null)
					break;
				sb.append(sq.sc.perMade() % 10);
				if (!full && i>60)
					break;
			}
	
			if (!full && i>60) {
				SQueue sq = list.get(list.size()-1);
				sb.append("..." + sq.sc.perMade() % 10);
			}			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return sb.toString();
	}

}
