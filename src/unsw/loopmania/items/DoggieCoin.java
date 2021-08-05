package unsw.loopmania.items;

import java.util.Random;

import javafx.beans.property.SimpleIntegerProperty;

public class DoggieCoin extends Item {

    public DoggieCoin(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setName("Doggie Coin");
        setPrice(1000);
    }

    /**
     * Set the market price of the Doggie coin according to whether a boss exists 
     * in the world or not
     * If a boss exists, the price is higher. Else, lower price
     * @param bossExists true: boss exists in the world, false: boss does not exist
     */
    public void setMarketPrice(Boolean bossExists) {
        Random rand = new Random();
        int value = rand.nextInt() % 1000;
        // If boss exists, value > 800
        if (bossExists) {
            if (value < 0) {
                value = 800;
            }
            if (value < 800) {
                value = value + 800;
            }
        }
        // Else, 0 < value < 800
        else {
            if (value < 0) {
                value = 200;
            }
            if (value > 800) {
                value = value - 500;
            }
        }
        // Set the price of doggie coin
        setPrice(value);
    }
}
