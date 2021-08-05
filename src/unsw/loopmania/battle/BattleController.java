package unsw.loopmania.battle;

import java.util.ArrayList;

import org.javatuples.Pair;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import javafx.util.Duration;
import unsw.loopmania.Character;
import unsw.loopmania.ImageManager;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.LoopManiaWorldController;
import unsw.loopmania.MovingEntity;
import unsw.loopmania.SoundManager;
import unsw.loopmania.cards.Card;
import unsw.loopmania.enemies.Enemy;
import unsw.loopmania.items.Item;

public class BattleController {

    @FXML
    private Pane root;

    @FXML
    private StackPane baseStack;

    @FXML
    private StackPane friendlies;

    @FXML
    private StackPane enemies;

    private BattleImageManager imageManager;

    private SoundManager soundManager;

    private ImageView battleBackground;

    private BattleSimulator battle;

    public SimpleBooleanProperty battleActive;

    private ArrayList<Pair<GridPane, MovingEntity>> onloadedEnemies;
    private ArrayList<Pair<GridPane, MovingEntity>> onloadedFriendlies;

    private int screenWidth = 360;

    private Timeline timeline;
    private LoopManiaWorldController worldController;

    private double tickRate = 0.8;

    private Turn turn = Turn.ALLY;

    private enum Turn {
        ALLY,
        ENEMY
    }

    public BattleController() {
        this.imageManager = new BattleImageManager();
        this.soundManager = new SoundManager();
        onloadedEnemies = new ArrayList<Pair<GridPane, MovingEntity>>();
        onloadedFriendlies = new ArrayList<Pair<GridPane, MovingEntity>>();
        battleActive = new SimpleBooleanProperty(true);
    }

    @FXML 
    public void initialize() {
        root.setStyle("-fx-font-family: Serif;");
        battleBackground = new ImageView(imageManager.getBackgroundImage());
        baseStack.getChildren().add(0, battleBackground);
        enemies.setAlignment(Pos.BOTTOM_RIGHT);
        friendlies.setAlignment(Pos.BOTTOM_LEFT);
    }

    /**
     * Run an instance of the battle 
     */
    public void runBattle() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(tickRate), event -> {
            updateDeadEntities();
            //updateAllegiances();
            shuffleDisplays();
            if (turn == Turn.ALLY) {
                battle.battleAllyAttack();
                turn = Turn.ENEMY; //
                if (battle.getBattleEnemy() == null && battle.getBattleAlly() != null)
                    // if other side has no next attacker but we do, keep attacking
                    turn = Turn.ALLY;
            } else if (turn == Turn.ENEMY) {
                battle.battleEnemyAttack();
                turn = Turn.ALLY;
                if (battle.getBattleAlly() == null && battle.getBattleEnemy() != null)
                    // if other side has no next attacker but we do, keep attacking
                    turn = Turn.ENEMY;
            }

            if(isBattleOver()) {
                stopBattle();
                return;
            }
            //updateDeadEntities();
            updateAllegiances();
            shuffleDisplays();
            battle.readyNextAttackers();

        //}
        //System.out.println("battle over");
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        //timeline.setOnFinished(e -> worldTimelime.play());
        timeline.play();
        System.out.println("Flag3");
    }

    /**
     * Update the entities who were defeated in the battle
     */
    private void updateDeadEntities() {
        ArrayList<Pair<GridPane, MovingEntity>> deadEnemies = new ArrayList<Pair<GridPane, MovingEntity>>();
        ArrayList<Pair<GridPane, MovingEntity>> deadFriendlies = new ArrayList<Pair<GridPane, MovingEntity>>();

        for (Pair<GridPane, MovingEntity> pair: onloadedEnemies) {
            if (pair.getValue1().getCurrHP() <= 0){
                deadEnemies.add(pair);
            }
        }
        for (Pair<GridPane, MovingEntity> pair: onloadedFriendlies) {
            if (pair.getValue1().getCurrHP() <= 0) {
                deadFriendlies.add(pair);
            }
        }

        for (Pair<GridPane, MovingEntity> pair: deadEnemies) {
            onloadedEnemies.remove(pair);
            // remove old display
            enemies.getChildren().remove(pair.getValue0());
        }
        for (Pair<GridPane, MovingEntity> pair: deadFriendlies) {
            onloadedFriendlies.remove(pair);
            // remove old display
            friendlies.getChildren().remove(pair.getValue0());
        }
    }

