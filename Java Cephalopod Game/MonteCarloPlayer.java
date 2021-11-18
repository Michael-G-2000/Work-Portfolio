package cephalopodGame;

import java.util.Vector;
import java.util.Random;

public class MonteCarloPlayer {
	
	private int xSize;
	private int ySize;
	private int simTimes = 100;
	private int player;
	private boolean isMonteSim = true;
	
	MonteCarloPlayer (int x, int y, int p) {
		xSize = x;
		ySize = y;
		player = p;
	}
	
	
	void makeMove(CephalopodBoard board, boolean isWhiteTurn) {
		int x = 0, y = 0;
		
		Vector<Integer> emptyCells = board.getEmptyCells();
		if (emptyCells.isEmpty())
			return;

		Vector<CephalopodCell> moves = new Vector<>();

		for (int index : emptyCells) {
			isMonteSim = true;
			int i = index / ySize;
			int j = index % ySize;
			CephalopodBoard tempBoard = new CephalopodBoard(xSize, ySize);
			createTempBoard(tempBoard, board);
			tempBoard.createHashTable(tempBoard);			
			tempBoard.placeDice(tempBoard, i+1, j+1, isWhiteTurn, isMonteSim);
			if (tempBoard.isGameOver() == true) {
				if (tempBoard.determineWinner() == player) {
					x = i;
					y = j;
					break;
				} else {
					continue;
				}
			}

			double utility = gameSim(tempBoard, isWhiteTurn);

			
			moves.add(new CephalopodCell(i, j ,utility));
		}
		
		if (moves.size() > 0) {
			int element = findGreatestUtil(moves);
			
			x = moves.get(element).getXCord();
			y = moves.get(element).getYCord();

			System.out.println(moves.size() + " Monte Carlo Utility = " + moves.get(element).getUtil() 
					+" at " + (x+1) +", " + (y+1));
		}
		
		isMonteSim = false;
		
		board.placeDice(board, x+1, y+1, isWhiteTurn, isMonteSim);
		
		return;
	}

	double gameSim(CephalopodBoard tempBoard, boolean isWhiteTurn) {

		int outcomeOfGame = 0;

		for (int i = 0; i < simTimes; i++) {
			CephalopodBoard tempBoard2 = new CephalopodBoard(xSize, ySize);
			createTempBoard(tempBoard2, tempBoard);
			tempBoard2.createHashTable(tempBoard2);
			outcomeOfGame += nextMove(tempBoard2, isWhiteTurn);
		}

		return ((double) outcomeOfGame / (double) simTimes);
	}

	int nextMove(CephalopodBoard tempBoard2, boolean isWhiteTurn) {
        int winner = tempBoard2.determineWinner();
		if (winner != 0) {
			if (winner == player) {
				return  1;
			} else if (winner != player) {
				return -1;
			}
		}

		makeRandomMove(tempBoard2, isWhiteTurn);
		
		if (isWhiteTurn == true) {
			isWhiteTurn = false;
		} else {
			isWhiteTurn = true;
		}
		
		return nextMove(tempBoard2, isWhiteTurn);
	}
	
	int findGreatestUtil(Vector<CephalopodCell> cells) {
		int largest = 0;
		
		for(int i = 0; i < cells.size(); i++)
		{
			if(cells.get(i).getUtil() > cells.get(largest).getUtil() ) {
				largest = i;
			}
		}
		
		
		return largest;
	}
	
	void createTempBoard(CephalopodBoard tempBoard, CephalopodBoard board) {
				
		for (int i = 0; i < xSize; i++) {
			for (int j = 0; j < ySize; j++) {
				tempBoard.setBoard(i, j, board.getValue(i,j));
			}
		}
	}
	
	void makeRandomMove(CephalopodBoard board, boolean isWhiteTurn) {
		Vector<Integer> tempEmptyCells = board.getEmptyCells();
		Random rand = new Random();
		
		int cellNum = tempEmptyCells.get(rand.nextInt(tempEmptyCells.size()));
		isMonteSim = true;
		
		board.placeDice(board, ((cellNum/ySize)+1), ((cellNum%ySize)+1), isWhiteTurn, isMonteSim);
	}

}
