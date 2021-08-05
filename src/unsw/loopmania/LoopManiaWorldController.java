package unsw.loopmania;

import java.util.ArrayList;
import java.util.List;

import org.codefx.libfx.listener.handle.ListenerHandle;
import org.codefx.libfx.listener.handle.ListenerHandles;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.control.Button;

import javafx.util.Duration;

import org.javatuples.Pair;
import org.json.JSONObject;
import org.json.JSONTokener;

import unsw.loopmania.LoopManiaWorld.GAME_STATE;
import unsw.loopmania.battle.BattleController;
import unsw.loopmania.battle.BattleLoader;
import unsw.loopmania.battle.BattleSimulator;
import unsw.loopmania.buildings.*;
import unsw.loopmania.cards.*;
import unsw.loopmania.enemies.*;
import unsw.loopmania.items.*;

import java.util.EnumMap;
import java.util.Iterator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


/**
 * the draggable types.
 * If you add more draggable types, add an enum value here.
 * This is so we can see what type is being dragged.
 */
enum DRAGGABLE_TYPE{
    CARD,
    ITEM
}

/**
 * A JavaFX controller for the world.
 * 
 * All event handlers and the timeline in JavaFX run on the JavaFX application thread:
 *     https://examples.javacodegeeks.com/desktop-java/javafx/javafx-concurrency-example/
 *     Note in https://openjfx.io/javadoc/11/javafx.graphics/javafx/application/Application.html under heading "Threading", it specifies animation timelines are run in the application thread.
 * This means that the starter code does not need locks (mutexes) for resources shared between the timeline KeyFrame, and all of the  event handlers (including between different event handlers).
 * This will make the game easier for you to implement. However, if you add time-consuming processes to this, the game may lag or become choppy.
 * 
 * If you need to implement time-consuming processes, we recommend:
 *     using Task https://openjfx.io/javadoc/11/javafx.graphics/javafx/concurrent/Task.html by itself or within a Service https://openjfx.io/javadoc/11/javafx.graphics/javafx/concurrent/Service.html
 * 
 *     Tasks ensure that any changes to public properties, change notifications for errors or cancellation, event handlers, and states occur on the JavaFX Application thread,
 *         so is a better alternative to using a basic Java Thread: https://docs.oracle.com/javafx/2/threads/jfxpub-threads.htm
 *     The Service class is used for executing/reusing tasks. You can run tasks without Service, however, if you don't need to reuse it.
 *
 * If you implement time-consuming processes in a Task or thread, you may need to implement locks on resources shared with the application thread (i.e. Timeline KeyFrame and drag Event handlers).
 * You can check whether code is running on the JavaFX application thread by running the helper method printThreadingNotes in this class.
 * 
 * NOTE: http://tutorials.jenkov.com/javafx/concurrency.html and https://www.developer.com/design/multithreading-in-javafx/#:~:text=JavaFX%20has%20a%20unique%20set,in%20the%20JavaFX%20Application%20Thread.
 * 
 * If you need to delay some code but it is not long-running, consider using Platform.runLater https://openjfx.io/javadoc/11/javafx.graphics/javafx/application/Platform.html#runLater(java.lang.Runnable)
 *     This is run on the JavaFX application thread when it has enough time.
 */
public class LoopManiaWorldController {
    /**
     * The main display of the game, as a stack pane to allow for overlays
     * Holds "squares"
     */
    @FXML
    private StackPane worldScreen;
 
    /**
     * squares gridpane includes path images, enemies, character, empty grass, buildings
     */
    @FXML
    private GridPane squares;
   /**
     * cards gridpane includes cards and the ground underneath the cards
     */
    @FXML
    private GridPane cards;

    /**
     * anchorPaneRoot is the "background". It is useful since anchorPaneRoot stretches over the entire game world,
     * so we can detect dragging of cards/items over this and accordingly update DragIcon coordinates
     */
    @FXML
    private AnchorPane anchorPaneRoot;

    @FXML
    private ImageView soundButtonImage;

    @FXML
    private Label loopCountFX;
    /**
     * equippedItems gridpane is for equipped items (e.g. swords, shield, axe)
     */
    @FXML
    private GridPane equippedItems;
    /**
     * Character Stats (atk, def, crit chance)
     */
    @FXML
    private Label currAtkValueFX;
    @FXML
    private Label currDefValueFX;
    @FXML
    private Label currCritValueFX;
    /**
     * HP bar and its text
     */
    @FXML 
    private Label hpValueLabelFX;
    @FXML
    private Label maxhpValueLabelFX;
    @FXML 
    private Rectangle maxHpBarFX;
    @FXML 
    private Rectangle currHpBarFX;
    /**
     * XP bar and its text
     */
    @FXML
    private Label xpValueLabelFX;
    @FXML
    private Label maxXpValueLabelFX;
    @FXML
    private Rectangle currXpBarFX;
    @FXML
    private Rectangle maxXpBarFX;
    /**
     * Other values
     */
    @FXML
    private Label levelValueFX;
    @FXML
    private Label goldValueFX;
    @FXML
    private Label potionQtyFX;
    @FXML
    private Label allyQtyFX;

    @FXML
    private Button pauseButtonFX;
    @FXML
    private ImageView pauseButtonImage;

    @FXML
    private GridPane unequippedInventory;

    // all image views including tiles, character, enemies, cards... even though cards in separate gridpane...
    private List<ImageView> entityImages;

    /**
     * when we drag a card/item, the picture for whatever we're dragging is set here and we actually drag this node
     */
    private DragIcon draggedEntity;

    private boolean isPaused = true;

    private boolean inBattle = false;

    private LoopManiaWorld world;

    /**
     * runs the periodic game logic - second-by-second moving of character through maze, as well as enemies, and running of battles
     */
    private Timeline timeline;

    /**
     * the image currently being dragged, if there is one, otherwise null.
     * Holding the ImageView being dragged allows us to spawn it again in the drop location if appropriate.
     */
    private ImageView currentlyDraggedImage;
    
