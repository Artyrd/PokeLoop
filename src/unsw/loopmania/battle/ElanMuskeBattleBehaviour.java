package unsw.loopmania.battle;

import java.util.ArrayList;

import unsw.loopmania.MovingEntity;
import unsw.loopmania.status.Status;
import unsw.loopmania.enemies.ElanMuske;

public class ElanMuskeBattleBehaviour implements BattleBehaviour {

    private BattleBehaviour baseBattle;
    private static ArrayList<String> immuneToStatus;

    public ElanMuskeBattleBehaviour(){
        this.baseBattle = new BasicBattleBehaviour();
        immuneToStatus = new ArrayList<String>();
        immuneToStatus.add("Tranced");
        immuneToStatus.add("Zombified");
    }

    /**
     * Deal basic damage to the target
     * @param source the attacking entity
     * @param target the entity being attacked
     * @param currATK the base damage being dealt
     */
    @Override
    public double attack(MovingEntity source, MovingEntity target, double currATK) {
        double damage = baseBattle.attack(source, target, currATK);
        return damage;
    }

    /**
     * Deal critical damage to the target of base * 1.5
     * Also heal allies by 30% of their maximum health
     * @param source the attacking entity
     * @param target the entity being attacked
     * @param currATK the base damage being dealt
     * @param alliesOfAttacker allies of source entity
     * @param enemiesOfAttacker enemies of source entity
     */
    @Override
    public double critAttack(MovingEntity source, MovingEntity target, double currATK, ArrayList<MovingEntity> bossAllies, ArrayList<MovingEntity> bossEnemies) {
        if(bossAllies != null){
            for(MovingEntity bossAlly : bossAllies){
                if(bossAlly.getCurrHP() > 0 && !(bossAlly instanceof ElanMuske)){
                    bossAlly.restoreHP(bossAlly.getMaxHP() * 0.3);
                    System.out.println("Oh no! Elan Muske healed all enemies!");
                }
            }
        }
        
        double damageDealt = baseBattle.critAttack(source, target, currATK, bossAllies, bossEnemies);
        return damageDealt;
    }

    /**
     * Receive an attack of amount damage, reduce damage from current defense
     * @param source the source of damage
     * @param self the entity being damaged
     * @param damage the amount of damage
     * @return the net damage received after reductions
     */
    @Override
    public double receiveAttack(MovingEntity source, MovingEntity self, double damage) {
        double netDamage = damage - self.getCurrDEF();
        self.reduceHP(netDamage);
        return netDamage;
    }

    /**
     * Receive a status effect
     * @param self the entity receiving the effect
     * @param status the status being applied
     * @return true: the status was received
     */
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
