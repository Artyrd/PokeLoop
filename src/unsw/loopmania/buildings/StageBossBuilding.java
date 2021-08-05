package unsw.loopmania.buildings;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.battle.BasicBattleBehaviour;
import unsw.loopmania.battle.BattleBehaviour;
import unsw.loopmania.battle.PetBattleBehaviour;
import unsw.loopmania.enemies.RewardBehaviour;
import unsw.loopmania.enemies.StageBoss;
import unsw.loopmania.enemies.StageBossReward;
import unsw.loopmania.enemies.StageBossPet;
import unsw.loopmania.movement.PathPosition;

public class StageBossBuilding extends Spawner {
    
    private boolean stageBossSpawned;

    private String bossName;
    private int bossHP;
    private int bossATK;
    private int bossDEF;
    private BattleBehaviour bossBattleBehaviour;

    private String petName;
    private int petHP;
    private int petATK;
    private int petDEF;
    private BattleBehaviour petBattleBehaviour;

    private int rewardGold;
    private int rewardXP;

    public StageBossBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setID("StageBossBuilding");
        stageBossSpawned = false;
    }

    public void setBossStats(String bossName, int bossHP, int bossATK, int bossDEF, int bossGold, int bossXP) {
        this.bossName = bossName;
        this.bossHP = bossHP;
        this.bossATK = bossATK;
        this.bossDEF = bossDEF;
        this.rewardGold = bossGold;
        this.rewardXP = bossXP;
    }

    public void setPetStats(String petName, int petHP, int petATK, int petDEF) {
        this.petName = petName;
        this.petHP = petHP;
        this.petATK = petATK;
        this.petDEF = petDEF;
    }

    /**
     * Spawns Elan musk on the platform
     */
    @Override
    public void update(LoopManiaWorld world) {
        // if we already spawned elan, return
        if (stageBossSpawned) {
            return;
        }

        world.addBoss(createStageBossPet(world));
        world.addBoss(createStageBoss(world));

        stageBossSpawned = true;
        System.out.println("Spawning the Gym Leader !!");
    }

    public StageBoss createStageBoss(LoopManiaWorld world) {
        PathPosition bossPosition = new PathPosition(getX(), getY(), world.getOrderedPath());
        int[] gold = {rewardGold};
        RewardBehaviour bossReward = new StageBossReward(gold, rewardXP);
        return new StageBoss(bossPosition, bossHP, bossATK, bossDEF, bossBattleBehaviour, bossReward, bossName);
    }

    public StageBossPet createStageBossPet(LoopManiaWorld world) {

        PathPosition petPosition = new PathPosition(getX(), getY(), world.getOrderedPath());
        int[] petGold = {rewardGold/2};
        RewardBehaviour petReward = new StageBossReward(petGold, rewardXP/2);
        return new StageBossPet(petPosition, petHP, petATK, petDEF, petBattleBehaviour, petReward, petName);
    }

    public void setBattleBehaviours(BasicBattleBehaviour bossBattleBehaviour,
                PetBattleBehaviour petBattleBehaviour) {
        // TODO change battle behaviours depdning on the gym
        this.bossBattleBehaviour = bossBattleBehaviour;
        this.petBattleBehaviour = petBattleBehaviour;
    }

}