    // updates the stage if any fighters change sides
    private void updateAllegiances() {
        
        ArrayList<Pair<GridPane, MovingEntity>> convertedEnemies = new ArrayList<Pair<GridPane, MovingEntity>>();
        ArrayList<Pair<GridPane, MovingEntity>> convertedFriendlies = new ArrayList<Pair<GridPane, MovingEntity>>();

        for (Pair<GridPane, MovingEntity> pair: onloadedEnemies) {
            if (pair.getValue1().isFriendly()) {
                convertedEnemies.add(pair);
            }
        }
        for (Pair<GridPane, MovingEntity> pair: onloadedFriendlies) {
            if (!pair.getValue1().isFriendly()) {
                convertedFriendlies.add(pair);
            }
        }
        for (Pair<GridPane, MovingEntity> pair: convertedEnemies) {
            onloadedEnemies.remove(pair);
            // remove old display
            enemies.getChildren().remove(pair.getValue0());
            onloadFriendly(pair.getValue1(), screenWidth/1.5 + 20 * onloadedFriendlies.size());
        }
        for (Pair<GridPane, MovingEntity> pair: convertedFriendlies) {
            onloadedFriendlies.remove(pair);
            // remove old display
            friendlies.getChildren().remove(pair.getValue0());
            onloadEnemy(pair.getValue1(), screenWidth/1.5 + 20 * onloadedEnemies.size());
        }
    }

    /**
     * Place entities on the battle display screen
     */
    private void shuffleDisplays() {
        int totalEnemies = onloadedEnemies.size();
        double enemyPadding = (screenWidth/1.5)/totalEnemies;
        double padding = 10;
        for (Pair<GridPane, MovingEntity> pair: onloadedEnemies) {
            pair.getValue0().setPadding(new Insets(0,padding,0,0)); // margins top/right/bottom/left
            padding += enemyPadding;
        }
        int totalFriendlies = onloadedFriendlies.size();
        double friendlyPadding = (screenWidth/1.5)/totalFriendlies;
        padding = 10;
        for (Pair<GridPane, MovingEntity> pair: onloadedFriendlies) {
            pair.getValue0().setPadding(new Insets(0,0,0,padding)); // margins top/right/bottom/left
            padding += friendlyPadding;
        }
    }

    /**
     * Load entities onto the screen
     * @param battle
     */
    public void loadBattle(BattleSimulator battle) {
        this.battle = battle;
        int totalEnemies = battle.getBattleEnemies().size();
        double enemyPadding = (screenWidth/1.5)/totalEnemies;
        double padding = 10;
        for (MovingEntity e: battle.getBattleEnemies()) {
            onloadEnemy(e, padding);
            padding += enemyPadding;
        }
        int totalFriendlies = battle.getBattleAllies().size();
        double friendlyPadding = (screenWidth/1.5)/totalFriendlies;
        padding = 10;
        for (MovingEntity e: battle.getBattleAllies()) {
            onloadFriendly(e, padding);
            padding += friendlyPadding;
        }
    }

    // Load enemies to the battle screen
    public void onloadEnemy(MovingEntity enemy, double rightPadding) {
        GridPane enemyDisplay = new GridPane();
        enemyDisplay.getRowConstraints().add(new RowConstraints(10)); // set min height of rows to 10px
        enemyDisplay.setPadding(new Insets(0,rightPadding,0,0)); // evenly space out the enemies
                                // margins: top/right/bottom/left

        ImageView image = new ImageView(imageManager.getEnemyImage(enemy));

        VBox statusEffects = new VBox();
        statusEffects.setAlignment(Pos.CENTER);
        // build the hp bar
        HBox hpContainer = new HBox();
        StackPane hpbar = new StackPane();
        Rectangle maxhp = new Rectangle(100, 5);
        Rectangle hp = new Rectangle(100, 5);
        hp.setStyle("-fx-fill: #ff0000; -fx-stroke: black; -fx-stroke-width: 1;");
        maxhp.setStyle("-fx-fill: #414141;-fx-stroke: black; -fx-stroke-width: 1;");
        hp.widthProperty().bind(enemy.getCurrHpProperty().multiply(40).divide(enemy.getMaxHpProperty()));
        maxhp.widthProperty().set(40);

        hpbar.setAlignment(Pos.CENTER_LEFT);
        hpbar.getChildren().add(maxhp);
        hpbar.getChildren().add(hp);
        hpContainer.getChildren().add(hpbar);
        hpContainer.setAlignment(Pos.CENTER);
        enemyDisplay.add(hpContainer, 0, 0);
        enemyDisplay.add(statusEffects, 0, 1);
        enemyDisplay.add(image, 0, 2);
        enemyDisplay.setAlignment(Pos.BOTTOM_RIGHT);

        enemy.action.addListener((obs, oldValue, newValue) -> {
            if (newValue.intValue() == 1) {
                // shake
                animateAttack(image, -10);
            } else if (newValue.intValue() == 2) {
                // opacity
                animateDamage(image, hp);
                damageSoundEffect(enemy);
            } else if (newValue.intValue() == 3) {
                // opacity
                animateHeal(image, hp);
            } else if (newValue.intValue() == 4) {
                animateStatus(enemy, statusEffects);
            }
        });

        enemies.getChildren().add(enemyDisplay);
        animateStatus(enemy, statusEffects);
        onloadedEnemies.add(new Pair<GridPane, MovingEntity>(enemyDisplay, enemy));
    }

