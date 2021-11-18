package cephalopodGame;

import java.util.Hashtable;
import java.util.Vector;
import java.lang.Math;

public class CephalopodCapture {
	
	private static int xSize, ySize, captureResult;
	static int x, y, vecSize, up = 0, down = 0, left = 0, right = 0, captureTotal = 0;

	public static void captureAll(CephalopodBoard board, int cellNum, Hashtable<Integer, Vector<CephalopodNeighbour>> neighTab) {
		int x, y;
		
		y = (cellNum%ySize);
		x = (cellNum/ySize);
		
		for(int i = 0; i < neighTab.get(cellNum).size(); i++) {
			if (neighTab.get(cellNum).get(i).getPos().equals("Up"))
				board.setBoard(x-1,y,0);
			
			if (neighTab.get(cellNum).get(i).getPos().equals("Down"))
				board.setBoard(x+1,y,0);
				
			if (neighTab.get(cellNum).get(i).getPos().equals("Left"))
				board.setBoard(x,y-1,0);
				
			if (neighTab.get(cellNum).get(i).getPos().equals("Right"))
				board.setBoard(x,y+1,0);
		}
	}

	public static void assignDim(int x, int y) {
		xSize = x;
		ySize = y;

	}

	public static int captureOptimal(CephalopodBoard board, int cellNum, 
			Hashtable<Integer, Vector<CephalopodNeighbour>> neighTab) {
		
		
		vecSize = neighTab.get(cellNum).size();
		
		y = (cellNum%ySize);
		x = (cellNum/ySize);
		
		switch (vecSize) {
		
		case 2:
			return 0;
			
		case 3:
			return captureResult = capture3(board, cellNum, neighTab, vecSize);
				
		case 4:
			return captureResult = capture4(board, cellNum, neighTab);
			
		}
		return 0;	
	}
	
	static int capture3(CephalopodBoard board, int cellNum, 
			Hashtable<Integer, Vector<CephalopodNeighbour>> neighTab, int vecSize) {
		int[] result = new int[3];
		String edge = "", resultUsed = "";
		int largest = 0;
		
		for(int i = 0; i < vecSize; i++) {

			if (neighTab.get(cellNum).get(i).getPos().equals("Up"))
				up = Math.abs(neighTab.get(cellNum).get(i).getPip());
				
			if (neighTab.get(cellNum).get(i).getPos().equals("Down"))
				down = Math.abs(neighTab.get(cellNum).get(i).getPip());
					
			if (neighTab.get(cellNum).get(i).getPos().equals("Left"))
				left = Math.abs(neighTab.get(cellNum).get(i).getPip());
					
			if (neighTab.get(cellNum).get(i).getPos().equals("Right"))
				right = Math.abs(neighTab.get(cellNum).get(i).getPip());	
		}
		
		if (up == 0)
			up = 6;
		
		if (down == 0)
			down = 6;
		
		if (left == 0)
			left = 6;
		
		if (right == 0)
			right = 6;
		
		if (x == 0) {
			result[0] = left + down;
			result[1] = down + right;
			result[2] = right + left;
			
			edge = "top";
			
		} else if (x == xSize - 1) {
			result[0] = left + up;
			result[1] = up + right;
			result[2] = right + left;
			
			edge = "bottom";
			
		} else if (y == 0) {
			result[0] = up + down;
			result[1] = down + right;
			result[2] = right + up;
			
			edge = "left";
			
		} else {
			result[0] = left + down;
			result[1] = down + up;
			result[2] = up + left;
			
			edge = "right";
			
		}
		
		for (int i = 0; i < result.length; i++) {
			if (result[i] > 6) {
				result[i] = 0;
			}
				
			if (result[i] > largest) {
				largest = result[i];
				
				switch (i) {
				case 0:
					resultUsed = "r1";
					break;
				
				case 1:
					resultUsed = "r2";
					break;
				
				case 2:
					resultUsed = "r3";
					break;
				}
			}
		}
		
		switch(edge) {
		case "top":
			switch (resultUsed) {
			case "r1":
				board.setBoard(x+1,y,0);
				board.setBoard(x,y-1,0);
				break;
				
			case "r2":
				board.setBoard(x+1,y,0);
				board.setBoard(x,y+1,0);
				break;
				
			case "r3":
				board.setBoard(x,y+1,0);
				board.setBoard(x,y-1,0);	
				break;
			}
			break;
			
		case "bottom":
			switch (resultUsed) {
			case "r1":
				board.setBoard(x-1,y,0);
				board.setBoard(x,y-1,0);
				break;
				
			case "r2":
				board.setBoard(x-1,y,0);
				board.setBoard(x,y+1,0);
				break;
				
			case "r3":
				board.setBoard(x,y+1,0);
				board.setBoard(x,y-1,0);
				break;
			}
			break;
			
		case "left":
			switch (resultUsed) {
			case "r1":
				board.setBoard(x+1,y,0);
				board.setBoard(x-1,y,0);
				break;
			
			case "r2":
				board.setBoard(x,y+1,0);
				board.setBoard(x+1,y,0);
				break;
				
			case "r3":
				board.setBoard(x,y+1,0);
				board.setBoard(x-1,y,0);
				break;
				
			}
			break;
			
		case "right":
			switch (resultUsed) {
			case "r1":
				board.setBoard(x+1,y,0);
				board.setBoard(x,y-1,0);
				break;
			
			case "r2":
				board.setBoard(x+1,y,0);
				board.setBoard(x-1,y,0);
				break;
				
			case "r3":
				board.setBoard(x-1,y,0);
				board.setBoard(x,y-1,0);
				break;
			}
			break;
			
		}
		
		return largest;
	}
	
