package poker;

import java.util.ArrayList;
import java.util.Collections;

public class LookupTable {

	public ArrayList<int[]> flops;
	public ArrayList<Pocket> range;
	public byte[][][] table;

	public LookupTable(int flopsSize, int rangeSize) {
		flops = flopGenerator(flopsSize);
		range = rangeGenerator(rangeSize);
		table = getTable();
	}

	// INITIALIZER METHODS

	public ArrayList<int[]> flopGenerator(int size) {

		ArrayList<int[]> flopsO = new ArrayList<int[]>();
		for (int i = 0; i < size; i++) {
			Deck d = new Deck();
			int[] f = d.deal(3);
			flopsO.add(f);
		}
		return flopsO;

	}

	public static ArrayList<Pocket> rangeGenerator(int size) {

		ArrayList<Pocket> rangeO = new ArrayList<Pocket>();
		for (int i = 0; i < size; i++) {
			Deck d = new Deck();
			Pocket p = new Pocket(d.deal(2));
			rangeO.add(p);
		}
		Collections.sort(rangeO);
		return rangeO;

	}

	public byte[][][] getTable() {

		Evaluator e = new Evaluator();

		int flopsSize = flops.size();
		int rangeSize = range.size();

		byte[][][] table = new byte[flopsSize][rangeSize][rangeSize];

		for (int f = 0; f < flopsSize; f++) {
			for (int h0 = 0; h0 < rangeSize; h0++) {
				for (int h1 = 0; h1 < rangeSize; h1++) { // Symmetric table
					if (h0 != h1) { // No collisions (but it would evaluate to 0
									// anyway)
						int[] flop = flops.get(f);
						Pocket hand1 = range.get(h0);
						Pocket hand2 = range.get(h1);
						int result = e.compareHandOnFlop(hand1, hand2, flop);
						table[f][h0][h1] = (byte) result;
					}
				}
			}
		}

		return table;

	}

	// OUTPUT METHODS

	public String toStringFlops() {
		Display dis = new Display();
		String out = "Flops:\n";
		for (int i = 0; i < flops.size(); i++)
			out += dis.displayArray(flops.get(i)) + "\n";
		return out;
	}

	public String toStringRange() {
		String out = "Range:\n";
		for (int i = 0; i < range.size(); i++)
			out += range.get(i) + "\n";
		return out;
	}

	public String toString() {
		String out = "";
		out += toStringFlops() + "\n" + toStringRange();
		return out;
	}

}
