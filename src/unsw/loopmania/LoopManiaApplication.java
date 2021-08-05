package unsw.loopmania;

import java.io.FileReader;
import java.io.IOException;

import org.json.JSONObject;
import org.json.JSONTokener;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * the main application
 * run main method from this class
 */
public class LoopManiaApplication extends Application {

    /**
     * the controller for the game. Stored as a field so can terminate it when click exit button
     */
    private LoopManiaWorldController mainGameController;
    private Stage primaryStage;
    private Parent mainGameRoot;
    private Parent mainMenuRoot;
    private Scene currScene;

    /**
     * Start the application
     * @param stage
     */
    @Override
    public void start(Stage stage) throws IOException {
        // set title on top of window bar
        primaryStage = stage;

        primaryStage.setTitle("Loop Mania");

        // prevent human player resizing game window (since otherwise would see white space)
        // alternatively, you could allow rescaling of the game (you'd have to program resizing of the JavaFX nodes)
        primaryStage.setResizable(false);

        // load the main menu
        MainMenuController mainMenuController = new MainMenuController();
        mainMenuController.setApplication(this);
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("MainMenuView.fxml"));
        menuLoader.setController(mainMenuController);
        //mainMenuController.initialise();

        mainMenuRoot = menuLoader.load();
        mainMenuRoot.setStyle("-fx-font-family: 'serif', 'arial', 'helvetica'");

        // create new scene with the main menu (so we start with the main menu)
        currScene = new Scene(mainMenuRoot);
        
        // load the standardgame at lvl 1
        loadNewGame("Standard", 1);

        // set functions which are activated when button click to switch menu is pressed
        // e.g. from main menu to start the game, or from the game to return to main menu
        mainMenuController.setGameSwitcher(() -> {
            switchToRoot(currScene, mainGameRoot, primaryStage);
            mainGameController.resume();
        });
        
        // deploy the main onto the stage
        mainGameRoot.requestFocus();
        primaryStage.setScene(currScene);
        primaryStage.show();
    }

    /**
     * Stop the application
     */
    @Override
    public void stop(){
        // wrap up activities when exit program
        mainGameController.terminate();
    }

    /**
     * switch to a different Root
     * @param scene scene to be shown
     * @param root
     * @param stage
     */
    private void switchToRoot(Scene scene, Parent root, Stage stage){
        scene.setRoot(root);
        root.requestFocus();
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    /**
     * Load a new game from the given game mode and level 
     * @param gameMode
     * @param worldLevel
     * @throws IOException
     */
    public void loadNewGame(String gameMode, int worldLevel) throws IOException {
        System.out.println("Loading new game... Game Mode: " + gameMode);
        // if a game is currently running, kill it
        if (mainGameController != null) mainGameController.terminate();

        mainGameRoot = loadMainGame(gameMode, worldLevel);
        // set functions which are activated when button click to switch menu is pressed
        // e.g. from main menu to start the game, or from the game to return to main menu
        mainGameController.setMainMenuSwitcher(() -> {switchToRoot(currScene, mainMenuRoot, primaryStage);});

    }

    /**
     * Load the main game from the given game mode and level
     * @param gameMode
     * @param worldLevel
     * @return
     * @throws IOException
     */
    private Parent loadMainGame(String gameMode, int worldLevel) throws IOException {

        // extract the world
        JSONObject campaign = new JSONObject(new JSONTokener(new FileReader("src/unsw/loopmania/campaignProgress.json")));
        JSONObject worlds = campaign.getJSONObject("worlds");
        String stageFile = worlds.getString(Integer.toString(worldLevel));

        // load the main game
        LoopManiaWorldControllerLoader loopManiaLoader = new LoopManiaWorldControllerLoader(stageFile, gameMode);
        //LoopManiaWorldControllerLoader loopManiaLoader = new LoopManiaWorldControllerLoader("world_with_twists_and_turns.json", gameMode);
        //LoopManiaWorldControllerLoader loopManiaLoader = new LoopManiaWorldControllerLoader("big_world_easy.json", gameMode);
        mainGameController = loopManiaLoader.loadController(gameMode);
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("LoopManiaView.fxml"));
        gameLoader.setController(mainGameController);
        Parent gameRoot = gameLoader.load();
        gameRoot.setStyle("-fx-font-family: 'serif', 'arial', 'helvetica'");

        return gameRoot;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
