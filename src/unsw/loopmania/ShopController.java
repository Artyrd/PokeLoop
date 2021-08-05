package unsw.loopmania;

import java.io.File;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import unsw.loopmania.cards.Card;
import unsw.loopmania.enemies.Boss;
import unsw.loopmania.enemies.Enemy;
import unsw.loopmania.items.ConfusedItem;
import unsw.loopmania.items.DoggieCoin;
import unsw.loopmania.items.Item;
import unsw.loopmania.items.Potion;
import unsw.loopmania.items.TheOneRing;
import unsw.loopmania.items.Protection;

public class ShopController {
    
    // List of items that can be bought in a standard shop
    private ArrayList<Item> items;
    // List of protection items to be removed if there is a limit
    private ArrayList<StackPane> protectionPanes;
    
    // Initialise world data
    private LoopManiaWorld world;
    private LoopManiaWorldController worldController;
    private ImageManager images;
    private SoundManager sounds;
    private Character character;
    private Shop shop;

    public ShopController() {
        this.shop = new Shop();
        this.images = new ImageManager();
        this.sounds = new SoundManager();

        this.protectionPanes = new ArrayList<StackPane>();
    }

    /**
     * Read in character data
     * @param character
     */
    public void setCharacter(Character character) {
        this.character = character;
    }

    /**
     * Read in world data and its controller
     * Set constraints on the shop according to the world game mode
     * @param world
     * @param controller
     */
    public void setWorld(LoopManiaWorld world, LoopManiaWorldController controller) {
        this.world = world;
        this.worldController = controller;
        if (world.getGamemode().equals("Survival")) {
            this.shop.setPotionLimit(1);
            this.shop.setDefenseLimit(0);
        }
        else if (world.getGamemode().equals("Berserker")) {
            this.shop.setPotionLimit(5);
            this.shop.setDefenseLimit(1);
        }
        // confusing mode has no shop changes
        else {
            this.shop.setPotionLimit(5);
            this.shop.setDefenseLimit(0);
        }
    }

    @FXML
    private Pane root;

    @FXML
    private ScrollPane buyScroll;

    @FXML
    private VBox buyVbox;

    @FXML
    private ScrollPane sellScroll;

    @FXML
    private VBox sellVbox;

    @FXML
    private Button exit;

    @FXML
    private ImageView background;

    @FXML
    private ImageView background1;

    /**
     * On button click, close the shop and exit, resuming the game
     * @param event
     */
    @FXML
    void returnToWorld(ActionEvent event) {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
        worldController.exitShop();
        worldController.resume();
        sounds.playSoundEffect("src/musics/click.mp3", 0.1);
    }

    /**
     * Buy the given item and remove it from view from the shop
     * If the item is a protection item and there is a limit on the number that can be bought,
     * remove the rest
     * @param item
     * @param itemView
     */
    public void buyItem(Item item, StackPane itemView) {
        // If the character fails to buy the item
        Item purchasedItem = shop.buyItem(character, item);

        if (purchasedItem == null) {
            System.out.println("Failed to purchase: " + item.getID());
            System.out.println("Insufficient funds? ; Current Funds: " + character.getInventory().getGold());
            return;
        }
        // Else, remove the item from view after buying
        buyVbox.getChildren().remove(itemView);
        System.out.println("Total inventory gold: " + character.getInventory().getGold());


        // Remove the rest of the defense items from view
        if (item instanceof Protection && shop.getDefenseLimit() == 1) {
            protectionPanes.remove(itemView);
            for (StackPane stock : protectionPanes) {
                buyVbox.getChildren().remove(stock);
            }
        }

        System.out.println(item);
        // Load the newly bought item into the inventory
        if (!item.getID().equals("Potion")) {
            worldController.onLoad(purchasedItem);
        }
        
        // Play click sound
        sounds.playSoundEffect("src/music/click.mp3", 0.1);
    }

    /**
     * Sell the given item or card and remove it from view from the shop
     * @param item
     * @param card
     * @param itemView
     */
    public void sellItem(Item item, Card card, StackPane itemView) {
        // Sell the item
        if (item != null) {
            shop.sellItem(character, item);
            System.out.println("Sold: " + item.getName() + ", Gained: " + item.getPrice());
        }
        // Sell the card
        else if (card != null) {
            shop.sellCard(character, world, card);
            System.out.println("Sold: " + card.getID() + ", Gained: " + card.getValue());
        }
        // Remove the item from view
        sellVbox.getChildren().remove(itemView);
        System.out.println("Total inventory gold: " + character.getInventory().getGold());
        
        // Play click sound
        sounds.playSoundEffect("src/music/click.mp3", 0.1);
    }

    @FXML 
    public void initialize() {

        root.setStyle("-fx-font-family: Serif;");

        // Run the initialiser later after we input the data from set world and set character
        Platform.runLater(() -> {
            this.items = new ArrayList<Item>(shop.getItems());

            initialiseBuy();
            initialiseSell();
        });
    }

