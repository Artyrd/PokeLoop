package unsw.loopmania;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;


public class GameUILoader {
    public GameUILoader() {}

    /**
     * Create an overlay to indicate a paused game
     * @return Stackpane of the overlay
     */
    public StackPane initPauseOverlay() {
        StackPane pauseOverlay = new StackPane();
        Rectangle background = new Rectangle();
        background.setStyle("-fx-background-color: BLACK");
        background.setOpacity(0.7);
        background.setWidth(225);
        background.setHeight(50);
        VBox pauseText = new VBox();
        pauseOverlay.setAlignment(Pos.CENTER);
        Label title = new Label("PAUSED");
        title.setAlignment(Pos.CENTER);
        title.setStyle("-fx-font-size: 20px; -fx-text-fill: white; -fx-font-family: 'serif'");
        Label text = new Label("Press <spacebar> to resume the game!");
        text.setAlignment(Pos.CENTER);
        text.setStyle("-fx-text-fill: white; -fx-font-family: 'serif'");
        pauseText.getChildren().add(title);
        pauseText.getChildren().add(text);
        pauseText.setAlignment(Pos.CENTER);
        pauseOverlay.getChildren().add(background);
        pauseOverlay.getChildren().add(pauseText);
        // so that you can still place buildings while paused
        pauseOverlay.setMouseTransparent(true);;

        return pauseOverlay;
    }

    /**
     * Create an overlay to indicate a lost game
     * @return Stackpane of the overlay
     */
    public StackPane initLossOverlay() {
        StackPane lossOverlay = new StackPane();
        Rectangle background = new Rectangle();
        background.setStyle("-fx-background-color: BLACK");
        background.setOpacity(0.7);
        background.setWidth(225);
        background.setHeight(100);
        VBox lossText = new VBox();
        lossOverlay.setAlignment(Pos.CENTER);
        Label title = new Label("YOU DIED");
        title.setStyle("-fx-font-size: 40px; -fx-text-fill: red; -fx-font-family: 'serif'");
        title.setAlignment(Pos.CENTER);
        Label text = new Label("... return to main menu?");
        text.setStyle("-fx-text-fill: red; -fx-font-family: 'serif'");
        text.setAlignment(Pos.CENTER);
        lossText.getChildren().add(title);
        lossText.getChildren().add(text);
        lossText.setAlignment(Pos.CENTER);
        lossOverlay.getChildren().add(background);
        lossOverlay.getChildren().add(lossText);
        return lossOverlay;
    }

    /**
     * Create an overlay to indicate a won game
     * @return Stackpane of the overlay
     */
    public StackPane initWinOverlay() {
        StackPane winOverlay = new StackPane();
        Rectangle background = new Rectangle();
        background.setStyle("-fx-background-color: BLACK");
        background.setOpacity(0.7);
        background.setWidth(225);
        background.setHeight(100);
        VBox winText = new VBox();
        winOverlay.setAlignment(Pos.CENTER);
        Label title = new Label("YOU WIN!");
        title.setStyle("-fx-font-size: 40px; -fx-text-fill: white; -fx-font-family: 'serif'");
        title.setAlignment(Pos.CENTER);
        Label text = new Label("... continue?");
        text.setStyle("-fx-text-fill: white; -fx-font-family: 'serif'");
        text.setAlignment(Pos.CENTER);
        winText.getChildren().add(title);
        winText.getChildren().add(text);
        winText.setAlignment(Pos.CENTER);
        winOverlay.getChildren().add(background);
        winOverlay.getChildren().add(winText);

        return winOverlay;
    }

    /**
     * Create an overlay to indicate a current battle
     * @return Stackpane of the overlay
     */
    public StackPane initInBattleOverlay() {
        StackPane inBattleOverlay = new StackPane();
        Rectangle background = new Rectangle();
        background.setStyle("-fx-background-color: BLACK");
        background.setOpacity(0.7);
        background.setWidth(225);
        background.setHeight(50);
        VBox inBattleText = new VBox();
        inBattleOverlay.setAlignment(Pos.CENTER);
        Label title = new Label("IN BATTLE");
        title.setAlignment(Pos.CENTER);
        title.setStyle("-fx-font-size: 20px; -fx-text-fill: white; -fx-font-family: 'serif'");
        Label text = new Label("A battle is currently in progress!");
        text.setAlignment(Pos.CENTER);
        text.setStyle("-fx-text-fill: white; -fx-font-family: 'serif'");
        inBattleText.getChildren().add(title);
        inBattleText.getChildren().add(text);
        inBattleText.setAlignment(Pos.CENTER);
        inBattleOverlay.getChildren().add(background);
        inBattleOverlay.getChildren().add(inBattleText);
        // so that you can still place buildings while inBattled
        inBattleOverlay.setMouseTransparent(true);;

        return inBattleOverlay;
    }


}
