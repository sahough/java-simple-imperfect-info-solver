package poker;

import java.util.Random;

public class Deck {

	boolean dealt[];
	Random r;

	public Deck() {
		dealt = new boolean[52];
		r = new Random();
	}

	public Deck(Deck d) {
		this();
		for (int i = 0; i < 52; i++)
			dealt[i] = d.dealt[i];

	}

	public int deal() {
		while (true) {
			int toDeal = r.nextInt(52);
			if (!dealt[toDeal]) {
				dealt[toDeal] = true;
				return toDeal;
			}
		}
	}

	public int[] deal(int x) {
		int ret[] = new int[x];
		for (int i = 0; i < x; i++)
			ret[i] = deal();
		return ret;
	}

	public void markAsDealt(Pocket p) {
		dealt[p.cards[0]] = true;
		dealt[p.cards[1]] = true;
	}

	public void markAsDealt(int board[]) {
		for (int i = 0; i < board.length; i++)
			dealt[board[i]] = true;
	}

}