    // Load the character and allies to the battle screen
    public void onloadFriendly(MovingEntity friendly, double leftPadding) {
        GridPane friendlyDisplay = new GridPane();
        friendlyDisplay.getRowConstraints().add(new RowConstraints(10));
        friendlyDisplay.setPadding(new Insets(0,0,0,leftPadding));
                                // margins: top/right/bottom/left

        ImageView image = new ImageView(imageManager.getFriendlyImage(friendly));

        VBox statusEffects = new VBox();
        statusEffects.setAlignment(Pos.CENTER);
        // build the hp bar
        HBox hpContainer = new HBox();
        StackPane hpbar = new StackPane();
        hpbar.setAlignment(Pos.CENTER_LEFT);
        Rectangle maxhp = new Rectangle(100, 5);
        Rectangle hp = new Rectangle(100, 5);
        hp.setStyle("-fx-fill: #ff0000; -fx-stroke: black; -fx-stroke-width: 1;");
        maxhp.setStyle("-fx-fill: #414141;-fx-stroke: black; -fx-stroke-width: 1;");
        if (friendly.getID().equals("Character")) {
            // make character's hp bar bigger than the rest
            hp.widthProperty().bind(friendly.getCurrHpProperty().multiply(80).divide(friendly.getMaxHpProperty()));
            maxhp.widthProperty().set(80);
        } else {
            hp.widthProperty().bind(friendly.getCurrHpProperty().multiply(40).divide(friendly.getMaxHpProperty()));
            maxhp.widthProperty().set(40);
        }
        hpbar.getChildren().add(maxhp);
        hpbar.getChildren().add(hp);
        hpContainer.getChildren().add(hpbar);
        hpContainer.setAlignment(Pos.CENTER);
        friendlyDisplay.add(hpContainer, 0, 0);
        friendlyDisplay.add(statusEffects, 0, 1);
        friendlyDisplay.add(image, 0, 2);
        friendlyDisplay.setAlignment(Pos.BOTTOM_LEFT);

        //addEntity(enemy, view); pair later
        friendly.action.addListener((obs, oldValue, newValue) -> {
            if (newValue.intValue() == 1) {
                animateAttack(image, 10);
            } else if (newValue.intValue() == 2) {
                animateDamage(image, hp);
                damageSoundEffect(friendly);
            } else if (newValue.intValue() == 3) {
                animateHeal(image, hp);
            } else if (newValue.intValue() == 4) {
                animateStatus(friendly, statusEffects);
            }
        });
        friendlies.getChildren().add(friendlyDisplay);
        animateStatus(friendly, statusEffects);
        onloadedFriendlies.add(new Pair<GridPane, MovingEntity>(friendlyDisplay, friendly));
    }

    public void stopBattle() {
        System.out.println("Flag stopping");
        timeline.stop();
    }

    /**
     * End the battle animation
     * @return the result of the battle (true; win, false; lost)
     */
    public boolean isBattleOver() {
        boolean result = battle.isBattleOver();
        if (result) {
            // fade out battle and do post battle work
            Timeline timeline = new Timeline(
                //new KeyFrame(Duration.ZERO, new KeyValue(battleBackground.opacityProperty(), 1.0)),
                new KeyFrame(Duration.seconds(tickRate), new KeyValue(battleBackground.opacityProperty(), 1.0))
            );
            timeline.setCycleCount(1);
            timeline.setOnFinished(e->{
                worldController.reactToEnemyDefeat(battle.getDefeated());
                worldController.battleResume();
                battleActive.set(false);
            });
            timeline.play();
        }
        return result;
    }

    public void setWorldController(LoopManiaWorldController worldController) {
        this.worldController = worldController;
    }

    /**
     * Animate damage to entities
     * @param image of the entity
     * @param hp of the entity
     */
    private void animateDamage(ImageView image, Rectangle hp) {
        FillTransition transition = new FillTransition(Duration.seconds(tickRate * 0.15), hp, Color.valueOf("#ff0000"), Color.valueOf("#ffffff"));
        transition.setCycleCount(2);
        transition.setAutoReverse(true);

        FadeTransition transition2 = new FadeTransition(Duration.seconds(tickRate * 0.15), image);
        transition2.setFromValue(1.0);
        transition2.setToValue(0.2);
        transition2.setCycleCount(4);
        transition2.setAutoReverse(true);
        transition2.setOnFinished(e-> {
            // if entity died, keep opacity 0
            if (hp.widthProperty().get() <= 0) {
                image.setOpacity(0);
            }
        });

        transition.play();
        transition2.play();
    }

