package model;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import config.Configuration;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import view.SceneConstant;

/**
 * An abstract class that must be extended by each simulation, used in MainView.java. 
 * author: Austin Gartside, Jordan Frazier and Charles Xu
 * Superclass for all of the simulations
 */
public abstract class CellGrid {

	protected ResourceBundle myResources;
	public static final String RESRC_PATH = "resources/SimulationResources";

	private static final int[] HEX_ROW_DELTAS = { -1, -1, 0, 1, 1, 0};
	private static final int[] HEX_COL_DELTAS = { 0, -1, -1, -1, 0, 1};

	private static final int[] TRI_ROW_DELTAS = { -1, -1, 0, 1, 1, 1, 0, -1 };
	private static final int[] TRI_COL_DELTAS = { 0, -1, -1, -1, 0, 1, 1, 1 };

	private static final int[] RECT_ROW_DELTAS = { 0, 1, 0, -1 };
	private static final int[] RECT_COL_DELTAS = { 1, 0, -1, 0 };

	private Cell[][] grid;
	private Configuration myConfig;
	private double xPos;
	private double yPos;

	private int[] rowDeltas;
	private int[] colDeltas;
	private String myShape;
	private String myWrappings;
	private boolean isToroidal;
	private int isEven = 0;

	public CellGrid(Configuration config) {
		myResources = ResourceBundle.getBundle(RESRC_PATH);
		myConfig = config;
		if (config.getNumRows() <= 0 || config.getNumCols() <= 0) {
			throw new IllegalArgumentException("Cannot have 0 or less rows/cols");
		}
		grid = new Cell[config.getNumRows()][config.getNumCols()];
	}

	/**
	 * Renders the grid based on the Configuration and state of current cells
	 * @param cellPane
	 * @param config
	 */
	public void renderGrid(Pane cellPane, Configuration config) {
		yPos = (SceneConstant.SCENE_HEIGHT.getValue() - SceneConstant.GRID_HEIGHT.getValue()) / 2;
		xPos = SceneConstant.GRID_PADDING.getValue();

		for (int i = 0; i < myConfig.getNumRows(); i++) {
			for (int j = 0; j < myConfig.getNumCols(); j++) {
				Cell currentCell = grid[i][j];
				Render rend = new Render(myConfig);
				Shape updatedCell = rend.chooseRender(currentCell, myShape, isEven++, xPos, yPos, i, j);
				cellPane.getChildren().add(updatedCell);
				checkifPatchCell(cellPane, i, j, currentCell, rend);
				allowClickableCells(config, currentCell, updatedCell);
			}
		}
	}

	
	/**
	 * Check if its a cell in a simulation that has some sort of agent/object on top of the cell
	 */
	private void checkifPatchCell(Pane cellPane, int i, int j, Cell currentCell, Render rend) {
		if (currentCell.hasPatch()){
			Shape patchCell = rend.renderPatch(currentCell, xPos, yPos, i, j);
			cellPane.getChildren().add(patchCell);
		}
	}
	
	private void allowClickableCells(Configuration config, Cell currentCell, Shape updatedCell) {
		updatedCell.setOnMouseClicked(e -> {
			int len = config.getAllStates().getList().size();
			for (int i = 0; i < len; i++) {
				if (config.getAllStates().getList().get(i).getValue().equals(currentCell.getCurrentstate())) {
					currentCell.setCurrentstate(config.getAllStates().getList().get((i + 1) % len).getValue());
					break;
				}
			}
			String color = myConfig.getAllStates().getStateByName(currentCell.getCurrentstate()).getAttributes()
					.get("color");
			updatedCell.setFill(Color.web(color));
		});
	}

	/**
	 * Returns the neighbors of a shape. May need to change row/column deltas
	 * based on definition of 'neighbor' (diagonals or not) Returns the
	 * neighbors of a shape. May need to change row/column deltas based on
	 * definition of 'neighbor' (diagonals or not)
	 * 
	 * @param cell
	 *            - the shape
	 * @return - ArrayList<Cell> of cell's neighbors
	 */

	public List<Cell> getNeighbors(Cell cell, int vision) {
		List<Cell> neighbors = new ArrayList<>();
		int rowPos = cell.getRowPos();
		int colPos = cell.getColPos();
		for (int i = 0; i < getRowDeltas().length; i++) {
			if (vision > 1) {
				for (int j = 1; j <= vision; j++) {
					int newRowPos = rowPos + getRowDeltas()[i] * j;
					int newColPos = colPos + getColDeltas()[i] * j;
					getValidNeighbor(neighbors, newRowPos, newColPos);
				}
			} else {
				int newRowPos = rowPos + getRowDeltas()[i];
				int newColPos = colPos + getColDeltas()[i];
  				getValidNeighbor(neighbors, newRowPos, newColPos);
			}
		}
		return neighbors;
	}

