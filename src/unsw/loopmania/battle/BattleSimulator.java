package unsw.loopmania.battle;

import java.util.ArrayList;

import javafx.beans.property.SimpleBooleanProperty;
import unsw.loopmania.Character;
import unsw.loopmania.MovingEntity;
import unsw.loopmania.enemies.Enemy;
import unsw.loopmania.status.Status;

/**
 * Battle simulator class that automates the turn based battle of enemies, friendlies and structures.
 * The battle is turn-based, with the character attacking first, then an enemy and so on and so forth.
 * 
 * The order of enemy attacks are based on their distance to the character, with closer enemies attacking first.
 * Enemy attacks will target allies first, then the character, except for the Vampire, which will 
 * always target the character.
 * 
 * The character will attack first, followed by their allies, followed by structures.
 * 
 * Allies/enemies who are converted (by a Zombified attack or a Tranced attack) will convert to
 * the opposing side, but will attack last. The character cannot be Zombified.
 * 
 * A battle is over when all enemies are dead, or there are no enemies to fight (the last enemy was tranced and converted),
 * or when the ally dies. 
 */
public class BattleSimulator {

    public Character character;
    private ArrayList<MovingEntity> friendlies;
    private ArrayList<Enemy> enemies;
    private ArrayList<Enemy> defeated;
    private ArrayList<MovingEntity> targetAllies;
    private ArrayList<MovingEntity> targetEnemies;
    private ArrayList<MovingEntity> battleAllies;
    private ArrayList<MovingEntity> battleEnemies;
    private MovingEntity battleAlly;
    private MovingEntity battleEnemy;
    private MovingEntity targetAlly;
    private MovingEntity targetEnemy;
    private boolean result;

    //public SimpleBooleanProperty battleActive;

    /**
     * Creates a new Battle Simulation
     * @param character - the character
     * @param fighters - all participating entities, the class handles who is on which side
     */
    public BattleSimulator (Character character, ArrayList <MovingEntity> fighters, ArrayList<MovingEntity> structures) {
        this.character = character;
        friendlies = new ArrayList<MovingEntity>();
        enemies = new ArrayList<Enemy>();
        defeated = new ArrayList<Enemy>();

        for (MovingEntity fighter: fighters) {
            if (fighter.isFriendly() && !(fighter instanceof Character)) {
                friendlies.add(fighter);
            } else if (!fighter.isFriendly()) {
                enemies.add((Enemy) fighter);
            }
        }
        
        // Initialise the first target on the ally and enemy side.
        targetAllies = new ArrayList<MovingEntity>(friendlies);
        targetAllies.add(character);        // Character is the last attack target
        targetEnemies = new ArrayList<MovingEntity>(enemies);
        targetAlly = targetAllies.get(0);
        targetEnemy = targetEnemies.get(0);
        
        // Initialise the first attackers on the ally and enemy side
        battleAllies = new ArrayList<MovingEntity>(friendlies);
        battleAllies.add(0, character);     // Character is the first attacker
        battleEnemies = new ArrayList<MovingEntity>(enemies);
        battleAlly = battleAllies.get(0);
        battleEnemy = battleEnemies.get(0);

        if(structures != null){
            battleAllies.addAll(structures);
        }
        
        result = false;

        /* frontend */
        //battleActive = new SimpleBooleanProperty(true);
    }

    /**
     * runs the battle simulation 
     * ONLY FOR TESTING
     * @deprecated - ran by the battle controller now
     * @return true if the battle was won, false if character dies
     */
    public boolean runBattle() {
        // loop the battles here

        //battleAllies.addAll(structures);

        battle();
        
        findDefeated();
        cleanStatuses();
        
        // End condition of battle is: all enemies die, or character dies
        if(character.getCurrHP() <= 0){
            return false;
        } else {
            result = true;
            return true;
        }
    }

