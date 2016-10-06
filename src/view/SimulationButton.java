package view;
import javafx.scene.control.Button;

/**
 * The classic simulation button on the main view. 
 * Can add functionality to this class as needed
 * @author Jordan Frazier (jrf30)
 *
 */

public class SimulationButton extends Button {
	
	private String displayName;
	
	public SimulationButton(String displayName) {
		
		this.displayName = displayName;
		setDisplayName(this.displayName);	
		
	}
	
	private void setDisplayName(String displayName) {
		this.setText(displayName);
	}
	
	public String getDisplayName() {
		return displayName;
	}

}
