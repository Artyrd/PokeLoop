package unsw.loopmania;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.javatuples.Pair;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.battle.BattleSimulator;
import unsw.loopmania.battle.BattleManager;
import unsw.loopmania.buildings.*;
import unsw.loopmania.cards.*;
import unsw.loopmania.enemies.*;
import unsw.loopmania.goal.Goal;
import unsw.loopmania.items.*;
import unsw.loopmania.movement.PathPosition;
import unsw.loopmania.observer.ObserverManager;

/**
 * A backend world.
 *
 * A world can contain many entities, each occupy a square. More than one
 * entity can occupy the same square.
 */
public class LoopManiaWorld {
    // TODO = add additional backend functionality
    public enum GAME_STATE {
        LOOPING,
        LOSE,
        WIN,
        SHOP
    }

    public GAME_STATE gameState;

    public static final int unequippedInventoryWidth = 4;
    public static final int unequippedInventoryHeight = 4;

    /**
     * width of the world in GridPane cells
     */
    private int width;

    /**
     * height of the world in GridPane cells
     */
    private int height;

    /**
     * the type of game mode initialised
     */
    private String gamemode;

    /**
     * the campaign stage level number
     */
    private int stageNumber;

    /**
     * generic entitites - i.e. those which don't have dedicated fields
     */
    private List<Entity> nonSpecifiedEntities;

    private Character character;

    private List<Enemy> enemies;

    private List<Boss> aliveBosses;
    private List<Boss> defeatedBosses;
    private int spawnedDoggies;
    private int spawnedElanCards;

    private List<Card> cardEntities;

    private List<Building> buildingEntities;

    private List<MovingEntity> activeStructures; // structures that are spawned as moving entities for battle purposes
    private List<MovingEntity> friendlyEntities; // moving entities in the world that fight with the character
    private int spawnedDragonCards;

    private List<Ally> allies;
    private SimpleIntegerProperty numAllies;

    private List<SpawnableItem> spawnedItems;

    private SimpleIntegerProperty loopNum; // the loop number

    private Goal worldGoal;

    private List<Item> possibleRareItems;

    /**
     * A pointer to the character's inventory
     */
    public Inventory inventory;
 
    /**
     * list of x,y coordinate pairs in the order by which moving entities traverse them
     */
    private List<Pair<Integer, Integer>> orderedPath;

    private CardManager cardManager;

    private StageBossManager stageBossManager;
    private ObserverManager observerManager;

    private BattleManager battleManager;

