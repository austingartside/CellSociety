//This entire file is part of my masterpiece
//Austin Gartside

package model;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import config.Configuration;
import view.Simulations;

/**
 * @author austingartside
 *
 * Creates and updates grid for game of life simulations, allows different neighbor conventions
 * Originally this code was very raw. It implemented the rules, but it was with many if and elfe statements and in very few
 * and large functions Additionally variable values and constants were hardcoded in and the grid was passed to the simulation.
 * Over the course of the project the code was changed so that the Grid implmenetation was hid in CellGrid, the String constants
 * were placed in a resoource file, and the starting states of the cells was determined by the XML. I think the code is 
 * well-designed because it is compact, hides unnecessary details in super classes, allows for neighbor choices (which it did
 * not do originally), has all functionality separated into different methods, and is not overly long. I worked hard to make
 * sure to only pass in List<> to functions or to return that instead of an array list since that is good design practice. I
 * extracted methods as much as possible so that the code is very modular and I avoid all magic numbers. I used inheritance
 * as much as possible to make the code more readable and flexible (inheritance meaning using the super class methods), so
 * it demonstrates the strength of the CellGrid class as well. Intersting design element is the neighbor convention selection. I
 * tried as much as possible to avoid duplicated code or unnecessary if and else statements. 
 */
public class GameOfLifeSimulation extends CellGrid {
	
	private static final String SIMULATION_NAME = Simulations.GAME_OF_LIFE.getName();
	private String DEAD;
	private String ALIVE;
	
	private String neighborConvention;
	private List<Integer> numsToSurvive;
	private List<Integer> numsToBeBorn;
	
	private static final int[] ROW_DELTAS = {-1, -1, 0, 1, 1, 1, 0, -1};
	private static final int[] COL_DELTAS = {0, -1, -1, -1, 0, 1, 1, 1};
		
	public GameOfLifeSimulation(Configuration config) {
		super(config);
		DEAD = myResources.getString("Dead");
		ALIVE = myResources.getString("Alive");
		//could change here to decide the neighbor convention
		neighborConvention = "B3 S23";
		getNeighborConvention();
	}
	
	
	
	/**
	 * based on the neighborConvention string of the format "B(numbers) S(numbers) makes the rules
	 * for number live neighbors to revive a cell and number of live neighbors to keep a cell alive
	 * Number(s) after B indicate number needed to rebirth a dead cell
	 * Number(s) after S indicate number of live neighbors to keep a cell alive
	 */
	private void getNeighborConvention(){
		String[] neighborConventionList = neighborConvention.split(" ");
		numsToSurvive = new ArrayList<Integer>();
		numsToBeBorn = new ArrayList<Integer>();
		if(neighborConventionList[0].length()>0){
			String numsForBirth = neighborConventionList[0].substring(1);
			for(int i = 0; i<numsForBirth.length(); i++){
				numsToBeBorn.add(Character.getNumericValue(numsForBirth.charAt(i)));
			}
		}
		if(neighborConventionList[1].length()>0){
			String numsForSurvival = neighborConventionList[1].substring(1);
			for(int i = 0; i<numsForSurvival.length(); i++){
				numsToSurvive.add(Character.getNumericValue(numsForSurvival.charAt(i)));
			}
		}
	}
	
	public void initSimulation() {
		super.initSimulation();
		double percentDead = Double.parseDouble(getConfig().getCustomParam("percentDead"));
		setDeltas(ROW_DELTAS, COL_DELTAS);
		createGrid(percentDead);
	}
	
	
	public void createGrid(double percentDead){
		Random generator = new Random();
		List<String> initialization = getStartingStateList(percentDead);
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				setGridCell(i, j, new Rectangle(i, j, getConfig()));
				if(initialization.size() == 0){
					getGridCell(i, j).setCurrentstate(DEAD);
				}
				else{
					int cellChoice = generator.nextInt(initialization.size());
					getGridCell(i, j).setCurrentstate(initialization.get(cellChoice));
					initialization.remove(cellChoice);
				}	
			}
		}
	}

	private List<String> getStartingStateList(double percentDead) {
		int size = getNumRows()*getNumCols();
		double numDead = percentDead*size;
		double numAlive = size-numDead;
		List<String> initialization = new ArrayList<String>();
		for(int i = 0; i<numDead; i++){
			initialization.add(DEAD);
		}
		for(int i = 0; i<numAlive; i++){
			initialization.add(ALIVE);
		}
		return initialization;
	}
	
	private void updateFutureStates(){
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				updateCell(getGridCell(i, j));
			}
		}
	}
	
	@Override
	public void updateGrid(){
		updateFutureStates();
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				Cell currentCell = getGridCell(i, j);
				currentCell.setCurrentstate(currentCell.getFuturestate());
			}
		}
	}
	
	@Override
	public void updateCell(Cell myCell){
		String myState = myCell.getCurrentstate();
		List<Cell> currentNeighbors = getNeighbors(myCell, 1);
		int liveCount = countCellsOfState(currentNeighbors, ALIVE);
		if(myState.equals(DEAD)){
			deadCellUpdate(myCell, liveCount);	
		}
		else{
			liveCellUpdate(myCell, liveCount);
		}
	}
	
	private void liveCellUpdate(Cell myCell, int liveCount){
		myCell.setFuturestate(DEAD);
		if(numsToSurvive.contains(liveCount)){
			myCell.setFuturestate(ALIVE);
		}
	}

	private void deadCellUpdate(Cell myCell, int liveCount){
		myCell.setFuturestate(DEAD);
		if(numsToBeBorn.contains(liveCount)){
			myCell.setFuturestate(ALIVE);
		}
	}
	
	private int countCellsOfState(List<Cell> currentNeighbors, String state){
		int stateCount = 0;
		for(Cell neighborCell: currentNeighbors){
			if(neighborCell.getCurrentstate().equals(state)){
				stateCount++;
			}
		}
		return stateCount;
	}

	@Override
	public String getSimulationName() {
		return SIMULATION_NAME;
	}
}
