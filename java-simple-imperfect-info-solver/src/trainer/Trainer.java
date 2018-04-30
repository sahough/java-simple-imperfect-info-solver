package trainer;

import poker.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Trainer {

	public LookupTable lt;
	int F_SIZE, C_SIZE;
	TreeMap<InfoSet, Node> nodeMap;

	public Trainer(LookupTable _lt) {
		lt = _lt;
		F_SIZE = lt.flops.size();
		C_SIZE = lt.range.size();
		nodeMap = new TreeMap<InfoSet, Node>();
	}

	// UTILITY METHODS

	public double[] uiFold(History h, double[] pi1) {

		double[] u = new double[C_SIZE];

		for (int c0 = 0; c0 < C_SIZE; c0++) {
			double sum = 0;
			for (int c1 = 0; c1 < C_SIZE; c1++) {
				if (c0 != c1) {
					double util = h.utilFold();
					double weightedUtil = pi1[c1] * util;
					sum += weightedUtil;
				}
			}
			sum /= (C_SIZE - 1);
			u[c0] = sum;
		}

		return u;

	}

	public double[] uiShowdown(History h, double[] pi1) {

		double[] u = new double[C_SIZE];

		for (int c0 = 0; c0 < C_SIZE; c0++) {
			double sum = 0;
			for (int c1 = 0; c1 < C_SIZE; c1++) {
				if (c0 != c1) {
					double util = h.utilShowdown();
					//byte result = lt.table[h.flopCode][c0][c1];
					byte result = lt.table[0][c0][c1]; //Kuhn Tests
					if (result == (byte)-1)
						util = -util;
					else if (result == (byte)0)
						util = 0;
					double weightedUtil = pi1[c1] * util;
					sum += weightedUtil;
				}
			}
			sum /= (C_SIZE - 1);
			u[c0] = sum;
		}
		
		return u;

	}

	public double[] CFR(History h, int i, double w, double[] pi1) {
		double[] u = new double[C_SIZE];

		int status = h.status();

		// CHANCE ACTION

		if (status == 3) {
			for (int f = 0; f < F_SIZE; f++) {
				History hf = new History(h);
				hf.flopCode = f;
				hf.nextStreet();
				double uPrime[] = CFR(hf, i, w, pi1);
				for (int c = 0; c < C_SIZE; c++)
					u[c] += uPrime[c] / F_SIZE; // Average out the results
												// normally
			}
			return u;
		}

		// TERMINAL

		if (status == 2) {
			u = uiShowdown(h, pi1);
			return u;
		}
		if (status == 1) {
			u = uiFold(h, pi1);
			if (h.currentPlayer() != i)
				u = flipVector(u);
			return u;
		}

		// PLAYER ACTION

		ArrayList<Integer> aSet = h.actions();
		int A_SIZE = aSet.size();

		InfoSet[] I = lookupInfoSets(h);
		double[][] sigma = regretMatching(I, A_SIZE);

		double[][] m = new double[C_SIZE][A_SIZE];

		if (h.currentPlayer() == i) {

			for (int a = 0; a < A_SIZE; a++) {

				History ha = new History(h);
				ha.add(aSet.get(a));
				double[] uPrime = CFR(ha, i, w, pi1);

				for (int c = 0; c < C_SIZE; c++)
					m[c][a] = uPrime[c];

				for (int c = 0; c < C_SIZE; c++)
					u[c] += sigma[c][a] * uPrime[c];

			}

		} else {

			for (int a = 0; a < A_SIZE; a++) {

				double[] pi1prime = new double[C_SIZE];
				for (int c = 0; c < C_SIZE; c++)
					pi1prime[c] = sigma[c][a] * pi1[c];

				History ha = new History(h);
				ha.add(aSet.get(a));
				double[] uPrime = CFR(ha, i, w, pi1prime);

				for (int c = 0; c < C_SIZE; c++)
					u[c] += uPrime[c];

			}

		}

		// UPDATE REGRET OR STRATEGY

		if (h.currentPlayer() == i) {

			for (int c = 0; c < C_SIZE; c++) {
				InfoSet is = I[c];
				Node n = nodeMap.get(is);
				for (int a = 0; a < A_SIZE; a++) {
					n.regretSum[a] = Math.max(n.regretSum[a] + m[c][a] - u[c], 0);
				}
			}

		} else {

			for (int c = 0; c < C_SIZE; c++) {
				InfoSet is = I[c];
				Node n = nodeMap.get(is);
				for (int a = 0; a < A_SIZE; a++) {
					n.strategySum[a] = n.strategySum[a] + sigma[c][a] * pi1[c] * w;
				}
			}

		}

		return u;
	}

	public InfoSet[] lookupInfoSets(History h) {
		InfoSet[] I = new InfoSet[C_SIZE];
		for (int c = 0; c < C_SIZE; c++) {
			InfoSet is = new InfoSet(c, h);
			I[c] = is;
		}
		return I;
	}

	public double[][] regretMatching(InfoSet[] I, int A_SIZE) {
		double[][] sigma = new double[C_SIZE][A_SIZE];
		for (int c = 0; c < C_SIZE; c++) {
			Node n = nodeMap.get(I[c]);
			if (n == null) {
				n = new Node(A_SIZE);
				nodeMap.put(I[c], n);
			}
			sigma[c] = n.getStrategy();
		}
		return sigma;
	}

	public void train(int T) {

		double[] pi1 = filledVector(1);

		for (int t = 0; t < T; t++) {
			for (int i = 0; i < 2; i++) {

				CFR(new History(), i, t, pi1);

			}
		}

	}

	// HELPER METHODS

	public double[] flipVector(double[] in) {
		double[] out = new double[in.length];
		for (int i = 0; i < in.length; i++)
			out[i] = -in[i];
		return out;
	}

	public double[] filledVector(double val) {
		double[] out = new double[C_SIZE];
		for (int c = 0; c < C_SIZE; c++)
			out[c] = val;
		return out;
	}

	// OUTPUT METHODS
	
	public double[] getStrategyAt(int handCode, History h) {
		
		InfoSet is = new InfoSet(handCode, h);
		Node n = nodeMap.get(is);
		return n.getAverageStrategy();
		
	}

	public String toString() {
		String out = "";
		for (Map.Entry<InfoSet, Node> entry : nodeMap.entrySet()) {

			InfoSet key = entry.getKey();
			out += toStringInfoSet(key) + "\n";

			Node value = entry.getValue();
			out += value.toString() + "\n";

		}
		return out;
	}

	public String toStringInfoSet(InfoSet is) {
		Display dis = new Display();
		String out = "";

		// HAND
		out += "Hand:  " + lt.range.get(is.handCode) + "\n";

		// FLOP
		int flopCode = is.h.flopCode;
		if (flopCode != -1)
			out += "Board: " + dis.displayArray(lt.flops.get(flopCode)) + "\n";

		// HISTORY
		out += is.h;

		return out;
	}

}
