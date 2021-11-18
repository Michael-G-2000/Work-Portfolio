package cephalopodGame;

public class CephalopodNeighbour {
	
	private String position;
	private int pipValue;
	
	public CephalopodNeighbour(String pos, int pip) {
		position = pos;
		pipValue = pip;
	}
	
	String getPos() {
		return position;
	}
	
	int getPip() {
		return pipValue;
	}
	
	void setVal(int val) {
		pipValue = val;
	}
}
