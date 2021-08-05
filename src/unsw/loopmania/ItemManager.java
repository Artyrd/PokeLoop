package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.items.*;

public class ItemManager {
    
    public ItemManager() {
    }

    /**
     * Get the correct item for the given itemID
     * @param itemID
     * @param x
     * @param y
     * @return item object
     */
    public Item newItemFromID(String itemID, SimpleIntegerProperty x, SimpleIntegerProperty y) {
        Item newItem = null;
        // Get the object according to the given ID
        switch(itemID) {
            /* Weapons */
            case "Sword":
                newItem = new Sword(x, y);
                break;
            case "Stake":
                newItem = new Stake(x, y);
                break;
            case "Staff":
                newItem = new Staff(x, y);
                break;

            /* Protection */
            case "Helmet":
                newItem = new Helmet(x, y);
                break;
            case "Armour":
                newItem = new Armour(x, y);
                break;
            case "Shield":
                newItem = new Shield(x, y);
                break;

            /* Other */
            case "Gold":
                newItem = new Gold(x, y);
                break;
            case "Potion":
                newItem = new Potion(x, y);
                break;
            case "TheOneRing":
                newItem = new TheOneRing(x, y);
            break;
        // etc...
        default:
            System.err.println("Error: invalid Item or Item.ID");

        }
        return newItem;
    }

}
