package test.worldTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import test.testHelpers.BetterHelper;
import unsw.loopmania.cards.Card;
import unsw.loopmania.cards.DragonSpawnerCard;
import unsw.loopmania.cards.ElanSpawnerCard;

public class CardSpawnTest {
    
    /**
     * Test the condition of getting the Dragon card and Elan Spawner card
     */
    @Test
    public void testSpawnDragonAndElanCard(){
        // Set  up world and character
        BetterHelper h = new BetterHelper();
        List<Card> spawnedCards = new ArrayList<Card>();
        h.testWorld.getCharacter().gainXP(100000);
        for(int loopNum = 0; loopNum < 50; loopNum++){
            h.testWorld.addLoop();
            List<Card> cards = h.testWorld.possiblyGiveCards();
            spawnedCards.addAll(cards);
        }
        assertEquals(spawnedCards.size(), 2 , 0.1);
        assertTrue(spawnedCards.get(0) instanceof DragonSpawnerCard);
        assertTrue(spawnedCards.get(1) instanceof ElanSpawnerCard);

    }

    /**
     * Test spawning elan through a card
     */
    @Test
    public void testSpawnElanCardNoEXP(){
        BetterHelper h = new BetterHelper();
        List<Card> spawnedCards = h.testWorld.possiblyGiveCards();
        
        for(int loopNum = 0; loopNum < 50; loopNum++){
            h.testWorld.addLoop();
            List<Card> cards = h.testWorld.possiblyGiveCards();
            if(cards.size() == 1){
                spawnedCards.addAll(h.testWorld.possiblyGiveCards());
                spawnedCards.addAll(h.testWorld.possiblyGiveCards());
            }
        }
        assert(spawnedCards.size() == 0);

    }


}
