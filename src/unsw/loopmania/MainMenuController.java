package unsw.loopmania;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * controller for the main menu.
 * TODO = you could extend this, for example with a settings menu, or a menu to load particular maps.
 */
public class MainMenuController {
    @FXML
    private StackPane root;
    @FXML
    private ImageView soundButtonImage;
    @FXML
    private Button resumeGameButton;
    @FXML
    private Button newGameButton;
    @FXML
    private VBox modeButtonsFX;
    @FXML
    private Button standardModeButton;
    @FXML
    private Button survivalModeButton;
    @FXML
    private Button berserkerModeButton;
    @FXML
    private Button confusingModeButton;
    @FXML
    private ImageView backgroundMain;
    @FXML
    private ImageView backgroundModeSelect;
    @FXML
    private StackPane campaignView;
    @FXML
    private StackPane gym1;
    @FXML
    private ImageView badge1FX;
    @FXML
    private StackPane gym2;
    @FXML
    private ImageView badge2FX;
    @FXML
    private StackPane gym3;
    @FXML
    private ImageView badge3FX;
    @FXML
    private StackPane gym4;
    @FXML
    private ImageView badge4FX;
    @FXML
    private StackPane gym5;
    @FXML
    private ImageView badge5FX;
    @FXML
    private StackPane gym6;
    @FXML
    private ImageView badge6FX;
    @FXML
    private StackPane gym7;
    @FXML
    private ImageView badge7FX;
    @FXML
    private StackPane gym8;
    @FXML
    private ImageView badge8FX;

    private ArrayList<ImageView> badges;

    private int currLevel = 1;

    private boolean enteredGame = false;

    @FXML
    private ImageView selectStageImageFX;
    @FXML
    private Button enterGameButton;

    private String gameMode = "Standard";
    private int worldLevel = 1;

    private ImageManager imageManager = new ImageManager();
    
    private SoundManager effectSoundManager = new SoundManager();
    private SoundManager backgroundSoundManager = new SoundManager();

    /**
     * facilitates switching to main game
     */
    private MenuSwitcher gameSwitcher;
    private LoopManiaApplication application;

    public void setGameSwitcher(MenuSwitcher gameSwitcher){
        this.gameSwitcher = gameSwitcher;
    }

    public void setApplication(LoopManiaApplication application){
        this.application = application;
    }

    /**
     * facilitates switching to main game upon button click
     * @throws IOException
     */
    @FXML
    private void switchToGame() throws IOException {
        enteredGame = true;
        resetScreen();
        gameSwitcher.switchMenu();
        effectSoundManager.playSoundEffect("src/musics/click.mp3", 0.1);
        backgroundSoundManager.stopBackgroundMusic();
    }

    /**
     * Change to game mode selection
     * @throws IOException
     */
    @FXML
    private void newGame() throws IOException {
        fadeToModeScreen();
        effectSoundManager.playSoundEffect("src/musics/click.mp3", 0.1);
    }

    /**
     * Start a new standard game
     * @throws IOException
     */
    @FXML
    private void newStandardGame() throws IOException {
        gameMode = "Standard";
        initialiseCampaignScreen();
        effectSoundManager.playSoundEffect("src/musics/click.mp3", 0.1);
        backgroundSoundManager.changeBackgroundMusic("src/musics/homeTown.mp3");
    }
    
    /**
     * Start a new survival game
     * @throws IOException
     */
    @FXML
    private void newSurvivalGame() throws IOException {
        gameMode = "Survival";
        initialiseCampaignScreen();
        effectSoundManager.playSoundEffect("src/musics/click.mp3", 0.1);
        backgroundSoundManager.changeBackgroundMusic("src/musics/homeTown.mp3");
    }

    /**
     * Start a new berserker game
     * @throws IOException
     */
    @FXML
    private void newBerserkerGame() throws IOException {
        gameMode = "Berserker";
        initialiseCampaignScreen();
        effectSoundManager.playSoundEffect("src/musics/click.mp3", 0.1);
        backgroundSoundManager.changeBackgroundMusic("src/musics/homeTown.mp3");
    }

