package unsw.loopmania.goal;

import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;

public class SimpleEXPGoal implements Goal{

    private String goalType;
    private int quantity;
    private int nestLevel = 0;

    public SimpleEXPGoal(int quantity){
        goalType = "experience";
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
     * Pretty print the goals 
     * @param character
     * @param world
     * @return the string containing the goal to be printed 
     */
    @Override
    public boolean evaluateGoalReached(Character character, LoopManiaWorld world) {
        if(character.getCummulativeXP() >= quantity) {
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
        return complete + "EXP: " + String.valueOf(character.getCummulativeXP()) + "/" + quantity;
    }

    @Override
    public int getNestLevel() {
        return nestLevel;
    }
    

    
}
