package view;

import config.Configuration;
import config.ConfigurationLoader;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.CellGrid;
import model.GameOfLifeSimulation;

public class MainView extends Application implements GameWorld {

	private Stage stage;
	private Scene scene;
	private BorderPane root;
	private FlowPane cellPane;
	private Configuration config;
	private CellGrid simulation;
	
	private static final double BUTTON_WIDTH = 200;

	private Timeline gameloop;
	
	Insets buttonPadding = new Insets((SCENE_HEIGHT - GRID_HEIGHT) / 2, SCENE_WIDTH / 40, (SCENE_HEIGHT - GRID_HEIGHT) / 2, 0);
	Insets cellPanePadding = new Insets((SCENE_HEIGHT - GRID_HEIGHT) / 2, 0, (SCENE_HEIGHT - GRID_HEIGHT) / 2, GRID_PADDING);

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		// Do configuration loader to get the information for scene / etc.
		// config =
		// ConfigurationLoader.loader().setSource("testxml.xml").load().getConfig();
		root = new BorderPane();
		scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
		// scene = new Scene(root, config.getSceneWidth(),
		// config.getSceneHeight());
		stage = primaryStage;

		// Creates initial cell grid
		createCellPane();

		// Creates initial simulation
		simulation = createSimulation();

		// add the buttons
		createAllButtons();

		// create game loop
		createGameLoop(simulation);

		stage.setScene(scene);
		stage.show();

	}

	private CellGrid createSimulation() {
		// TODO: Jordan - simulation type, rows, and columns should come from
		// XML
		// config = ConfigurationLoader.loader().getConfig();
		// double rows = config.getNumRows();
		// double cols = config.getNumCols();
		// String simulation = config.getSimulationName();

		// Going to have to loop through list of CellGrid simulations to
		// determine which one to use?
		// how to convert from String to actual simulation?

		simulation = new GameOfLifeSimulation(10, 5);
		simulation.updateGrid();
		simulation.renderGrid(cellPane);
		cellPane.setPadding(cellPanePadding);
		root.setLeft(cellPane);
		return simulation;
	}

	private void createGameLoop(CellGrid a) {
		// TODO: Duration should come from XML frames/sec
		gameloop = new Timeline();
		gameloop.getKeyFrames().add(new KeyFrame(Duration.seconds(1), e -> {
			cellPane.getChildren().removeAll(cellPane.getChildren());
			a.updateGrid();
			a.renderGrid(cellPane);
			root.setLeft(cellPane);
			System.out.println(a.getGrid()[0][0].getCurrentstate());
		}));
	}

	public void createAllButtons() throws Exception {
		VBox buttonContainer = new VBox(PADDING);

		// Sets simulation combo box action
		setSimulationEventHandler();

		// TODO: Jordan - set padding on buttons and button size from XML
		HBox hbox1 = new HBox(PADDING);
		HBox hbox2 = new HBox(PADDING);

		SimulationButton playBtn = new SimulationButton(PLAY);
		setStartEventHandler(playBtn);

		// SimulationButton resumeBtn = new SimulationButton(RESUME);
		// setStartEventHandler(resumeBtn);

		SimulationButton pauseBtn = new SimulationButton(PAUSE);
		setStopEventHandler(pauseBtn);
		hbox1.getChildren().addAll(playBtn, pauseBtn);

		SimulationButton resetBtn = new SimulationButton(RESET);
		setStopEventHandler(resetBtn);

		SimulationButton stepBtn = new SimulationButton(STEP);
		setStepEventHandler(stepBtn);
		hbox2.getChildren().addAll(stepBtn, resetBtn);

		VBox basicBtnBox = new VBox(PADDING);
		basicBtnBox.getChildren().addAll(SIMULATIONS, hbox1, hbox2);
		VBox additionalSliders = new VBox(PADDING);

		// loop through the rest of the things needed from config.getShit,
		// create necessary sliders
		// for (String str : config.getAllCustomParamNames()) {
		// SimulationSlider slider = new SimulationSlider(str);
		// additionalSliders.getChildren().add(slider);
		// }

		buttonContainer.getChildren().addAll(basicBtnBox, additionalSliders);
		// Right inset will be the same padding used on the left side of grid
		buttonContainer.setPadding(buttonPadding);
		buttonContainer.setMaxWidth(140);
		root.setRight(buttonContainer);

	}

	private void setSimulationEventHandler() {
		SIMULATIONS.setValue("Simulations");
		SIMULATIONS.setMinWidth(BUTTON_WIDTH + PADDING);
		SIMULATIONS.setMaxWidth(BUTTON_WIDTH + PADDING);
		SIMULATIONS.valueProperty().addListener(e -> {
			gameloop.pause();
			// TODO: Simulation box changes
			// Needs to stop simulation, change XML to whatever simulation we
			// want,
			// create the new grid and buttons, then wait for 'play' action

			try {
				config = ConfigurationLoader.loader().setSource("testxml.xml").load().getConfig();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		});
	}

	private void setStartEventHandler(SimulationButton btn) {
		setDimensions(btn);
		btn.setOnAction(e -> {
			gameloop.setCycleCount(Timeline.INDEFINITE);
			gameloop.playFromStart();
			// config.setRunning(true);
		});
	}

	private void setStepEventHandler(SimulationButton btn) {
		setDimensions(btn);
		btn.setOnAction(e -> {
			gameloop.pause();
			gameloop.setCycleCount(1);
			gameloop.playFromStart();
			gameloop.setOnFinished(event -> {
				gameloop.pause();
			});
		});
	}

	private void setStopEventHandler(SimulationButton btn) {
		setDimensions(btn);
		btn.setOnAction(e -> {
			// If button is reset, then reset parameters back to what's on XML
			if (btn.getDisplayName().equals(RESET)) {
				try {
					config = ConfigurationLoader.loader().setSource("testxml.xml").load().getConfig();
				} catch (Exception e1) {
					e1.printStackTrace();
					throw new IllegalArgumentException("Failed loading XML file");
				}
			}
			gameloop.pause();
			// config.setRunning(false);
		});
	}
	
	private void setDimensions(SimulationButton btn) {
		btn.setMinWidth(BUTTON_WIDTH / 2);
		btn.setMaxWidth(BUTTON_WIDTH / 2);
	}

	private void createCellPane() {
		// TODO: Set cellpane parameters based on XML
		cellPane = new FlowPane();

		// cellPane.setPrefWidth(config.getGridWidth());
		// cellPane.setPrefHeight(config.getGridHeight());
		
		//Have to add whatever padding you add on to the left side of the grid for some
		// strange fucking reason
		cellPane.setMaxWidth(GRID_WIDTH + GRID_PADDING);
		cellPane.setMaxHeight(GRID_HEIGHT);

		// cellPane.setPrefWidth(20);
		// cellPane.setPrefHeight(300);
		// cellPane.setPrefWrapLength(50);
		// cellPane.setStyle("-fx-border-color: black; -fx-border-width: 2;");
	}

}