	static int capture4(CephalopodBoard board, int cellNum, 
			Hashtable<Integer, Vector<CephalopodNeighbour>> neighTab) {
		int temp = 0;
		
		up = Math.abs(neighTab.get(cellNum).get(0).getPip());
		down = Math.abs(neighTab.get(cellNum).get(1).getPip());
		left = Math.abs(neighTab.get(cellNum).get(2).getPip());
		right = Math.abs(neighTab.get(cellNum).get(3).getPip());
		
		if (up + left < 6 && (up != 0 && left != 0)) {
			temp = 6 - (up + left);
			
			if (down <= temp && down != 0) {
				board.setBoard(x+1,y,0);
				board.setBoard(x,y-1,0);
				board.setBoard(x-1,y,0);
				
				return (up + left + down);
				
			} else if(right <= temp && right != 0) {
				board.setBoard(x-1,y,0);
				board.setBoard(x,y-1,0);
				board.setBoard(x,y+1,0);
				
				return (up + left + right);
			} else {
				board.setBoard(x+1,y,0);
				board.setBoard(x,y-1,0);
				
				return (up + left);
			}
		} else if(up + left == 6 && (up != 0 && left != 0)) {
			board.setBoard(x+1,y,0);
			board.setBoard(x,y-1,0);
			
			return (up + left);
		}
		
		
		if (down + right < 6 && (down != 0 && right != 0)) {
			temp = 6 - (down + right);
			
			if (up <= temp && up != 0) {
				board.setBoard(x-1,y,0);
				board.setBoard(x+1,y,0);
				board.setBoard(x,y+1,0);
				
				return (up + right + down);
				
			} else if(left <= temp && left != 0) {
				board.setBoard(x+1,y,0);
				board.setBoard(x+1,y,0);
				board.setBoard(x,y+1,0);
				
				return (down + left + right);
			} else {
				board.setBoard(x+1,y,0);
				board.setBoard(x,y+1,0);
				
				return (down + right);
			}
		} else if(down + right == 6 && (down != 0 && right != 0)) {
			board.setBoard(x+1,y,0);
			board.setBoard(x,y+1,0);
			
			return (down + right);
		}
		
		if (up + right < 6 && (up != 0 && right != 0)) {
			board.setBoard(x-1,y,0);
			board.setBoard(x,y+1,0);
			
			return (up + right);
		}
		
		if (down + left < 6 && (down != 0 && left != 0)) {
			board.setBoard(x+1,y,0);
			board.setBoard(x,y-1,0);
			
			return (down + left);
		}
		
		if (right + left < 6 && (left != 0 && right != 0)) {
			board.setBoard(x,y+1,0);
			board.setBoard(x,y-1,0);
			
			return (right + left);
		}
		
		if (down + up < 6 && (up != 0 && down != 0)) {
			board.setBoard(x+1,y,0);
			board.setBoard(x-1,y,0);
			
			return (down + up);
		}
		
		return 0;
	}
	
