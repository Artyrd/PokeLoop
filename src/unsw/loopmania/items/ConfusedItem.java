package unsw.loopmania.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Character;
import unsw.loopmania.MovingEntity;
import unsw.loopmania.status.Status;

public class ConfusedItem extends Item implements Weapon, Protection, Reviver {

    private ArrayList<String> worldRareItems;
    private String base;
    private String extra;

    private Anduril anduril;
    private TreeStump treeStump;
    private TheOneRing theOneRing;

    public ConfusedItem(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setPrice(999);
        this.worldRareItems = new ArrayList<String>();
        // initialise items for method forwarding
        anduril = new Anduril(null, null);
        anduril.setStar(this.getStar());
        treeStump = new TreeStump(null, null);
        treeStump.setStar(this.getStar());
        theOneRing = new TheOneRing(null, null);
    }

    /**
     * Create a confused item that doesnt exist in the world yet 
     */
    public ConfusedItem() {
        this(null, null);
    }

    /**
     * Get the base item
     */
    public String getBaseItem() {
        return this.base;
    }

    /**
     * Given a list of potential rare items, confused item then morphs into a combination of 2 of them.
     * @param potentialItems
     */
    public void setWorldRareItems(List<Item> potentialItems) {
        for (Item item: potentialItems) {
            this.worldRareItems.add(item.getID());
        }
        // Create a random combination of confused item
        createCombination();
    }

    /**
     * Set the rare items that exist in the world
     * @param ring - TheOneRing can exist in the world
     * @param anduril - Anduril, Flame of the west can exist in the world
     * @param treeStump - Tree Stump can exist in the world
     */
    public void setWorldRareItems(boolean ring, boolean anduril, boolean treeStump) {
        // Add rare items to a list of potential items
        if (ring) {
            this.worldRareItems.add("TheOneRing");
        }
        if (anduril) {
            this.worldRareItems.add("Anduril");
        }
        if (treeStump) {
            this.worldRareItems.add("TreeStump");
        }
        // Create a random combination of confused item
        createCombination();
    }
    
    /**
     * Create combinations of confused item attributes, with a base item and its extra effects
     * Set its durability and attributes depending on the combination
     */
    public void createCombination() {
        // Randomly assign the base item and extra effect of the confused item
        // according to available rare items in the world
        Random rand = new Random();
        int num = rand.nextInt(this.worldRareItems.size());
        this.base = this.worldRareItems.get(num);
        this.worldRareItems.remove(num);

        if (this.worldRareItems.size() >= 1) {
            num = rand.nextInt(this.worldRareItems.size());
            this.extra = this.worldRareItems.get(num);
        }
        else {
            this.extra = "None";
        }
        
        // If the base item is not the one ring, set its durability to 20
        if (this.base.equals("TheOneRing")) {
            this.setMaxDurability(1);
        } else {
            this.setMaxDurability(20);
        }
        // If the item has tree stump attributes, set scalar defense reduction
        if (this.base.equals("TreeStump") || this.extra.equals("TreeStump")) {
            setScalarReduction(0.4);
        }
        // If the item has anduril attributes, set attack attribute
        if (this.base.equals("Anduril") || this.extra.equals("Anduril")) {
            setATK(15);
        }
        setID(this.base);
        System.err.println(this.base + this.extra);
        setBaseType();
    }
    
    /**
     * This function is used only for testing, to test specific combinations of confused items
     * Functions same as createCombinations without the random aspect
     * Their durability and attributes are the same as a normal combination creation
     */
    public void setTestCombinations(String base, String extra) {
        this.base = base;
        this.extra = extra;
        if (this.base.equals("TheOneRing")) {
            this.setMaxDurability(1);
        } else {
            this.setMaxDurability(20);
        }
        if (this.base.equals("TreeStump") || this.extra.equals("TreeStump")) {
            setScalarReduction(0.4);
        }
        if (this.base.equals("Anduril") || this.extra.equals("Anduril")) {
            setATK(15);
        }
        setID(this.base);
        setBaseType();
        System.err.println(this.base + this.extra);
    }
   
    private void setBaseType() {
        switch (base) {
            case "Anduril":
                setType("Weapon");
                setName(this.anduril.getName() + " (Confused)");
                setPrice(this.anduril.getPrice());
                break;
            case "TreeStump":
                setType("Shield");
                setName(this.treeStump.getName() + " (Confused)");
                setPrice(this.treeStump.getPrice());
                break;
            case "TheOneRing":
                setType("Accessory");
                setName(this.theOneRing.getName() + " (Confused)");
                setPrice(this.theOneRing.getPrice());
                break;
        }
        System.err.println(this.base + this.extra);
    }
    /**
     * If the item has a TreeStump attribute, give it reduce damage behaviour
     * @param enemy
     * @param damage
     */
    @Override
    public double reduceDamage(MovingEntity enemy, double damage) {
        // Decrease durability if it is the base item
        if (this.base.equals("TreeStump")) {
            decreaseDurability(1);
        }
        // Reduce damage
        if (this.base.equals("TreeStump") || this.extra.equals("TreeStump")) {
            return treeStump.reduceDamage(enemy, damage);
        }
        // If doesnt have tree stump attribute
        return damage;
    }

    /**
     * This item does not resist status effects
     */
    @Override
    public boolean resistStatus(Status status) {
        return false;
    }

    /**
     * If the item has the anduril sword attribute, give it attack behaviour
     * @param enemy 
     * @param charCritAtk the normal attack of the character
     */
    @Override
    public double attack(MovingEntity enemy, double charAtk) {
        // Decrease durability if it is base item
        if (this.base.equals("Anduril")) {
            decreaseDurability(1);
        }
        // Deal extra damage against bosses
        if (this.base.equals("Anduril") || this.extra.equals("Anduril")) {
            return anduril.attack(enemy, charAtk);
        }
        // If doesnt have anduril attribute
        // character attacks with his bare firsts
        return charAtk;
    }

    /**
     * If the item has the anduril sword attribute, give it critical attack behaviour
     * @param enemy 
     * @param charCritAtk the critical attack of the character
     */
    @Override
    public double critAttack(MovingEntity enemy, double charCritAtk) {
        // Decrease durability if it is base item and apply burn effect
        if (this.base.equals("Anduril")) {
            decreaseDurability(2);
            return anduril.critAttack(enemy, charCritAtk);
        } else if (this.extra.equals("Anduril")) {
            // if the base item isnt a flaming sword, no burn effect
            // just regular mulitplier ontop of the charCritAtk
            return anduril.attack(enemy, charCritAtk);
        }
        // If doesnt have anduril attribute
        // character attacks with his bare firsts
        return charCritAtk;
    }

    /**
     * If the item has the one ring attribute, give it revive behaviour
     * @param character
     */
    @Override
    public boolean revive(Character character) {
        boolean result = false;
        // If the base item is the ring
        if ((base.equals("TheOneRing") || extra.equals("TheOneRing")) 
                && theOneRing != null) {
            result = theOneRing.revive(character);
            theOneRing = null; // destroy the used ring
            // if the confused item itself is the ring, destroy it
            if (base.equals("TheOneRing")) {
                this.destroy();
            }
        }
        return result;
    }
   

}