    /**
     * Deprecated: now for testing only!!
     * Battle is now looped through the BattleController
     * Continually runs the battle until character dies, or all enemies die, or no more enemies to fight.
     * Since enemies and friendlies are sorted by distance to character, they are the
     * order of targetting and priority of attacking.
     */
    private void battle(){

        while(!isBattleOver()){

            battleAllyAttack();
            if(isBattleOver()) return;

            battleEnemyAttack();
            if(isBattleOver()) return;

            readyNextAttackers();

        }
        System.out.println("battle over");

    }

    public void battleAllyAttack() {
        // Each "Battle Round" starts with the character attacking
        // Update the statuses of all entities affected by statuses
        if(battleAlly instanceof Character){
            battleTickStatuses();
            updateBattleSides();
        }

        // battleAlly now attacks targetEnemy or preference based on the type of battleAlly
        if(battleAlly != null){         // If all allies have attacked, they cannot attack anymore for this round
            MovingEntity target = battleAlly.getAttackPreference(targetEnemies);
            // if not picky, get preference is the default next target
            if(target == null) {
                target = targetEnemy;
            }
            battleAlly.attack(target, targetAllies, targetEnemies);
            //System.out.println(battleAlly.getID() + " is attacking " + target.getID() + " remaining hp: " + target.getCurrHP());
            if (updateTarget(target) == false) {
                // if target still alive, check if its still unfriendly
                targetEnemy = checkSideSwap(targetEnemy, true, battleAllies, targetAllies, battleEnemies, targetEnemies);
            }
            battleAlly = nextAttacker(battleAlly, battleAllies);

            }
    }

    public void battleEnemyAttack() {
        // battleEnemy now attacks targetAlly or preference based on the type of battleEnemy
        if(battleEnemy != null){        // If all enemies have attacked, they cannot attack anymore for this round

            MovingEntity target = battleEnemy.getAttackPreference(targetAllies);
            if(target == null){
                target = targetAlly;
            }

            battleEnemy.attack(target, targetEnemies, targetAllies);
            //System.out.println(battleEnemy.getID() + " is attacking " + target.getID() + " remaining hp: " + target.getCurrHP());
            if (updateTarget(target) == false) {
                // if target still alive, check if its still friendly incase of zombify
                targetAlly = checkSideSwap(targetAlly, false, battleEnemies, targetEnemies, battleAllies, targetAllies);
            }

            battleEnemy = nextAttacker(battleEnemy, battleEnemies);

        }
    }

    public void readyNextAttackers() {
        // If all battlers from both sides have finished attacking, restart the attacks by going to the next round
        if(battleAlly == null && battleEnemy == null){
        //if(battleAlly == null && battleEnemy == null && !isBattleOver()){
            battleAlly = battleAllies.get(0);
            battleEnemy = battleEnemies.get(0);
        }
    }


    /**
     * Ticks down all fighter's battleticks
     */
    private void battleTickStatuses(){
        updateStatuses(battleEnemies);
        updateStatuses(battleAllies);
    }

    /**
     * Update the sides of battle, checking if allies have been converted to enemies
     * and if enemies have been converted to allies
     */
    private void updateBattleSides() {
        ArrayList<MovingEntity> convertedEnemies = new ArrayList<MovingEntity>();
        ArrayList<MovingEntity> convertedAllies = new ArrayList<MovingEntity>();
        for (MovingEntity e: battleEnemies) {
            // check converting enemies to allies
            if (e.isFriendly() == true) {
                convertedEnemies.add(e);
            }
        }
        // convert here to avoid ConcurrentModificationException
        for (MovingEntity converted: convertedEnemies) {
            checkSideSwap(converted, true, battleAllies, targetAllies, battleEnemies, targetEnemies);
        }
        for (MovingEntity e: battleAllies) {
            // check converting allies to enemies
            if (e.isFriendly() == false) {
                convertedAllies.add(e);
            }
        }
        for (MovingEntity converted: convertedAllies) {
            checkSideSwap(converted, false, battleEnemies, targetEnemies, battleAllies, targetAllies);
        }
    }
    /**
     * Updates the battle status of an Entity post attack
     * @param target
     */
    private boolean updateTarget(MovingEntity target) {
        // check if the target died
        if (target.getCurrHP() > 0) {
            return false;
        }
        // else, target is dead and update accordingly

        if (battleEnemies.contains(target)) {
            if (battleEnemy != null && battleEnemy.equals(target)) {
                battleEnemy = nextAttacker(battleEnemy, battleEnemies);
            }
            targetEnemy = nextTarget(targetEnemy, targetEnemies, battleEnemies);
            battleEnemies.remove(target);

        } else if (battleAllies.contains(target)) {
            if (battleAlly != null && battleAlly.equals(target)) {
                battleAlly = nextAttacker(battleEnemy, battleAllies);
            }
            targetAlly = nextTarget(targetAlly, targetAllies, battleAllies);
            battleAllies.remove(target);
        }
        return true;
    }