	public static int captureOptimalTemp(int[][] board, int cellNum, 
			Hashtable<Integer, Vector<CephalopodNeighbour>> neighTab) {
		
		
		vecSize = neighTab.get(cellNum).size();
		
		y = (cellNum%xSize);
		x = (cellNum/xSize);
		
		switch (vecSize) {
		
		case 2:
			return 0;
			
		case 3:
			return captureResult = capture3Temp(cellNum, neighTab, vecSize);
				
		case 4:
			return captureResult = capture4Temp(cellNum, neighTab);
			
		}
		return 0;	
	}
	
	static int capture3Temp(int cellNum, 
			Hashtable<Integer, Vector<CephalopodNeighbour>> neighTab, int vecSize) {
		int[] result = new int[3];
		String edge = "", resultUsed = "";
		int largest = 0;
		
		for(int i = 0; i < vecSize; i++) {

			if (neighTab.get(cellNum).get(i).getPos().equals("Up"))
				up = Math.abs(neighTab.get(cellNum).get(i).getPip());
				
			if (neighTab.get(cellNum).get(i).getPos().equals("Down"))
				down = Math.abs(neighTab.get(cellNum).get(i).getPip());
					
			if (neighTab.get(cellNum).get(i).getPos().equals("Left"))
				left = Math.abs(neighTab.get(cellNum).get(i).getPip());
					
			if (neighTab.get(cellNum).get(i).getPos().equals("Right"))
				right = Math.abs(neighTab.get(cellNum).get(i).getPip());	
		}
		
		if (up == 0)
			up = 6;
		
		if (down == 0)
			down = 6;
		
		if (left == 0)
			left = 6;
		
		if (right == 0)
			right = 6;
		
		if (x == 0) {
			result[0] = left + down;
			result[1] = down + right;
			result[2] = right + left;
			
			edge = "top";
			
		} else if (x == xSize - 1) {
			result[0] = left + up;
			result[1] = up + right;
			result[2] = right + left;
			
			edge = "bottom";
			
		} else if (y == 0) {
			result[0] = up + down;
			result[1] = down + right;
			result[2] = right + up;
			
			edge = "left";
			
		} else {
			result[0] = left + down;
			result[1] = down + up;
			result[2] = up + left;
			
			edge = "right";
			
		}
		
		for (int i = 0; i < result.length; i++) {
			if (result[i] > 6) {
				result[i] = 0;
			}
				
			if (result[i] > largest) {
				largest = result[i];
			}
		}
		 	
		return largest;
	}
	
	static int capture4Temp(int cellNum, 
			Hashtable<Integer, Vector<CephalopodNeighbour>> neighTab) {
		int temp = 0;
		
		up = Math.abs(neighTab.get(cellNum).get(0).getPip());
		down = Math.abs(neighTab.get(cellNum).get(1).getPip());
		left = Math.abs(neighTab.get(cellNum).get(2).getPip());
		right = Math.abs(neighTab.get(cellNum).get(3).getPip());
		
		if (up + left < 6 && (up != 0 && left != 0)) {
			temp = 6 - (up + left);
			
			if (down <= temp && down != 0) {
				return (up + left + down);
				
			} else if(right <= temp && right != 0) {
				return (up + left + right);
				
			} else {
				return (up + left);
			}
		}
		
		
		if (down + right < 6 && (down != 0 && right != 0)) {
			temp = 6 - (down + right);
			
			if (up <= temp && up != 0) {
				return (up + right + down);
				
			} else if(left <= temp && left != 0) {
				return (down + left + right);
				
			} else {
				return (down + right);
			}
		} 
		
		if (up + right < 6 && (up != 0 && right != 0)) {
			return (up + right);
		}
		
		if (down + left < 6 && (down != 0 && left != 0)) {
			return (down + left);
		}
		
		if (right + left < 6 && (left != 0 && right != 0)) {
			return (right + left);
		}
		
		if (down + up < 6 && (up != 0 && down != 0)) {
			return (down + up);
		}
		
		return 0;
	}
}
