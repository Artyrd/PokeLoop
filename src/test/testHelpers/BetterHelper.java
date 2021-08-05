package test.testHelpers;

import java.util.ArrayList;
import java.util.List;
import org.javatuples.Pair;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Character;
import unsw.loopmania.Dragon;
import unsw.loopmania.Inventory;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.battle.BasicBattleBehaviour;
import unsw.loopmania.battle.PetBattleBehaviour;
import unsw.loopmania.Ally;
import unsw.loopmania.enemies.*;
import unsw.loopmania.items.*;
import unsw.loopmania.buildings.*;
import unsw.loopmania.movement.PathPosition;
import unsw.loopmania.status.Stunned;

public class BetterHelper {
    
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
    public LoopManiaWorld testWorld;
    public ElanMuske testElanMuske;
    public Doggie testDoggie;
    public Dragon testDragon;
    public StageBoss testStageBoss;
    public StageBossPet testStageBossPet;

    /**
     * Creates a basic world with the Character, a Slug, a Zombie, a Vampire, an Ally, 
     * all in the 1 position (2,1) of a straight 10x10 looped path
     * Also creates all items in the world
     */
    public BetterHelper() {
        LoadBasicLoopWorld();
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
        this.testStaff = new Staff(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        this.testStake = new Stake(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        this.testAnduril = new Anduril(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        this.testArmour = new Armour(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        this.testShield = new Shield(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        this.testHelmet = new Helmet(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        this.testTreeStump = new TreeStump(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        this.tenGold = new Gold(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        this.tenGold.setValue(10);
        this.testPotion = new Potion(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        this.testWorld = new LoopManiaWorld(12, 12, orderedPath);
        this.testWorld.setCharacter(testCharacter);
        this.testElanMuske = new ElanMuske(testPosition);
        this.testDoggie = new Doggie(testPosition);
        this.testDragon = new Dragon(testPosition);
        int stageBossGold[] = {100};
        int stageBossXP = 1000;
        this.testStageBoss = new StageBoss(testPosition, 100, 20, 2, new BasicBattleBehaviour(), new StageBossReward(stageBossGold, stageBossXP), "TestStageBoss");
        this.testStageBossPet = new StageBossPet(testPosition, 100, 20, 2, new PetBattleBehaviour(new Stunned()), new StageBossReward(stageBossGold, stageBossXP), "TestStageBossPet");

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