    /**
     * Helper method that updates the statuses of a given array of entities
     * @param targets
     */
    private void updateStatuses(ArrayList<MovingEntity> targets){
        //ArrayList<MovingEntity> swapped = new ArrayList<>();
        for(MovingEntity target : targets) {
            for (Status s: target.getStatusList()) {
                if(s != null) s.battleTick(target);
            }
            /*
            if(target.isFriendly() == friendly){
                target.setStatus(null);
                swapped.add(target);
            }
            */
        }
    }

    /**
     * Method to check if target has been converted due to an attack, and updates the corresponding arraylists if they
     * have been converted
     * @param target - the target we may have converted
     * @param isFriendly - the target's new allegiance
     * @param battleDest - the caster's battle side (battleAllies/ battleEnemies)
     * @param targetDest - the caster side's currently targetted entity
     * @param battleSrc - the target's battle side
     * @param targetSrc - the target side's currently targetted entity
     * @return nextTarget if current target has been converted, null if there are no more targets after current target has been converted, or
     *      current target if they have not been converted
     */
    private MovingEntity checkSideSwap(MovingEntity target, boolean isFriendly, ArrayList<MovingEntity> battleDest, ArrayList<MovingEntity> targetDest, 
        ArrayList<MovingEntity> battleSrc, ArrayList<MovingEntity> targetSrc)
    {
        // Check if target has changed sides due to attack (been tranced or been zombified), 
        if(target.isFriendly() == isFriendly && target.getCurrHP() > 0){
            // target was converted! >:)
            if (battleEnemy != null && battleEnemy.equals(target)) {
                battleEnemy = nextAttacker(battleEnemy, battleEnemies);
            } else if (battleAlly != null && battleAlly.equals(target)) {
                battleAlly = nextAttacker(battleEnemy, battleAllies);
            }
            MovingEntity nextTarget = null;
            int currIndexTarget = targetSrc.indexOf(target);
            if(currIndexTarget < targetSrc.size() - 1){
                nextTarget = targetSrc.get(currIndexTarget + 1);
            }
            System.out.println(target.getID() + " is swapping sides");
            battleDest.add(target);
            targetDest.add(target);
            battleSrc.remove(target);
            targetSrc.remove(target);

            return nextTarget;
        }

        return target;

    }

    /**
     * Returns the next target for attacks, if no more targets, returns null
     * @param currTarget
     * @param availableTargets
     * @return next target that will be targetted by the other team for attacks, or null if no more targets
     */
    private MovingEntity nextTarget(MovingEntity currTarget, ArrayList<MovingEntity> availableTargets, ArrayList <MovingEntity> battlers){
        int currIndex = availableTargets.indexOf(currTarget);

        if (currTarget.getCurrHP() > 0) {
            return currTarget;
        }

        // currTarget is now dead, remove from availableTargets and battlers
        availableTargets.remove(currTarget);
        battlers.remove(currTarget);
        
        if(currIndex < availableTargets.size()){
            System.out.println(currTarget.getID() + " is dead, moving on to next target is " + availableTargets.get(currIndex).getID());
            return availableTargets.get(currIndex);
        } else {
            return null;
        }
    }

