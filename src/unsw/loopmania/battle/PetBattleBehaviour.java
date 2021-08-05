package unsw.loopmania.battle;

import java.util.ArrayList;
import java.util.List;

import unsw.loopmania.MovingEntity;
import unsw.loopmania.status.Status;
import unsw.loopmania.Character;

public class PetBattleBehaviour implements BattleBehaviour {

    private BattleBehaviour baseBattle;
    private Status status;
    private List<String> immuneToStatus;

    /**
     * Pets may be able to inflict statuses on their crit attacks.
     * Different pets may inflict different statuses depending on what is given.
     * @param status
     */
    public PetBattleBehaviour(Status status){
        this.baseBattle = new BasicBattleBehaviour();
        this.status = status;
        immuneToStatus = new ArrayList<String>();
        immuneToStatus.add("Tranced");
        immuneToStatus.add("Zombified");
    }

    @Override
    public double attack(MovingEntity source, MovingEntity target, double currATK) {
        double damage = baseBattle.attack(source, target, currATK);
        return damage;
    }

    @Override
    public double critAttack(MovingEntity source, MovingEntity target, double currATK,
            ArrayList<MovingEntity> alliesOfAttacker, ArrayList<MovingEntity> enemiesOfAttacker) {
                if(status != null){
                    if(status.getID().equals("Zombified")){
                        if(!(target instanceof Character)){
                            target.receiveStatus(status);
                        }
                    } else {
                        target.receiveStatus(status);
                    }
                }
                
                double damageDealt = baseBattle.critAttack(source, target, currATK, alliesOfAttacker, enemiesOfAttacker);
                return damageDealt;
    }

    @Override
    public double receiveAttack(MovingEntity source, MovingEntity self, double damage) {
        
        double netDamage = damage - self.getCurrDEF();
        self.reduceHP(netDamage);
        return netDamage;
    }

    @Override
    public boolean receiveStatus(MovingEntity self, Status status) {
        if(!immuneToStatus.contains(status.getID())){
            self.setStatus(status);
            status.applyStatus(self);
        }
        return true;
    }

    @Override
    public MovingEntity getAttackPreference(ArrayList<MovingEntity> targets) {
        return null;
    }   
    
}
