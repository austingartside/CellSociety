package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

/**
 * An enum of all simulations. 
 * Add simulations to this enum when new ones are implemented
 * @author Jordan Frazier
 *
 */
public enum Simulations {

	GAME_OF_LIFE("Game_Of_Life"), PREDATOR_PREY("Predator_Prey"), FIRE("Fire"), SEGREGATION("Segregation"), ANT(
			"Ant"), SUGAR("Sugar"),

	COMBOBOX;

	String name;
	ComboBox<String> SIMULATIONS;
	ComboBox<String> SHAPES;
	ComboBox<String> WRAPPINGS;

	Simulations() {
		ObservableList<String> simulationOptions = FXCollections.observableArrayList("Game_Of_Life", "Predator_Prey",
				"Fire", "Segregation", "Ant", "Sugar");
		SIMULATIONS = new ComboBox<>(simulationOptions);

		ObservableList<String> shapeOptions = FXCollections.observableArrayList("Rectangle", "Triangle", "Hexagon");
		SHAPES = new ComboBox<>(shapeOptions);
		SHAPES.setValue("Rectangle");

		ObservableList<String> wrappingOptions = FXCollections.observableArrayList("Finite", "Wrapping");
		WRAPPINGS = new ComboBox<>(wrappingOptions);
		WRAPPINGS.setValue("Finite");
	}

	Simulations(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public ComboBox<String> getSimulationComboBox() {
		return SIMULATIONS;
	}

	public ComboBox<String> getShapesComboBox() {
		return SHAPES;
	}

	public ComboBox<String> getWrappingsComboBox() {
		return WRAPPINGS;
	}
}
