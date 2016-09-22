import java.util.ArrayList;

import javafx.scene.layout.GridPane;

public class CellGrid extends GridPane {

	private Cell[][] grid;
	
	// TODO: reset grid
	// TODO: change parameters 
	//       number of initially empty (resets grid)
	// 		 percentage of states to each other (resets)
	// 		 step delay
	// 		 size of cells
	// 		 have a percentage of satisfied cells (dynamically)	 

	public CellGrid(int rows, int cols) {
		if (rows <= 0 || cols <= 0) {
			throw new IllegalArgumentException("Cannot have 0 or less rows/cols");
		}
		grid = new Cell[rows][cols];
		// set row/column constraints?
	}

	private void renderGrid() {
		
		// loop through 2d grid, render each cell. should have already set up state correctly, 
		// this just needs to display it.
		
		for(int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				Cell currentCell = grid[i][j];
				currentCell.render(); //TODO: implement render in each shape class
				// Place inside of root 
			}
		}
		
	}

	// Backend does this
	/*
	private void updateGrid() { 
		// touch each cell and figure out future state
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				Cell currentCell = grid[i][j];
				setNeighbors(currentCell); //dont need to do this. calls getNeighbors() in backend
				// update future state based on simulation rules;
				// which is done in Simulation backend
			}
		}

		// loop and update each cell
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				Cell currentCell = grid[i][j];
				updateCurrentState(currentCell);
			}
		}
	}
	*/
	
	// Not needed. backend will just call getNeighbors()
	private void setNeighbors(Cell cell) {
		// need this in case user updates cell row/col to illegal spot?
		if (!isValidLocation(cell)) {
			throw new IllegalArgumentException("Location not valid");
		}
		ArrayList<Cell> neighbors = getNeighbors(cell);
		cell.setNeighbors(neighbors);
	}
	
	/**
	 * Returns the neighbors of a shape. May need to change
	 * row/column deltas based on definition of 'neighbor'
	 * (diagonals or not)
	 * 
	 * @param cell - the shape
	 * @return - ArrayList<Cell> of cell's neighbors
	 */
	protected ArrayList<Cell> getNeighbors(Cell cell) {
		ArrayList<Cell> neighbors = new ArrayList<>();
		int rowPos = cell.getRowPos();
		int colPos = cell.getColPos();
		for (int i = 0; i < cell.getRowDeltas().length; i++) {
			int newRowPos = rowPos + cell.getRowDeltas()[i];
			int newColPos = colPos + cell.getColDeltas()[i];
			if (isValidLocation(grid[newRowPos][newColPos])) {
				neighbors.add(grid[newRowPos][newColPos]);
			}
		}
		return neighbors;
	}

	private void updateCurrentState(Cell cell) {
		cell.setCurrentstate(cell.getFuturestate());
	}

	private void setFutureState(Cell cell, int futurestate) {
		cell.setFuturestate(futurestate);
	}

	private boolean isValidLocation(Cell cell) {
		return 0 <= cell.getRowPos() && 0 <= cell.getColPos() && cell.getRowPos() < getNumRows()
				&& cell.getColPos() < getNumCols();
	}
	
	public Cell[][] getGrid() {
		return grid;
	}

	public int getNumRows() {
		return grid.length;
	}

	public int getNumCols() {
		return grid[0].length;
	}

}
