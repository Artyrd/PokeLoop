package test.buildingTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import javafx.beans.property.SimpleIntegerProperty;
import test.testHelpers.BasicHelper;
import test.testHelpers.BetterHelper;
import unsw.loopmania.Shop;
import unsw.loopmania.cards.Card;
import unsw.loopmania.items.Gold;
import unsw.loopmania.items.Item;


public class ShopTest {

    /**
     * Test the basic buying properties of the shop
     */
    @Test
    public void testBasicBuying() {
        BasicHelper help = new BasicHelper();
        Shop shop = new Shop();

        Gold moreGold = new Gold(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        moreGold.setValue(100);
        help.testSword.setStar(1);
        // sword is at index 0
        Item randomItem = shop.getItems().get(0);
        // This should fail because the character does not have enough gold
        assertEquals(null, shop.buyItem(help.testCharacter, randomItem));

        // This should pass because the character has enough gold now
        help.testCharacter.addItem(moreGold);
        assertTrue(null != shop.buyItem(help.testCharacter, randomItem));
        
        // Check that the characters gold decreased to 40, sword costs 60
        assertEquals(100 - randomItem.getPrice(), help.testCharacter.getGold());

        // Buy the sword again, this should fail because there are no more swords in stock
        assertEquals(shop.buyItem(help.testCharacter, randomItem), null);
    
        // Check that the item is added to the inventory
        assertTrue(help.testCharacter.getInventory().contains(randomItem));
    }

    @Test
    public void testBasicBuyingProtection() {
        BasicHelper help = new BasicHelper();
        Shop shop = new Shop();

        Gold moreGold = new Gold(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        moreGold.setValue(100);
        help.testSword.setStar(1);

        Item sword = shop.getItems().get(0);
        // This should fail because the character does not have enough gold
        assertEquals(null, shop.buyItem(help.testCharacter, sword));

        // armour at index 9
        Item armour = shop.getItems().get(9);
        // This should pass because the character has enough gold now
        help.testCharacter.addItem(moreGold);
        assertTrue(null != shop.buyItem(help.testCharacter, armour));
    }

    @Test
    public void testBasicBuyingProtectionDefLimit() {
        BasicHelper help = new BasicHelper();
        Shop shop = new Shop();
        shop.setDefenseLimit(1);

        Gold moreGold = new Gold(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        moreGold.setValue(500);
        help.testSword.setStar(1);

        // This should fail because the character does not have enough gold
        assertEquals(null, shop.buyItem(help.testCharacter, shop.getItems().get(0)));  // sword

        // This should pass because the character has enough gold now
        help.testCharacter.addItem(moreGold);
        assertTrue(null != shop.buyItem(help.testCharacter, shop.getItems().get(9)));// armor
        assertTrue(null != shop.buyItem(help.testCharacter, shop.getItems().get(0)));  // sword
    }

    /**
     * Shop does not contain potions, therefore character cannot buy potions after purchasing a defense item when the limit = 1
     */
    @Test
    public void testOutOfStock() {
        BasicHelper help = new BasicHelper();
        Shop shop = new Shop();
        shop.setDefenseLimit(1);

        Gold moreGold = new Gold(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        moreGold.setValue(500);
        help.testSword.setStar(1);

        // This should pass because the character has enough gold now
        help.testCharacter.addItem(moreGold);
        assertTrue(null != shop.buyItem(help.testCharacter, shop.getItems().get(0)));  // sword
        assertEquals(null, shop.buyItem(help.testCharacter, help.testPotion));  
    }


    /**
     * Test the basic selling properties of the shop
     */
    @Test
    public void testSelling() {
        BasicHelper help = new BasicHelper();
        Shop shop = new Shop();

        // Give the character some items
        help.testCharacter.addItem(help.testArmour);
        help.testCharacter.addItem(help.testSword);
        help.testArmour.setStar(1);
        help.testSword.setStar(1);

        // Sell armour, should gain 50 gold
        shop.sellItem(help.testCharacter, help.testArmour);
        assertEquals(50, help.testCharacter.getGold());
        // Sell sword, should gain 60 gold
        shop.sellItem(help.testCharacter, help.testSword);
        assertEquals(110, help.testCharacter.getGold(), 110);

        // Check that it doesnt exist in inventory anymore
        assertFalse(help.testCharacter.getInventory().contains(help.testSword));
        assertFalse(help.testCharacter.getInventory().contains(help.testArmour));
    }

    /**
     * Test selling cards in the shop
     */
    @Test
    public void testSellingCards() {
        BetterHelper help = new BetterHelper();
        Shop shop = new Shop();
        shop.setPotionLimit(1);

        // Set up some cards
        Card barracksCard = help.testWorld.loadCard("BarracksCard");
        Card campfireCard = help.testWorld.loadCard("CampfireCard");
        Card towerCard = help.testWorld.loadCard("TowerCard");
        Card castleCard = help.testWorld.loadCard("VampireCastleCard");

        assertEquals(4, help.testWorld.getCardCount());

        // Sell barracks card, should gain 10 gold
        // Check that the card is not in the world anymore
        shop.sellCard(help.testCharacter, help.testWorld, barracksCard);
        assertEquals(10, help.testCharacter.getGold());
        assertFalse(help.testWorld.getCards().contains(barracksCard));

        // Sell campfire card, should gain 5 gold
        // Check that the card is not in the world anymore
        shop.sellCard(help.testCharacter, help.testWorld, campfireCard);
        assertEquals(15, help.testCharacter.getGold());
        assertFalse(help.testWorld.getCards().contains(campfireCard));

        // Sell tower card, should gain 10 gold
        // Check that the card is not in the world anymore
        shop.sellCard(help.testCharacter, help.testWorld, towerCard);
        assertEquals(25, help.testCharacter.getGold());
        assertFalse(help.testWorld.getCards().contains(towerCard));

        // Sell castle card, should gain 20 gold
        // Check that the card is not in the world anymore
        shop.sellCard(help.testCharacter, help.testWorld, castleCard);
        assertEquals(45, help.testCharacter.getGold());
        assertFalse(help.testWorld.getCards().contains(castleCard));

    }



}
