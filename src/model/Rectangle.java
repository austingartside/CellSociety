package model;

import config.Configuration;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import view.SceneConstant;

/**
 * A rectangle class
 * @author Jordan Frazier (jrf30)
 *
 */
public class Rectangle extends Cell {

	private int[] rowDeltas = { -1, 0, 1, 0, 1, 1, -1, -1 };
	private int[] colDeltas = { 0, -1, 0, 1, 1, -1, 1, -1 };
	private Configuration myConfig;

	public Rectangle(int row, int col, Configuration config) {
		super(row, col);
		myConfig = config;
	}

	@Deprecated
	@Override
	public Shape render() {
		double rows = myConfig.getNumRows();
		double cols = myConfig.getNumCols();
		double width = calculateSize(SceneConstant.GRID_WIDTH.getValue(), cols);
		double height = calculateSize(SceneConstant.GRID_HEIGHT.getValue(), rows);

		javafx.scene.shape.Rectangle rect = new javafx.scene.shape.Rectangle(width, height);

		String color = myConfig.getAllStates().getStateByName(getCurrentstate()).getAttributes()
				.get("color");
		rect.setFill(Color.web(color));
		return rect;
	}

	@Deprecated
	private double calculateSize(double edgeSize, double numSpots) {
		return (edgeSize / numSpots);
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
