package view;

import java.util.ResourceBundle;

import config.Configuration;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/** 
 * Creates the sliders on the MainView that are linked to dynamically updating
 * labels. 
 * Also updates the Configuration with its new values
 * @author Jordan Frazier (jrf30)
 *
 */

public class SimulationSlider {

	private HBox genericSliderContainer;
	private Slider slider;
	private VBox customSliderContainer;
	
	private ResourceBundle myResources;
	public static final String RESRC_PATH = "resources/GridResources";

	// Used in MainView to determine whether a slider needs to reset the grid on release
	public static boolean reset;
	
	public SimulationSlider(String text, Configuration config) {
		myResources = ResourceBundle.getBundle(RESRC_PATH);
		reset = false;

		double defaultValue = Double.parseDouble(config.getCustomParam(text));
		Label lbl = new Label(String.valueOf(defaultValue * 100));
		Label displayName = new Label(text);

		slider = new Slider(0, 1, defaultValue);
		customSliderContainer = new VBox(5);
		genericSliderContainer = new HBox(10);
		genericSliderContainer.getChildren().addAll(slider, lbl);
		customSliderContainer.getChildren().addAll(displayName, genericSliderContainer);

		updateSliderOnDrag(lbl, 100.0);

		slider.setOnMouseReleased(e -> {
			Double newValue = (double) Math.round(slider.getValue() * 100.0) / 100.0;
			lbl.textProperty().setValue(String.valueOf(newValue * 100.00));
			config.setCustomParam(text, String.valueOf(newValue));
			if (text.contains(myResources.getString("Percent"))) {
				reset = true;
			}
		});
	}

	public SimulationSlider(SceneConstant min, SceneConstant max, double defaultVal, String displayName, boolean set, Configuration config) {
		myResources = ResourceBundle.getBundle(RESRC_PATH);
		Label lbl = new Label(String.valueOf(defaultVal));
		Label display = new Label(displayName);

		reset = false;
		genericSliderContainer = new HBox(10);
		slider = new Slider(min.getValue(), max.getValue(), defaultVal);
		genericSliderContainer.getChildren().addAll(display, slider, lbl);

		updateSliderOnDrag(lbl, 10.0);
		
		if (displayName.contains(myResources.getString("Percent")) || displayName.contains(myResources.getString("Columns"))
				|| displayName.contains(myResources.getString("Rows"))) {
			slider.setOnMouseReleased(e -> {
				Double newValue = (double) Math.round(slider.getValue() * 10.0) / 10.0;
				int i = newValue.intValue();
				lbl.textProperty().setValue(String.valueOf(i));
				if (displayName.contains(myResources.getString("Rows"))) {
					config.setNumRows(i);
				} else if (displayName.contains(myResources.getString("Columns"))) {
					config.setNumCols(i);
				} else {
					lbl.textProperty().setValue(String.valueOf(newValue));
				}
				reset = true;
			});
		}

	}

	private void updateSliderOnDrag(Label lbl, double roundValue) {
		slider.valueProperty().addListener(e -> {
			Double newValue = (double) Math.round(slider.getValue() * roundValue) / roundValue;
			lbl.textProperty().setValue(String.valueOf(newValue));
		});
	}

	public HBox getGenericSlider() {
		return genericSliderContainer;
	}

	public VBox getCustomSlider() {
		return customSliderContainer;
	}

	public Slider getSlider() {
		return slider;
	}

	public void setReset(boolean reset) {
		SimulationSlider.reset = reset;
	}

}