    /**
     * null if nothing being dragged, or the type of item being dragged
     */
    private DRAGGABLE_TYPE currentlyDraggedType;

    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dropped over its appropriate gridpane
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneSetOnDragDropped;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dragged over the background
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> anchorPaneRootSetOnDragOver;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dropped in the background
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> anchorPaneRootSetOnDragDropped;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dragged into the boundaries of its appropriate gridpane
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneNodeSetOnDragEntered;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dragged outside of the boundaries of its appropriate gridpane
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneNodeSetOnDragExited;

    /**
     * object handling switching to the main menu
     */
    private MenuSwitcher mainMenuSwitcher;

    private StackPane pauseOverlay;
    private StackPane inBattleOverlay;
    private StackPane lossOverlay;
    private StackPane winOverlay;

    //private Stage currStage;

    private ImageManager imageManager;
    private SoundManager backgroundSoundManager;
    private SoundManager effectSoundManager;
    private String gameMode;

    /**
     * 
     */
    private List<Entity> onloadedEntities;

    private BattleLoader battleLoader;

    /**
     * @param world world object loaded from file
     * @param initialEntities the initial JavaFX nodes (ImageViews) which should be loaded into the GUI
     */
    public LoopManiaWorldController(LoopManiaWorld world, String gameMode, ImageManager imageManager, List<ImageView> initialEntities) {
        this.world = world;
        this.gameMode = gameMode;
        onloadedEntities = new ArrayList<Entity>();
        entityImages = new ArrayList<>(initialEntities);
        currentlyDraggedImage = null;
        currentlyDraggedType = null;

        this.imageManager = imageManager;
        backgroundSoundManager = new SoundManager();
        effectSoundManager = new SoundManager();

        // initialize them all...
        gridPaneSetOnDragDropped = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        anchorPaneRootSetOnDragOver = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        anchorPaneRootSetOnDragDropped = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        gridPaneNodeSetOnDragEntered = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        gridPaneNodeSetOnDragExited = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
    }

