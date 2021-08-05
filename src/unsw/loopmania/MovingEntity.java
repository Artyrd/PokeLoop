package unsw.loopmania;

import java.util.ArrayList;
import java.util.Random;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.battle.BattleBehaviour;
import unsw.loopmania.movement.MovementBehaviour;
import unsw.loopmania.movement.PathPosition;
import unsw.loopmania.status.Status;

/**
 * The moving entity: a flexible object that has optional: 
 * hp, atk, def, critChance, friendly bool, Status, PathPosition, MovementBehaviour, BattleBehaviour
 */
public abstract class MovingEntity extends Entity {

    //private double maxHP;
    private SimpleDoubleProperty maxHP;
    private SimpleDoubleProperty currHP;
    //private SimpleDoubleProperty percentageHP;
    private double baseATK;
    private double baseDEF;
    private SimpleDoubleProperty currATK;
    private SimpleDoubleProperty currDEF;
    private SimpleDoubleProperty critChance;

    // initialise random here so testing goes smoothly
    private Random chance = new Random(2511);
    private int battleRadius;
    private int supportRadius;
    private boolean friendly;
    //private Status status;
    private ArrayList<Status> statuses;
    private PathPosition position;
    private MovementBehaviour mvBehaviour;
    private BattleBehaviour battleBehaviour;

    public SimpleIntegerProperty action;
    //private SimpleDoubleProperty currHpProperty;

    /**
     * Create an empty moving entity which behaviours and attributes could be attached to
     * @param position represents the current position in the path
     */

     /*
    public MovingEntity(PathPosition position) {
        this(null, 0 ,0 ,0 ,0 , false, null, null);
    }
    */

    /**
     * Creates a MovingEntity which has no movement or battle behaviour
     * @param position
     * @param hp
     * @param atk
     * @param def
     * @param critChance
     * @param friendly
     */
    public MovingEntity(PathPosition position, double hp, double atk, double def, int critChance, boolean friendly){
        this(position, hp, atk, def, critChance, friendly, null, null);
    }

    /**
     * Creates a MovingEntity which can move and battle
     * @param position - the initial PathPosition
     * @param hp - base hp
     * @param atk - base attack
     * @param def - base defence
     * @param critChance - base critical attack chance (in %)
     * @param friendly - if the entity is friendly to the character or not
     * @param mvBehaviour - the MovementBehaviour of the entity
     * @param battleBehaviour - the BattleBehaviour of the entity
     */
    public MovingEntity(PathPosition position, double hp, double atk, double def, int critChance, boolean friendly, MovementBehaviour mvBehaviour, BattleBehaviour battleBehaviour) {
        super();
        this.position = position;
        this.maxHP = new SimpleDoubleProperty(hp);
        this.currHP = new SimpleDoubleProperty(hp);
        //this.percentageHP = new SimpleDoubleProperty(1.0);
        this.baseATK = atk;
        this.currATK = new SimpleDoubleProperty(atk);
        this.baseDEF = def;
        this.currDEF = new SimpleDoubleProperty(def);
        this.critChance = new SimpleDoubleProperty(critChance);
        this.friendly = friendly;
        this.mvBehaviour = mvBehaviour;
        this.battleBehaviour = battleBehaviour;
        this.statuses = new ArrayList<Status>();

        this.action = new SimpleIntegerProperty(0); // for battle screen, 0 = nothing, 1 = attack, 2 = receive attack
    }

    public BattleBehaviour getBattleBehaviour() {
        return this.battleBehaviour;
    }

    public void setBattleBehaviour(BattleBehaviour battleBehaviour) {
        this.battleBehaviour = battleBehaviour;
    }

    public void setMovementBehaviour(MovementBehaviour movementBehaviour) {
        this.mvBehaviour = movementBehaviour;
    }

    public PathPosition getPosition() {
        return position;
    }

    public double getCurrHP() {
        return currHP.get();
    }

    public void setCurrHP(double hp) {
        currHP.set(hp);
    }

    public double getMaxHP() {
        return maxHP.get(); 
    }

    public void setMaxHP(double maxHP) {
        this.maxHP.set(maxHP);
    }

