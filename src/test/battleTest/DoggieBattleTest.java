package test.battleTest;

import org.junit.Test;

import test.testHelpers.BasicHelper;
import test.testHelpers.BetterHelper;
import unsw.loopmania.Ally;
import unsw.loopmania.Character;
import unsw.loopmania.enemies.*;
import unsw.loopmania.status.Status;

public class DoggieBattleTest {

    /**
     * Test the stun effect of doggie
     */
    @Test
    public void testStunCharacter(){
        BasicHelper helper = new BasicHelper();
        // Set up the character and doggie
        Character character = helper.testCharacter;
        character.setCritChance(0);
        Doggie doggie = helper.testDoggie;
        
        // Test attacks against doggie
        character.equip(helper.testSword); // Character has 7 ATK
        character.attack(doggie, null, null);
        assert(doggie.getCurrHP() == 296);

        // Test doggies crit attack against the character
        doggie.critAttack(character, null, null);
        assert(character.getStatusIDs().contains("Stunned"));
        character.attack(doggie, null, null);
        
        // Character is stunned for one attack
        assert(doggie.getCurrHP() == 296);
        // Tick the stun status
        for(Status s: character.getStatusList()){
            s.battleTick(character);
        }
        character.attack(doggie, null, null);
        
        for(Status s: character.getStatusList()){
            s.battleTick(character);
        }
        // Character is stunned for second attack
        assert(doggie.getCurrHP() == 296);
        
        character.attack(doggie, null, null);

        for(Status s: character.getStatusList()){
            s.battleTick(character);
        }
        // Character can now attack
        assert(doggie.getCurrHP() == 292);
    }

    /**
     * Test the stub effect against an ally
     */
    @Test
    public void testStunAlly(){
        BasicHelper helper = new BasicHelper();
        // Set up an ally and doggie
        Doggie doggie = helper.testDoggie;
        Ally ally = helper.testAlly;

        // Critically attack the ally and check their status
        doggie.critAttack(ally, null, null);
        assert(!ally.getStatusIDs().contains("Stunned"));
        assert(ally.getCurrHP() < ally.getMaxHP());

    }

    /**
     * Doggie can be burned, check status is applied
     */
    @Test
    public void testImmune(){
        BetterHelper helper = new BetterHelper();
        // Set up an ally and doggie
        Doggie doggie = helper.testDoggie;
        Character character = helper.testCharacter;
        character.equip(helper.testAnduril);

        character.critAttack(doggie, null, null);

        assert(doggie.getStatusIDs().contains("Burned"));
    }


    
}
