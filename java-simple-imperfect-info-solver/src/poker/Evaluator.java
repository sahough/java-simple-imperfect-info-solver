package poker;

public class Evaluator {

	// A 1
	// Tie 0
	// B -1

	public int compareHand(Hand a, Hand b) {

		int typeA = a.evalType();
		int typeB = b.evalType();

		if (typeA > typeB)
			return 1;
		if (typeA < typeB)
			return -1;

		int ranksA[] = a.cardsRank;
		int ranksB[] = b.cardsRank;

		for (int i = 0; i < 5; i++) {
			if (ranksA[i] > ranksB[i])
				return 1;
			if (ranksA[i] < ranksB[i])
				return -1;
		}

		return 0;

	}

	public int compareHandOnFlop(Pocket a, Pocket b, int flop[]) {

		int handAVals[] = { a.cards[0], a.cards[1], flop[0], flop[1], flop[2] };
		int handBVals[] = { b.cards[0], b.cards[1], flop[0], flop[1], flop[2] };
		return compareHand(new Hand(handAVals), new Hand(handBVals));

	}

}