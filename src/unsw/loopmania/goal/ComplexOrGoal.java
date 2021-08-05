package unsw.loopmania.goal;

import java.util.ArrayList;

import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;

public class ComplexOrGoal implements Goal {

    private String goalType;
    private ArrayList<Goal> subgoals;

    public ComplexOrGoal(String goalType, ArrayList<Goal>subgoals) {
        this.goalType = goalType;
        this.subgoals = subgoals;
    }

    @Override
    public String getGoalType() {
        return goalType;
    }

    @Override
    public int getGoalQuantity() {
        return 0;
    }

    /**
     * Check whether the goal was reached
     * @param character 
     * @param world
     * @return true if reached, false if not
     */
    @Override
    public boolean evaluateGoalReached(Character character, LoopManiaWorld world) {
        boolean goalReached = false;
        for(Goal subgoal : subgoals){
            goalReached = goalReached | subgoal.evaluateGoalReached(character, world);
        }
        return goalReached;
    }

    /**
     * Pretty print the goals 
     * @param character
     * @param world
     * @return the string containing the goal to be printed 
     */
    @Override
    public String showGoals(Character character, LoopManiaWorld world) {
        String goalString = "";
        for(Goal subgoal: subgoals){
            goalString = goalString + subgoal.showGoals(character, world);
            goalString = padNewLines(goalString);
            goalString = goalString + "     " + goalType;
            goalString = padNewLines(goalString);
        }
        goalString = goalString.substring(0, goalString.length() - (goalType.length() + 1 + getNestLevel()));
        return goalString;
    }

    /**
     * Get the number of nested goals within a goal
     * @return the number of nested goals
     */
    @Override
    public int getNestLevel() {
        int maxNest = 0;
        for(Goal subgoal : subgoals){
            if(subgoal.getNestLevel() > maxNest){
                maxNest = subgoal.getNestLevel();
            }
        }
		return 1 + maxNest;
    }

    /**
     * Helper to add a new line to goal pretty printing
     * @param goalString
     * @return
     */
    public String padNewLines(String goalString){
        int nestLevel = getNestLevel();
        for(int nest = 0; nest < nestLevel; nest++){
            goalString = goalString + "\n";
        } return goalString;
    }
    
}
