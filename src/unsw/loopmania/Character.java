package unsw.loopmania;

import org.javatuples.Pair;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.battle.CharacterBattleBehaviour;
import unsw.loopmania.items.Item;
import unsw.loopmania.items.Reviver;
import unsw.loopmania.items.Weapon;
import unsw.loopmania.movement.CharacterMovementBehaviour;
import unsw.loopmania.movement.PathPosition;
import unsw.loopmania.status.Status;

/**
 * represents the main character in the backend of the game world
 * On instantiation, we need to give it an inventory before it begins questing!
 */
public class Character extends MovingEntity {

    private SimpleIntegerProperty lvl; // "level" unabbreviated looks strange
    private SimpleDoubleProperty xp;
    private SimpleDoubleProperty maxXP;
    private SimpleDoubleProperty cummulativeXP;
    private Inventory inventory;
    private EquipComposite equippedItems; // pointer to the invenctory's equipped items

    public Character (PathPosition position, Inventory inventory) {
        super(position, 100, 2, 1, 25, true);
        //super(position, 1000, 20, 1, 25, true);
        setID("Character");
        this.lvl = new SimpleIntegerProperty(1);
        this.xp = new SimpleDoubleProperty(0);
        this.maxXP = new SimpleDoubleProperty(getRequiredXP());
        this.cummulativeXP = new SimpleDoubleProperty(0);
        this.inventory = inventory;
        this.equippedItems = inventory.getEquipComposite();
        setMovementBehaviour(new CharacterMovementBehaviour());
        setBattleBehaviour(new CharacterBattleBehaviour(this));
    }

    /* Two ways to give the character an item */

    /**
     * Give the character an item by Item Entity
     * @return the item with its x,y inside the inventory
     */
    public Item addItem (Item item) {
        return inventory.addItem(this, item);
    }
    /**
     * Give the character an item by ItemID string
     */
    public Item addItem (String itemID) {
        return inventory.addItem(this, itemID);
    }

    public int getGold () {
        return inventory.getGold();
    }

    public int getPotions () {
        return inventory.getPotions();
    }
    /**
     * Commands the Character to use a potion
     * @return the hp restored, 0 if out of potions
     */
    public double usePotion() {
        return inventory.usePotion(this);
    }

    /**
     * Equip the item and update any ATK/DEF stats from the item to the character
     * @param item to be equipped
     * @return the previous item equipped
     */
    public Item equip(Item item) {
        Item oldEquip;
        oldEquip = inventory.equipItem(this, item);
        updateStats();
        return oldEquip;
    }

    public EquipComposite getEquippedItems() {
        return this.equippedItems;
    }

    public int getLevel() {
        return lvl.get();
    }

    public int getXP() {
        return (int) xp.get();
    }
    
    /**
     * Update the ATK and DEF values of the character with any items equipped 
     * or if a campfire is nearby
     */
    public void updateStats() {
        Pair<Double, Double> bonusStats = equippedItems.calculateStats();
        setCurrATK(getBaseATK() + bonusStats.getValue0());
        setCurrDEF(getBaseDEF() + bonusStats.getValue1());
        Status buff = getStatusByID("CampfireBuff");
        if (buff != null) {
            buff.applyStatus(this);
        }
    }

    /**
     * Returns the currently equipped weapon
     * For testing purposes
     * @return the currently equipped weapon, null otherwise
     */
    public Weapon getWeapon() {
        if (equippedItems.getWeapons().size() > 0) {
            return equippedItems.getWeapons().get(0);
        }
        return null;
    }

    /**
     * Grants the character more xp
     * @param additionalXP - the xp gained
     * @return Character's total xp
     */
    public int gainXP(int additionalXP) {
        xp.set(xp.get() + additionalXP);
        cummulativeXP.set(cummulativeXP.get() + additionalXP);
        updateLevel();
        return (int) xp.get();
    }

    /**
     * Calculates the character's lvl based off of xp
     * LevelsUp character if pass the threshhold
     */
    private void updateLevel() {
        int requiredXP = getRequiredXP();
        while (xp.get() >= requiredXP) {
            xp.set(xp.get() - requiredXP);
            levelUp();
            requiredXP = getRequiredXP();
        }
    }

    /**
     * get the XP threshhold required for the character to level up
     * @return
     */
    public int getRequiredXP() {
        return (lvl.get() * 50 + 50);
    }    

    public int getMaxXP() {
        return (int)maxXP.get();
    }

    public int getCummulativeXP() {
        return (int)cummulativeXP.get();
    }

    /**
     * Unconditionally levels up the Character, increasing their stats
     */
    private void levelUp() {
        lvl.set(lvl.get() + 1);
        increaseMaxHP(5);
        setBaseATK(getBaseATK() + 0.5);
        setCurrATK(getCurrATK() + 0.5);
        setBaseDEF(getBaseDEF() + 0.3);
        setCurrDEF(getCurrDEF() + 0.3);
        maxXP.set(getRequiredXP());
    }

    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Receive damage from battle and reduce HP
     * If taking damage kills the character and they are wearing the revivng ring,
     * revive the character
     * @param damage received
     */
    @Override
    public double reduceHP(double damage) {
        double remainingHP = super.reduceHP(damage);

        // one ring defence mechanism
        if (remainingHP <= 0 && equippedItems.hasRevivers() != null) {
            // loop through all potential reviving equipment until we actually revive
            for (Reviver reviver: equippedItems.hasRevivers()) {
                System.err.println("reviving with: " + ((Item) reviver).getID());
                boolean result = reviver.revive(this);

                if (result == true) {
                    break;
                }
            }
            remainingHP = getCurrHP();
        }
        return remainingHP;
    }

///// front end stuff /////
    public SimpleDoubleProperty getXpProperty() {
        return xp;
    }

    public SimpleDoubleProperty getMaxXpProperty() {
        return maxXP;
    }

    public SimpleDoubleProperty getCummulativeXpProperty() {
        return cummulativeXP;
    }

    public SimpleIntegerProperty getLevelProperty() {
        return lvl;
    }
}
