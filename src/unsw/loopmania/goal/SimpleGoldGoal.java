package unsw.loopmania.goal;

import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;

public class SimpleGoldGoal implements Goal {

    private String goalType;
    private int quantity;
    private int nestLevel = 0;

    public SimpleGoldGoal(int quantity){
        goalType = "gold";
        this.quantity = quantity;
    }

	@Override
	public String getGoalType() {
		return goalType;
	}

	@Override
	public int getGoalQuantity() {
		return quantity;
	}

	/**
     * Check whether the goal was reached
     * @param character 
     * @param world
     * @return true if reached, false if not
     */
	@Override
	public boolean evaluateGoalReached(Character character, LoopManiaWorld world) {
		if(character.getGold() >= quantity){
            return true;
        } else {
            return false;
        }
	}

	/**
     * Pretty print the goals 
     * @param character
     * @param world
     * @return the string containing the goal to be printed 
     */
	@Override
	public String showGoals(Character character, LoopManiaWorld world) {
		String complete = "";
        if(evaluateGoalReached(character, world)){
            complete = "X  ";
        } else {
            complete = "O  ";
        }
        return complete + "GOLD: " + String.valueOf(character.getGold()) + "/" + quantity;
	}

	@Override
	public int getNestLevel() {
		return nestLevel;
	}
    
}