    /**
     * Returns the nextAttacker in the battle, returns null if reaches end of list of attackers
     * @param currAttacker
     * @param availableAttackers
     * @return next attacker if they exist, null if reaches end of list
     */
    private MovingEntity nextAttacker(MovingEntity currAttacker, ArrayList<MovingEntity> availableAttackers){
        int currIndex = availableAttackers.indexOf(currAttacker);

        if(currIndex < availableAttackers.size() - 1){
            //System.out.println(currAttacker.getID() + " just finished attacking, next attacker is " + availableAttackers.get(currIndex + 1).getID());
            return availableAttackers.get(currIndex + 1);
        } else {
            return null;
        }
    }

    /**
     * Method to check if there are any more enemies to fight, or if the character is alive
     * @return true if battle is over (regardless of which team won), false if battle is not over
     */
    public boolean isBattleOver(){

        if(battleEnemies.size() == 0) {
            System.out.println("Battle over all enemies dead");
            findDefeated();
            cleanStatuses();
            result = true;

            return true;
        }
        if(character.getCurrHP() <= 0){
            System.out.println("Battle over character has died");
            //findDefeated();
            cleanStatuses();
            result = false;

            return true;
        }
        //System.out.printf("remaining enemies: %s\n", battleEnemies.size());
        return false;
    }

    /**
     * updates the fighters in the battle, if any are 0 hp, add them to defeated list
     */
    public void findDefeated(){
        for(Enemy enemy: enemies){
            if(enemy.getCurrHP() <= 0 || enemy.isFriendly()){
                if (!defeated.contains(enemy)) {
                    defeated.add(enemy);
                }
            }
        }
    }

    /**
     * removes all statuses from all friendlies and allies at the end of battle
     */
    public void cleanStatuses(){

        System.out.println("cleaning battle statuses: ");
        // for(MovingEntity enemy: enemies){
        for(MovingEntity enemy: battleEnemies){
            for (Status enemyStatus: enemy.getStatusList()) {
                if (enemyStatus != null) enemyStatus.endStatus(enemy);
            }
        }

        //for(MovingEntity friendly: friendlies){
        for(MovingEntity friendly: battleAllies){
            System.out.println("cleaning battle statuses for: " + friendly.getID());
            System.out.println(friendly.getStatusIDs().toString());
            if (friendly.getStatusIDs().contains("Tranced")) {
                // kill off tranced entities
                friendly.setCurrHP(0);
            }
            for (Status friendlyStatus: friendly.getStatusList()) {
                System.out.println("status was: " + friendlyStatus.getID());
                if (friendlyStatus != null) friendlyStatus.endStatus(friendly);
            }
        }

    }

    /**
     * Getter for set of defeated enemies at the end of the battle
     * @return
     */
    public ArrayList<Enemy> getDefeated() {
        return defeated;
    }

    /**
     * Debuging printing helper: shows the entities on the battling side of enemies and allies
     */
    public void printCurrentBattlers() {
        //System.out.println("Char hp: " + character.getCurrHP());
        System.out.printf("battle Allies: ");
        for (MovingEntity e: battleAllies) {
            System.out.printf("%s ",e.getID());
        }
        System.out.printf("\n");

        System.out.printf("battle Enemies: ");
        for (MovingEntity e: battleEnemies) {
            if (e.getBattleBehaviour() instanceof ZombieBattleBehaviour) {
                System.out.printf("Z");
            }
            System.out.printf("%s ",e.getID());
        }
        System.out.printf("\n");
    }


    // GETTERS
    public ArrayList<MovingEntity> getBattleAllies() {
        return battleAllies;
    }
    public ArrayList<MovingEntity> getBattleEnemies() {
        return battleEnemies;
    }

    public MovingEntity getBattleAlly() {
        return battleAlly;
    }

    public MovingEntity getBattleEnemy() {
        return battleEnemy;
    }

    public boolean getResult() {
        return result;
    }
}