    /**
     * Start a new confusing game
     * @throws IOException
     */
    @FXML
    private void newConfusingGame() throws IOException {
        gameMode = "Confusing";
        initialiseCampaignScreen();
        effectSoundManager.playSoundEffect("src/musics/click.mp3", 0.1);
        backgroundSoundManager.changeBackgroundMusic("src/musics/homeTown.mp3");
    }


    @FXML
    private void startNewGame() throws IOException {
        application.loadNewGame(gameMode, worldLevel);
        switchToGame();
        effectSoundManager.playSoundEffect("src/musics/click.mp3", 0.1);
    }

    /**
     * Initialise the main menu and stage selection screen
     */
    @FXML
    public void initialize() {
        resumeGameButton.setVisible(false);
        // bind opacity of mode buttons to the mode select background
        modeButtonsFX.opacityProperty().bind(backgroundModeSelect.opacityProperty());
        // bind visiblity so that you can't click on the buttons
        modeButtonsFX.visibleProperty().bind(backgroundModeSelect.visibleProperty());
        // now hide the mode select background + buttons together
        backgroundModeSelect.setOpacity(0);
        backgroundModeSelect.setVisible(false);

        backgroundMain.requestFocus();
        // campaign already hidden in fxml

        // add badges into an array to make it easy to work with
        badges = new ArrayList<ImageView>();
        badges.add(badge1FX);
        badges.add(badge2FX);
        badges.add(badge3FX);
        badges.add(badge4FX);
        badges.add(badge5FX);
        badges.add(badge6FX);
        badges.add(badge7FX);
        badges.add(badge8FX);

        root.requestFocus();

        backgroundSoundManager.playBackgroundMusic("src/musics/homeScreen.mp3");
    }

    /**
     * Update the screen depending on the state of starting a game or not
     */
    private void resetScreen() {
        // change start game button to resume after first instantiation
        if (enteredGame) {
            resumeGameButton.setVisible(true);
        }
        newGameButton.setText("Campaign");
        newGameButton.setVisible(true);
        newGameButton.setOpacity(1);
        // hide mode select
        backgroundModeSelect.setVisible(false);
        backgroundModeSelect.setOpacity(0);
        // reset campaign screen
        selectStageImageFX.setImage(null);
        enterGameButton.setVisible(false);
        campaignView.setVisible(false);

        root.requestFocus();
    }

    /**
     * Fade in the new background + mode buttons
     */
    private void fadeToModeScreen () {
        FadeTransition fadeIn = new FadeTransition();  
        fadeIn.setDuration(Duration.millis(1000));
        fadeIn.setFromValue(0);
        fadeIn.setToValue(10);
        fadeIn.setNode(backgroundModeSelect);
        fadeIn.setCycleCount(1);

        fadeIn.play();

        newGameButton.setVisible(false);
        resumeGameButton.setVisible(false);

        backgroundModeSelect.setVisible(true);
        root.requestFocus();
    }

    /**
     * Mute musics in the game
     */
    @FXML
    public void toggleMute() {
        if (backgroundSoundManager.isMuted()) {
            // unmute
            effectSoundManager.playSoundEffect("src/musics/click.mp3", 0.1);
            soundButtonImage.setImage(new Image((new File("src/images/game_interface/sound_on.png")).toURI().toString()));
            backgroundSoundManager.setMuted(false);

        } else {
            // mute
            effectSoundManager.playSoundEffect("src/musics/click.mp3", 0.1);
            soundButtonImage.setImage(new Image((new File("src/images/game_interface/sound_off.png")).toURI().toString()));
            backgroundSoundManager.setMuted(true);
        }
        root.requestFocus();
    }

