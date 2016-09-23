package model;

import javafx.scene.Node;

public class Rectangle extends Cell {
	
	//included diagonals
	private int[] rowDeltas = {-1, 0, 1, 0, 1, 1, -1, -1};
	private int[] colDeltas = {0, -1, 0, 1, 1, -1, 1, -1};

	public Rectangle(int row, int col) {
		super(row, col);
		//do some other rectangular things?
	}

	@Override
	public void render() {
		// yeah
	}
	
	@Override
	public int[] getRowDeltas() {
		return rowDeltas;
	}
	
	@Override
	public int[] getColDeltas() {
		return colDeltas;
	}	

}
