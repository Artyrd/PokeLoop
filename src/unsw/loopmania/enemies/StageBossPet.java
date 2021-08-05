package unsw.loopmania.enemies;

import unsw.loopmania.battle.BattleBehaviour;
import unsw.loopmania.movement.PathPosition;
import unsw.loopmania.movement.StageBossPetMovementBehaviour;

public class StageBossPet extends Boss {
   
    private String name;

    public StageBossPet(PathPosition position, double hp, double atk, double def, BattleBehaviour stageBossPetBattleBehaviour, RewardBehaviour rewardBehaviour, String name){
        super(position, hp, atk, def, 25, false, new StageBossPetMovementBehaviour(), stageBossPetBattleBehaviour, rewardBehaviour);
        this.name = name;
        setID("StageBossPet");
        // pet doesnt instigate any battles, only joins the stage boss/ any other instigators
        setBattleRadius(0);
        setSupportRadius(2);
    }

    public String getName() {
        return name;
    }

}
