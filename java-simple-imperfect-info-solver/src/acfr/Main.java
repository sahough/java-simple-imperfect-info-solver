package acfr;

import game.*;

import java.io.IOException;
import java.util.Scanner;

public class Main {

	public static void main(String args[]) throws IOException {

		Game g = new Game(1, 100);
		Scanner scanner = new Scanner(System.in);

		System.out.println(g.displayLookupTable());

		boolean sessionRunning = true;
		
		while (sessionRunning) {
			
			//for(int i = 0; i < 60; i ++)
			//	System.out.println();
			
			while (g.gameRunning) {

				System.out.println(g);
				System.out.print("Action: ");

				int a = scanner.nextInt();
				g.makeAction(a);

				System.out.println();
			}
			
			System.out.println(g);
			System.out.println(g.bothHands());
			
			System.out.print("New Hand?");
			scanner.nextLine();
			scanner.hasNextLine(); 
			
			System.out.println();
			g.newHand();
			
		}

		System.out.println(g);

		scanner.close();

	}

}
