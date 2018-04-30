package trainer;

import java.util.ArrayList;
import java.util.Arrays;

/* Status codes:
 * 
 * 0 - Player Action
 * 1 - Terminal Fold
 * 2 - Terminal Showdown
 * 3 - Chance Action
 * 
 * On a chance action, we have to wait to receive the flopCode and a signal to go to the next street.
 * 
 * The History only stores the flopCode and not the flop itself, like the InfoSet only stores a hand code.
 * 
 */

public class History {
	
	ArrayList<StreetHistory> streets;
	
	int sb = 25;
	int bb = 50;
	int[][] structure = {{0,bb,bb*2},
						 {0,bb*2,bb*4}};
	
	int numStreets = structure.length;
	
	public int flopCode = -1;
	
	//int[][] structure = {{0,1}};
	
	public History() {
		streets = new ArrayList<StreetHistory>();
		StreetHistory preflop = new StreetHistory(structure[0]);
		preflop.add(0);
		preflop.add(sb);
		preflop.add(bb);
		streets.add(preflop);
	}
	
	public History(History h) {
		streets = new ArrayList<StreetHistory>();
		for(int i = 0; i < h.streets.size(); i ++)
			streets.add(new StreetHistory(h.streets.get(i)));
		flopCode = h.flopCode;
	}
	
	//ACCESS FUNCTIONS
	
	public StreetHistory activeStreet() {
		return streets.get(streets.size()-1);
	}
	
	public void add(int a) {
		activeStreet().add(a);
	}
	
	public int currentPlayer() {
		return activeStreet().currentPlayer();
	}
	
	//ACTION FUNCTIONS
	
	public int status() {
		if(hasOption())
			return 0;
		int status = activeStreet().status();
		//Checks for ultimate terminal or chance node
		if(status == 2 && streets.size() < numStreets)
			return 3;
		return status;
	}
	
	public boolean hasOption() {
		//Special scenario where "option" exists versus limping
		if(streets.size() == 1 && activeStreet().size() == 4 && activeStreet().line.get(3) == bb)
			return true;
		return false;
	}
	
	public ArrayList<Integer> actions() {
		ArrayList<Integer> aSet = activeStreet().actions();
		//This stops a pointless fold action
		if(hasOption())
			aSet.remove(0);
		return aSet;
	}
	
	public void nextStreet() {
		streets.add(new StreetHistory(structure[streets.size()]));
	}
	
	//UTILITY FUNCTIONS
	
	public int potSize() {
		//Note it does NOT include last street (that falls under utilFold/utilShowdown)
		int pot = 0; //Default 0, can be weighted for Kuhn tests
		for(int i = 0; i < streets.size() - 1; i ++) {
			StreetHistory cur = streets.get(i);
			pot += cur.safeGet(cur.size()-1);
		}
		return pot;
	}
	
	public int utilFold() {
		return potSize() + activeStreet().utilFold();
	}
	
	public int utilShowdown() {
		return potSize() + activeStreet().utilShowdown();
	}
	
	//OUTPUT FUNCTIONS
	
	public String toString() {
		String out = "History: \n";
		for(int i = 0; i < streets.size(); i ++) {
			switch(i) {
			case 0: out += "(Pre-Flop) "; break;
			case 1: out += "(Flop)     "; break;
			case 2: out += "(Turn)     "; break;
			case 3: out += "(River)    "; break;
			}
			out += streets.get(i).toString() + "\n";
		}
		out += statusString() + "\n";
		return out;
	}
	
	public String statusString() {
		int status = status();
		String out = "Status: ";
			switch(status) {
			case 0: out += "Player Action: "; 
			out += Arrays.toString(actions().toArray());
			break;
			case 1: out += "Terminal Fold: "; 
			out += utilFold();
			break;
			case 2: out += "Terminal Showdown: ";
			out += utilShowdown();
			break;
			case 3: out += "Chance Action"; break;
			}
		return out;
	}

}