    /**
     * The attack animation
     * @param image the entity image we're moving
     * @param direction the x direction and amount moved, +ve for allies, -ve for enemies
     */
    private void animateAttack(ImageView image, int direction) {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(tickRate * 0.15), image);
        transition.setByX(direction);
        transition.setAutoReverse(true);
        transition.setCycleCount(2);
        transition.play();
    }

    /**
     * Healing animation
     * @param image of the entity
     * @param hp of the entity
     */
    private void animateHeal(ImageView image, Rectangle hp) {
        FillTransition transition = new FillTransition(Duration.seconds(tickRate * 0.5), hp, Color.valueOf("#ff0000"), Color.valueOf("#66ff66"));
        transition.setCycleCount(2);
        transition.setAutoReverse(true);

        Glow glow = new Glow(0);
        image.setEffect(glow);
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(glow.levelProperty(), 0.4)),
            new KeyFrame(Duration.seconds(tickRate*0.25), new KeyValue(glow.levelProperty(), 1)
        ));
        timeline.setCycleCount(2);

        timeline.setOnFinished(e->{image.setEffect(null);});

        transition.play();
        timeline.play();

    }

    /**
     * Animate status effects
     * @param entity
     * @param statusEffects
     */
    private void animateStatus(MovingEntity entity, VBox statusEffects) {
        // clear the existing statuses incase they timed out
        statusEffects.getChildren().clear();
        if ((entity.getStatusByID("Tranced") != null)|| // <- needed so the "winning" trance is shown
            (entity instanceof Enemy && entity.isFriendly()) ||
            (entity.getID().equals("Ally") && !entity.isFriendly())) {
            // tranced enemies and allies that aren't friendly are confused
            System.out.println("Rendering duckies on: " + entity.getID());
            statusEffects.getChildren().add(new ImageView(imageManager.getStatusImage("Confused")));
        }
        if ((entity.getStatusByID("Stunned") != null)) { // if stunned is affecting entity
            System.out.println("Rendering stars on: " + entity.getID());
            statusEffects.getChildren().add(new ImageView(imageManager.getStatusImage("Stunned")));
        }
        if ((entity.getStatusByID("VampireDebuff") != null)) { // if vampire debuff is affecting entity
            System.out.println("Rendering vamp debuff on: " + entity.getID());
            statusEffects.getChildren().add(new ImageView(imageManager.getStatusImage("VampireDebuff")));
        }
        if ((entity.getStatusByID("Burned") != null)) { // if vampire debuff is affecting entity
            System.out.println("Rendering burn effect on: " + entity.getID());
            statusEffects.getChildren().add(new ImageView(imageManager.getStatusImage("Burned")));
        }
        if ((entity.getStatusByID("Frozen") != null)) { // if vampire debuff is affecting entity
            statusEffects.getChildren().add(new ImageView(imageManager.getStatusImage("Frozen")));
        }
        if ((entity.getStatusByID("Paralysed") != null)) { // if vampire debuff is affecting entity
            statusEffects.getChildren().add(new ImageView(imageManager.getStatusImage("Paralysed")));
        }
        if ((entity.getStatusByID("Poisoned") != null)) { // if vampire debuff is affecting entity
            statusEffects.getChildren().add(new ImageView(imageManager.getStatusImage("Poisoned")));
        }
        if ((entity.getStatusByID("Sleeping") != null)) { // if vampire debuff is affecting entity
            statusEffects.getChildren().add(new ImageView(imageManager.getStatusImage("Sleeping")));
        }
    }

    public void transferStageBossBattleImages(ImageManager otherImageManager) {
        imageManager.setStageBossBattleImages(otherImageManager.getStageBossBattleImage(), otherImageManager.getStageBossPetBattleImage());
    }
    
    public void damageSoundEffect(MovingEntity entity) {
        
        switch(entity.getID()) {
            case "Character": 
                soundManager.playSoundEffect("src/musics/hurt.mp3", 0.05);
                break;
            case "Slug":
                soundManager.playSoundEffect("src/musics/slugDmg.wav", 0.05);
                break;
            case "Zombie":
                soundManager.playSoundEffect("src/musics/zombieDmg.wav", 0.05);
                break;
            case "Vampire":
                soundManager.playSoundEffect("src/musics/vampireDmg.wav", 0.05);
                break;
            default:
                //silence
        }

    }

}
