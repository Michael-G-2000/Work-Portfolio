package cephalopodGame;

import java.util.Random;
import java.util.Vector;

public class SmartPlayer {
	
	private int cellNum, result, largest = 0;
	private boolean isMonteSim = false;
	private Vector<Integer> availableCells = new Vector<>();
	private Random rand = new Random();
		
	public void makeMove(CephalopodBoard board, int ySize, boolean turn) {
		cellNum = -1;
		result = 0;
		largest = 0;
		
		availableCells = board.getEmptyCells();
				
		for (int i = 0; i < availableCells.size(); i++) {
			result = board.checkNeighboursSmart(availableCells.get(i));
			
			if (result > largest) {
				largest = result;
				cellNum = availableCells.get(i);
			}
		}
		
		if (cellNum == -1) {
			cellNum = availableCells.get(rand.nextInt(availableCells.size()));
		}
				
		board.placeDice(board, (cellNum/ySize)+1, (cellNum%ySize)+1, turn, isMonteSim);
		
	}
	
	void shuffle(Vector<Integer> availableCells) {
		int temp, element;
		
		for(int i = 0; i < availableCells.size(); i++) {
			temp = availableCells.get(i);
			element = rand.nextInt(availableCells.size());
			availableCells.set(i, availableCells.get(element));
			availableCells.set(element, temp);
		}
	}
	
}
