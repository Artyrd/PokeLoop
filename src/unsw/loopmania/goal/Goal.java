package unsw.loopmania.goal;

import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.Character;

public interface Goal {

    /**
     * Get the type of goal: Experience, gold, cycles
     * @return string of goal type
     */
    public String getGoalType();

    /**
     * Get the numeric requirement of the goal
     * @return int of the goal requirement
     */
    public int getGoalQuantity();

    /**
     * Evaluate whether the goal was completed or not
     * @param character 
     * @param world
     * @return true/false whether the goal was reached
     */
    public boolean evaluateGoalReached(Character character, LoopManiaWorld world);

    /**
     * Get the goals as a string to show on the front end
     * @param character
     * @param world
     * @return string of goals
     */
    public String showGoals(Character character, LoopManiaWorld world);

    /**
     * Get the number of nested goals
     * @return int of goals
     */
    public int getNestLevel();

}