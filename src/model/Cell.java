package model;
import java.util.ArrayList;

import config.State;
import javafx.scene.shape.Shape;

/**
 * Cell defines the behavior each node in the simulation will have.
 * @author Jordan Frazier (jrf30)
 * @author Charles Xu (cx15)
 *
 */

public abstract class Cell extends Shape {
	
	private int rowPos;
	private int colPos;
	private ArrayList<Cell> neighbors;
	
	private String futurestate;
	private String currentstate;
	public Cell(int row, int col){
		rowPos = row;
		colPos = col;
	}
	
	public int getRowPos() {
		return rowPos;
	}

	public void setRowPos(int rowPos) {
		this.rowPos = rowPos;
	}

	public int getColPos() {
		return colPos;
	}

	public void setColPos(int colPos) {
		this.colPos = colPos;
	}

	public String getFuturestate() {
		return futurestate;
	}

	public void setFuturestate(String futurestate) {
		this.futurestate = futurestate;
	}

	public String getCurrentstate() {
		return currentstate;
	}

	public void setCurrentstate(String currentstate) {
		this.currentstate = currentstate;
	}
	
	public ArrayList<Cell> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(ArrayList<Cell> neighbors) {
		this.neighbors = neighbors;
	}
	
	public Shape render() { return null; }
	
	@Deprecated
	public abstract int[] getRowDeltas();
	
	@Deprecated
	public abstract int[] getColDeltas();
	
	public boolean hasPatch(){
		return false;
	}
	
	public boolean isSugarCell(){
		return false;
	}
	
	public boolean isAntCell(){
		return false;
	}
	
	/**
	 * Serialize to an element in XML
	 * @param parser
	 */
	public State serialize() {
		State s = new State();
		s.getAttributes().put("row", rowPos + "");
		s.getAttributes().put("col", colPos + "");
		s.getAttributes().put("currentState", currentstate);
		s.getAttributes().put("futureState",  futurestate);
		return s;
	}

	@Override
	public com.sun.javafx.geom.Shape impl_configShape() {
		return null;
	}
}