    /**
     * create the world (constructor)
     * 
     * @param width width of world in number of cells
     * @param height height of world in number of cells
     * @param orderedPath ordered list of x, y coordinate pairs representing position of path cells in world
     */
    public LoopManiaWorld(int width, int height, List<Pair<Integer, Integer>> orderedPath) {
        this.width = width;
        this.height = height;
        this.gamemode = "Standard";
        // create the inventory (this will be linked to the character in the Loader)
        inventory = new Inventory(unequippedInventoryWidth, unequippedInventoryHeight);
        cardManager = new CardManager();
        observerManager = new ObserverManager(this);
        battleManager = new BattleManager(this);
        this.orderedPath = orderedPath;
        character = null;
        enemies = new ArrayList<>();
        aliveBosses = new ArrayList<>();
        defeatedBosses = new ArrayList<>();
        spawnedDoggies = 0;
        spawnedElanCards = 0;
        stageBossManager = null;
        allies = new ArrayList<>();
        numAllies = new SimpleIntegerProperty(0);
        cardEntities = new ArrayList<>();
        buildingEntities = new ArrayList<>();
        activeStructures = new ArrayList<>();
        friendlyEntities = new ArrayList<>();
        spawnedDragonCards = 0;
        nonSpecifiedEntities = new ArrayList<>();
        spawnedItems = new ArrayList<>();
        possibleRareItems = new ArrayList<>();
        loopNum = new SimpleIntegerProperty(1);

        gameState = GAME_STATE.LOOPING;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getGamemode() {
        return this.gamemode;
    }

    public void setGameStateLose(){
        gameState = GAME_STATE.LOSE;
    }

    public void setGamemode(String gamemode) {
        this.gamemode = gamemode;
    }

    /**
     * set the character. This is necessary because it is loaded as a special entity out of the file
     * @param character the character
     */
    public void setCharacter(Character character) {
        this.character = character;
    }

    /**
     * adds an enemy to the enemy list. For conditions that fall outside natural spawns
     * @param enemy
     */
    public void addEnemy(Enemy enemy) {
        this.enemies.add(enemy);
        updateObservers();
    }

    /**
     * spawns a boss in the backend
     * adds to both aliveBosses and Enemies lists
     * @param boss
     */
    public void addBoss(Boss boss){
        this.aliveBosses.add(boss);
        addEnemy(boss);
    }

    /**
     * adds an ally to the ally list
     * @param ally
     */
    public void addAlly(Ally ally) {
        this.allies.add(ally);
        numAllies.set(allies.size());
    }

    public void setNumAllies(int num){
        numAllies.set(num);
    }

    /**
     * adds a building to the building list
     * @param building
     */
    public void addBuilding (Building building) {
        buildingEntities.add(building);
        updateObservers();
    }
    /**
     * adds an active structure such as a tower battler to the world
     * @param entity
     */
    public void addFriendlyEntity(MovingEntity entity){
        friendlyEntities.add(entity);
    }

    /**
     * adds an active structure such as a tower battler to the world
     * @param entity
     */
    public void addActiveStructures(MovingEntity entity){
        activeStructures.add(entity);
    }
    /**
     * @deprecated you shouldnt be using this, use another add___ that is more specific
     * add a generic entity (without it's own dedicated method for adding to the world)
     * @param entity
     */
    public void addEntity(Entity entity) {
        // for adding non-specific entities (ones without another dedicated list)
        nonSpecifiedEntities.add(entity);
    }

    /**
     * adds an item to the list of rare items that could appear in this world
     */
    public void addPossibleRareItem(Item item) {
        possibleRareItems.add(item);
    }

    public List<Item> getPossibleRareItems() {
        return possibleRareItems;
    }



    /**
     * spawn an item in the world, adds it to the inventory and return the item entity
     * note that gold and potions dont appear in the inventory and hence return null
     * @return an item to be spawned in the controller as a JavaFX node
     */
    public Item addUnequippedItem(String itemID){
        Item item = character.addItem(itemID);
        return item;
    }

    /**
     * add an existing item to the inventory and return the item entity
     * note that gold and potions dont appear in the inventory and hence return null
     * @return an item to be moved in the controller as a JavaFX node
     */
    public Item addUnequippedItem(Item item){
        return character.addItem(item);
    }

    /**
     * gets the current loop number the character is on
     */
    public int getLoop() {
        return loopNum.get();
    }

    /**
     * increments the loop number
     */
    public void addLoop() {
        loopNum.set(loopNum.get() + 1);;
    }

    /**
     * gets the character of the world
     */
    public Character getCharacter() {
        return character;
    }

    /**
     * gets the enemies of the world
     */
    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Boss> getAliveBosses(){
        return aliveBosses;
    }

    public List<Boss> getDefeatedBosses(){
        return defeatedBosses;
    }

    public List<Ally> getAllies() {
        return allies;
    }

    public List<Building> getBuildings() {
        return buildingEntities;
    }

    public List<MovingEntity> getActiveStructures() {
        return activeStructures;
    }

    public List<MovingEntity> getFriendlyEntities() {
        return friendlyEntities;
    }

    public List<SpawnableItem> getSpawnedItems() {
        return spawnedItems;
    }
    /**
     * Gets all the moving entities that exist in the world and move about
     * @return a List of moving entities
     */
    public List<MovingEntity> getMovingEntities() {
        List<MovingEntity> allEntities = new ArrayList<MovingEntity>();
        allEntities.add(character);
        allEntities.addAll(enemies);
        allEntities.addAll(friendlyEntities);

        return allEntities;
    }
    /**
     * Gets all the moving entities that exist in the world (not inventory/ equips)
     * @return a List of entities including: MovingEntity's, StaticEntity's
     */
    public List<Entity> getEntities() {
        List<Entity> allEntities = new ArrayList<Entity>();
        allEntities.add(character);
        allEntities.addAll(enemies);
        allEntities.addAll(allies);
        allEntities.addAll(buildingEntities);
        allEntities.addAll(nonSpecifiedEntities);
        allEntities.addAll(spawnedItems);

        return allEntities;
    }

    /**
     * Given a chance bound (default 1000), randomly returns a random rare item
     * if multiple rareitems exist, they all have an equal chance of being spat out
     * chance of rare item appearing == 1/bound
     * @return null, or a rare item if we successfully land a hit within the given bound
     */
    public Item generateRareItem(int bound) {

        if (possibleRareItems.size() == 0) {
            return null;
        }
        // 1/bound chance of not returning null
        Random random = new Random();
        int chance = random.nextInt(bound);
        if (chance != 0) {
            return null;
        }
        // got a hit! return a random rare item...
        int index = random.nextInt(possibleRareItems.size());
        // pop out the rare item and give it to the player
        Item rareItem = possibleRareItems.get(index);
        possibleRareItems.remove(rareItem); // <- so same item doesnt appear again
        return rareItem;
    }

    /**
     * Registers all subjects and notifies observers
     */
    private void updateObservers() {
        observerManager.updateObservers();
    }

    /**
     * spawns enemies if the conditions warrant it, adds to world
     * @return list of the enemies to be displayed on screen
     */
    public List<Enemy> possiblySpawnEnemies() {
        List<Enemy> spawningEnemies = new ArrayList<>();
        // spawn slugs
        Pair<Integer, Integer> pos = possiblyGetSpawnPosition(5); // 1/5 chance
        // max of 2 slugs in the empty world at once
        if (pos != null && enemies.size() < 2){
            int indexInPath = orderedPath.indexOf(pos);
            Slug enemy = new Slug(new PathPosition(indexInPath, orderedPath));
            enemies.add(enemy);
            spawningEnemies.add(enemy);
        }
        // spawn doggie at loop 20, only once, ever.
        // TODO change 2 -> 20
        if (loopNum.get() == 20 && spawnedDoggies < 1) {
            // spawn doggie on the otherside of the map
            Doggie doggie = new Doggie(new PathPosition(orderedPath.size()/2, orderedPath));
            addBoss(doggie);
            spawnedDoggies ++;
            spawningEnemies.add(doggie);
        }

        return spawningEnemies;
    }

    /**
     * spawns items if the conditions warrant it, adds to world
     * @return list of the enemies to be displayed on screen
     */
    public List<SpawnableItem> possiblySpawnItems() {
        Pair<Integer, Integer> pos = possiblyGetSpawnPosition(20); // 1/20 chance
        List<SpawnableItem> spawningItems = new ArrayList<>();
        // max of 1 spawned item in the world at once
        if (pos != null && spawnedItems.size() < 2) {
            // randomly spawn a potion or gold
            SpawnableItem item;
            int indexInPath = orderedPath.indexOf(pos);
            Random random = new Random();
            // 70% chance gold, 30% potion
            if (random.nextInt(10) < 7 ) {
                item = new Gold(new PathPosition(indexInPath, orderedPath));
            } else {
                item = new Potion(new PathPosition(indexInPath, orderedPath));
            }
            spawnedItems.add(item);
            spawningItems.add(item);
        }
        return spawningItems;
    }


    /**
     * Possibly reward the player cards when certain conditions are met.
     * @return a list of new cards to be given to the player
     */
    public List<Card> possiblyGiveCards() {
    //public List<String> possiblyGiveCards() {
        ArrayList<Card> cards = new ArrayList<Card>();
        // give Elan card after 40 loops and 10k xp
        // TODO change to real values after demo
        if (loopNum.get()== 40 && character.getCummulativeXP() >= 10000 && spawnedElanCards < 1) {
            cards.add(new ElanSpawnerCard(null, null));
            spawnedElanCards++;
        }
        if (stageBossManager != null && stageBossManager.canChallenge(character, this)) {
            cards.add(stageBossManager.giveCard());
        }
        // give Dragon card after character reaches lvl 20
        // TODO change to real values after demo
        if (character.getLevel() >= 20 && spawnedDragonCards < 1) {
            cards.add(new DragonSpawnerCard(null, null));
            spawnedDragonCards++;
        }



        return cards;
    }

    /**
     * run the expected battles in the world, based on current world state
     * @return list of enemies which have been killed
     */
    public BattleSimulator findBattle() {
        return battleManager.findBattle();      
    }

    /**
     * @deprecated - Now ran by the battle controller
     * ONLY FOR TESTING!
     * Runs the battle simulation, 
     * @param battle - a battle found by findBattle()
     * @return - a list of defeated Enemies
     */
    public List<Enemy> runBattle(BattleSimulator battle) {
        return battleManager.runBattle(battle);
    }
    
    /**
     * Removes any dead enemies from the world after a battle is complete
     * @param defeatedEnemies
     */
    public void cleanupBattle(List<Enemy> defeatedEnemies) {
        battleManager.cleanupBattle(defeatedEnemies);
    }


    /**
     * get all cards
     * @return list of all cards
     */
    public List<Card> getCards() {
        return this.cardEntities;
    }

    /**
     * get number of cards in the world
     * @return number of cards
     */
    public int getCardCount() {
        return cardEntities.size();
    }

    /**
     * spawn a card in the world and return the card entity
     * @return a card to be spawned in the controller as a JavaFX node
     */
    public Card loadCard(String cardID) {
        // if adding more cards than have, remove the first card...
        if (cardEntities.size() >= getWidth()){
            Card removedCard = cardEntities.get(0);
            this.inventory.addGold(character, removedCard.getValue());
            removeCard(0);
        }
        Card newCard = cardManager.newCardFromID(cardID, new SimpleIntegerProperty(cardEntities.size()), new SimpleIntegerProperty(0));
        cardEntities.add(newCard);
        return newCard;
    }

    /**
     * place an instantiated card into the world and return the card entity
     * @return a card to be spawned in the controller as a JavaFX node
     */
    public Card loadCard(Card card) {
        // if adding more cards than have, remove the first card...
        if (cardEntities.size() >= getWidth()){
            Card removedCard = cardEntities.get(0);
            this.inventory.addGold(character, removedCard.getValue());
            removeCard(0);
        }
        card.changeIntegerProperty(new SimpleIntegerProperty(cardEntities.size()), new SimpleIntegerProperty(0));
        cardEntities.add(card);
        return card;
    }

    /**
     * remove card at a particular index of cards (position in gridpane of unplayed cards)
     * @param index the index of the card, from 0 to length-1
     */
    public void removeCard(int index) {
        Card c = cardEntities.get(index);
        int x = c.getX();
        c.destroy();
        cardEntities.remove(index);
        shiftCardsDownFromXCoordinate(x);
    }

    /**
     * run moves which occur with every tick without needing to spawn anything immediately
     */
    public void runTickMoves(){
        character.move();
        moveBasicEnemies();
        moveFriendlyEntities();
    }

    /**
     * shift card coordinates down starting from x coordinate
     * @param x x coordinate which can range from 0 to width-1
     */
    private void shiftCardsDownFromXCoordinate(int x) {
        for (Card c: cardEntities){
            if (c.getX() >= x){
                c.x().set(c.getX()-1);
            }
        }
    }

    /**
     * move all enemies
     */
    private void moveBasicEnemies() {
        for (Enemy e: enemies){
            e.move();
        }
    }
    /**
     * move all enemies
     */
    private void moveFriendlyEntities() {
        for (MovingEntity e: friendlyEntities){
            e.move();
        }
    }
    /**
     * get a randomly generated position which could be used to spawn an enemy or item
     * @param bound - the bound of the random distribution, depending on how rare this spawn should be
     * @return null if random choice is that wont be spawning an enemy or it isn't possible, or random coordinate pair if should go ahead
     */
    private Pair<Integer, Integer> possiblyGetSpawnPosition(int bound) {
        
        // has a chance spawning a basic enemy on a tile the character isn't on or immediately before or after (currently space required = 2)...
        Random rand = new Random();
        //int choice = rand.nextInt(2); // <- change based on spec... currently low value for dev purposes...
        int choice = rand.nextInt(bound); 
        if ((choice == 0)){
            List<Pair<Integer, Integer>> orderedPathSpawnCandidates = new ArrayList<>();
            int indexPosition = orderedPath.indexOf(new Pair<Integer, Integer>(character.getX(), character.getY()));
            // inclusive start and exclusive end of range of positions not allowed
            int startNotAllowed = (indexPosition - 2 + orderedPath.size())%orderedPath.size();
            int endNotAllowed = (indexPosition + 3)%orderedPath.size();
            // note terminating condition has to be != rather than < since wrap around...
            for (int i=endNotAllowed; i!=startNotAllowed; i=(i+1)%orderedPath.size()){
                orderedPathSpawnCandidates.add(orderedPath.get(i));
            }

            // choose random choice
            Pair<Integer, Integer> spawnPosition = orderedPathSpawnCandidates.get(rand.nextInt(orderedPathSpawnCandidates.size()));

            return spawnPosition;
        }
        return null;
    }

    /**
     * remove a card by its x, y coordinates and places the new Building
     * @param cardNodeX x index from 0 to width-1 of card to be removed
     * @param cardNodeY y index from 0 to height-1 of card to be removed
     * @param buildingNodeX x index from 0 to width-1 of building to be added
     * @param buildingNodeY y index from 0 to height-1 of building to be added
     * @return the new Building, null if invalid location
     */
    public Building convertCardToBuildingByCoordinates(int cardNodeX, int cardNodeY, int buildingNodeX, int buildingNodeY) {
        // start by getting card
        Card card = null;
        for (Card c: cardEntities){
            if ((c.getX() == cardNodeX) && (c.getY() == cardNodeY)){
                card = c;
                break;
            }
        }
        if (card == null) {
            System.err.println("cant find card");
            return null;
        }
        // check if location is valid
        if (!canPlaceBuilding(card.getID(), buildingNodeX, buildingNodeY)) {
            return null;
        }

        // now spawn building
        Building newBuilding = card.getBuiltBuilding(new SimpleIntegerProperty(buildingNodeX), new SimpleIntegerProperty(buildingNodeY));
        buildingEntities.add(newBuilding);

        // destroy the card
        card.destroy();
        cardEntities.remove(card);
        shiftCardsDownFromXCoordinate(cardNodeX);

        return newBuilding;
    }

    /**
     * Check if the coordinates given is a path
     * @param x
     * @param y
     * @return if the coordinate is a path 
     */
    public boolean isPath(int x, int y) {
        if (orderedPath.contains(new Pair<Integer, Integer>(x,y))) {
            return true;
        }
        return false;
    }


    public List<Pair<Integer, Integer>> getOrderedPath() {
        return orderedPath;
    }

    /**
     * Update buildings in the world
     */
    public void updateBuildings() {
        for (Building building: buildingEntities) {
            //System.out.println(building.getID());
            building.update(this);
        }
        // remove buildings that shouldnt exist
        // IMPORTANT = we remove buildings here,
        // if we remove building in prior loop, we get java.util.ConcurrentModificationException
        // due to mutating list we're iterating over
        for (Iterator<Building> iterator = buildingEntities.iterator(); iterator.hasNext();) {
            Building building = iterator.next();
            if(!building.shouldExist().get()) {
                iterator.remove();
            }
        }
    }

    /**
     * Update moving entities in the world, if their HP is below 0, remove them from the world
     */
    public void updateMovingEntities() {
        // remove MovingEntities that shouldnt exist/ died of natural causes outside of battle
        // use iterator to safely remove while iterating the list
        for (Iterator<Enemy> iterator = enemies.iterator(); iterator.hasNext();) {
            Enemy enemy = iterator.next();
            if(enemy.getCurrHP() <= 0) {
                // signal frontend its dead
                enemy.destroy();
                // remove from list
                iterator.remove();
            }
        }
        // allies dont die of natural causes as of yet

        // other...? Milestone3
    }

    /**
     * Apply any statuses on entities in the world
     */
    public void runStatusWorldTicks() {
        character.runStatusWorldTicks();
        for (Enemy e: enemies){
            e.runStatusWorldTicks();
        }
    }

    public void setWorldGoal(Goal worldGoal){
        this.worldGoal = worldGoal;
    }

    /**
     * Evaluates whether a goal has been reached 
     * @return true if all goals reached, false if not reached
     */
    public boolean reachedGoal() {
        if (worldGoal == null) {
            System.err.println("\n\n CANT FIND GOAL \n\n");
            return false;
        }
        return worldGoal.evaluateGoalReached(character, this) && stageBossManager.defeatedStageBossGoal(this);
        
    }

    /**
     * Displays goal of the world as a string and current progress in those goals
     * @return
     */
    public String goalsPrettyPrint(){
        if (worldGoal == null) {
            System.err.println("\n CANT FIND GOAL \n");
            return "no goals yet!";
        }

        String goalString = worldGoal.showGoals(character, this);
        goalString = goalString.trim();

        //String firstWords = goalString.substring(0, goalString.lastIndexOf("\n"));
        //return firstWords;
        /*
        if(worldGoal.getGoalType().equals("AND")){
        }*/
        
        String challengeString = stageBossManager.showChallengeGoal(character, this);
        challengeString = challengeString.trim();
        String gymGoal = stageBossManager.showStageBossGoal(character, this);

        return "\n" + goalString + "\n\n" + gymGoal + "\n\n\n" + "Gym Battle Conditions\n" + challengeString;
        //return "World Goals:\n" + goalString + "\n\n" + gymGoal + "\n" + "Gym Battle Conditions\n" + challengeString + "\n\nGym Battle Challenge Goal:\n" + gymGoal;
    }
    /**
     * Checks if the building is allowed to be placed at the given x, y
     * @param cardID the ID of the card
     * @param x x coord in the world
     * @param y y coord in the world
     * @return true if placement is legal, false otherwise
     */
    public boolean canPlaceBuilding(String cardID, int x, int y) {
        // first check if there's a building underneath
        for (Building building: buildingEntities) {
            if (building.getX() == x && building.getY() == y) {
                return false;
            }
        }
        // next check the card/ building's placement conditions
        Card helperCard = cardManager.newCardFromID(cardID, null, null);
        // ask helper card if it can be placed at the coordinates
        return helperCard.canPlaceBuilding(x, y, this);
   }

    /**
     * Helper function to determine if the given x,y coord has a path tile adjacent to it (inc diagonal)
     * @param x the x coord
     * @param y the y coord
     * @return true if the coord is adjacent to the path, false otherwise
     */
    public boolean adjacentToPath(int x, int y) {
        if (isPath(x, y)) {
            // not adjacent to path if it is on the path
            return false;
        }
        int startX = x - 1;
        int startY = y - 1;

        // looks at a 3x3 square with the Spawner in the centre
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (isPath(startX + i, startY + j)) {
                    // we found a path tile adjacent to us
                    return true;
                }
            }
        }
        // no path tile found
        return false;
    }

    /*
     ############################
          FRONT END STUFF
     ############################
     */

    public SimpleIntegerProperty getLoopNumProperty() {
        return loopNum;
    }

    public SimpleIntegerProperty getAllyNumProperty() {
        return numAllies;
    }

    /**
     * for campaign, the world's stage number
     */
    public void setStageNumber(int stageNumber) {
        this.stageNumber = stageNumber;
    }

    /**
     * @return the world's campaign stage number
     */
    public int getStageNumber() {
        return stageNumber;
    }

    public void setStageBossManager(StageBossManager stageBossManager) {
        this.stageBossManager = stageBossManager;
    }


}
