Introduction
============
We would like to implement a program that is able to simulate any types of Cellular Automaton (CA) regardless of its state transition functions, list of states, number of neighbors, etc. Definitions as such pertaining to one particular CA problem are described in an XML file. Our implementation should be flexible enough such that it renders and animates accordingly given a specific XML. 

Overview
========
![Alt text](Overview.png?raw=true "Overview.png")

 - XML 
	 - Parser Picks the simulations and other specifications for the UI
 - UI
	 - Display Class
		 - displayGrid()
	 - Different Classes for different
   buttons - extending interface custombutton
	   - Reset
	   - Play
	   - Pause
	   - Step
	   - Pick simulation 
 - Backend
	- Cell
		- Current and previous state
		- Getters and
   setters
   - Model Class (extending gridpane)
	   - Display grid
  - Abstract class for each of the different simulation classes to extend
	  - getNeighbors()
		  - Use an ArrayList possibly because we don’t know how many neighbors there would be
		- Different methods for counting number of neighbors in a specific state
		- updateSelf()
		- updateNeighbors()
		- updateGrid()
		
User Interface
========
![Alt text](UI.png?raw=true "UI.png")

Include dynamic updating, should be able to change the number of grids for a specified state, also resize (use sliders), also have to be able to adjust based on the number of states
Have something to check that there is data everywhere there needs to be and the data is of the right type.

Design Details
========
The Parser takes in an XML source file and populates the CA model with settings from the XML. The XML also defines the initial states of the grid and the rules for state transition for each step. The grid gets rendered to scene, which is installed with several event handler used to update the grid. 

On the backend there’s going to be an abstract class that acts more as an interface for each of the simulations. Each simulation will have its own class because the shapes, probabilities, relationships, and states could all vary from simulation to simulation. In general, each simulation will have a way of finding neighbors and updating those neighbors, but we want our program to be as flexible as possible (not accept only squares), so that method itself could vary for each simulation. Each of the simulations will be communicating with the UI (the display) because that’s where the updates will be visualized. Additionally the UI will be passing information about different probabilities or specifications that will act as parameters in the simulation classes.  Additionally, the display will have different buttons, which will have classes that will communicate back to the simulations because conditions might be updated or a different simulation might be chosen. In this way it will be easy to add new simulations with new specifications because we just create new classes that have similar needs ot the other subclasses. One class that is used in all of the simulation classes is the Cell class.This only contains current and future states, as well getters and setter. This needs to be as generic as possible because we don’t know exactly what the cell will look like. We only know that it will have some state that is capable of being changed in some way. This allows us to have cells of differing shape and position because we handle actual updating in the simulation classes.

###Use Cases

 - In these cases we are considering the game of life, so we would have
   a class called gameOfLifeSim that would have these rules reflected in
   the methods. Using the get Neighbors method, we will obtain a list of
   all the neighbors. We have methods to count how many of the neighbors
   are in each state, so we would use those as well. Finally we would
   have updateNeighbors and update Self to update the cell. We would be
   able to do this for every cell because even after we change the
   state, we would store that as the future state and run our methods on
   every cell, and not actually update current state until the end. 
 - Handled exactly as in the first case, just with possible different
   rules that would be built into the method.
 - Updating the cells and displaying them graphically starts with the backend. Assuming we’ve
   done the process described in 1 and 2, we now have a current and
   future state for each cell. Having update everything we update, the
   current state to be what the future state was, and then communicate
   with the UI that it can go through and display the result for each
   cell.
 - The parser will grab information from the XML file including
   the probability of the fire. This will be passed to the appropriate
   simulation in the backend immediately.
 - The GUI will have a drop down
   menu with all of the possible simulations, so when you select a
   simulation, that is communicated with the backend. An instance of
   that specific simulation is created and the process begins. The UI is
   in charge of displaying the repeated updates.


Design Considerations
========
 - Creating a new grid on each cycle or updating the current one
	 - Creating a new grid each time will make it easier to input the states without worrying about overwriting a previous cell out of order, and you can immediately update by placing the new grid on top of the old one
	 - Since it’s not necessary to create a new grid if you implement the proper checks, the data creation would be a waste of resources, therefore decision is to update the current grid 
 - Having a superclass for every new simulation to extend 
	 - Hard to assume that the algorithms will have enough in common to justify creating a superclass
	 - An interface defining very basic methods might be better
		 - getNeighbors()
		 - updateCell()
		 - updateGrid()
		 - updateNeighbors()
 - Slider vs Input box for parameters
	 - A user should be able to dynamically change the parameters of a simulation, such as percent of neighbors needed to justify an update to the current cell
	 - An input box would need to handle bad input
	 - A slider class is available in JavaFX library, therefore easy to implement
	 - Each simulation will need to create custom button layout, as one simulation may have 4 possible states for a cell, and another just 2. For example, in Schelling’s Model of Segregation, there is a slider that allows the user to specify the starting ratio of red vs blue cells. A custom slider would be created for the 4 state simulation. 
	 - Slider seems more efficient to implement thanks to its built in functions
 - How to keep track of cell’s neighbors
	 - Using a dynamically sizing ArrayList<Cell> vs. an array[] of set value based on the simulation rules
	 - The array[] may be faster for updating, but cannot change size to account for cells on the corners and edges of grid. Those cells would need to be custom created, adding an extra layer of complexity that ArrayList would not need to worry about.
	 - ArrayList is the final decision based on its flexibility for sizes
- Assumptions:
	 - Each cell is an identical shape that fits together on a plane, such as all squares or all triangles. No simulation has cells of different shapes or sizes. 
	 - The XML file is written and formatted by our team, as our program will read the file in a specific way to get the parameters for the simulation
	 - The grid is of finite length

Team Responsibilities
========
Charles: XML and Backend
Austin: General Backend
Jordan: Frontend


### Adding new features:
Add to UI:
* All UI changes should be added or implemented inside of MainView.java. Rendering new shapes can be implemented in Render.java. 
At the moment, to implement a new shape, one does not need to add a new shape class. It is enough to define a new set of 
neighbors (with row / column deltas) inside of CellGrid.java, and calculations to render the new shape in Render.java.
* To add a new simulation and be able to switch to it from the main view, simply add the simulation name into the resource file,
then add that onto the MainView.findSimulation() method. 
* New button functionality needs to be added to MainView.createAllButtons(), and then the event handlers implemented in a separate class.
In the future, I would like to encapsulate all button functionality into another class to increase readability and ensure that
each class is doing as little as possible to function correctly

Add Simulation:

Add XMl?: 



