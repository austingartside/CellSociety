package model;

import config.Configuration;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import view.SceneConstant;


/**
 * Defines the calculations to render different shapes. 
 * To add new shapes, define the shape's calculations here. 
 * Used by CellGrid.java to render specified shapes onto the UI. 
 * @author Jordan Frazier
 *
 */
public class Render {

	private Configuration myConfig;
	private int isEven;

	private String[] colorList = { "#f6edd6", "#f4e5c0", "#f4dea9", "#f4d790", "#e7c675", "#ceaa55", "#b8943d",
			"#a58026", "#967013", "#7e5900", "#9c5f00" };
	
	private double rows;
	private double cols;
	private double width;
	private double height;
	
	public Render(Configuration config) {
		myConfig = config;
		rows = myConfig.getNumRows();
		cols = myConfig.getNumCols();
		width = calculateSize(SceneConstant.GRID_WIDTH.getValue(), cols);
		height = calculateSize(SceneConstant.GRID_HEIGHT.getValue(), rows);
	}

	/**
	 * Chooses which shape to render
	 * Add to this method when new shapes are implemented
	 * @param cell
	 * @param shape
	 * @param isEven
	 * @param xPos
	 * @param yPos
	 * @param yIndex
	 * @param xIndex
	 * @return
	 */
	public Shape chooseRender(Cell cell, String shape, int isEven, double xPos, double yPos, int yIndex, int xIndex) {
		this.isEven = isEven;
		Shape renderedShape = null;
		if (shape.equals("rectangle")) {
			renderedShape = renderRectangle(cell, xPos, yPos, yIndex, xIndex);
		} else if (shape.equals("triangle")) {
			renderedShape = renderTriangle(cell, xPos, yPos, yIndex, xIndex);
		} else if (shape.equals("hexagon")) {
			renderedShape = renderHexagon(cell, xPos, yPos, yIndex, xIndex);
		} else {
			// new shapes go here
		}
		return renderedShape;
	}

	private Shape renderRectangle(Cell cell, double xPos, double yPos, int yIndex, int xIndex) {
		Shape rect = new javafx.scene.shape.Rectangle(xPos + (width * xIndex), yPos + (height * yIndex), width, height);
		String color = myConfig.getAllStates().getStateByName(cell.getCurrentstate()).getAttributes().get("color");
		if (cell.isSugarCell()) {
			color = setSugarColor(cell, color);
		}
		if (cell.isAntCell() && !isSource(cell)) {
			color = setAntColor(cell, color);
		}
		rect.setFill(Color.web(color));
		return rect;
	}

	private Shape renderTriangle(Cell cell, double xPos, double yPos, int yIndex, int xIndex) {
		Polygon triangle = new Polygon();
		if (cols % 2 == 0) {
			cols--;
			myConfig.setNumCols((int)cols);
		}
		width=width*2;
		String color = myConfig.getAllStates().getStateByName(cell.getCurrentstate()).getAttributes().get("color");
		if (cell.isSugarCell()) {
			color = setSugarColor(cell, color);
		}
		if(cell.isAntCell() && !isSource(cell)){
			color = setAntColor(cell, color);
		}
		triangle.setFill(Color.web(color));
		Double[] upsideDownTrianglePoints = {xPos + (xIndex*width)/2 - width/2, yPos + (yIndex*height) - height/2, xPos + (xIndex*width)/2 + width/2, yPos + (yIndex*height) - height/2, xPos + (xIndex*width)/2, yPos + (yIndex*height) + height/2};
		Double[] trianglePoints = {xPos + (xIndex*width)/2 - width/2, yPos + (yIndex*height) + height/2, xPos + (xIndex*width)/2 + width/2, yPos + (yIndex*height) + height/2, xPos + (xIndex*width)/2, yPos + (yIndex*height) - height/2};

		if (isEven % 2 == 0) {
			triangle.getPoints().addAll(trianglePoints);
		} else {
			triangle.getPoints().addAll(upsideDownTrianglePoints);
		}
		
		return triangle;
	}

	private Shape renderHexagon(Cell cell, double xPos, double yPos, int yIndex, int xIndex) {
		Polygon hexagon = new Polygon();
		double[] center = new double[2];
		if (isEven % 2 == 0) {
			center[0] = xPos + width * xIndex;
			center[1] = yPos + height * yIndex;
		} else {
			center[0] = xPos + width * xIndex;
			center[1] = (yPos + height * yIndex) + height/2;
		}

		for (int k = 0; k < 6; k++) {
			Double[] a = getPoint(center, width / 2, k);
			hexagon.getPoints().addAll(a[0], a[1]);
		}
		String color = myConfig.getAllStates().getStateByName(cell.getCurrentstate()).getAttributes().get("color");
		if (cell.isSugarCell()) {
			color = setSugarColor(cell, color);
		}
		if(cell.isAntCell() && !isSource(cell)){
			color = setAntColor(cell, color);
		}
		hexagon.setFill(Color.web(color));
		return hexagon;
	}

	private Double[] getPoint(double[] center, double size, double i) {
		Double[] a = new Double[2];
		double angle_deg = 60 * i;
		double angle_rad = Math.PI / 180 * angle_deg;
		a[0] = center[0] + size * Math.cos(angle_rad);
		a[1] = center[1] + size * Math.sin(angle_rad);
		return a;
	}

	@Deprecated
	private Shape renderOffset(double width) {
		Line offset = new Line(0.0, width / 4, width / 2, width / 4);
		offset.setFill(Color.WHITE);
		return offset;
	}

	private String setSugarColor(Cell cell, String color) {
		int sugarLevel = ((SugarAgent) cell).getPatch().getSugar();
		color = colorList[sugarLevel - 1];
		return color;
	}

	private String setAntColor(Cell cell, String color) {
		int antLevel = ((AntCell) cell).getAnts().size();
		color = colorList[antLevel];
		return color;
	}

	private boolean isSource(Cell cell) {
		return cell.getCurrentstate().equals("home") || cell.getCurrentstate().equals("food")
				|| cell.getCurrentstate().equals("obstacle");
	}

	/**
	 * Renders the 'patch' shape used in sugar and ant simulations
	 * @param cell
	 * @param xPos
	 * @param yPos
	 * @param yIndex
	 * @param xIndex
	 * @return
	 */
	public Shape renderPatch(Cell cell, double xPos, double yPos, int yIndex, int xIndex) {
		double radiusX = calculateSize(SceneConstant.GRID_WIDTH.getValue(), cols) / 5;
		double radiusY = calculateSize(SceneConstant.GRID_HEIGHT.getValue(), rows) / 5;

		Shape ellipse = new Ellipse(xPos + (width * xIndex) + width/2, yPos + (height * yIndex) + height/2, radiusX, radiusY);
		String color = myConfig.getAllStates().getStateByName(cell.getCurrentstate()).getAttributes().get("color");
		ellipse.setFill(Color.web(color));

		return ellipse;

	}

	private double calculateSize(double edgeSize, double numSpots) {
		return (edgeSize / numSpots);
	}

}
