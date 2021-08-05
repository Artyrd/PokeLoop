package test.testHelpers;

import java.util.ArrayList;
import java.util.List;
import org.javatuples.Pair;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Character;
import unsw.loopmania.Inventory;
import unsw.loopmania.Ally;
import unsw.loopmania.enemies.*;
import unsw.loopmania.items.*;
import unsw.loopmania.buildings.*;
import unsw.loopmania.movement.PathPosition;

public class BasicHelper {
    
    public List<Pair<Integer, Integer>> orderedPath;
    public int pathLength = 100;
    public int loopEdge = 10;
    public int totalLoopLength;
    public HeroCastleBuilding testHeroCastle;
    public PathPosition testPosition;
    public Slug testSlug;
    public Zombie testZombie;
    public Vampire testVampire;
    public Character testCharacter;
    public Ally testAlly;
    public Sword testSword;
    public Staff testStaff;
    public Stake testStake;
    public Anduril testAnduril;
    public Armour testArmour;
    public Shield testShield;
    public Helmet testHelmet;
    public TreeStump testTreeStump;
    public Gold tenGold;
    public Potion testPotion;
    public ElanMuske testElanMuske;
    public Doggie testDoggie;

    /**
     * Creates a basic world with the Character, a Slug, a Zombie, a Vampire, an Ally, 
     * all in the 1st position of a straight line path
     * Also creates all items in the world
     */
    public BasicHelper() {
        // Create a path of pathLength tiles long
        this.orderedPath = new ArrayList<Pair<Integer, Integer>>();
        for(int i = 0; i < pathLength; i++){
            Pair<Integer, Integer> pos = new Pair<Integer, Integer>(i, 1);
            this.orderedPath.add(pos);
        }
        this.totalLoopLength = pathLength;
        Pair<Integer,Integer> pathStart = orderedPath.get(0);
        this.testHeroCastle = new HeroCastleBuilding(new SimpleIntegerProperty(pathStart.getValue0()),
                                                     new SimpleIntegerProperty(pathStart.getValue1()));

        this.testPosition = new PathPosition(1, orderedPath);

        this.testCharacter = new Character(testPosition, new Inventory(4,4));
        this.testSlug = new Slug(testPosition);
        this.testZombie = new Zombie(testPosition);
        this.testVampire = new Vampire(testPosition);
        this.testAlly = new Ally();
        this.testSword = new Sword(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        this.testSword.setATK(5);
        this.testSword.setMaxDurability(20);
        this.testSword.setStar(1, testCharacter);
        this.testStaff = new Staff(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        this.testStaff.setATK(1);
        this.testStaff.setMaxDurability(15);
        this.testStaff.setStar(1, testCharacter);
        this.testStake = new Stake(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        this.testStake.setATK(3);
        this.testStake.setMaxDurability(10);
        this.testStake.setStar(1, testCharacter);
        this.testAnduril = new Anduril(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        this.testAnduril.setATK(15);
        this.testAnduril.setMaxDurability(20);
        this.testAnduril.setStar(1, testCharacter);
        this.testArmour = new Armour(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        this.testArmour.setDEF(3);
        this.testArmour.setMaxDurability(20);
        this.testArmour.setStar(1, testCharacter);
        this.testShield = new Shield(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        this.testShield.setDEF(1);
        this.testShield.setMaxDurability(20);
        this.testShield.setStar(1, testCharacter);
        this.testHelmet = new Helmet(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        this.testHelmet.setDEF(1);
        this.testHelmet.setMaxDurability(15);
        this.testHelmet.setStar(1, testCharacter);
        this.testTreeStump = new TreeStump(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        this.testTreeStump.setDEF(1);
        this.testTreeStump.setMaxDurability(20);
        this.testTreeStump.setStar(1, testCharacter);
        this.tenGold = new Gold(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        this.tenGold.setValue(10);
        this.testPotion = new Potion(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        this.testElanMuske = new ElanMuske(testPosition);
        this.testDoggie = new Doggie(testPosition);
    }

    /**
     * Creates a basic world with the Character, a Slug, a Zombie, a Vampire, an Ally, 
     * all in the 1st position of a straight line path
     */
    public void LoadBasicLoopWorld() {
        createBasicLoopPath();
        Pair<Integer,Integer> pathStart = orderedPath.get(0);
        this.testHeroCastle = new HeroCastleBuilding(new SimpleIntegerProperty(pathStart.getValue0()),
                                                     new SimpleIntegerProperty(pathStart.getValue1()));
        this.testPosition = new PathPosition(0, orderedPath);
        this.testCharacter = new Character(testPosition, new Inventory(4,4));
        this.testSlug = new Slug(testPosition);
        this.testZombie = new Zombie(testPosition);
        this.testVampire = new Vampire(testPosition);
        this.testAlly = new Ally();
        this.testElanMuske = new ElanMuske(testPosition);
        this.testDoggie = new Doggie(testPosition);
    }

    // Create a looped path with sides of 10 tiles long, starting at 1,1
    /*
    1,1  ... 10,1
    .          .
    .          .
    .          .
    1,10 ... 10,10

    */
    public void createBasicLoopPath() {
        this.orderedPath = new ArrayList<Pair<Integer, Integer>>();
        // top edge: 1,1 - 9,1
        for(int i = 1; i < loopEdge; i++){
            Pair<Integer, Integer> pos = new Pair<Integer, Integer>(i, 1);
            this.orderedPath.add(pos);
        }
        // right edge: 10,1 - 10,9
        for(int i = 1; i < loopEdge; i++){
            Pair<Integer, Integer> pos = new Pair<Integer, Integer>(loopEdge, i);
            this.orderedPath.add(pos);
        }
        // bottom edge: 10,10 - 2,10
        for(int i = 1; i < loopEdge; i++){
            Pair<Integer, Integer> pos = new Pair<Integer, Integer>(loopEdge - i + 1, loopEdge);
            this.orderedPath.add(pos);
        }
        // left edge: 1,10 - 1,1
        for(int i = 1; i < loopEdge; i++){
            Pair<Integer, Integer> pos = new Pair<Integer, Integer>(1, loopEdge - i + 1);
            this.orderedPath.add(pos);
        }
        this.totalLoopLength = 4 * (loopEdge-1);
    }
}