	private void getValidNeighbor(List<Cell> neighbors, int newRowPos, int newColPos) {
		if (!rowOutOfBounds(newRowPos) && !colOutOfBounds(newColPos)) {
			neighbors.add(grid[newRowPos][newColPos]);
		} else if (isToroidal) {
			if (rowOutOfBounds(newRowPos)) {
				newRowPos = gridRowWrap(newRowPos);
			}
			if (colOutOfBounds(newColPos)) {
				newColPos = gridColWrap(newColPos);
			}
			neighbors.add(grid[newRowPos][newColPos]);
		}
	}

	private int gridRowWrap(int newRowPos) {
		int wrapRowPos;
		if (newRowPos < 0) {
			wrapRowPos = getNumRows() + newRowPos;
		} else {
			wrapRowPos = newRowPos - getNumRows();
		}
		return wrapRowPos;
	}

	private int gridColWrap(int newColPos) {
		int wrapColPos;
		if (newColPos < 0) {
			wrapColPos = getNumCols() + newColPos;
		} else {
			wrapColPos = newColPos - getNumCols();
		}
		return wrapColPos;
	}

	private boolean rowOutOfBounds(int row) {
		return row < 0 || row >= getNumRows();
	}

	private boolean colOutOfBounds(int col) {
		return col < 0 || col >= getNumCols();
	}

	public boolean isToroidal() {
		return isToroidal;
	}

	/**
	 * Save each cell to the configuration which then could be serialized
	 * 
	 * @return
	 */
	public Configuration save() {
		myConfig.getInitialCells().clear();
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				myConfig.getInitialCells().add(grid[i][j].serialize());
			}
		}
		return myConfig;
	}

	private void chooseRowDeltas() {
		if (myShape.equals("hexagon")) {
			rowDeltas = HEX_ROW_DELTAS;
			colDeltas = HEX_COL_DELTAS;
		} else if (myShape.equals("triangle")) {
			rowDeltas = TRI_ROW_DELTAS;
			colDeltas = TRI_COL_DELTAS;
		} else if (myShape.equals("rectangle")) {
			rowDeltas = RECT_ROW_DELTAS;
			colDeltas = RECT_COL_DELTAS;
		}
	}

	/**
	 * Sets the deltas that will be used to calculate the cells neighbors
	 * @param newRowDeltas
	 * @param newColDeltas
	 */
	public void setDeltas(int[] newRowDeltas, int[] newColDeltas) {
		rowDeltas = newRowDeltas;
		colDeltas = newColDeltas;
	}

	private int[] getRowDeltas() {
		return rowDeltas;
	}

	private int[] getColDeltas() {
		return colDeltas;
	}

	/**
	 * Load cellgrid from config
	 */
	public void load() {
		for (config.State s :getConfig().getInitialCells()) {
			int row = Integer.parseInt(s.getAttributes().get("row"));
			int col = Integer.parseInt(s.getAttributes().get("col"));
			Cell r = new Rectangle(row, col, getConfig());
			r.setCurrentstate(s.getAttributes().get("currentState"));
			r.setFuturestate(s.getAttributes().get("futureState"));
			setGridCell(row, col, r);
		}
	}
	
	public int getNumRows() {
		return grid.length;
	}

	public int getNumCols() {
		return grid[0].length;
	}

	public void setGridCell(int row, int col, Cell myCell) {
		grid[row][col] = myCell;
	}

	public Cell getGridCell(int row, int col) {
		return grid[row][col];
	}

	public Configuration getConfig() {
		return myConfig;
	}
	
	/**
	 * Used to get the distance between two locations
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public double distance(int x1, int y1, int x2, int y2){
		return Math.sqrt(1.0*((x1-x2)*(x1-x2) + (y2-y1)*(y2-y1)));
	}
	
	/**
	 * Updates grid based on specific simulation functionality
	 */
	public abstract void updateGrid();
	
	/**
	 * Updates specific cell based on simulation rules
	 * @param myCell cell to update
	 */
	public abstract void updateCell(Cell myCell);

	public abstract String getSimulationName();

	/**
	 * Begins specific simulation, creating grid and updating cells.
	 */
	public void initSimulation() {
		myShape = myConfig.getShape().toLowerCase();
		myWrappings = myConfig.getWrapping().toLowerCase();
		isToroidal = true;
		if (myWrappings.equals("finite")) {
			isToroidal = false;
		}
		chooseRowDeltas();
	}

}