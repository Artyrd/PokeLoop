package unsw.loopmania.enemies;

import unsw.loopmania.battle.ElanMuskeBattleBehaviour;
import unsw.loopmania.movement.PathPosition;
import unsw.loopmania.movement.RandomMovementBehaviour;

public class ElanMuske extends Boss {
    
    public ElanMuske(PathPosition position){
        super(position, 500, 40, 5, 40, false, new RandomMovementBehaviour(5), new ElanMuskeBattleBehaviour(), new ElanMuskeReward());
        setID("ElanMuske");
        setBattleRadius(1);
        setSupportRadius(2);
    }


}
