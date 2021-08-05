package test.itemsTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import test.testHelpers.BetterHelper;
import unsw.loopmania.items.DoggieCoin;

public class DoggieCoinTest {
    
    /**
     * Test the value of the doggie coin's price depending on whether a boss
     * exists in the world or not
     */
    @Test
    public void testValue() {
        DoggieCoin coin = new DoggieCoin(null, null);

        assertEquals(1000, coin.getPrice());
        
        // If boss exists, market price should be greater than 800
        boolean bossExists = true;
        // test the market price 1000 times
        for (int i = 0; i < 1000; i++) {
            coin.setMarketPrice(bossExists);
            System.err.println(coin.getPrice());
            assertTrue(coin.getPrice() >= 800);
        }

        // If boss does not exist, market price should be less than 800
        bossExists = false;
        // test the market price 1000 times
        for (int i = 0; i < 1000; i++) {
            coin.setMarketPrice(bossExists);
            System.err.println(coin.getPrice());
            assertTrue(coin.getPrice() <= 800);
        }

    }
}
