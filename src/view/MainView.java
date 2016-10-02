package view;

import java.util.ResourceBundle;

import config.Configuration;
import exceptions.MalformedXMLSourceException;
import exceptions.QueryExpressionException;
import exceptions.UnrecognizedQueryMethodException;
import exceptions.XMLParserException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import model.CellGrid;
import model.FireSimulation;
import model.GameOfLifeSimulation;
import model.PredatorPreySimulation;
import model.SegregationSimulation;

/**
 * 
 * @author Jordan Frazier (jrf30)
 *
 */
public class MainView {

	private Scene myScene;
	private Group myRoot;
	private GridPane myCellPane;
	private CellGrid mySimulation;
	private Timeline myGameloop;
	private Configuration myConfig;

	private boolean isGridLinesVisible;
	private boolean simIsRunning;

	private ResourceBundle myResources;
	public static final String RESRC_PATH = "resources/GridResources";

	private Insets buttonPadding = new Insets((SceneConstant.SCENE_HEIGHT.getValue() - SceneConstant.GRID_HEIGHT.getValue()) / 2, SceneConstant.GRID_PADDING.getValue(),
			(SceneConstant.SCENE_HEIGHT.getValue() - SceneConstant.GRID_HEIGHT.getValue()) / 2, -1 * SceneConstant.GRID_PADDING.getValue());
	private Insets cellPanePadding = new Insets((SceneConstant.SCENE_HEIGHT.getValue() - SceneConstant.GRID_HEIGHT.getValue()) / 2, 0, (SceneConstant.SCENE_HEIGHT.getValue() - SceneConstant.GRID_HEIGHT.getValue()) / 2,
			SceneConstant.GRID_PADDING.getValue());
	
	// TODO: Move these
	private static final ObservableList<String> SIMULATION_OPTIONS = FXCollections.observableArrayList(
			"Game_Of_Life",
			"Predator_Prey",
			"Fire",
			"Segregation");

	public static final ComboBox<String> SIMULATIONS = new ComboBox<>(SIMULATION_OPTIONS);

	public Scene initScene() throws Exception {
		myRoot = new Group();
		myScene = new Scene(myRoot, SceneConstant.SCENE_WIDTH.getValue(), SceneConstant.SCENE_HEIGHT.getValue());
		myResources = ResourceBundle.getBundle(RESRC_PATH);
		// isGridLinesVisible = false;
		beginInitialSetup();
  
		return myScene;
	}

	private void beginInitialSetup() throws NumberFormatException, MalformedXMLSourceException, XMLParserException, UnrecognizedQueryMethodException, QueryExpressionException {
		myConfig = new Configuration("Fire.xml");
		initSimulation();
		setSimulationEventHandler();
		createResetTimelineChecker();
		createGameLoop();
	}

	private void initSimulation() throws NumberFormatException, MalformedXMLSourceException, XMLParserException, UnrecognizedQueryMethodException, QueryExpressionException {
		myRoot.getChildren().removeAll(myRoot.getChildren());
		createCellPane();
		createSimulation();
		createAllButtons();
	}

	private void createResetTimelineChecker() {
		Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames()
				.add(new KeyFrame(Duration.millis(Double.parseDouble(myResources.getString("MaxFPS"))), e -> {
					if (SimulationSlider.reset) {
						try {
							resetGrid();
							// TODO: Handle each error based on exception type
						} catch (NumberFormatException e1) {
							e1.printStackTrace();
						} catch (MalformedXMLSourceException e1) {
							e1.printStackTrace();
						} catch (XMLParserException e1) {
							e1.printStackTrace();
						} catch (UnrecognizedQueryMethodException e1) {
							// when user tries to parse with a different query method
							e1.printStackTrace();
						} catch (QueryExpressionException e1) {
							e1.printStackTrace();
						}
					}
				}));
		timeline.play();
	}

	private void resetGrid() throws NumberFormatException, MalformedXMLSourceException, XMLParserException, UnrecognizedQueryMethodException, QueryExpressionException {
//		myConfig = new Configuration(SIMULATIONS.getValue() + ".xml");
		initSimulation();
		SimulationSlider.reset = false;
		myGameloop.pause();
		simIsRunning = false;
	}

	private void createSimulation() {
//		Configuration config = new Configuration("Fire_Simulation.xml");
		// findSimulation(config.getSimulationName());
		findSimulation();
		mySimulation.initSimulation();
		mySimulation.renderGrid(myCellPane);
	}

