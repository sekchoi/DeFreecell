package quixote.ai.defreecell.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import quixote.ai.defreecell.cell.Card;
import quixote.ai.defreecell.gui.WinLogging2;


// queue move action 
public class ActionList {


	List<Action> list = new ArrayList<Action>();
	Scene sc0;

	protected void n(Object e) { WinLogging2.logn(e.toString()); }
	
	public ActionList(Scene st) {
		this.sc0 = st;
	}

	Scene getScene() {
		return sc0;
	}


	private void push(String act) {
		Scene s2 = sc0.clone();
		s2.move(act);
		list.add(new Action(act, s2.getPoint(sc0)));
	}

	Action pop() {
		int cnt = list.size()-1;
		if (cnt<0)
			return null;
		Action act = list.get(cnt);
		list.remove(cnt);
		return act;
	}

	boolean available() {
		return list.size()>0;
	}
	

	class CompAction implements Comparator<Action> {
		public int compare(Action a, Action b) {
			return (a.point - b.point);
		}
	}

	/**
	 * find all move actions and store to the list 
	 */
	public int findActions() {
		list.clear();
		
		findAct_2H(sc0);

		if (list.size() <=0) {
			findActD2D_n(sc0);
		
		//if (list.size() <=0)
			findActF2D(sc0);
		
		//if (list.size() <=0)
			findActD2F(sc0);
		}

		if (list.size() <=0)
			return 0;
		
		Collections.sort(list, new CompAction());

		// remove dup
		for (int i=0; i<list.size(); ) {
			if (i >= list.size()-1) break;
			if (list.get(i).acts.equals(list.get(i+1).acts))
				list.remove(i);
			else i++;
		}

		return list.size();
	}


	/**
	 * Home 으로 가는 action
	 */
	private void findAct_2H(Scene st) {
		// f->h
		//Scene st2  = st.clone();
		String acts = findAct_2H_sub1(st);
		if (acts.length()>0)
			push(acts);
	}

	private String findAct_2H_sub1(Scene st) {
		// f->h
		Scene st2  = st.clone();
		StringBuilder sb = new StringBuilder();
		for (;;) {
			String act = findAct_2H_sub2(st2);
			if (act == null) break;
			st2.move(act);
			if (sb.length()>0) sb.append(",");
			sb.append(act);
		}
		return sb.toString();
	}
	
	private String findAct_2H_sub2(Scene st) {
		// f->h
		for (int i=0; i<12; i++) {
			Card c = st.cell[i].popable();
			if (c==null) continue;
			for (int j=12; j<16; j++) {
				if (st.cell[j].pushable(c)) {
					return st.makeAct(i,j,1, c);					
				}
			}
		}
		return null;
	}
	
	/**
	 * Free -> deal 
	 */
	private void findActF2D(Scene st) {
		// f->d
		for (int i=8; i<12; i++) {
			Card c = st.cell[i].popable();
			if (c==null) continue;
			for (int j=0; j<8; j++) {
				if (st.cell[j].pushable(c)) {
					push(st.makeAct(i,j,1, c));
					if (st.cell[j].cnt == 0) break;
				}
			}
		}
	}

	static int ruleNo = 0;

	public static void incRuleNo() {
		if (++ruleNo >= 8)
			ruleNo = 0;
	}
	
	/**
	 * Deal 간의 이동, multi
	 */
	void findActD2D_n(Scene st) {
		// 복수개 이동 확인 
		int free = st.freeCell();

		// d->d
		for (int I=0; I<8; I++) {
			int i = (I + ruleNo) % 8;
			if (st.cell[i].cnt == 0)
				continue;
			if (st.cell[i].wellOrdered()>0)
				continue;
			int n;
			Card c = null;
			// n장을 옮길 수 있는지 확인
			for (n=free+1; n>=1; n--) {
				c = st.cell[i].popable(n);
				if (c!=null) break;
			}
			if (c==null) continue;

			for (int j=0; j<8; j++) {
				if (i==j) continue;
				if (st.cell[i].cnt == n && st.cell[j].cnt == 0)
					continue;

				if (st.cell[j].pushable(c)) {
					int n2 = n;
					if (n==free+1 && st.cell[j].cnt == 0)
						n2 = n-1;
					
					// check auto move
					String act = st.makeAct(i,j,n2, st.cell[i].popable(n2));
					Scene st2 = st.clone();
					st2.move(act);
					
					String ac2 = findAct_2H_sub1(st2);
					if (ac2.length() > 0) {
						/*n("D2D");
						n(st.dispText());
						n("act: " + act);
						n(st2.dispText());
						n("ac2: " + ac2);*/

						act += "," + ac2;
						//n("act: " + act);
					}
					push(act);
				}
			}
		}
	}


	/**
	 * Free -> deal 
	 */
	void findActD2F(Scene st) {
		// f->d
		// 빈 deal이 있고, cnt가 1이면 올지 말 것
		int emptyDeal = 0;
		for (int i=0; i<8; i++) {
			if (st.cell[i].cnt==0)
				emptyDeal ++;
		}
		for (int i=0; i<8; i++) {
			if (st.cell[i].cnt <=0)
				continue;
			if (st.cell[i].cnt ==1 && emptyDeal>0)
				continue;
			
			Card c = st.cell[i].popable();
			if (c==null) continue;

			if (st.cell[i].wellOrdered()>0)
				continue;
			
			for (int j=8; j<12; j++) {
				if (st.cell[j].pushable(c)) {

					String act = st.makeAct(i,j,1, c);
					Scene st2 = st.clone();
					st2.move(act);
					String ac2 = findAct_2H_sub1(st2);
					
					// check auto move
					if (ac2.length() > 0) {
						/*n("D2F");
						n(st.dispText());
						n("act: " + act);
						n(st2.dispText());
						n("ac2: " + ac2);*/

						act += "," + ac2;
						//n("act: " + act);
					}
					push(act);
					if (st.cell[j].cnt == 0) break;
				}
			}
		}
	}


	@Override
	public String toString() {
		return "action count: " + list.size();
	}
	

	class Action {
		String acts;	// {move}
		int    point;	// 완성도
		
		public Action(String acts, int point) {
			this.acts = acts;
			this.point = point;
		}

		@Override
		public String toString() {
			return acts + " PT:" + point;
		}
	}

	public int size() {
		return list.size();
	};

}