    /**
     * Initialise the stage selection screen
     * @throws FileNotFoundException
     */
    private void initialiseCampaignScreen() throws FileNotFoundException {
        // parse the save file
        JSONObject json = new JSONObject(new JSONTokener(new FileReader("src/unsw/loopmania/campaignProgress.json")));
        currLevel = json.getInt("curr_level");
        for (int i = 0; i < badges.size(); i++) {
            badges.get(i).setVisible(true);
            if (i+1 < currLevel) {
                // completed stages get their badge coloured in
                badges.get(i).setImage(imageManager.getColouredBadgeImage(i+1));
            } else if (i+1 == currLevel) {
                // current stage is default greyed
                badges.get(i).setOpacity(1);
            } else {
                // hide inaccesible stages
                badges.get(i).setVisible(false);
            }
        }
        // fade in screen
        FadeTransition fadeIn = new FadeTransition();  
        fadeIn.setDuration(Duration.millis(1000));
        fadeIn.setFromValue(0);
        fadeIn.setToValue(10);
        fadeIn.setNode(campaignView);
        fadeIn.setCycleCount(1);
        fadeIn.play();

        campaignView.setVisible(true);
    }

    /**
     * Return to menu 
     */
    @FXML
    void campaignReturnToMenu() {
        resetScreen();
        effectSoundManager.playSoundEffect("src/musics/click.mp3", 0.1);
    }

    /**
     * Show selected stage
     * @param gym
     * @param selectedGymImage
     */
    void showSelectedGym(int gym, ImageView selectedGymImage) {
        if (gym <= currLevel) {
            enterGameButton.setText("Stage " + gym + ": Enter");
            enterGameButton.setVisible(true);
            // unglow all badges
            for (ImageView badge: badges) {
                badge.setEffect(null);
            }
            // glow the selected badge
            Glow glow = new Glow(0.7);
            selectedGymImage.setEffect(glow);
        }
    }

    /**
     * Show selected gym when clicking on the icon
     * @param event 
     */
    @FXML
    void selectGym1(MouseEvent event) {
        worldLevel = 1;
        selectStageImageFX.setImage(badge1FX.getImage());
        showSelectedGym(1, badge1FX);
        effectSoundManager.playSoundEffect("src/musics/click.mp3", 0.1);
    }
    @FXML
    void selectGym2(MouseEvent event) {
        worldLevel = 2;
        selectStageImageFX.setImage(badge2FX.getImage());
        showSelectedGym(2, badge2FX);
        effectSoundManager.playSoundEffect("src/musics/click.mp3", 0.1);
    }
    @FXML
    void selectGym3(MouseEvent event) {
        worldLevel = 3;
        selectStageImageFX.setImage(badge3FX.getImage());
        showSelectedGym(3, badge3FX);
        effectSoundManager.playSoundEffect("src/musics/click.mp3", 0.1);
    }
    @FXML
    void selectGym4(MouseEvent event) {
        worldLevel = 4;
        selectStageImageFX.setImage(badge4FX.getImage());
        showSelectedGym(4, badge4FX);
        effectSoundManager.playSoundEffect("src/musics/click.mp3", 0.1);
    }
    @FXML
    void selectGym5(MouseEvent event) {
        worldLevel = 5;
        selectStageImageFX.setImage(badge5FX.getImage());
        showSelectedGym(5, badge5FX);
        effectSoundManager.playSoundEffect("src/musics/click.mp3", 0.1);
    }
    @FXML
    void selectGym6(MouseEvent event) {
        worldLevel = 6;
        selectStageImageFX.setImage(badge6FX.getImage());
        showSelectedGym(6, badge6FX);
        effectSoundManager.playSoundEffect("src/musics/click.mp3", 0.1);
    }
    @FXML
    void selectGym7(MouseEvent event) {
        worldLevel = 7;
        selectStageImageFX.setImage(badge7FX.getImage());
        showSelectedGym(7, badge7FX);
        effectSoundManager.playSoundEffect("src/musics/click.mp3", 0.1);
    }
    @FXML
    void selectGym8(MouseEvent event) {
        worldLevel = 8;
        selectStageImageFX.setImage(badge8FX.getImage());
        showSelectedGym(8, badge8FX);
        effectSoundManager.playSoundEffect("src/musics/click.mp3", 0.1);
    }
}
