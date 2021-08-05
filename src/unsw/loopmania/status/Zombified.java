package unsw.loopmania.status;

import unsw.loopmania.MovingEntity;
import unsw.loopmania.battle.ZombieBattleBehaviour;

public class Zombified implements Status {

    //private double duration;
    private String id;

    public Zombified() {
        this.id = "Zombified";
        // Duration does not end
        //this.duration = Double.POSITIVE_INFINITY;
    }

    @Override
    public String getID() {
        return this.id;
    }

    public void setID(String id) {
        this.id = id;
    }

    /**
     * Apply the status to the entity, turning them into a zombie
     * @param entity the entity receiving the status
     */
    @Override
    public void applyStatus(MovingEntity entity) {
        entity.setFriendly(false);
        // Change the battle behaviour of the entity to the Zombie battle behaviour
        entity.setBattleBehaviour(new ZombieBattleBehaviour(entity.getBattleBehaviour()));
    }

    /**
     * End the status of the entity
     * @param entity the entity receiving the status
     */
    @Override
    public void endStatus(MovingEntity entity) {
        // this status never ends
        return;
    }

    /**
     * Continue the status of the entity during battle
     * @param entity the entity receiving the status
     */
    @Override
    public void battleTick(MovingEntity entity) {
        // do nothing, this status does not end during battle
        
    }
    
    /**
     * Continue the status of the entity during world travel
     * @param entity the entity receiving the status
     */
    @Override
    public void worldTick(MovingEntity entity) {
        // do nothing, this status does not exist outside of battle
    }

}