    /**
     * Initialise the game
     */
    @FXML
    public void initialize() {
        Image pathTilesImage;
        switch (this.gameMode) {
            case ("Standard"):
                pathTilesImage = new Image((new File("src/images/32x32GrassAndDirtPath.png")).toURI().toString());
                break;
            case ("Survival"):
                pathTilesImage = new Image((new File("src/images/32x32GrassAndDirtPathSurvival.png")).toURI().toString());
                break;
            case ("Berserker"):
                pathTilesImage = new Image((new File("src/images/32x32GrassAndDirtPathBerserker.png")).toURI().toString());
                break;
            case ("Confusing"):
                pathTilesImage = new Image((new File("src/images/32x32GrassAndDirtPathConfusing.png")).toURI().toString());
                break;
            default:
                pathTilesImage = new Image((new File("src/images/32x32GrassAndDirtPath.png")).toURI().toString());
        }

        Image inventorySlotImage = new Image((new File("src/images/game_interface/inventory_slot.png")).toURI().toString());

        Rectangle2D imagePart = new Rectangle2D(0, 0, 32, 32);

        // Add the ground first so it is below all other entities (inculding all the twists and turns)
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                ImageView groundView = new ImageView(pathTilesImage);
                // add ids to view so highlighter can easily do its logic
                groundView.setId("groundView");
                groundView.setViewport(imagePart);
                squares.add(groundView, x, y);
            }
        }

        // load entities loaded from the file in the loader into the squares gridpane
        for (ImageView entity : entityImages){
            squares.getChildren().add(entity);
        }
        
        // add the ground underneath the cards
        for (int x=0; x<world.getWidth(); x++){
            ImageView groundView = new ImageView(pathTilesImage);
            groundView.setViewport(imagePart);
            cards.add(groundView, x, 0);
        }

        // bind the loop num
        loopCountFX.textProperty().bind(world.getLoopNumProperty().asString());

        // bind the Level
        levelValueFX.textProperty().bind(world.getCharacter().getLevelProperty().asString());
        // bind ATK, DEF, CRIT
        currAtkValueFX.textProperty().bind(world.getCharacter().getCurrAtkProperty().asString("%.1f"));
        currDefValueFX.textProperty().bind(world.getCharacter().getCurrDefProperty().asString("%.1f"));
        currCritValueFX.textProperty().bind(world.getCharacter().getCurrCritProperty().asString("%.0f"));

        // bind the Gold
        goldValueFX.textProperty().bind(world.inventory.getGoldProperty().asString());
        // bind the Potions
        potionQtyFX.textProperty().bind(world.inventory.getPotionProperty().asString());
        // bind the Allies
        allyQtyFX.textProperty().bind(world.getAllyNumProperty().asString());

        // bind the hp bar
        hpValueLabelFX.textProperty().bind(world.getCharacter().getCurrHpProperty().asString("%.0f"));
        maxhpValueLabelFX.textProperty().bind(world.getCharacter().getMaxHpProperty().asString("%.0f"));
        DoubleBinding healthBarBind = maxHpBarFX.widthProperty().multiply(world.getCharacter().getCurrHpProperty().divide(world.getCharacter().getMaxHpProperty()));
        currHpBarFX.widthProperty().bind(healthBarBind);

        // bind the xp bar
        xpValueLabelFX.textProperty().bind(world.getCharacter().getXpProperty().asString("%.0f"));
        maxXpValueLabelFX.textProperty().bind(world.getCharacter().getMaxXpProperty().asString("%.0f"));
        DoubleBinding xpBarBind = maxXpBarFX.widthProperty().multiply(world.getCharacter().getXpProperty().divide(world.getCharacter().getMaxXpProperty()));
        currXpBarFX.widthProperty().bind(xpBarBind);


        // add the empty slot images for the unequipped inventory
        for (int x=0; x<LoopManiaWorld.unequippedInventoryWidth; x++){
            for (int y=0; y<LoopManiaWorld.unequippedInventoryHeight; y++){
                ImageView emptySlotView = new ImageView(inventorySlotImage);
                unequippedInventory.add(emptySlotView, x, y);
            }
        }

        // create the draggable icon
        draggedEntity = new DragIcon();
        draggedEntity.setVisible(false);
        draggedEntity.setOpacity(0.7);
        anchorPaneRoot.getChildren().add(draggedEntity);

        //initialize pause, loss, win overlays
        GameUILoader uiLoader = new GameUILoader();
        pauseOverlay = uiLoader.initPauseOverlay();
        inBattleOverlay = uiLoader.initInBattleOverlay();
        lossOverlay = uiLoader.initLossOverlay();
        winOverlay = uiLoader.initWinOverlay();

        battleLoader = new BattleLoader();
        battleLoader.transferStageBossBattleImages(imageManager);
        
        backgroundSoundManager.playBackgroundMusic("src/musics/battle.mp3");
        backgroundSoundManager.stopBackgroundMusic();
    }

    /**
     * create and run the timer in the world
     */
    public void startTimer(){    
        System.out.println("starting timer");
        isPaused = false;
        // trigger adding code to process main game logic to queue. JavaFX will target framerate of 0.3 seconds
        timeline = new Timeline(new KeyFrame(Duration.seconds(0.2), event -> {

            if (world.reachedGoal()) {
                world.gameState = GAME_STATE.WIN;
            }
            if (world.gameState != GAME_STATE.LOOPING) {
                System.out.println("not looping!!");
                handleGameState();
                return;
            }
            // moves all movingEntities
            world.runTickMoves();
            world.runStatusWorldTicks();
            // check if the character picked up anything
            for (Iterator<SpawnableItem> iterator = world.getSpawnedItems().iterator(); iterator.hasNext();) {
                SpawnableItem item = iterator.next();
                if (world.getCharacter().getX() == item.getX() && world.getCharacter().getY() == item.getY()) {
                    // give it to the character...
                    Item addedItem = world.addUnequippedItem(item);
                    // remove from spawnedItems list
                    iterator.remove();
                    // load it in the frontend at its new inventory location
                    onLoad(addedItem);
                }
            }

            world.updateBuildings(); // <- GAME_STATE.SHOP might be activated here
            world.updateMovingEntities();
            updateOnLoadEntities();

            BattleSimulator battle = world.findBattle();
            if (battle != null) {
                battlePause();
                BattleController battleController = null;
                battleLoader.showBattle(battle);
                battleController = battleLoader.getController();
                battleController.setWorldController(this);
                battleController.runBattle();
            }

            if (world.gameState == GAME_STATE.LOSE) {
                System.out.println("lol u died");
                handleGameState();
            }
            world.possiblySpawnEnemies();
            updateOnLoadEntities();

            // reward player cards if the conditions allow it
            for (Card givenCard: world.possiblyGiveCards()) {
                System.out.println("\ngivenCard was:" + givenCard.getID());
                loadCard(givenCard);
            }

            // randomly spawn items
            List<SpawnableItem> newItems = world.possiblySpawnItems();
            // spawned items have to be handled differently
            for (SpawnableItem newItem: newItems){
                onLoad(newItem);
            }

            printThreadingNotes("HANDLED TIMER");
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * Update enemies in the world after battle
     * @param defeatedEnemies
     */
    public void reactToEnemyDefeat(List<Enemy> defeatedEnemies) {
        System.out.println("reacting to defeated enemies");
        System.out.println(defeatedEnemies.size());

        for (Enemy e: defeatedEnemies) {
            reactToEnemyDefeat(e);
        }
        // tiny chance of obtaining a rare item post battle, add to inventory
        onLoad(world.addUnequippedItem(world.generateRareItem(1))); // <- TODO change 5 to 1000 after demo
        world.cleanupBattle(defeatedEnemies);
    }

    /**
     * Update the frontend display on moving entities that died/ spawned
     */
    private void updateOnLoadEntities() {
        // check if anything shouldnt be displayed anymore
        for (Iterator<Entity> iterator = onloadedEntities.iterator(); iterator.hasNext();) {
            Entity entity = iterator.next();
            if(!entity.shouldExist().get()) {
                iterator.remove();
            }
        }
        // check for any spawned Moving Entities that should be displayed, since spawned items are separately handled
        for (MovingEntity entity: world.getMovingEntities()) {
            // character is already loaded at the start
            if (!onloadedEntities.contains(entity) && !entity.getID().equals("Character")){
                onLoad(entity);
            }
        }
    }

    /**
     * pause game for battle to take place
     * the human player can still drag and drop items during the battle
     */
    public void battlePause() {
        inBattle = true;
        System.out.println("battle pausing");
        timeline.pause();
        loadWorldScreenOverlay(inBattleOverlay);
    }

    /**
     * resume the world after battle has ended
     */
    public void battleResume() {
        inBattle = false;
        System.out.println("battle complete");
        unloadWorldScreenOverlay(inBattleOverlay);
        // UX addition:
        // If user wishes to pause the game, the finished battle won't resume the game
        if (isPaused) {
            return;
        }
        startTimer();
    }

    /**
     * pause the execution of the game loop
     * the human player can still drag and drop items during the game pause
     */
    public void pause(){
        if (isPaused) {
            // already pasued
            return;
        }
        isPaused = true;
        System.out.println("pausing");
        timeline.pause();
        pauseButtonImage.setImage(imageManager.getGameInterfaceImage("Play"));
        pauseButtonFX.setOnAction(e -> {
            resumeGame();
        });
        loadWorldScreenOverlay(pauseOverlay);

        backgroundSoundManager.pause();
    }

    /**
     * Resume the game
     */
    public void resume() {
        if (!isPaused || inBattle) {
            // already resumed, or in battle
            return;
        }
        exitShop();

        isPaused = false;
        System.out.println("resuming game");
        unloadWorldScreenOverlay(pauseOverlay);
        pauseButtonImage.setImage(imageManager.getGameInterfaceImage("Pause"));
        pauseButtonFX.setOnAction(e -> {
            pauseGame();
        });
        startTimer();

        backgroundSoundManager.resume();
    }

    public void terminate(){
        pause();
    }

    /**
     * pair the entity an view so that the view copies the movements of the entity.
     * add view to list of entity images
     * @param entity backend entity to be paired with view
     * @param view frontend imageview to be paired with backend entity
     */
    private void addEntity(Entity entity, ImageView view) {
        onloadedEntities.add(entity);
        trackPosition(entity, view);
        entityImages.add(view);
    }

    /**
     * load a card from a given card ID, and pair it with an image in the GUI
     */
    private void loadCard(String cardID) {
        Card newCard = world.loadCard(cardID);
        onLoad(newCard);
    }

    /**
     * load an instantiated card from the backend, and pair it with an image in the GUI
     */
    private void loadCard(Card card) {
        Card newCard = world.loadCard(card);
        onLoad(newCard);
    }


    /**
     * load an Item from the world, and pair it with an image in the GUI
     */
    private void loadItem(String itemID) {
        // start by getting first available coordinates
        Item item = world.addUnequippedItem(itemID);
        if (item != null) {
            onLoad(item);
        }
    }

    /**
     * run GUI events after an enemy is defeated, such as spawning items/experience/gold
     * @param enemy defeated enemy for which we should react to the death of
     */
    private void reactToEnemyDefeat(Enemy enemy){
        // react to character defeating an enemy
        // in starter code, spawning extra card/weapon...
        Character character = world.getCharacter();

        System.out.println("Enemy defeated was: " + enemy.getID());
        for (String itemReward: enemy.generateItemRewards()) {
            System.out.println(itemReward);
            loadItem(itemReward);
        }
        for (String cardReward: enemy.generateCardRewards()) {
            System.out.println(cardReward);
            loadCard(cardReward);
        }
        Gold rewardGold = new Gold(null, null);
        rewardGold.setValue(enemy.generateGoldRewards());
        world.addUnequippedItem(rewardGold);

        character.gainXP(enemy.generateExperience());
        System.out.printf("Character now has: Gold[%d], XP[%d], LVL[%d], HP[%f]\n",character.getGold(),character.getXP(), character.getLevel(), character.getCurrHP());

    }

    /**
     * load a vampire castle card into the GUI.
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the cards GridPane.
     * @param vampireCastleCard
     */
    private void onLoad(Card card) {
        Image cardImage = imageManager.getCardImage(card);
        ImageView view = new ImageView(cardImage);
        view.setId(card.getID());

        // FROM https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
        // note target setOnDragOver and setOnDragEntered defined in initialize method
        addDragEventHandlers(view, cardImage,DRAGGABLE_TYPE.CARD, cards, squares);

        addEntity(card, view);
        cards.getChildren().add(view);
    }

    /**
     * load an item into the GUI.
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the unequippedInventory GridPane.
     * @param item - the item to be loaded
     */
    public void onLoad(Item item) {

        if (item == null) return; // nothing to load

        //System.out.println("Onloading Item" + item.getID() + item.getStar() + " to slot " + item.getX() + " " + item.getY());
        Image itemImage = imageManager.getItemImage(item);
        ImageView view = new ImageView(itemImage);
        view.setId(item.getID());
        addDragEventHandlers(view, itemImage, DRAGGABLE_TYPE.ITEM, unequippedInventory, equippedItems);
        addEntity(item, view);
        ColorAdjust colorAdjust = new ColorAdjust(); 
        colorAdjust.brightnessProperty().bind(item.getCurrDurabilityProperty().divide(item.getMaxDurabilityProperty()).subtract(1));
        view.setEffect(colorAdjust);
        unequippedInventory.getChildren().add(view);
    }

    /**
     * load an spawnable item into the World map.
     * No drag-dropping for this.
     * @param item - the spawnable item
     */
    private void onLoad(SpawnableItem item) {
        if (item == null) return; // nothing to load
        Image itemImage = imageManager.getItemImage(item);
        ImageView view = new ImageView(itemImage);
        view.setId(item.getID());
        addEntity(item, view);
        squares.getChildren().add(view);
    }
    /**
     * load a moving enemy into the GUI
     * @param entity - the entity to be loaded in the world frontend
     */
    private void onLoad(MovingEntity entity) {
        ImageView view = new ImageView(imageManager.getMovingEntityImage(entity));
        view.setId(entity.getID());
        addEntity(entity, view);
        squares.getChildren().add(view);
    }

    /**
     * load a building into the GUI
     * @param building
     */
    //private void onLoad(VampireCastleBuilding building){
    private void onLoad(Building building){
        ImageView view = new ImageView(imageManager.getBuildingImage(building));
        view.setId(building.getID());
        addEntity(building, view);
        squares.getChildren().add(view);
        System.out.printf("\nonloaded buildingid: " + building.getID());
        //System.err.printf("\nin WorldController @380:\n[bX = %d][bY = %d]\n[viewX = %f][viewY = %f]\n\n", building.getX(), building.getY(), view.getX(), view.getY());
    }

    /**
     * add drag event handlers for dropping into gridpanes, dragging over the background, dropping over the background.
     * These are not attached to invidual items such as swords/cards.
     * @param draggableType the type being dragged - card or item
     * @param sourceGridPane the gridpane being dragged from
     * @param targetGridPane the gridpane the human player should be dragging to (but we of course cannot guarantee they will do so)
     */
    private void buildNonEntityDragHandlers(DRAGGABLE_TYPE draggableType, GridPane sourceGridPane, GridPane targetGridPane){
        // for example, in the specification, villages can only be dropped on path, whilst vampire castles cannot go on the path
        gridPaneSetOnDragDropped.put(draggableType, new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /*
                 *you might want to design the application so dropping at an invalid location drops at the most recent valid location hovered over,
                 * or simply allow the card/item to return to its slot (the latter is easier, as you won't have to store the last valid drop location!)
                 */
                if (currentlyDraggedType == draggableType){
                    // problem = event is drop completed is false when should be true...
                    // https://bugs.openjdk.java.net/browse/JDK-8117019
                    // putting drop completed at start not making complete on VLAB...

                    //Data dropped
                    //If there is an image on the dragboard, read it and use it
                    Dragboard db = event.getDragboard();
                    Node node = event.getPickResult().getIntersectedNode();
                    if(node != targetGridPane && db.hasImage()){

                        Integer cIndex = GridPane.getColumnIndex(node);
                        Integer rIndex = GridPane.getRowIndex(node);
                        int x = cIndex == null ? 0 : cIndex;
                        int y = rIndex == null ? 0 : rIndex;
                        //Places at 0,0 - will need to take coordinates once that is implemented
                        ImageView image = new ImageView(db.getImage());

                        int nodeX = GridPane.getColumnIndex(currentlyDraggedImage);
                        int nodeY = GridPane.getRowIndex(currentlyDraggedImage);
                        switch (draggableType){
                            case CARD:
                                // nodeX, nodeY = card location; x,y = building dropoff location 
                                removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                //VampireCastleBuilding newBuilding = convertCardToBuildingByCoordinates(nodeX, nodeY, x, y);
                                Building newBuilding = convertCardToBuildingByCoordinates(nodeX, nodeY, x, y);
                                if (newBuilding != null) {
                                    onLoad(newBuilding);
                                } else {
                                    System.out.println("Invalid Location");
                                    // make the original card on the cardpane visible again
                                    currentlyDraggedImage.setVisible(true);
                                    //return;
                                }
                                break;
                            case ITEM:
                       
                                removeDraggableDragEventHandlers(draggableType, targetGridPane);

                                // ^ not spawning anymore, just moving
                                Item draggedItem = (Item) world.inventory.getUnequippedInventoryItemEntityByCoordinates(nodeX, nodeY);
                                System.err.printf("dragged item: " + draggedItem.getID() + " " + draggedItem.getX() + " " + draggedItem.getY()+ "\n");
                                // equip the item...
                                Item oldItem = world.getCharacter().equip(draggedItem); // oldItem = old equipped item we replaced
                                if (oldItem != null) {
                                    oldItem.destroy(); // kill the old equipped image
                                    oldItem.revive(); // set bool back to normal before we put back in inventory
                                }
                                onLoad(oldItem); // load the old item back into the frontend

                                System.out.println("target grid plane was: " + targetGridPane.toString());
                                // we dont care where the image got dragged to, add the equip at the correct spot in frontend
                                Pair<Integer, Integer> equipCoord = getEquipCoord(draggedItem);

                                targetGridPane.add(image, equipCoord.getValue0(), equipCoord.getValue1(), 1, 1);
                                ColorAdjust colorAdjust = new ColorAdjust(); 
                                colorAdjust.brightnessProperty().bind(draggedItem.getCurrDurabilityProperty().divide(draggedItem.getMaxDurabilityProperty()).subtract(1));
                                image.setEffect(colorAdjust);
                                draggedItem.shouldExist().addListener((obs, oldValue, newValue) -> {
                                    if (newValue.booleanValue() == false)
                                        image.setImage(null);
                                });
                                break;
                            default:
                                System.out.println("not card or item");
                                break;
                        }
                        
                        draggedEntity.setVisible(false);
                        draggedEntity.setMouseTransparent(false);
                        //targetGridPane.getChildren().
                        // remove drag event handlers before setting currently dragged image to null
                        currentlyDraggedImage = null;
                        currentlyDraggedType = null;
                        
                        printThreadingNotes("DRAG DROPPED ON GRIDPANE HANDLED");
                    } else {
                        // we drag and dropped at an invalid area, reset the original dragged image
                        System.out.println("if statement @line 529 failed");
                        currentlyDraggedImage.setVisible(true);
                        draggedEntity.setVisible(false);
                        draggedEntity.setMouseTransparent(false);
                        currentlyDraggedImage = null;
                        currentlyDraggedType = null;

                    }
                }
                event.setDropCompleted(true);
                // consuming prevents the propagation of the event to the anchorPaneRoot (as a sub-node of anchorPaneRoot, GridPane is prioritized)
                // https://openjfx.io/javadoc/11/javafx.base/javafx/event/Event.html#consume()
                // to understand this in full detail, ask your tutor or read https://docs.oracle.com/javase/8/javafx/events-tutorial/processing.htm
                event.consume();
            }
        });

        // this doesn't fire when we drag over GridPane because in the event handler for dragging over GridPanes, we consume the event
        anchorPaneRootSetOnDragOver.put(draggableType, new EventHandler<DragEvent>(){
            // https://github.com/joelgraff/java_fx_node_link_demo/blob/master/Draggable_Node/DraggableNodeDemo/src/application/RootLayout.java#L110
            @Override
            public void handle(DragEvent event) {
                if (currentlyDraggedType == draggableType){
                    if(event.getGestureSource() != anchorPaneRoot && event.getDragboard().hasImage()){
                        event.acceptTransferModes(TransferMode.MOVE);
                    }
                }
                if (currentlyDraggedType != null){
                    draggedEntity.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
                }
                event.consume();
            }
        });

        // this doesn't fire when we drop over GridPane because in the event handler for dropping over GridPanes, we consume the event
        anchorPaneRootSetOnDragDropped.put(draggableType, new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                if (currentlyDraggedType == draggableType){
                    //Data dropped
                    //If there is an image on the dragboard, read it and use it
                    Dragboard db = event.getDragboard();
                    Node node = event.getPickResult().getIntersectedNode();
                    if(node != anchorPaneRoot && db.hasImage()){
                        //Places at 0,0 - will need to take coordinates once that is implemented
                        currentlyDraggedImage.setVisible(true);
                        draggedEntity.setVisible(false);
                        draggedEntity.setMouseTransparent(false);
                        // remove drag event handlers before setting currently dragged image to null
                        removeDraggableDragEventHandlers(draggableType, targetGridPane);
                        
                        currentlyDraggedImage = null;
                        currentlyDraggedType = null;
                    }
                }
                //let the source know whether the image was successfully transferred and used
                event.setDropCompleted(true);
                event.consume();
            }
        });
    }

    /**
     * returns the appropriate coordinates on the equips gridpane for the appropriate equipment
     * @param draggedItem
     * @return
     */
    protected Pair<Integer, Integer> getEquipCoord(Item draggedItem) {
        Pair<Integer, Integer> equipCoord = null;
        System.err.println(draggedItem);
        System.err.println(draggedItem.getType());
        switch (draggedItem.getType()) {
            case "Helmet":
                equipCoord = new Pair<Integer, Integer>(0, 0);
                break;
            case "Armour":
                equipCoord = new Pair<Integer, Integer>(0, 1);
                break;
            case "Weapon":
                equipCoord = new Pair<Integer, Integer>(1, 0);
                break;
            case "Shield":
                equipCoord = new Pair<Integer, Integer>(1, 1);
                break;
            case "Accessory":
                equipCoord = new Pair<Integer, Integer>(1, 2);
                break;
            default:
                System.err.println("Error: cannot find frontend equip slot for this item");
                break;
            }
 
        return equipCoord;
    }

    /**
     * remove the card from the world, and spawn and return a building instead where the card was dropped
     * @param cardNodeX the x coordinate of the card which was dragged, from 0 to width-1
     * @param cardNodeY the y coordinate of the card which was dragged (in starter code this is 0 as only 1 row of cards)
     * @param buildingNodeX the x coordinate of the drop location for the card, where the building will spawn, from 0 to width-1
     * @param buildingNodeY the y coordinate of the drop location for the card, where the building will spawn, from 0 to height-1
     * @return building entity returned from the world
     */
    //private VampireCastleBuilding convertCardToBuildingByCoordinates(int cardNodeX, int cardNodeY, int buildingNodeX, int buildingNodeY) {
    private Building convertCardToBuildingByCoordinates(int cardNodeX, int cardNodeY, int buildingNodeX, int buildingNodeY) {
        return world.convertCardToBuildingByCoordinates(cardNodeX, cardNodeY, buildingNodeX, buildingNodeY);
    }

    /**
     * remove an item from the unequipped inventory by its x and y coordinates in the unequipped inventory gridpane
     * @param nodeX x coordinate from 0 to unequippedInventoryWidth-1
     * @param nodeY y coordinate from 0 to unequippedInventoryHeight-1
     */
    /*
    private void removeItemByCoordinates(int nodeX, int nodeY) {
        world.inventory.removeUnequippedInventoryItemByCoordinates(nodeX, nodeY);
    }
    */

    /**
     * add drag event handlers to an ImageView
     * @param view the view to attach drag event handlers to
     * @param entityImage the entity's image
     * @param draggableType the type of item being dragged - card or item
     * @param sourceGridPane the relevant gridpane from which the entity would be dragged
     * @param targetGridPane the relevant gridpane to which the entity would be dragged to
     */
    private void addDragEventHandlers(ImageView view, Image entityImage, DRAGGABLE_TYPE draggableType, GridPane sourceGridPane, GridPane targetGridPane){
        view.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                currentlyDraggedImage = view; // set image currently being dragged, so squares setOnDragEntered can detect it...
                currentlyDraggedType = draggableType;
                //Drag was detected, start drap-and-drop gesture
                //Allow any transfer node
                Dragboard db = view.startDragAndDrop(TransferMode.MOVE);
    
                //Put ImageView on dragboard
                ClipboardContent cbContent = new ClipboardContent();
                cbContent.putImage(view.getImage());
                db.setContent(cbContent);
                view.setVisible(false);

                buildNonEntityDragHandlers(draggableType, sourceGridPane, targetGridPane);

                draggedEntity.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));

                draggedEntity.setImage(entityImage);

                draggedEntity.setVisible(true);
                draggedEntity.setMouseTransparent(true);
                draggedEntity.toFront();

                // IMPORTANT!!!
                // to be able to remove event handlers, need to use addEventHandler
                // https://stackoverflow.com/a/67283792
                targetGridPane.addEventHandler(DragEvent.DRAG_DROPPED, gridPaneSetOnDragDropped.get(draggableType));
                anchorPaneRoot.addEventHandler(DragEvent.DRAG_OVER, anchorPaneRootSetOnDragOver.get(draggableType));
                anchorPaneRoot.addEventHandler(DragEvent.DRAG_DROPPED, anchorPaneRootSetOnDragDropped.get(draggableType));

                for (Node n: targetGridPane.getChildren()){
                    // events for entering and exiting are attached to squares children because that impacts opacity change
                    // these do not affect visibility of original image...
                    // https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
                    gridPaneNodeSetOnDragEntered.put(draggableType, new EventHandler<DragEvent>() {
                        public void handle(DragEvent event) {
                            if (currentlyDraggedType == draggableType){
                            //The drag-and-drop gesture entered the target
                            //show the user that it is an actual gesture target
                                if(event.getGestureSource() != n && event.getDragboard().hasImage()){
                                    // get the x, y coord <same code as in buildNonEntityDragHandlers>
                                    Node node = event.getPickResult().getIntersectedNode();
                                    Integer cIndex = GridPane.getColumnIndex(node);
                                    Integer rIndex = GridPane.getRowIndex(node);
                                    int x = cIndex == null ? 0 : cIndex;
                                    int y = rIndex == null ? 0 : rIndex;
                                    //System.out.println(x + " " + y);

                                    switch (draggableType){
                                        case CARD:
                                            if (world.canPlaceBuilding(currentlyDraggedImage.getId(), x, y)) {
                                                n.setOpacity(0.7);
                                            }
                                            break;
                                        case ITEM:
                                            n.setOpacity(0.7);
                                            System.out.println("target Gridpane: " + targetGridPane.getId());
                                            break;
                                        default:
                                            //n.setOpacity(0.7);
                                            break;
                                    }

                                }
                            }
                            event.consume();
                        }
                    });
                    gridPaneNodeSetOnDragExited.put(draggableType, new EventHandler<DragEvent>() {
                        public void handle(DragEvent event) {
                            if (currentlyDraggedType == draggableType){
                                n.setOpacity(1);
                            }
                
                            event.consume();
                        }
                    });
                    n.addEventHandler(DragEvent.DRAG_ENTERED, gridPaneNodeSetOnDragEntered.get(draggableType));
                    n.addEventHandler(DragEvent.DRAG_EXITED, gridPaneNodeSetOnDragExited.get(draggableType));
                }
                event.consume();
            }
            
        });
    }

    /**
     * remove drag event handlers so that we don't process redundant events
     * this is particularly important for slower machines such as over VLAB.
     * @param draggableType either cards, or items in unequipped inventory
     * @param targetGridPane the gridpane to remove the drag event handlers from
     */
    private void removeDraggableDragEventHandlers(DRAGGABLE_TYPE draggableType, GridPane targetGridPane){
        // remove event handlers from nodes in children squares, from anchorPaneRoot, and squares
        targetGridPane.removeEventHandler(DragEvent.DRAG_DROPPED, gridPaneSetOnDragDropped.get(draggableType));

        anchorPaneRoot.removeEventHandler(DragEvent.DRAG_OVER, anchorPaneRootSetOnDragOver.get(draggableType));
        anchorPaneRoot.removeEventHandler(DragEvent.DRAG_DROPPED, anchorPaneRootSetOnDragDropped.get(draggableType));

        for (Node n: targetGridPane.getChildren()){
            n.removeEventHandler(DragEvent.DRAG_ENTERED, gridPaneNodeSetOnDragEntered.get(draggableType));
            n.removeEventHandler(DragEvent.DRAG_EXITED, gridPaneNodeSetOnDragExited.get(draggableType));
            n.setOpacity(1); // remove the "highlighting"
        }
    }

    /**
     * handle the pressing of keyboard keys.
     * Specifically, we should pause when pressing SPACE
     * @param event some keyboard key press
     */
    @FXML
    public void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
        case SPACE:
            if (isPaused){
                resume();
            }
            else{
                pause();
            }
            break;
        default:
            break;
        }
    }

    public void setMainMenuSwitcher(MenuSwitcher mainMenuSwitcher){
        // TODO = possibly set other menu switchers
        this.mainMenuSwitcher = mainMenuSwitcher;
    }

    /**
     * this method is triggered when click button to go to main menu in FXML
     * @throws IOException
     */
    @FXML
    private void switchToMainMenu() throws IOException {
        pause();
        effectSoundManager.playSoundEffect("src/musics/click.mp3", 0.1);
        mainMenuSwitcher.switchMenu();
    }
    @FXML
    private void resumeGame() {
        resume();
        // this is needed so that the pause button wont be highlighted anymore
        anchorPaneRoot.requestFocus();
        effectSoundManager.playSoundEffect("src/musics/click.mp3", 0.1);
    }
    @FXML
    private void pauseGame() {
        pause();
        // this is needed so that the pause button wont be highlighted anymore
        anchorPaneRoot.requestFocus();
        effectSoundManager.playSoundEffect("src/musics/click.mp3", 0.1);
    }

    @FXML
    private void usePotion() {
        world.getCharacter().usePotion();
        // this is needed so that the potion button wont be highlighted anymore and we can press <spacebar>
        anchorPaneRoot.requestFocus();
        effectSoundManager.playSoundEffect("src/musics/click.mp3", 0.1);
    }


    /**
     * Set a node in a GridPane to have its position track the position of an
     * entity in the world.
     *
     * By connecting the model with the view in this way, the model requires no
     * knowledge of the view and changes to the position of entities in the
     * model will automatically be reflected in the view.
     * 
     * note that this is put in the controller rather than the loader because we need to track positions of spawned entities such as enemy
     * or items which might need to be removed should be tracked here
     * 
     * NOTE teardown functions setup here also remove nodes from their GridPane. So it is vital this is handled in this Controller class
     * @param entity
     * @param node
     */
    private void trackPosition(Entity entity, Node node) {
        GridPane.setColumnIndex(node, entity.getX());
        GridPane.setRowIndex(node, entity.getY());

        ChangeListener<Number> xListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                GridPane.setColumnIndex(node, newValue.intValue());
            }
        };
        ChangeListener<Number> yListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                GridPane.setRowIndex(node, newValue.intValue());
            }
        };

        // if need to remove items from the equipped inventory, add code to remove from equipped inventory gridpane in the .onDetach part
        ListenerHandle handleX = ListenerHandles.createFor(entity.x(), node)
                                               .onAttach((o, l) -> o.addListener(xListener))
                                               .onDetach((o, l) -> {
                                                    o.removeListener(xListener);
                                                    entityImages.remove(node);
                                                    squares.getChildren().remove(node);
                                                    cards.getChildren().remove(node);
                                                    equippedItems.getChildren().remove(node);
                                                    unequippedInventory.getChildren().remove(node);
                                                })
                                               .buildAttached();
        ListenerHandle handleY = ListenerHandles.createFor(entity.y(), node)
                                               .onAttach((o, l) -> o.addListener(yListener))
                                               .onDetach((o, l) -> {
                                                   o.removeListener(yListener);
                                                   entityImages.remove(node);
                                                   squares.getChildren().remove(node);
                                                   cards.getChildren().remove(node);
                                                   equippedItems.getChildren().remove(node);
                                                   unequippedInventory.getChildren().remove(node);
                                                })
                                               .buildAttached();
        handleX.attach();
        handleY.attach();

        // this means that if we change boolean property in an entity tracked from here, position will stop being tracked
        // this wont work on character/path entities loaded from loader classes
        entity.shouldExist().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> obervable, Boolean oldValue, Boolean newValue) {
                handleX.detach();
                handleY.detach();
            }
        });
    }

    /**
     * we added this method to help with debugging so you could check your code is running on the application thread.
     * By running everything on the application thread, you will not need to worry about implementing locks, which is outside the scope of the course.
     * Always writing code running on the application thread will make the project easier, as long as you are not running time-consuming tasks.
     * We recommend only running code on the application thread, by using Timelines when you want to run multiple processes at once.
     * EventHandlers will run on the application thread.
     */
    private void printThreadingNotes(String currentMethodLabel){
        /*
        System.out.println("\n###########################################");
        System.out.println("current method = "+currentMethodLabel);
        System.out.println("In application thread? = "+Platform.isFxApplicationThread());
        System.out.println("Current system time = "+java.time.LocalDateTime.now().toString().replace('T', ' '));
        */
    }

    /**
     * Show the goals on the frontend in a window interface
     */
    @FXML
    public void showGoals() {
        pause();
        Stage newStage = new Stage();
        StackPane pane = new StackPane();
        ImageView background = new ImageView();
        background.setImage(new Image((new File("src/images/game_interface/goalBackground.png")).toURI().toString()));
        pane.getChildren().add(background);
        VBox vBox = new VBox();
        
        // margins: top/right/bottom/left
        vBox.setPadding(new Insets(85,0,0,60));
        
        // create the test for title and goals
        Label title = new Label("World Goals");
        title.setAlignment(Pos.CENTER);
        title.setFont(Font.loadFont("file:src/fonts/pokemon.ttf", 20));
        Label goalsList = new Label(world.goalsPrettyPrint());
        goalsList.setAlignment(Pos.CENTER);
        goalsList.setFont(Font.loadFont("file:src/fonts/pokemon.ttf", 15));
        vBox.getChildren().add(title);
        vBox.getChildren().add(goalsList);
        pane.getChildren().add(vBox); 
        
        Scene stageScene = new Scene(pane, 400, 550);
        newStage.setScene(stageScene);

        // this is needed so that the goals button wont be highlighted anymore
        anchorPaneRoot.requestFocus();
   
        newStage.show();
        effectSoundManager.playSoundEffect("src/musics/click.mp3", 0.1);
    }

    /**
     * Show the shop in the game through a window interface
     * @throws IOException
     */
    public void showShop() throws IOException {
        Stage newStage = new Stage();
        newStage.setTitle("PokeCentre");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ShopView.fxml"));
        
        Parent root = loader.load();

        ShopController controller = loader.<ShopController>getController();
        controller.setCharacter(world.getCharacter());
        controller.setWorld(world, this);
        
        Scene scene = new Scene(root);

        newStage.setScene(scene);
        newStage.show();
    }

    /**
     * Exit the shop and continue the game
     */
    public void exitShop() {
        if (world.gameState == GAME_STATE.SHOP) {
            world.gameState = GAME_STATE.LOOPING;
        }
    }

    /**
     * Load the overlay on the screen from the given overlay
     * @param overlay
     */
    private void loadWorldScreenOverlay(Node overlay) {
        if (worldScreen.getChildren().contains(overlay)) {
            return;
        }
        worldScreen.getChildren().add(overlay);
    }

    /**
     * Unload the given overlay from the screen
     * @param overlay
     */
    private void unloadWorldScreenOverlay(Node overlay) {
        if (worldScreen.getChildren().contains(overlay)) {
            worldScreen.getChildren().remove(overlay);
            return;
        }
    }

    /**
     * Control the state of the game for Losing, Winning and Shopping
     */
    public void handleGameState() {
        System.out.println("handling state: " + world.gameState);
        switch(world.gameState) {
            case LOSE:
                backgroundSoundManager.stopBackgroundMusic();
                effectSoundManager.playSoundEffect("src/musics/lose.mp3", 0.05);
                timeline.stop();
                loadWorldScreenOverlay(lossOverlay);
                break;
            case WIN:
                backgroundSoundManager.stopBackgroundMusic();
                effectSoundManager.playSoundEffect("src/musics/win.mp3", 0.05);
                timeline.stop();
                loadWorldScreenOverlay(winOverlay);
                try {
                    updateCampaignProgress();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
                break;
            case SHOP:
                // Calculate if this loop is one where shopping is available
                int loopNum = world.getLoop() - 1;
                int i = 1;
                while (loopNum > 0) {
                    loopNum = loopNum - i;
                    i = i + 1;
                }
                // If it is available, show the shop
                if (loopNum == 0) {
                    pause();
                    try {
                        showShop();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                // Else, continue the game
                else {
                    exitShop();
                }
                break;

            default:
                return;
        }
    }

    /**
     * Updates the campaign progress json that we've completed the stage
     */
    private void updateCampaignProgress() throws FileNotFoundException {
        int stageNumber = world.getStageNumber();
        JSONObject json = new JSONObject(new JSONTokener(new FileReader("src/unsw/loopmania/campaignProgress.json")));
        int currLevel = json.getInt("curr_level");
        // if we complete the latest unlocked stage, increment
        if (stageNumber == currLevel) {
            System.out.println("\nIncrementing new curr level\n");
            json.put("curr_level", stageNumber + 1);

           //Write into the file
           try (FileWriter file = new FileWriter("src/unsw/loopmania/campaignProgress.json"))  {
               file.write(json.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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
        anchorPaneRoot.requestFocus();
    }
}
