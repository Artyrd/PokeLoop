package unsw.loopmania;

import java.util.ArrayList;
import java.util.List;
import org.javatuples.Pair;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.items.Item;
import unsw.loopmania.items.Gold;
import unsw.loopmania.items.Potion;

public class Inventory {

    private SimpleIntegerProperty gold;
    private SimpleIntegerProperty potions;
    private double potionHP;

    EquipComposite equippedItems;
    private List<Item> unequippedInventoryItems;
    private int inventoryWidth;
    private int inventoryHeight;

    private ItemManager itemManager;

    public Inventory(int inventoryWidth ,int inventoryHeight) {
        gold = new SimpleIntegerProperty(0);
        potions = new SimpleIntegerProperty(0);
        potionHP = 30;
        this.inventoryWidth = inventoryWidth;
        this.inventoryHeight = inventoryHeight;
        unequippedInventoryItems = new ArrayList<>();
        equippedItems = new EquipComposite();
        itemManager = new ItemManager();
        
    }

    public EquipComposite getEquipComposite() {
        return equippedItems;
    }

    public int getItemCount() {
        return unequippedInventoryItems.size();
    }
    public int getMaxInventorySize() {
        return inventoryHeight * inventoryWidth;
    }

    public int getGold() {
        return gold.get();
    }

    public int getPotions() {
        return potions.get();
    }

    /**
     * Consumes a potion
     * @return the hp restored
     */
    public double usePotion(Character character) {
        if (potions.get() > 0) {
            potions.set(potions.get() - 1);
            character.restoreHP(potionHP);
            return potionHP;
        }
        return 0;
    }

    /**
     * Equip the specified item to the Character and figures out the item's equipslot.
     * If its from the inventory, move it accordingly.
     * Add the replacedItem back in the inventory.
     * @param item the item we wish to equip
     * @return the item the equipped item replaced
     */
    public Item equipItem(Character character, Item item) {
        if (unequippedInventoryItems.contains(item)) {
            unequippedInventoryItems.remove(item);
        }
        Item replacedItem = equippedItems.equipItemLeaf(new EquipLeaf(item));
        // put the replaced item back in the inventory
        addItem(character, replacedItem);
        return replacedItem;
    }

    /**
     * Give the character a set amount of gold
     */
    public void addGold(Character character, int value){
        // gets the current amount of gold and add the value on top
        gold.set(gold.get() + value);
    }  

    /**
     * Spawn an item in the inventory and return the item entity
     * @return an item to be spawned in the controller as a JavaFX node
     */
    public Item addItem(Character character, String itemID){
        Item newItem = itemManager.newItemFromID(itemID, null, null);
        // hand off the baby item to the main method
        return addItem(character, newItem);
    }   

    /**
     * Add an already spawned item into the inventory
     * @param character the Character
     * @param item the item being given to the character
     * @return the item with its x,y inside the inventory
     */
    public Item addItem(Character character, Item item) {
        if (item == null) {
            return null;
        }
        else if (item instanceof Gold) {
            // recast then obtain value
            Gold goldItem = (Gold) item;
            this.gold.set(gold.get() + goldItem.getValue());
            // gold item consumed, let the listeners know
            item.destroy();
            return null;
        } else if (item instanceof Potion) {
            Potion potionItem = (Potion) item;
            this.potions.set(potions.get() + 1);
            // if we encounter better potions later in Milestone3, potionHP will be updated
            potionHP = potionItem.getValue();
            // potion item consumed, let the listeners know
            item.destroy();
            return null;
        } else {
            // move item into the inventory
            //Pair<Integer, Integer> firstAvailableSlot = getFreeItemSlot(character);
            Pair<Integer, Integer> firstAvailableSlot = getFirstAvailableSlotForItem();
            if (firstAvailableSlot == null) {
                Item oldItem = getItemByPositionInUnequippedInventoryItems(0);
                refundItem(character, oldItem);
                removeUnequippedInventoryItem(oldItem);
                firstAvailableSlot = getFirstAvailableSlotForItem();
            }

            // now we insert the new item, as we know we have at least made a slot available...
            item.changeIntegerProperty(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), 
                                       new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
            unequippedInventoryItems.add(item);
            // return the Item so controller can update the frontend
            return item;
        }
    }

