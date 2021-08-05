package unsw.loopmania.enemies;

import unsw.loopmania.movement.PathPosition;
import unsw.loopmania.battle.DoggieBattleBehaviour;
import unsw.loopmania.movement.RandomQuickMovementBehaviour;

public class Doggie extends Boss {
    
    public Doggie(PathPosition position){
        super(position, 300, 25, 3, 40, false, new RandomQuickMovementBehaviour(), new DoggieBattleBehaviour(), new DoggieReward());
        setID("Doggie");
        setBattleRadius(1);
        setSupportRadius(2);
    }

}
