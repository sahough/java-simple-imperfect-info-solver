package trainer;

import java.util.ArrayList;

public class StreetHistory {

	ArrayList<Integer> line;
	int[] structure;

	public StreetHistory(int[] _structure) {
		line = new ArrayList<Integer>();
		structure = new int[_structure.length];
		for (int i = 0; i < _structure.length; i++)
			structure[i] = _structure[i];
	}

	public StreetHistory(StreetHistory sh) {
		line = new ArrayList<Integer>();
		for (int i : sh.line)
			line.add(i);
		structure = new int[sh.structure.length];
		for (int i = 0; i < sh.structure.length; i++)
			structure[i] = sh.structure[i];
	}

	// ACCESS FUNCTIONS

	public int size() {
		return line.size();
	}

	public int safeGet(int index) {
		if (index < 0 || index >= size())
			return -999;
		return line.get(index);
	}

	public void add(int a) {
		line.add(a);
	}

	public int currentPlayer() {
		return size() % 2;
	}

	// ACTION FUNCTIONS

	public int status() {
		int last = safeGet(size() - 1);
		int preLast = safeGet(size() - 2);
		if (last == -999 || preLast == -999)
			return 0;
		if (last == preLast)
			return 2;
		if (last == 0 && preLast > 0)
			return 1;
		return 0;
	}

	public ArrayList<Integer> actions() {
		ArrayList<Integer> tiers = new ArrayList<Integer>();
		for (int i = 0; i < structure.length; i++)
			tiers.add(structure[i]);
		if (size() == 0)
			return tiers;
		int last = safeGet(size() - 1);
		if (last == 0)
			return tiers;
		ArrayList<Integer> aset = new ArrayList<Integer>();
		aset.add(0);
		for (int i : tiers) {
			if (i >= last)
				aset.add(i);
		}
		return aset;
	}

	// UTILITY FUNCTIONS

	public int utilShowdown() {
		return safeGet(size() - 1);
	}

	public int utilFold() {
		int sum = 0;
		if (size() >= 3)
			sum += safeGet(size() - 3);
		return sum;
	}

	// OUTPUT FUNCTIONS

	public String toString() {
		String out = "";
		for (int i = 0; i < line.size(); i++)
			out += line.get(i) + " ";
		return out;
	}

}