    /**
     * Refunds character the value of the specified item
     * DOES NOT DESTROY THE ITEM
     * Gold = itemPrice/2
     * XP = itemPrice
     */
    private void refundItem(Character character, Item item) {
        gold.set(gold.get() + item.getPrice()/2);
        character.gainXP(item.getPrice());
    }


    ////////////////////////////////////////////////////
    ////////////// specialised methods /////////////////
    ////////////////////////////////////////////////////

    /**
     * remove an item by x,y coordinates
     * @param x x coordinate from 0 to width-1
     * @param y y coordinate from 0 to height-1
     */
    public void removeUnequippedInventoryItemByCoordinates(int x, int y){
        Entity item = getUnequippedInventoryItemEntityByCoordinates(x, y);
        removeUnequippedInventoryItem(item);
    }

    /**
     * remove an item from the unequipped inventory
     * @param item item to be removed
     */
    public void removeUnequippedInventoryItem(Entity item){
        item.destroy();
        unequippedInventoryItems.remove(item);
    }

    /**
     * return an unequipped inventory item by x and y coordinates
     * assumes that no 2 unequipped inventory items share x and y coordinates
     * @param x x index from 0 to width-1
     * @param y y index from 0 to height-1
     * @return unequipped inventory item at the input position
     */
    //private Entity getUnequippedInventoryItemEntityByCoordinates(int x, int y){
    public Entity getUnequippedInventoryItemEntityByCoordinates(int x, int y){
        for (Entity e: unequippedInventoryItems){
            if ((e.getX() == x) && (e.getY() == y)){
                return e;
            }
        }
        return null;
    }

    /**
     * get the item at a particular index in the unequipped inventory items list (this is ordered based on age in the starter code)
     * @param index index from 0 to length-1
     */
    public Item getItemByPositionInUnequippedInventoryItems(int index){
        return unequippedInventoryItems.get(index);
    }

    // /**
    //  * remove item at a particular index in the unequipped inventory items list (this is ordered based on age in the starter code)
    //  * @param index index from 0 to length-1
    //  */
    // private void removeItemByPositionInUnequippedInventoryItems(int index){
    //     Item item = unequippedInventoryItems.get(index);
    //     item.destroy();
    //     unequippedInventoryItems.remove(index);
    // }

    /**
     * get the first pair of x,y coordinates which don't have any items in it in the unequipped inventory
     * @return x,y coordinate pair, null if full
     */
    private Pair<Integer, Integer> getFirstAvailableSlotForItem(){
        // first available slot for an item...
        // IMPORTANT - have to check by y then x, since trying to find first available slot defined by looking row by row
        for (int height = 0; height < inventoryHeight; height++){
            for (int width = 0; width < inventoryWidth; width++){
                if (getUnequippedInventoryItemEntityByCoordinates(width, height) == null){
                    return new Pair<Integer, Integer>(width, height);
                }
            }
        }
        return null;
    }

    /**
     * Decrease gold in inventory according to the given price
     * @param price
     */
    public void spendGold(Gold price) {
        this.gold.set(this.gold.getValue() - price.getValue());
    }

    /**
     * For Testing purpsoes, returns if the inventory contains an item
     */
    public boolean contains(Item item) {
        for (Item inventory : unequippedInventoryItems) {
            if (inventory.getClass().equals(item.getClass())) {
                return true;
            }
        }
        return false;
    }

    public SimpleIntegerProperty getGoldProperty() {
        return gold;
    }

    public SimpleIntegerProperty getPotionProperty() {
        return potions;
    }

}