    /**
     * reduces current hp by the damage taken
     * @return the new current HP
     */
    public double reduceHP(double damage) {
        // avoid healing when damage is calculated as negative
        if (damage < 0) {
            damage = 0;
        }

        currHP.set(currHP.get() - damage);
        if (currHP.get() < 0) {
            currHP.set(0);
        }

        return currHP.get();
    }

    /**
     * restores current hp by the health given
     * @return the new current HP
     */
    public double restoreHP(double health) {
        action.set(3); // 3 to animate restore hp
        action.set(0);

        currHP.set(currHP.get() + health);
        if (currHP.get() > maxHP.get()) {
            currHP.set(maxHP.get());
        }
        return currHP.get();
    }

    /**
     * Increases the Moving Entity's max hp, does not increase currentHP.
     * aka: increases the entity's maximum "possible" hp.
     * @param hp - the amount of hp to increase
     * @return the new maxHP
     */
    public double increaseMaxHP(double hp) {
        maxHP.set(maxHP.get() + hp);
        return maxHP.get();
    }
    /**
     * gets the MovingEntity's base attack, it is the attack the unit is born with unless it trains hard and gets stronger naturally
     */
    public double getBaseATK() {
        return baseATK;
    }
    public void setBaseATK(double atk) {
        this.baseATK = atk;
    }
    /**
     * gets the MovingEntity's current defence, which could be modified from its base stat by items and powerups
     */
    public double getCurrATK() {
        return currATK.get();
    }
    public void setCurrATK(double atk) {
        currATK.set(atk);
    }
    /*
    public void addCurrATK(double atk) {
        currATK.set(currATK.get() + atk);
    }
    */

    /**
     * gets the MovingEntity's base defence, it is the defence the unit is born with unless it trains hard and gets stronger naturally
     */
    public double getBaseDEF() {
        return baseDEF;
    }

    public void setBaseDEF(double def) {
        this.baseDEF = def;
    }
    /**
     * gets the MovingEntity's current defence, which could be modified from its base stat by items and powerups
     */
    public double getCurrDEF() {
        return currDEF.get();
    }
    public void setCurrDEF(double def) {
        currDEF.set(def);
    }
    /*
    public void addCurrDEF(double def) {
        currDEF.set(currDEF.get() + def);
    }
    */
    /**
     * @return the entity's chance of inflicting a critical attack instead of a regular attack
     */
    public int getCritChance() {
        return (int) critChance.get();
    }
    public void setCritChance(int critChance) {
        this.critChance.set(critChance);
    }

    public int getBattleRadius(){
        return battleRadius;
    }

    public void setBattleRadius(int battleRadius) {
        this.battleRadius = battleRadius;
    }

    public int getSupportRadius(){
        return supportRadius;
    }

    public void setSupportRadius(int supportRadius) {
        this.supportRadius = supportRadius;
    }

    /**
     * returns the name all statuses affecting the MovingEntity
     * @return a list of Status ids, or an empty list if unafflicted by any Statuses
     */
    public ArrayList<String> getStatusIDs() {
        cleanStatus();
        ArrayList<String> statusIDs = new ArrayList<String>();
        for (Status status: statuses) {
            statusIDs.add(status.getID());
        }

        return statusIDs;
    }

    /**
     * Check for statuses which have ended
     * If they have ended, remove them 
     */
    private void cleanStatus(){
        ArrayList<Status> timedOutStatuses = new ArrayList<Status>();
        for(int status = statuses.size() - 1; status >= 0; status--){
            Status lastStatus = this.statuses.get(status);
            if(lastStatus != null){
                if(lastStatus.getID().equals("null")){
                    timedOutStatuses.add(lastStatus);
                }
            }
            
        }
        if (timedOutStatuses.size() > 0) {
            statuses.removeAll(timedOutStatuses);
            action.set(4); // signal frontend we're clearing statuses
            action.set(0);
        }

    }

    /*
    ########################################
    !!!         TESTING METHODS          !!!
    ########################################
    */
    public void clearStatuses() {
        statuses.clear();
    }

    /**
     * Returns the Status object affecting the entity which matches the statusID
     * @param statusID
     * @return the Status object
     */
    public Status getStatusByID(String statusID) {
        cleanStatus();

        if(statuses.size() == 0){
            return null;
        } else {
            for (Status status: statuses) {
                if (status.getID().equals(statusID)) {
                    return status;
                }
            }
        }
        return null;
    }

    public ArrayList<Status> getStatusList(){
        cleanStatus();
        return statuses;
    }

