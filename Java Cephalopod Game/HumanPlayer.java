package cephalopodGame;

import java.util.Scanner;

public class HumanPlayer {
	
	private Scanner keyboard = new Scanner(System.in);
	private int player, x, y;
	private char sign;
	private boolean isMonteSim = false;
	
	HumanPlayer(int p, char s) {
		player = p;
		sign = s;
	}
	
	void makeMove(CephalopodBoard board, boolean isWhiteTurn) {
		
		System.out.println("Player " + player + " (" + sign + ") row number for dice placement:");
		x = keyboard.nextInt();
		
		System.out.println("Player " + player + " (" + sign + ") column number for dice placement:");
		y = keyboard.nextInt();
		
		while (board.isAvailable(board, x-1, y-1) == false) {
			System.out.println("ERROR: Space already occupied.");
			
			System.out.println("Player " + player + " (" + sign + ") row number for dice placement:");
			x = keyboard.nextInt();
			
			System.out.println("Player " + player + " (" + sign + ") column number for dice placement:");
			y = keyboard.nextInt();
		}
		
		board.placeDice(board, x, y, isWhiteTurn, isMonteSim);
	}
	
}
