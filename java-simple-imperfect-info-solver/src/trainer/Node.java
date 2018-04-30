package trainer;

public class Node {

	int ACTION_CT;
	double[] regretSum, strategySum;

	public Node(int _ACTION_CT) {
		ACTION_CT = _ACTION_CT;
		regretSum = new double[ACTION_CT];
		strategySum = new double[ACTION_CT];
	}

	public double[] getStrategy() {
		double[] strategy = new double[ACTION_CT];
		for (int i = 0; i < ACTION_CT; i++)
			strategy[i] = regretSum[i] > 0 ? regretSum[i] : 0;
		strategy = getNormalizedArray(strategy);
		return strategy;
	}

	public double[] getAverageStrategy() {
		return getNormalizedArray(strategySum);
	}

	public double[] getNormalizedArray(double[] in) {
		double[] out = new double[in.length];
		double normalizingSum = 0;
		for (int i = 0; i < in.length; i++)
			normalizingSum += in[i];
		if (normalizingSum > 0) {
			for (int i = 0; i < in.length; i++)
				out[i] = in[i] / normalizingSum;
		} else {
			for (int i = 0; i < in.length; i++)
				out[i] = 1.0 / in.length;
		}
		return out;
	}
	
	public String toString() {
		String out = "";
		out += "Average Strategy: ";
		double[] avg = getAverageStrategy();
		for (int i = 0; i < avg.length; i++) {
			out += String.format("%.8f ", avg[i]);
		}
		out += "\n";
		return out;
	}

}