    /**
     * Adds a new Status effect to the entity
     * If a 2nd instance of a status is applied, it gets "refreshed"
     */
    public void setStatus(Status status) {
        // kick out any existing instance of the same Status
        Status oldStatus = getStatusByID(status.getID());
        if (oldStatus != null) {
            statuses.remove(oldStatus);
        }
        // now add the Status
        this.statuses.add(status);
        //System.out.println("setting status: " + status.getID());

        action.set(4); // signal frontend we're getting a status inflicted
        action.set(0);
    }

    public boolean isFriendly() {
        return friendly;
    }

    public void setFriendly(boolean friendly) {
        this.friendly = friendly;
    }

    /**
     * move the MovingEntity according to its MovementBehaviour
     */
    public void move() {
        if (mvBehaviour == null) {
            return;
        } else {
            mvBehaviour.move(position, this);
        }
    }

    /**
     * Apply statuses in the world
     */
    public void runStatusWorldTicks() {
        cleanStatus();
        if(statuses.size() > 0){
            for(Status currentStatus: statuses){
                if(currentStatus != null){
                    currentStatus.worldTick(this);
                }
            }
        } else {
            return;
        }
        
    }
    
    /*
    ########################################
    !!!           BATTLE STUFF           !!!
    ########################################
    */
    
    /**
     * Attack the target according to the moving entity's battle behaviour
     * There is a chance to critically attack
     * @param target 
     * @param enemies list of enemies
     * @param allies list of allies
     * @return damage dealt
     */
    public double attack(MovingEntity target, ArrayList<MovingEntity> enemies, ArrayList<MovingEntity> allies) {
        // has a chance to do a critical attack!
        if (isCritical()) {
            return critAttack(target, enemies, allies);
        }
        action.set(1); // signal frontend we're attacking
        action.set(0);
        return battleBehaviour.attack(this, target, currATK.get());
    }

    /**
     * Critically attack the target according to the moving entitys battle behaviour
     * @param target
     * @param enemies list of enemies
     * @param allies list of allies
     * @return damage dealt
     */
    public double critAttack(MovingEntity target, ArrayList<MovingEntity> enemies, ArrayList<MovingEntity> allies) {
        action.set(1); // signal frontend we're attacking
        action.set(0);
        return battleBehaviour.critAttack(this, target, currATK.get(), enemies, allies);
    }

    /**
     * Receive an attack from a source of amount damage according to the 
     * moving entity's battle behaviour
     * @param source
     * @param damage
     * @return
     */
    public double receiveAttack(MovingEntity source, double damage) {
        action.set(2); // signal frontend we're taking damage
        action.set(0);
        return battleBehaviour.receiveAttack(source, this, damage);
    }

    /**
     * Receive a given status according to the moving entity's battle behaviour
     * @param status
     * @return
     */
    public boolean receiveStatus(Status status) {
        return battleBehaviour.receiveStatus(this, status);
    }
    public MovingEntity getAttackPreference(ArrayList<MovingEntity> targets) {
        return battleBehaviour.getAttackPreference(targets);
    }

    /**
     * Calculates off Moving Entity's critChance if it lands a critical attack
     * @return true if rng gives a crit, false if no crit
     */
    protected boolean isCritical() {
        //int chance = new Random().nextInt(100);
        int crit = chance.nextInt(100);
        if (crit < critChance.get()) {
            return true;
        } else {
            return false;
        }


    }

    /* 
    ########################################
    !!!!        FRONT END STUFF         !!!!
    ########################################
    */
    public SimpleDoubleProperty getMaxHpProperty() {
        return maxHP; 
    }
    public SimpleDoubleProperty getCurrHpProperty() {
        return currHP;
    }
    public SimpleDoubleProperty getCurrAtkProperty() {
        return currATK;
    }
    public SimpleDoubleProperty getCurrDefProperty() {
        return currDEF;
    }
    public SimpleDoubleProperty getCurrCritProperty() {
        return critChance;
    }

    public int getX() {
        return x().get();
    }

    public int getY() {
        return y().get();
    }

    public SimpleIntegerProperty x() {
        if (position == null) {
            return null;
        }
        return position.getX();
    }

    public SimpleIntegerProperty y() {
        if (position == null) {
            return null;
        }
        return position.getY();
    }

}
