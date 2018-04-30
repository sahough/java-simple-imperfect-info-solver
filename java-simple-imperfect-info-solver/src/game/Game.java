package game;

import poker.*;
import trainer.*;

import java.util.Random;

public class Game {

	int F_SIZE;
	int C_SIZE;

	Trainer trainer;
	History history;
	int[] handCodes;

	boolean[] players;
	int score = 0;

	public boolean gameRunning;

	Random r = new Random();

	public Game(int _F_SIZE, int _C_SIZE) {

		F_SIZE = _F_SIZE;
		C_SIZE = _C_SIZE;

		trainer = new Trainer(new LookupTable(F_SIZE, C_SIZE));

		long startTime = System.currentTimeMillis();
		trainer.train(100);
		System.out.println("Train Time (ms): "+(System.currentTimeMillis() - startTime));
		history = new History();
		handCodes = new int[2];

		players = new boolean[2];
		players[0] = true;
		players[1] = false;

		newHand();

	}

	public void switchPlayers() {
		players[0] = !players[0];
		players[1] = !players[1];
	}

	public void newHand() {

		gameRunning = true;

		switchPlayers();

		history = new History();
		int c0 = r.nextInt(C_SIZE);
		int c1 = c0;
		while (c0 == c1)
			c1 = r.nextInt(C_SIZE);
		handCodes[0] = c0;
		handCodes[1] = c1;

		update();

	}

	public void update() {

		boolean log = false;
		int status = history.status();

		// Chance action
		if (status == 3) {
			if(log) System.out.println("Status 3");
			history.flopCode = r.nextInt(F_SIZE);
			history.nextStreet();
			update();
		}

		// Showdown
		if (status == 2) {
			if(log) System.out.println("Status 2");
			gameEndShowdown();
		}

		// Fold
		if (status == 1) {
			if(log) System.out.println("Status 1");
			gameEndFold();
		}

		// Action
		if (status == 0) {
			// Player action
			if (players[history.currentPlayer()]) {
				if(log) System.out.println("Status 0 Player");
			}
			// Engine actio
			else {
				if(log) System.out.println("Status 0 Engine");
				engineAction();
			}
		}
		
	}

	public void engineAction() {

		int enginePlayer = history.currentPlayer();
		int engineHandCode = handCodes[enginePlayer];
		double[] strat = trainer.getStrategyAt(engineHandCode, history);
		int action = history.actions().get(rollAction(strat));
		makeAction(action);

	}

	public int rollAction(double[] strat) {

		double sum = 0;
		double roll = r.nextDouble();
		for (int i = 0; i < strat.length; i++) {
			sum += strat[i];
			if (roll < sum)
				return i;
		}

		return -1; // unreachable

	}

	public void makeAction(int a) {
		history.add(a);
		update();
	}

	public void gameEndShowdown() {

		int util = history.utilShowdown();
		int result = trainer.lt.table[history.flopCode][handCodes[0]][handCodes[1]];
		if(players[0]) {
			if(result == 0)
			util = 0;
			if(result == -1)
				util = -util;
		}
		else {
			if(result == 0)
			util = 0;
			if(result == 1)
				util = -util;			
		}
		score += util;
		gameRunning = false;

	}

	public void gameEndFold() {

		int util = history.utilFold();

		if (!players[history.currentPlayer()])
			util = -util;

		score += util;
		gameRunning = false;

	}

	public String displayLookupTable() {
		return trainer.lt.toString();
	}

	public String toString() {
		Display dis = new Display();
		String out = "";

		out += "Score:   " + score + "\n";

		if(players[0])
		out += "BB Hand: " + trainer.lt.range.get(handCodes[0]) + "\n";
		if(players[1])
		out += "SB Hand: " + trainer.lt.range.get(handCodes[1]) + "\n";
		
		if (history.flopCode != -1) {
			int[] flop = trainer.lt.flops.get(history.flopCode);
			out += dis.displayArray(flop) + "\n";
		}

		out += history;

		return out;
	}
	
	public String bothHands() {
		String out = "";
		out += "BB Hand: " + trainer.lt.range.get(handCodes[0]) + "\n";
		out += "SB Hand: " + trainer.lt.range.get(handCodes[1]) + "\n";
		return out;
	}

}
