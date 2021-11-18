package cephalopodGame;

import java.util.Hashtable;
import java.util.Vector;

public class CephalopodBoard {
	
	private int[][] grid;
	private static int xSize;
	private static int ySize;
	Hashtable<Integer, Vector<CephalopodNeighbour>> neighTab = new Hashtable<>();
	
	public CephalopodBoard (int x, int y) {
		
		xSize = x;
		ySize = y;
		grid = new int[xSize][ySize];
		Hashtable<Integer, Vector<CephalopodNeighbour>> neighTab = new Hashtable<>();
		
		
		CephalopodCapture.assignDim(xSize, ySize);
		
		
	}
	
	void createHashTable(CephalopodBoard board) {
		for (int i = 0; i < (xSize*ySize); i++) {
			board.neighTab.put(i, storeNeighbourInfo(i, board));
		}
	}
		
	Vector<Integer> getEmptyCells() {
		Vector<Integer> empty = new Vector<>();
		
		for (int i = 0; i < xSize; i++) {
			for (int j = 0; j < ySize; j++) {
				if (grid[i][j] == 0) {
					empty.add((i*ySize)+j);
				}
			}
		}
		
		return empty;
		
	}
		
	void placeDice (CephalopodBoard board, int y, int x, boolean isWhiteTurn, boolean isMonteSim) {
		int cellNumber = (((y-1) * ySize) + (x-1));
		int placeValue;
		
		placeValue = checkNeighbours(board, cellNumber, isWhiteTurn, isMonteSim);
				
		if (placeValue > 1 || placeValue < -1) {
			if (isWhiteTurn == true)
			{
				grid[y-1][x-1] = placeValue;
			} else {
				grid[y-1][x-1] = placeValue*-1;
			}
		} else {
			if (isWhiteTurn == true)
			{
				grid[y-1][x-1] = 1;
			} else {
				grid[y-1][x-1] = -1;
			}
		}
		
		for (int i = 0; i < (xSize*ySize); i++) {
			neighTab.put(i, storeNeighbourInfo(i, board));
		}
	}
	
	static int checkNeighbours(CephalopodBoard board, int cellNum, boolean whiteTurn, boolean isMonteSim) {
		int up = 0, down = 0, left = 0, right = 0, total = 0, captureCount = 0;
		
		
		for (int i = 0; i < board.neighTab.get(cellNum).size(); i++) {
			if (board.neighTab.get(cellNum).get(i).getPos().equals("Up")) {
				up = board.neighTab.get(cellNum).get(i).getPip();
				if (up != 0) {
					captureCount++;
				}	
				up = Math.abs(up);
			}
			
			if (board.neighTab.get(cellNum).get(i).getPos().equals("Down")) {
				down = board.neighTab.get(cellNum).get(i).getPip();
				if (down != 0) {
					captureCount++;
				}				
				down = Math.abs(down);
			}
			
			if (board.neighTab.get(cellNum).get(i).getPos().equals("Left")) {
				left = board.neighTab.get(cellNum).get(i).getPip();
				if (left != 0) {
					captureCount++;
				}
					
				left = Math.abs(left);
			}
			
			if (board.neighTab.get(cellNum).get(i).getPos().equals("Right")) {
				right = board.neighTab.get(cellNum).get(i).getPip();
				if (right != 0) {
					captureCount++;
				}		
				right = Math.abs(right);
			}
		}
		
		total = up+down+left+right;
		
		if ((total < 7 && total > 1) && captureCount > 1) {
			CephalopodCapture.captureAll(board, cellNum, board.neighTab);
		} else if (total > 6 && captureCount > 1) {
				total = CephalopodCapture.captureOptimal(board, cellNum, board.neighTab);
		} else {
			total = 0;
		}
		
		if (isMonteSim == false) {
			board.printNeighInfo(up, down, left, right, total, whiteTurn);
		}
				
		return total;
	}
	
	void printNeighInfo(int up, int down, int left, int right, int total, boolean whiteTurn) {
		
		if (up != 0)
			System.out.println("Up: " + up);
		
		if (down != 0)
			System.out.println("Down: " + down);
		
		if (left != 0)
			System.out.println("Left: " + left);
		
		if ( right != 0)
			System.out.println("Right: " + right);
		
		if (total != 0) {
			if (whiteTurn == true) {
				System.out.println("Total: " + total);
			} else {
				System.out.println("Total: " + total*-1);
			}		
		}
	}
	
	int getValue(int x, int y) {
		return grid[x][y];
	}
	
