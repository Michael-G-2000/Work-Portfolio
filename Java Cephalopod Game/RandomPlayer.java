package cephalopodGame;

import java.util.Vector;
import java.util.Random;

public class RandomPlayer {

	private int cellNum;
	private boolean isMonteSim = false;
	Vector<Integer> availableCells = new Vector<>();	
	
	public void makeMove(CephalopodBoard board, int xSize, boolean turn) {
		
		availableCells = board.getEmptyCells();
		
		Random rand = new Random();
		cellNum = availableCells.get(rand.nextInt(availableCells.size()));
		
		board.placeDice(board, (cellNum/xSize)+1, (cellNum%xSize)+1, turn, isMonteSim);
		
	}
}
