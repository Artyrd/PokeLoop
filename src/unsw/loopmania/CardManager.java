package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.cards.*;

/**
 * A translator that produces cards based on their ID string
 */
public class CardManager {

    public CardManager() {}

    /**
     * Produce a card based on the given ID, can instantiate x and y too if needed
     * @param cardID - the card's ID string
     * @param x - SimplePropertyX (frontend)
     * @param y - SimplePropertyY (frontend)
     * @return an instantiated card that correlates to the ID, null if invalid ID
     */
    public Card newCardFromID(String cardID, SimpleIntegerProperty x, SimpleIntegerProperty y) {
        Card newCard = null;
        switch(cardID) {
            case "BarracksCard":
                newCard = new BarracksCard(x, y);
                break;
            case "CampfireCard":
                newCard = new CampfireCard(x, y);
                break;
            case "TowerCard":
                newCard = new TowerCard(x, y);
                break;
            case "TrapCard":
                newCard = new TrapCard(x, y);
                break;
            case "VampireCastleCard":
                newCard = new VampireCastleCard(x, y);
                break;
            case "VillageCard":
                newCard = new VillageCard(x, y);
                break;
            case "ZombiePitCard":
                newCard = new ZombiePitCard(x, y);
                break;
            case "ElanSpawnerCard":
                newCard = new ElanSpawnerCard(x, y);
                break;
            case "StageBossCard":
                newCard = new StageBossCard(x, y);
                break;
            case "DragonSpawnerCard":
                newCard = new DragonSpawnerCard(x, y);
                break;
            // etc...
            default:
                System.err.println("Error in CardManager.java: invalid Card or Card.ID: " + cardID);

        }
        return newCard;
    }

}
