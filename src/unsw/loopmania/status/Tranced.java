package unsw.loopmania.status;


import unsw.loopmania.MovingEntity;

public class Tranced implements Status {

    private int duration;
    private String id;

    public Tranced() {
        this.id = "Tranced";
        this.duration = 5;
    }

    @Override
    public String getID() {
        return this.id;
    }

    /**
     * Apply the status to the entity, trancing the entity
     * @param entity the entity receiving the status
     */
    @Override
    public void applyStatus(MovingEntity entity) {
        // Make sure the enemy stays tranced as a friendly entity and reduce the duration
        entity.setFriendly(true);
        duration = duration - 1;
        // End after duration = 0
        if (duration == 0) {
            endStatus(entity);
            return;
        }
        System.out.printf("%s is tranced!\n", entity.getID());
    }

    /**
     * End the status of the entity, returning them back to their normal behaviour
     * @param entity the entity receiving the status
     */
    @Override
    public void endStatus(MovingEntity entity) {
        id = "null";
        entity.setFriendly(false);
        System.out.printf("%s is no longer tranced :(\n", entity.getID());

    }

    /**
     * Continue the status of the entity during battle
     * @param entity the entity receiving the status
     */
    @Override
    public void battleTick(MovingEntity entity) {
        applyStatus(entity);
    }

    /**
     * Continue the status of the entity during world travel
     * @param entity the entity receiving the status
     */
    @Override
    public void worldTick(MovingEntity entity) {
        // do nothing, does not apply outside of battle
    }
    
}
