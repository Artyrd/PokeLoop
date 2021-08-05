package unsw.loopmania.battle;

import java.util.ArrayList;

import unsw.loopmania.Character;
import unsw.loopmania.MovingEntity;
import unsw.loopmania.items.Item;
import unsw.loopmania.items.Protection;
import unsw.loopmania.items.Weapon;
import unsw.loopmania.status.Status;

/**
 * The Character's battle behaviour contains a pointer to the character
 * The arguments in its methods are just to conform to BattleBehaviour
 */
public class CharacterBattleBehaviour implements BattleBehaviour {

    Character character;

    public CharacterBattleBehaviour (Character character) {
        this.character = character;
    }

    /**
     * Deal basic damage + weapon damage to the target
     * If stunned or consumed a potion, do not attack and deal damage
     * @param source the attacking entity
     * @param target the entity being attacked
     * @param currATK the base damage being dealt
     */
    @Override
    public double attack(MovingEntity self, MovingEntity target, double currATK) {
        if(!consumePotion() && !isStunned()){
            double damageDealt = target.receiveAttack(character, weaponAttack(target));
            //System.out.println("damage dealt: "+damageDealt);
            return damageDealt;
        } return 0;
        
    }

    /**
     * Deal a critical attack + critical weapon attack to the target
     * If stunned or consumed a potion, do not attack and deal damage
     * @param source the attacking entity
     * @param target the entity being attacked
     * @param currATK the base damage being dealt
     * @param alliesOfAttacker allies of source entity
     * @param enemiesOfAttacker enemies of source entity
     */
    @Override
    public double critAttack(MovingEntity self, MovingEntity target, double currATK, ArrayList<MovingEntity> allies, ArrayList<MovingEntity> enemies) {
        if(!consumePotion() && !isStunned()){
            double damageDealt = target.receiveAttack(character, weaponCritAttack(target));
            return damageDealt;
        } return 0;        
    }

    /**
     * Receive an attack of amount damage, reduce damage from defense items and current defense
     * @param source the source of damage
     * @param self the entity being damaged
     * @param damage the amount of damage
     * @return the net damage received after reductions
     */
    @Override
    public double receiveAttack(MovingEntity source, MovingEntity self, double damage) {
        double netDamage = damage;
        netDamage = reduceDamage(source, netDamage);
        netDamage = netDamage - character.getCurrDEF();
        character.reduceHP(netDamage);
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
        // character may resist receiving the status
        if (resistStatus(status)) {
            return false;
        } // else
        self.setStatus(status);
        status.applyStatus(self);
        return true;
    }

    @Override
    public MovingEntity getAttackPreference(ArrayList<MovingEntity> targets) {
        return null;
    }

    /**
     * Consume a potion if health is below 60. If below 30, consume 2
     * forfeits an attacking turn if consuming a potion
     * @return true if a potion is consumed
     */
    public boolean consumePotion(){
        if(character.getCurrHP() <= 30 && character.getPotions() >= 2){
            character.usePotion();
            character.usePotion();
            System.out.println("Consuming two pots");
            return true;
        } else if (character.getCurrHP() <= 60 && character.getPotions() >= 1){
            character.usePotion();
            System.out.println("Consuming one pot");
            return true;
        }
        return false;
    }

    /**
     * Check if the character is stunned
     * @return true if stunned
     */
    public boolean isStunned(){
        ArrayList<String> statusID = character.getStatusIDs();
        if(statusID.contains("Stunned") || statusID.contains("Paralysed") ||
        statusID.contains("Frozen") || statusID.contains("Sleeping")){
            return true;
        } return false;
    }

    /**
     * Commands the Character to attack with their weapon
     * @param target - the target of the attack
     * @return the damage dealt
     */
    public double weaponAttack(MovingEntity target) {
        double damageDealt = character.getCurrATK();
        for (Weapon weapon: character.getEquippedItems().getWeapons()) {
            // each weapon gives a multplier/ effect to the target
            damageDealt = weapon.attack(target, damageDealt);
            if (((Item) weapon).checkBroken()) {
                character.updateStats();
            }  
        }
        return damageDealt;
    }

    /**
     * Commands the Character to critAttack with their weapon
     * @param target - the target of the attack
     * @return the damage dealt
     */
    public double weaponCritAttack(MovingEntity target) {
        // crit multiplication here so weapons only need ot apply their effects
        double damageDealt = character.getCurrATK() * 1.5;

        for (Weapon weapon: character.getEquippedItems().getWeapons()) {
            // each weapon gives a multplier/ effect to the target
            damageDealt = weapon.critAttack(target, damageDealt);
            if (((Item) weapon).checkBroken()) {
                character.updateStats();
            }  
        }
        return damageDealt;
    }

    /**
    * Calculates the net damage post item mitigation (not counting DEF)
    * Solely Item effects
    * @param damage - the received damage post item mitigation
    * @return the net damage
    */
    public double reduceDamage(MovingEntity enemy, double damage) {
        double netDamage = damage;
        for (Protection protection: character.getEquippedItems().getProtectionEquips()) {
            // calculate the reductions from equipment
            netDamage = protection.reduceDamage(enemy, netDamage);
            if (((Item) protection).checkBroken()) {
                character.updateStats();
            }            
        }
        return netDamage;
    }

    /**
     * Calculates if the character may successfully resist status effects
     * @param status - the status being applied
     * @return true if the character sucessfully resisted the status, false otherwise
     */
    public boolean resistStatus(Status status) {
        if (status.getID().equals("Zombified") || status.getID().equals("Confused")) {
            // Character is immune to zombification/ confusion
            return true;
        } else {
            for (Protection protection: character.getEquippedItems().getProtectionEquips()) {
                if (protection.resistStatus(status)) {
                    // if item helps resist status, we successfully resisted
                    return true;
                }
            }
        }
        return false;
    }
    
}
