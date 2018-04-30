package poker;

public class Pocket implements Comparable<Pocket> {

	public int cards[] = new int[2];

	public Pocket(int cardsIn[]) {
		for (int i = 0; i < 2; i++) {
			cards[i] = cardsIn[i];
		}

		// Auto-order by largest rank first
		if (cards[0] % 13 < cards[1] % 13) {
			int temp = cards[0];
			cards[0] = cards[1];
			cards[1] = temp;
		}
	}

	public String toString() {

		Display dis = new Display();

		return dis.displayArray(cards);

	}

	public int compareTo(Pocket obj) {

		// First compare ranks
		int c0rank = cards[0] % 13;
		int c1rank = cards[1] % 13;
		int _c0rank = obj.cards[0] % 13;
		int _c1rank = obj.cards[1] % 13;
		if (c0rank < _c0rank)
			return -1;
		if (c0rank > _c0rank)
			return 1;
		if (c1rank < _c1rank)
			return -1;
		if (c1rank > _c1rank)
			return 1;

		// Then compare suits order c d h s (not important)
		int c0suit = cards[0] / 13;
		int c1suit = cards[1] / 13;
		int _c0suit = obj.cards[0] / 13;
		int _c1suit = obj.cards[1] / 13;
		if (c0suit < _c0suit)
			return -1;
		if (c0suit > _c0suit)
			return 1;
		if (c1suit < _c1suit)
			return -1;
		if (c1suit > _c1suit)
			return 1;

		return 0;
	}

}