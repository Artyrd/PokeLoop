package unsw.loopmania.enemies;

import unsw.loopmania.battle.BattleBehaviour;
import unsw.loopmania.movement.PathPosition;
import unsw.loopmania.movement.StageBossMovementBehaviour;

public class StageBoss extends Boss {
   
    private String name;

    public StageBoss(PathPosition position, double hp, double atk, double def, BattleBehaviour stageBossBattleBehaviour, RewardBehaviour rewardBehaviour, String name){
        super(position, hp, atk, def, 25, false, new StageBossMovementBehaviour(), stageBossBattleBehaviour, rewardBehaviour);
        this.name = name;
        setID("StageBoss");
        setBattleRadius(1);
        setSupportRadius(0);
    }

    public String getName() {
        return name;
    }

}
