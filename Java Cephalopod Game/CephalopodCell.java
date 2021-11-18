package cephalopodGame;

public class CephalopodCell {
	
	private int xCord;
	private int yCord;
	private double util;
	
	CephalopodCell(int x, int y, double u) {
		xCord = x;
		yCord = y;
		util = u;
	}
		
	public double getUtil() {
		return util;
	}
	
	public int getXCord() {
		return xCord;
	}
	
	public int getYCord() {
		return yCord;
	}
}
