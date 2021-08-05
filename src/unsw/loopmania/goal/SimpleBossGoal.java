package unsw.loopmania.goal;

import java.util.ArrayList;
import java.util.List;

import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.enemies.Boss;
import unsw.loopmania.enemies.Doggie;
import unsw.loopmania.enemies.ElanMuske;

public class SimpleBossGoal implements Goal {

    private String goalType;
    private int nestLevel = 0;
    List<Class<? extends Boss>> bosses = new ArrayList<Class<? extends Boss>>();

    public SimpleBossGoal(){
        goalType = "bosses";
        bosses.add(Doggie.class);
        bosses.add(ElanMuske.class);
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
        List<Boss> defeatedBosses = world.getDefeatedBosses();
        if(defeatedBosses.size() != bosses.size()) {
            return false;
        }

        return true;

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
        return complete + "BOSS: Defeat all bosses";
    }

    @Override
    public int getNestLevel() {
        return nestLevel;
    }
    
}
