package unsw.loopmania.enemies;

import java.util.List;

import unsw.loopmania.MovingEntity;
import unsw.loopmania.battle.BattleBehaviour;
import unsw.loopmania.movement.MovementBehaviour;
import unsw.loopmania.movement.PathPosition;

/**
 * An enemy class which is a superclass to the enemy subclasseses (Slug, Zombie, Vampire)
 * This class has a random movement behaviour, and can generate a reward when an enemy 
 * object dies by calling the enemy's rewardBehaviour to generate loot.
 */
public class Enemy extends MovingEntity {
    
    RewardBehaviour rewardBehaviour;

    /*
    public Enemy(PathPosition position) {
        super(position);
    }
    */
    /**
     * Constructor overload, passes battle stats to superclass MovingEntity. Also sets the
     * rewardBehaviour from enemies subclasses.
     * @param position
     * @param rewardBehaviour
     * @param HP
     * @param ATK
     * @param DEF
     * @param critChance
     * @param friendly
     */
    public Enemy(PathPosition position, RewardBehaviour rewardBehaviour, double HP, double ATK, double DEF, int critChance, boolean friendly) {
        //super(position, HP, ATK, DEF, critChance, friendly);
        super(position, HP, ATK, DEF, critChance, friendly);
        this.rewardBehaviour = rewardBehaviour;
    }
    public Enemy(PathPosition position, double hp, double atk, double def, int critChance, boolean friendly, MovementBehaviour mBehaviour, BattleBehaviour battleBehaviour, RewardBehaviour rewardBehaviour) {
        super(position, hp , atk, def, critChance, friendly, mBehaviour, battleBehaviour);
        this.rewardBehaviour = rewardBehaviour;
    }

    /**
     * Generates loot as a list of strings which can be identified by their id.
     * @return List of String of rewards, last item in list is gold of loot
     */
    public List<String> giveReward(){
        return rewardBehaviour.generateAllRewards();
    }
    public List<String> generateCardRewards(){
        return rewardBehaviour.generateCardRewards();
    }

    public List<String> generateItemRewards(){
        return rewardBehaviour.generateItemRewards();
    }

    public int generateGoldRewards(){
        return rewardBehaviour.generateGoldRewards();
    }
    public int generateExperience(){
        return rewardBehaviour.generateExperience();
    }


}
