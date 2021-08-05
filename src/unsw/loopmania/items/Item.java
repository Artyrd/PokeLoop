package unsw.loopmania.items;

import java.util.Random;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.MovingEntity;
import unsw.loopmania.StaticEntity;
import unsw.loopmania.Character;

/**
 * represents an equipped or unequipped item in the backend world
 */
public abstract class Item extends StaticEntity {

    private double atk;
    private double def;
    private int price;
    private String type;
    private String name;
    private double scalarReduction;
    private SimpleDoubleProperty maxDurability;
    private SimpleDoubleProperty currDurability;
    private int starTier;

    public Item(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        atk = 0;
        def = 0;
        price = 0;
        type = "Item";
        name = null;
        scalarReduction = 0;
        maxDurability = new SimpleDoubleProperty(1);
        currDurability = new SimpleDoubleProperty(0);

        Random rand = new Random();
        
        starTier = rand.nextInt(3) + 1;
    }

    public double getATK() {
        return atk;
    }

    public void setATK(double atk) {
        this.atk = atk;
    }

    public double getDEF() {
        return def;
    }
    public void setDEF(double def) {
        this.def = def;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int value) {
        this.price = value;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return this.type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }    
    /**
    * Set the percentage damage mitigation of the protection item
    * @param percentage the percentage in decimals
    */
    public void setScalarReduction (double percentage) {
        scalarReduction = percentage;
    }
    /**
     * Get the percentage damage mitigation of the protection item
     * @return
     */
    public double getScalarReduction() {
        return this.scalarReduction;
    }

    /**
     * Set the maximum durability of an item, and set its current durability to that amount
     * @param durability
     */
    public void setMaxDurability (int durability) {
        maxDurability.set(durability);
        currDurability.set(durability);
    }

    /**
     * Decrease the durability by the given amount
     * @param amount
     */
    public void decreaseDurability(int amount) {
        // avoid divide by negative error in frontend
        if (amount > currDurability.get()) {
            currDurability.set(0);
        }
        currDurability.set(currDurability.get() - amount);
    }

    public int getMaxDurability() {
        return (int) maxDurability.get();
    }

    public int getCurrDurability() {
        return (int) currDurability.get();
    }

    public boolean checkBroken() {
        if (currDurability.get() <= 0) {
            this.destroy(); // equipleaf also removes itself when the item is destoyed
            return true;
        }
        return false;
    }

    /**
     * Set the star tier of an item 
     * This function is used only for testing purposes
     * @param star the new star level 
     * @param character update character stats for testing purposes
     */
    public void setStar(int star, Character character) {
        this.starTier = star;
        this.atk = this.atk * star;
        this.def = this.def * star;
        if (character != null) {
            character.updateStats();
        }
    }

    /**
     * Set the star tier of an item for the shop to sell
     * Overridden in each item independently
     * @param star the new star level
     */
    public void setStar(int star) {
        this.starTier = star;
    }

    /**
     * Get the star tier of an item
     * @return
     */
    public int getStar() {
        return this.starTier;
    }

    // FRONT END //
    public SimpleDoubleProperty getCurrDurabilityProperty() {
        return currDurability;
    }
    public SimpleDoubleProperty getMaxDurabilityProperty() {
        return maxDurability;
    }
}
