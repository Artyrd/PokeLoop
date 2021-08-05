package unsw.loopmania.enemies;

import unsw.loopmania.battle.BattleBehaviour;
import unsw.loopmania.movement.MovementBehaviour;
import unsw.loopmania.movement.PathPosition;

public abstract class Boss extends Enemy {
    
    public Boss(PathPosition position, double hp, double atk, double def, int critChance, boolean friendly, MovementBehaviour mBehaviour, BattleBehaviour battleBehaviour, RewardBehaviour rewardBehaviour){
        super(position, hp, atk, def, critChance, friendly, mBehaviour, battleBehaviour, rewardBehaviour);
    }

}