    /**
     * Initialise the buyside of the shop
     */
    public void initialiseBuy() {
        // For items available in the shop, create a stackpane presenting them
        // for the frontend to use
        for (int i = 0; i < items.size(); i++) {
            Item forSaleItem = items.get(i);

            StackPane saleItem = new StackPane();
            GridPane saleGrid = setupDisplay(forSaleItem, null);

            Button buyButton = new Button("Buy: " + forSaleItem.getPrice());
            buyButton.setStyle("-fx-font-family: Serif;");
            // Set the action of the buy button to execute the buying method
            buyButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    buyItem(forSaleItem, saleItem);
                }
            });
            saleGrid.add(buyButton, 2, 0);

            saleItem.getChildren().add(saleGrid);
            buyVbox.getChildren().add(saleItem);

            // If the item is a protection item, add it to a special list for later removal
            if (forSaleItem instanceof Protection) {
                protectionPanes.add(saleItem);
            }
        }
        // Add the list of purchasable items to a scrollpane
        buyScroll.setContent(buyVbox);
    }

    /**
     * Initialise the sellside of the shop
     */
    public void initialiseSell() {
        // For items in the character inventory, create a stackpane presenting them
        // for the frontend to use
        for (int i = 0; i < character.getInventory().getItemCount(); i++) {
            Item forSaleItem = character.getInventory().getItemByPositionInUnequippedInventoryItems(i);
            
            StackPane sellableItem = new StackPane();
            GridPane sellableItemGrid = setupDisplay(forSaleItem, null);           
            
            if (forSaleItem instanceof DoggieCoin) {
                boolean bossExist = false;
                for (Enemy enemy : world.getEnemies()) {
                    if (enemy instanceof Boss) {
                        bossExist = true;
                        break;
                    }
                }
                ((DoggieCoin) forSaleItem).setMarketPrice(bossExist);
            }
             
            int forSaleItemPrice = forSaleItem.getPrice() * (forSaleItem.getCurrDurability() / forSaleItem.getMaxDurability());
            Button sellButton = new Button("Sell: " + forSaleItemPrice);
            sellButton.setStyle("-fx-font-family: Serif;");
            // Set the action of the buy button to execute the selling method
            sellButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    sellItem(forSaleItem, null, sellableItem);
                }
            });
            sellableItemGrid.add(sellButton, 2, 0);

            sellableItem.getChildren().add(sellableItemGrid);
            sellVbox.getChildren().add(sellableItem);
        }

        // For cards at the character's disposal, create a stackpane presenting
        // them for the frontend to use
        for (int i = 0; i < world.getCardCount(); i++) {
            Card forSaleCard = world.getCards().get(i);

            StackPane sellableItem = new StackPane();
            GridPane sellableItemGrid = setupDisplay(null, forSaleCard);

            Button sellButton = new Button("Sell: " + forSaleCard.getValue());
            sellButton.setStyle("-fx-font-family: Serif;");
            // Set the action of the buy button to execute the selling method
            sellButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    sellItem(null, forSaleCard, sellableItem);
                }
            });
            sellableItemGrid.add(sellButton, 2, 0);

            sellableItem.getChildren().add(sellableItemGrid);
            sellVbox.getChildren().add(sellableItem);
        }   
        
        // Add the list of sellable items to a scrollpane
        sellScroll.setContent(sellVbox);
    }

    /**
     * Helper method that sets up the gridpane to contain an image of the item
     * and the name of the item within confined column constraints
     * @param item to be displayed in the shop
     * @param card to be displayed in the shop
     * @return a gridpane holding all display information
     */
    public GridPane setupDisplay(Item item, Card card) {
        GridPane grid = new GridPane();

        // Set column widths to make alignments
        ColumnConstraints col1 = new ColumnConstraints(90);
        ColumnConstraints col2 = new ColumnConstraints(360);
        ColumnConstraints col3 = new ColumnConstraints(70);
        grid.getColumnConstraints().addAll(col1,col2,col3);
        
        // Set the image size and initialise a text object
        ImageView image = new ImageView();
        image.setFitHeight(75.0);
        image.setFitWidth(75.0);
        
        Text text = new Text();
        // For an item, set item image and item name
        if (item != null) {
            image.setImage(this.images.getItemImage(item));
            if (item instanceof Potion || item instanceof TheOneRing || 
            (item instanceof ConfusedItem && ((ConfusedItem) item).getBaseItem().equals("TheOneRing"))) {
                
                text = new Text(item.getName());
            }
            else {
                text = new Text(item.getName() + " (" + item.getStar() + " Star)");
            }
        }
        // For a card, set card iamge and card name
        else if (card != null) {
            image.setImage(this.images.getCardImage(card));
            text = new Text(card.getName());
        }
        
        // Add it to the grid and return
        text.setFont(Font.loadFont("file:src/fonts/pokemon.ttf", 10));
        grid.add(image, 0, 0);
        grid.add(text, 1, 0);

        return grid;
    }
}
