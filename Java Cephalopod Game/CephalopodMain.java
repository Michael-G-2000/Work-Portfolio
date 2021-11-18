package cephalopodGame;

import java.util.Scanner;
import java.io.IOException;

public class CephalopodMain {

	static Scanner keyboard = new Scanner(System.in);
	static int xSize, ySize;
	static boolean isWhiteTurn = true, isMonteSim = false;
	
	public static void main(String[] args) throws IOException {
		
		getBoardDim();
		
		int gameSelection = playerSelection();
		
		CephalopodBoard board = new CephalopodBoard(xSize, ySize);
		board.createHashTable(board);
		board.printBoard();
		
		HumanPlayer player1H = new HumanPlayer(1, '+');
		HumanPlayer player2H = new HumanPlayer(2, '-');
		RandomPlayer player1R = new RandomPlayer();
		RandomPlayer player2R = new RandomPlayer();
		SmartPlayer player1S = new SmartPlayer();
		SmartPlayer player2S = new SmartPlayer();
		MonteCarloPlayer player1MC = new MonteCarloPlayer(xSize, ySize, 1);
		MonteCarloPlayer player2MC = new MonteCarloPlayer(xSize, ySize, 2);

		
		while (board.isGameOver() == false) {
			
			switch (gameSelection) {
			
			case 1:
				isWhiteTurn = true;
				
				player1H.makeMove(board, isWhiteTurn);
				
				board.printBoard();
				
				if (board.isGameOver() == true) {
					break;
				}
				
				isWhiteTurn = false;
				
				player2H.makeMove(board, isWhiteTurn);
				
				board.printBoard();
				break;
				
			case 2:
				{
					isWhiteTurn = true;
					
					player1H.makeMove(board, isWhiteTurn);
					
					board.printBoard();
					
					if (board.isGameOver() == true) {
						break;
					}
				
					isWhiteTurn = false;
								
					player2R.makeMove(board, ySize, isWhiteTurn);
					break;
				}
			
			case 3:
				{
					isWhiteTurn = true;
					
					player1H.makeMove(board, isWhiteTurn);
					
					board.printBoard();
					
					if (board.isGameOver() == true) {
						break;
					}
				
					isWhiteTurn = false;
								
					player2S.makeMove(board, ySize, isWhiteTurn);
					break;
				}	
			case 4:
				{
					isWhiteTurn = true;
							
					player1R.makeMove(board, ySize, isWhiteTurn);
					
					board.printBoard();
					
					if (board.isGameOver() == true) {
						break;
					}
				
					isWhiteTurn = false;
								
					player2R.makeMove(board, ySize, isWhiteTurn);
					
					board.printBoard();
					break;
				}
				
			case 5:
				{
					isWhiteTurn = true;
					
					player1S.makeMove(board, ySize, isWhiteTurn);
					
					board.printBoard();
					
					if (board.isGameOver() == true) {
						break;
					}
				
					isWhiteTurn = false;
								
					player2R.makeMove(board, ySize, isWhiteTurn);
					
					board.printBoard();
					break;
				}
			
			case 6:
				{
					isWhiteTurn = true;
					
					player1S.makeMove(board, ySize, isWhiteTurn);
					
					board.printBoard();
					
					if (board.isGameOver() == true) {
						break;
					}
				
					isWhiteTurn = false;
								
					player2S.makeMove(board, ySize, isWhiteTurn);
					
					board.printBoard();
					break;
				}
				
			case 7:
				{
					isWhiteTurn = true;
					
					player1S.makeMove(board, ySize, isWhiteTurn);
					
					board.printBoard();
					
					if (board.isGameOver() == true) {
						break;
					}
				
					isWhiteTurn = false;
								
					player2MC.makeMove(board, isWhiteTurn);
					
					board.printBoard();
					break;
				}
			
			case 8:
				{
					isWhiteTurn = true;
					
					player1R.makeMove(board, xSize, isWhiteTurn);
					
					board.printBoard();
					
					if (board.isGameOver() == true) {
						break;
					}
				
					isWhiteTurn = false;
								
					player2MC.makeMove(board, isWhiteTurn);
					
					board.printBoard();
					break;
				}
			
			case 9:
				{
					isWhiteTurn = true;
					
					player1H.makeMove(board, isWhiteTurn);
					
					board.printBoard();
					
					if (board.isGameOver() == true) {
						break;
					}
				
					isWhiteTurn = false;
								
					player2MC.makeMove(board, isWhiteTurn);
					
					board.printBoard();
					break;
				}
			
			case 10:
				{
					isWhiteTurn = true;
					
					player1MC.makeMove(board, isWhiteTurn);
					
					board.printBoard();
					
					if (board.isGameOver() == true) {
						break;
					}
				
					isWhiteTurn = false;
								
					player2MC.makeMove(board, isWhiteTurn);
					
					board.printBoard();
					break;
				}
			}		
		}
		
		board.printPoints();
		if (board.determineWinner() == 1) {
			System.out.println("Player 1 wins!");
		} else {
			System.out.println("Player 2 wins!");
		}
		
		System.out.println("Game has Finished");
	}
	
	static void getBoardDim() {
		System.out.println("How many rows should there be?");
		xSize = keyboard.nextInt();
		
		while(xSize < 3) {
			System.out.println("ERROR xSize cannot be less than 3 Please enter another number:");
			xSize = keyboard.nextInt();
		}
		
		System.out.println("How many columns should there be?");
		ySize = keyboard.nextInt();
		
		while(ySize > 10 || ySize < 1) {
			System.out.println("ERROR ySize cannot be less than 1 or greater than 1o Please enter another number:");
			ySize = keyboard.nextInt();
		}
	}
	
	static int playerSelection() {
		System.out.println("Who is Playing?");
		System.out.println("1. Human vs Human");
		System.out.println("2. Human vs Random AI");
		System.out.println("3. Human vs Smart AI");
		System.out.println("4. Random AI vs Random AI");
		System.out.println("5. Smart AI vs Random AI");
		System.out.println("6. Smart AI vs Smart AI");
		System.out.println("7. Smart AI vs MonteCarlo AI");
		System.out.println("8. Random AI vs MonteCarlo AI");
		System.out.println("9. Human AI vs MonteCarlo AI");
		System.out.println("10. MonteCarlo AI vs MonteCarlo AI");
		int value = keyboard.nextInt();
		
		while (value > 10 || value < 1) {
			System.out.println("ERROR: Not valid selection");
			System.out.println("Who is Playing?");
			System.out.println("1. Human vs Human");
			System.out.println("2. Human vs Random AI");
			System.out.println("3. Human vs Smart AI");
			System.out.println("4. Random AI vs Random AI");
			System.out.println("5. Smart AI vs Random AI");
			System.out.println("6. Smart AI vs Smart AI");
			System.out.println("7. Smart AI vs MonteCarlo AI");
			System.out.println("8. Random AI vs MonteCarlo AI");
			System.out.println("9. Human AI vs MonteCarlo AI");
			System.out.println("10. MonteCarlo AI vs MonteCarlo AI");
			value = keyboard.nextInt();
		}
		
		return value;
	}
}