	private void findSimulation() {
		if (myConfig.getSimulationName().equals(Simulations.FIRE.getName())) {
			mySimulation = new FireSimulation(myConfig);
		} else if (myConfig.getSimulationName().equals(Simulations.GAME_OF_LIFE.getName())) {
			mySimulation = new GameOfLifeSimulation(myConfig);
		} else if (myConfig.getSimulationName().equals(Simulations.SEGREGATION.getName())) {
			mySimulation = new SegregationSimulation(myConfig);
		} else if (myConfig.getSimulationName().equals(Simulations.PREDATOR_PREY.getName())) {
			mySimulation = new PredatorPreySimulation(myConfig);
		}
	}

	private void createGameLoop() {
		myGameloop = new Timeline();
		myGameloop.getKeyFrames().add(new KeyFrame(Duration.seconds(1), e -> {
			updateGrid();
		}));
	}

	private void updateGrid() {
		myCellPane.getChildren().removeAll(myCellPane.getChildren());
		mySimulation.updateGrid();
		mySimulation.renderGrid(myCellPane);
	}

	private void createAllButtons() {
		
		VBox buttonContainer = new VBox(SceneConstant.PADDING.getValue());

		HBox hbox1 = new HBox(SceneConstant.PADDING.getValue());
		HBox hbox2 = new HBox(SceneConstant.PADDING.getValue());
		VBox vbox3 = new VBox(SceneConstant.PADDING.getValue());

		SimulationButton playBtn = makeButton("Play", new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				playButtonHandler();
			}
		});

		SimulationButton pauseBtn = makeButton("Pause", new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				pauseButtonHandler();
			}
		});

		SimulationButton resetBtn = makeButton("Reset", new  EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				resetButtonHandler();
			}
		});

		SimulationButton stepBtn = makeButton("Step", new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				stepButtonHandler();
			}
		});
		
		SimulationSlider rowsSlider = new SimulationSlider(SceneConstant.SLIDER_MINIMUM, SceneConstant.SLIDER_MAXIMUM, myConfig.getNumRows(),
				myResources.getString("Rows"), false);
		setRowAndColEventHandler(rowsSlider.getSlider(), true);

		SimulationSlider colsSlider = new SimulationSlider(SceneConstant.SLIDER_MINIMUM, SceneConstant.SLIDER_MAXIMUM, myConfig.getNumCols(),
				myResources.getString("Columns"), false);
		setRowAndColEventHandler(colsSlider.getSlider(), false);

		SimulationSlider fpsSlider = new SimulationSlider(SceneConstant.SLIDER_MINIMUM, SceneConstant.SLIDER_MAXIMUM, myConfig.getFramesPerSec(),
				myResources.getString("FPS"), false);
		setFPSEventHandler(fpsSlider.getSlider());

		// TODO: Grid lines
		SimulationButton gridVisible = new SimulationButton(myResources.getString("GridLines"));
		// setGridLinesVisible(gridVisible);

		hbox1.getChildren().addAll(playBtn, pauseBtn);
		hbox2.getChildren().addAll(stepBtn, resetBtn);
		vbox3.getChildren().addAll(rowsSlider.getGenericSlider(), colsSlider.getGenericSlider());

		VBox basicBtnBox = new VBox(SceneConstant.PADDING.getValue());
		basicBtnBox.getChildren().addAll(SIMULATIONS, hbox1, hbox2, vbox3, fpsSlider.getGenericSlider(), gridVisible);
		basicBtnBox.setMinWidth(300);
		buttonContainer.getChildren().add(basicBtnBox);

		createCustomButtons(buttonContainer);
		setButtonContainerParameters(buttonContainer);
	}

	/**
	 * Creates button and sets an event handler
	 * 
	 * @param name - name of the button
	 * @param handler - the event handler of the button
	 * @return
	 */
	private SimulationButton makeButton(String name, EventHandler<ActionEvent> handler) {
		SimulationButton button = new SimulationButton(myResources.getString(name));
		setDimensions(button);
		button.setOnAction(handler);
		return button;
	}
	
	private void playButtonHandler() {
		simIsRunning = true;
		myGameloop.setCycleCount(Timeline.INDEFINITE);
		myGameloop.playFromStart();
	}

	private void stepButtonHandler() {
		pauseGrid();
		updateGrid();
	}

	private void pauseButtonHandler() {
		pauseGrid();
	}

	private void resetButtonHandler() {
		try {
			initSimulation();
		} catch (Exception e) {
			throw new NullPointerException("Unable to init simulation");
		}
		pauseGrid();
	}
	
	private void pauseGrid() {
		myGameloop.pause();
		simIsRunning = false;
	}


	private void setButtonContainerParameters(VBox buttonContainer) {
		buttonContainer.setPadding(buttonPadding);
		buttonContainer.setMaxWidth(SceneConstant.BUTTON_CONTAINER_WIDTH.getValue());
		buttonContainer.setMinWidth(SceneConstant.BUTTON_CONTAINER_WIDTH.getValue());
		buttonContainer.setLayoutX(Integer.parseInt(myResources.getString("SceneWidth")) - SceneConstant.BUTTON_CONTAINER_WIDTH.getValue());
		myRoot.getChildren().add(buttonContainer);
	}

	private void createCustomButtons(VBox buttonContainer) {
		VBox custom = new VBox(SceneConstant.PADDING.getValue());
		 for (String str : myConfig.getAllCustomParamNames()) {
			SimulationSlider slider = new SimulationSlider(str, myConfig);
			custom.getChildren().add(slider.getCustomSlider());
		}
		buttonContainer.getChildren().add(custom);
	}

	private void setRowAndColEventHandler(Slider sizeSlider, boolean isRow) {
		sizeSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				myGameloop.pause();
				int newval = (int) ((double) newValue);
				if (isRow) {
					myConfig.setNumRows(newval);
				} else {
					myConfig.setNumCols(newval);
				}
			}
		});
	}

	private void setFPSEventHandler(Slider fpsSlider) {
		fpsSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				double fpsdouble = 1000.0 / (double) newValue;
				myConfig.setFramesPerSec((int) fpsdouble);
				myGameloop.pause();
				myGameloop.getKeyFrames().remove(0);
				myGameloop.getKeyFrames().add(new KeyFrame(Duration.millis(fpsdouble), e -> {
					updateGrid();
				}));
				if (simIsRunning) {
					myGameloop.playFromStart();
				}
			}
		});
	}

	private void setSimulationEventHandler() {
		SIMULATIONS.setValue(myConfig.getSimulationName());
		SIMULATIONS.setMinWidth(SceneConstant.BUTTON_CONTAINER_WIDTH.getValue() + SceneConstant.PADDING.getValue());
		SIMULATIONS.setMaxWidth(SceneConstant.BUTTON_CONTAINER_WIDTH.getValue() + SceneConstant.PADDING.getValue());
		SIMULATIONS.valueProperty().addListener(e -> {
			myGameloop.stop();
			myRoot.getChildren().removeAll(myRoot.getChildren());
//			try {
				try {
					myConfig = new Configuration(SIMULATIONS.getValue() + ".xml");
					initSimulation();
				} catch (NumberFormatException | MalformedXMLSourceException | XMLParserException
						| UnrecognizedQueryMethodException | QueryExpressionException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//			} catch (Exception e1) {
//				throw new NullPointerException("Unable to load simulation");
//			}
		});
	}


	// private void setGridLinesVisible(SimulationButton btn) {
	// setDimensions(btn);
	// btn.setOnAction(e -> {
	// if (!gridLinesVisible) {
	// setGridLines(true);
	// } else {
	// setGridLines(false);
	// }
	// });
	// }
	//
	// private void setGridLines(boolean isVisible) {
	// if (isVisible) {
	// cellPane.setGridLinesVisible(true);
	// gridLinesVisible = true;
	// } else {
	// cellPane.setGridLinesVisible(false);
	// gridLinesVisible = false;
	// }
	// }c

	private void setDimensions(SimulationButton btn) {
		btn.setMinWidth(SceneConstant.BUTTON_CONTAINER_WIDTH.getValue() / 2);
		btn.setMaxWidth(SceneConstant.BUTTON_CONTAINER_WIDTH.getValue() / 2);
	}

	/**
	 * Creates a new GridPane for your simulation and adds it to the root
	 */
	private void createCellPane() {
		myCellPane = new GridPane();
		myCellPane.setMaxWidth(SceneConstant.GRID_WIDTH.getValue() + SceneConstant.GRID_PADDING.getValue());
		// cellPane.setMinWidth(GRID_WIDTH + GRID_PADDING);
		myCellPane.setMaxWidth(SceneConstant.GRID_WIDTH.getValue() + SceneConstant.GRID_PADDING.getValue());
		myCellPane.setMinWidth(SceneConstant.GRID_WIDTH.getValue() + SceneConstant.GRID_PADDING.getValue());

		myCellPane.setMaxHeight(SceneConstant.GRID_HEIGHT.getValue());
		myCellPane.setMinHeight(SceneConstant.GRID_HEIGHT.getValue());
		
		myRoot.getChildren().add(myCellPane);
		// cellPane.setPrefWrapLength(50);
		// cellPane.setStyle("-fx-border-color: black; -fx-border-width: 2;");
	}

}