	int checkNeighboursSmart(int cellNum) {
		int up = 0, down = 0, left = 0, right = 0, total = 0, captureCount = 0;
		
		
		for (int i = 0; i < neighTab.get(cellNum).size(); i++) {
			if (neighTab.get(cellNum).get(i).getPos().equals("Up")) {
				up = neighTab.get(cellNum).get(i).getPip();
				if (up != 0) {
					captureCount++;
				}	
				up = Math.abs(up);
			}
			
			if (neighTab.get(cellNum).get(i).getPos().equals("Down")) {
				down = neighTab.get(cellNum).get(i).getPip();
				if (down != 0) {
					captureCount++;
				}				
				down = Math.abs(down);
			}
			
			if (neighTab.get(cellNum).get(i).getPos().equals("Left")) {
				left = neighTab.get(cellNum).get(i).getPip();
				if (left != 0) {
					captureCount++;
				}
					
				left = Math.abs(left);
			}
			
			if (neighTab.get(cellNum).get(i).getPos().equals("Right")) {
				right = neighTab.get(cellNum).get(i).getPip();
				if (right != 0) {
					captureCount++;
				}		
				right = Math.abs(right);
			}
		}
		
		total = up+down+left+right;
		
		if ((total < 7 && total > 1) && captureCount > 1) {
			return total;
		} else if (total > 6 && captureCount > 1) {
			total = CephalopodCapture.captureOptimalTemp(grid, cellNum, neighTab);
		} else {
			total = 0;
		}
				
		return total;
	}
	
	
	static Vector<CephalopodNeighbour> storeNeighbourInfo(int cell, CephalopodBoard board) {
		Vector<CephalopodNeighbour> vec = new Vector<CephalopodNeighbour>();
		int x, y;
		x = cell/ySize;
		y = cell%ySize;
		
		if (cell > ySize-1) {
			vec.add(new CephalopodNeighbour("Up", board.getValue(x-1, y)));
		}
		
		if (cell < (xSize -1)* ySize) {
			vec.add(new CephalopodNeighbour("Down", board.getValue(x+1,y)));
		}
		
		if (cell%ySize != 0) {
			vec.add (new CephalopodNeighbour("Left", board.getValue(x,y-1)));
		}
		
		if (cell%ySize != ySize -1) {
			vec.add(new CephalopodNeighbour("Right", board.getValue(x,y+1)));
		}
		
		return vec;
	}
	
	void setBoard (int x, int y, int value) {
		grid[x][y] = value;
	}
		
	boolean isAvailable(CephalopodBoard board, int x, int y) {
		if (board.getValue(x, y) != 0) {
			return false;
		} else {
			return true;
		}
		
	}
	
	boolean isGameOver() {
		for (int i = 0; i < xSize; i++) {
			for (int j = 0; j < ySize; j++) {
				if (grid[i][j] == 0) {
					return false;
				}
			}
		}	
		return true;
	}
	
	void printPoints() {
		int blackPoints = 0, whitePoints = 0;
		
		for (int i = 0; i < xSize; i++) {
			for(int j= 0; j < ySize; j++) {
				if (grid[i][j] > 0) {
					whitePoints += grid[i][j];
				} else {
					blackPoints += grid[i][j];
				}
			}
		}
		
		System.out.println("Player 1 points: " + whitePoints);
		System.out.println("Player 2 points: " + blackPoints);
	}
	
	int determineWinner() {
		int blackPoints = 0, whitePoints = 0;
		
		if (isGameOver() == false) {
			return 0;
		}
		
		for (int i = 0; i < xSize; i++) {
			for(int j= 0; j < ySize; j++) {
				if (grid[i][j] > 0) {
					whitePoints += grid[i][j];
				} else {
					blackPoints += grid[i][j];
				}
			}
		}
					
		if (whitePoints > (blackPoints*-1)) {
			return 1;
		} else {
			return 2;
		}	
	}
	
	void printBoard() {
		System.out.print("    ");
	    for (int j = 0; j < ySize; j++) {
	    	System.out.print((j + 1) + "   ");
	    }
	    System.out.print("\n");
	    
	    for (int i = 0; i < xSize; i++) {
	    	System.out.print("  ");
	        for (int j = 0; j < ySize; j++) {
	        	System.out.print(" ---");
	        }
	        System.out.print("\n");
	        
	        System.out.print((i + 1) + " |");
	        
	        for (int j = 0; j < ySize; j++) {
	            if (grid[i][j] == 0) {
	            	System.out.print("   |");
	            } else if (grid[i][j] > 0) {
	            	System.out.print(" " + grid[i][j] + " |");
	            } else if (grid[i][j] < 0) {
	            	System.out.print(grid[i][j] + " |");
	            }
	        }
	        System.out.print("\n");
	    }
	    System.out.print("  ");
	    
	    for (int j = 0; j < ySize; j++) {
	    	System.out.print(" ---");
	    }
	    System.out.print("\n\n");
	}
}