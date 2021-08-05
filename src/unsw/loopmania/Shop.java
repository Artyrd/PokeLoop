package unsw.loopmania;

import unsw.loopmania.cards.Card;
import unsw.loopmania.items.Armour;
import unsw.loopmania.items.Gold;
import unsw.loopmania.items.Helmet;
import unsw.loopmania.items.Item;
import unsw.loopmania.items.Potion;
import unsw.loopmania.items.Shield;
import unsw.loopmania.items.Staff;
import unsw.loopmania.items.Stake;
import unsw.loopmania.items.Sword;
import unsw.loopmania.items.Protection;


import java.util.ArrayList;

import javafx.beans.property.SimpleIntegerProperty;

public class Shop {
    // List of items that can be sold
    private ArrayList<Item> items;
    
    // Limit constraints on the number of respective items which can be bought
    private int potionLimit;
    private int defenseLimit;

    public Shop() {
        // Populate items for sale in the shop
        this.items = new ArrayList<Item>();

        Sword sword1 = new Sword(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        sword1.setStar(1);
        Sword sword2 = new Sword(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        sword2.setStar(2);
        Sword sword3 = new Sword(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        sword3.setStar(3);
        this.items.add(sword1);
        this.items.add(sword2);
        this.items.add(sword3);

        Staff staff1 = new Staff(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        staff1.setStar(1);
        Staff staff2 = new Staff(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        staff2.setStar(2);
        Staff staff3 = new Staff(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        staff3.setStar(3);
        this.items.add(staff1);
        this.items.add(staff2);
        this.items.add(staff3);

        Stake stake1 = new Stake(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        stake1.setStar(1);
        Stake stake2 = new Stake(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        stake2.setStar(2);
        Stake stake3 = new Stake(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        stake3.setStar(3);
        this.items.add(stake1);
        this.items.add(stake2);
        this.items.add(stake3);

        Armour armour1 = new Armour(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        armour1.setStar(1);
        Armour armour2 = new Armour(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        armour2.setStar(2);
        Armour armour3 = new Armour(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        armour3.setStar(3);
        this.items.add(armour1);
        this.items.add(armour2);
        this.items.add(armour3);

        Helmet helmet1 = new Helmet(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        helmet1.setStar(1);
        Helmet helmet2 = new Helmet(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        helmet2.setStar(2);
        Helmet helmet3 = new Helmet(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        helmet3.setStar(3);
        this.items.add(helmet1);
        this.items.add(helmet2);
        this.items.add(helmet3);

        Shield shield1 = new Shield(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        shield1.setStar(1);
        Shield shield2 = new Shield(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        shield2.setStar(2);
        Shield shield3 = new Shield(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        shield3.setStar(3);
        this.items.add(shield1);
        this.items.add(shield2);
        this.items.add(shield3);

        this.potionLimit = 5;
        this.defenseLimit = 0;
    }

    /**
     * For a given item in the shop, buy it for gold
     * @param character
     * @param item
     * @return the Item purchased, null if not instock/ not enough gold
     */
    public Item buyItem(Character character, Item item) {
        String itemID = item.getID();
        // Check if the item is in stock
        Item inStock = null;
        for (Item stock : this.items) {
            if (stock.equals(item)) {
                System.out.println("flag1");
            //if (stock.getClass().equals(item.getClass())) {
                inStock = stock;
                break;
            }
        }
        // If the item is not in stock, return false
        if (inStock == null) {
            return null;
        }
        System.out.println("flag2");
        // Check if the character has enough gold to buy the item
        if (checkGold(character, item.getPrice()) == false) {
            return null;
        }

        // If the item is in stock and they have enough gold
        // Add the item to their inventory and decrease their gold
        items.remove(inStock);
        Item purchasedItem = character.addItem(inStock);
        Gold buyPrice = new Gold(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        buyPrice.setValue(item.getPrice());
        character.getInventory().spendGold(buyPrice);

        // If the item is a protection item and there are limits to them, remove
        // the other items from available stock
        ArrayList<Item> removedItems = new ArrayList<Item>();
        if (item instanceof Protection && defenseLimit == 1) {
            for (Item stock : this.items) {
                if (stock instanceof Protection) {
                    removedItems.add(stock);
                }
            }
        }
        items.removeAll(removedItems);

        // Return successful purchase
        if (itemID.equals("Potion")) {
            // dummy potion to signal a potion was bought
            return new Potion(null);
        }
        return purchasedItem;
    }

    /**
     * For a given item in the inventory, sell it for gold
     * We assume that the item is not equipped (inventory only)
     * @param character
     * @param item
     */
    public void sellItem(Character character, Item item) {
        // Get the monetary value of the item
        Gold sellPrice = new Gold(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        int finalSellPrice = item.getPrice() * (item.getCurrDurability() / item.getMaxDurability());
        sellPrice.setValue(finalSellPrice);
        // Remove the item from inventory and compensate the character
        character.getInventory().removeUnequippedInventoryItem(item);
        character.addItem(sellPrice);
    }

    /**
     * For a given card in the inventory, sell it for gold
     * @param character
     * @param world
     * @param card
     */
    public void sellCard(Character character, LoopManiaWorld world, Card card) {
        // Get the monetary value of the item
        Gold sellPrice = new Gold(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        sellPrice.setValue(card.getValue());
        // Remove the item from the world and compensate the character
        world.removeCard(world.getCards().indexOf(card));
        character.addItem(sellPrice);
    }

    /**
     * Check if the character has enough gold in their wallet for this pricetag
     * @param character
     * @param price
     * @return false if not enough, true if enough
     */
    private boolean checkGold(Character character, int price) {
        if (character.getGold() >= price) {
            return true;
        }
        return false;
    }

    /**
     * Set a limit on the number of potions which can be bought in the shop
     * @param limit
     */
    public void setPotionLimit(int limit) {
        this.potionLimit = limit;
        for (int potNum = 0; potNum < this.potionLimit; potNum++) {
            this.items.add(new Potion(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)));
        }
    }

    /**
     * Set a limit on the number of defense items which can be bought in the shop
     * @param limit
     */
    public void setDefenseLimit(int limit) {
        this.defenseLimit = limit;
    }

    /**
     * Get the number of potions that can be bought in the shop
     * @return number of potions that can be bought 
     */
    public int getPotionLimit() {
        return this.potionLimit;
    }
    
    /**
     * Get the number of defense items that can be bought in the shop
     * @return number of defense items that can be bought 
     */
    public int getDefenseLimit() {
        return this.defenseLimit;
    }

    /**
     * Get the list of items for sale at each shop 
     * @return list of in stock items
     */
    public ArrayList<Item> getItems() {
        return this.items;
    }

}