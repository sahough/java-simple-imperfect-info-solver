package poker;

import java.util.Arrays;

public class Hand {

	int cards[] = new int[5];
	int cardsRank[] = new int[5];
	int cardsSuit[] = new int[5];
	int type = 0;

	Hand(int[] cardsIn) {
		for (int i = 0; i < 5; i++) {
			cards[i] = cardsIn[i];
			cardsRank[i] = cardsIn[i] % 13;
			cardsSuit[i] = cardsIn[i] / 13;
		}
		sortRanks(); // TODO: ROOM TO OPTIMIZE if this becomes expensive
		type = evalType();
	}

	public void sortRanks() {

		int rankBuckets[] = new int[13];
		for (int i = 0; i < 5; i++)
			rankBuckets[cardsRank[i]]++;

		int index = 0;

		for (int freq = 4; freq >= 1; freq--) {
			for (int i = 12; i >= 0; i--) {
				if (rankBuckets[i] == freq) {
					rankBuckets[i] = 0;
					for (int j = 0; j < freq; j++) {
						cardsRank[index] = i;
						index++;
					}
				}
			}
		}

		int wheel[] = { 12, 3, 2, 1, 0 };
		int fixedWheel[] = { 3, 2, 1, 0, 12 };
		// Reset Wheel from 12-4-3-2-1 to 4-3-2-1-12
		if (Arrays.equals(cardsRank, wheel))
			cardsRank = fixedWheel;

	}

	public int evalType() {

		int matchCount = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = i + 1; j < 5; j++) {
				if (cardsRank[i] == cardsRank[j]) {
					matchCount++;
				}
			}
		}

		// Return for all paired hands
		switch (matchCount) {
		case 1:
			return 1;
		case 2:
			return 2;
		case 3:
			return 3;
		case 4:
			return 6;
		case 6:
			return 7;
		}

		boolean flush = true;
		boolean straight = true;

		for (int i = 1; i < 5; i++) {
			if (cardsSuit[0] != cardsSuit[i])
				flush = false;
		}

		for (int i = 0; i < 4; i++) {
			if (cardsRank[i] - 1 != cardsRank[i + 1])
				straight = false;
		}

		int fixedWheel[] = { 3, 2, 1, 0, 12 };
		if (Arrays.equals(cardsRank, fixedWheel))
			straight = true;

		if (!straight && !flush)
			return 0;
		if (straight && !flush)
			return 4;
		if (!straight && flush)
			return 5;
		if (straight && flush)
			return 8;

		return 0;

	}

	public String toString() {

		Display dis = new Display();
		String out = "";
		for (int i = 0; i < 5; i++) {
			out += dis.displayCard(cards[i]);
			out += " ";
		}

		out += "\n\n";

		out += dis.displayType(type);

		out += "\n[ ";

		for (int i = 0; i < 5; i++) {
			out += dis.displayRank(cardsRank[i]);
			out += " ";
		}

		out += "]\n\n";

		return out;

	}

}