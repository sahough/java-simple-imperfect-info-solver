package poker;

public class Display {

	public String displayRank(int rank) {
		switch(rank) {
        case 0: return "2";
        case 1: return "3";
        case 2: return "4";
        case 3: return "5";
        case 4: return "6";
        case 5: return "7";
        case 6: return "8";
        case 7: return "9";
        case 8: return "T";
        case 9: return "J";
        case 10: return "Q";
        case 11: return "K";
        case 12: return "A";
        }
        return "";
	}
	
	public String displaySuit(int suit) {
		switch(suit) {
        case 0: return "♣";
        case 1: return "♦";
        case 2: return "♥";
        case 3: return "♠";
        }
        return "";
	}
	
	public String displayCard(int card) {
		return displayRank(card%13) + displaySuit(card/13);
	}
	
	public String displayType(int type) {
		switch(type) {
        case 0: return "High Card";
        case 1: return "One Pair";
        case 2: return "Two Pair";
        case 3: return "Three Of A Kind";
        case 4: return "Straight";
        case 5: return "Flush";
        case 6: return "Full House";
        case 7: return "Four Of A Kind";
        case 8: return "Straight Flush";
        }
        return "";
	}
	
	public String displayArray(int[] cards) {
		String out = "";
		for(int i = 0; i < cards.length; i ++)
			out += displayCard(cards[i]) + " ";
		out = out.substring(0, out.length()-1);
		return out;
	}
	
}