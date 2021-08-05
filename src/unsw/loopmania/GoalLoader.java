package unsw.loopmania;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import unsw.loopmania.goal.*;

public class GoalLoader {

    public GoalLoader() {}

    /**
     * Given the world and the JSON goal-condition, recursively parses the goals and returns the goals as the Goal composite class
     * @param currentJsonGoal
     * @return
     */
    public Goal loadGoals(JSONObject currentJsonGoal){
        ArrayList<Goal> subgoals = new ArrayList<Goal>();
        return loadGoal(currentJsonGoal, subgoals);
    }

    /**
     * Load goals from JSON as goal objects, returning the final overall goal object
     * @param currentJsonGoal
     * @param subgoals
     * @return final goal object
     */
    private Goal loadGoal(JSONObject currentJsonGoal, ArrayList<Goal> subgoals){
        String goalType = currentJsonGoal.getString("goal");
        String[] goalTypes = {"experience", "gold", "cycles"};
        String[] nonQtyGoals = {"bosses"};
        List<String> goalTypesList = Arrays.asList(goalTypes);
        List<String> nonQtyGoalsList = Arrays.asList(nonQtyGoals);

        if(nonQtyGoalsList.contains(goalType)){ // Goal type is of bosses
            SimpleBossGoal goal = new SimpleBossGoal();
            return goal;    
        }
        if(goalTypesList.contains(goalType)){   // Simple goal of either experience, gold, cycles
            int quantity = currentJsonGoal.getInt("quantity");
            if(goalType.equals("experience")){
                SimpleEXPGoal goal = new SimpleEXPGoal(quantity);
                return goal;
            } else if(goalType.equals("gold")){
                SimpleGoldGoal goal = new SimpleGoldGoal(quantity);
                return goal;
            } else {
                SimpleLoopGoal goal = new SimpleLoopGoal(quantity);
                return goal;
            }
            //SimpleGoal goal = new SimpleGoal(goalType, quantity);
            //world.setWorldGoal(goal);
        } else {    // Complex goal made of combination of goals
            JSONArray nextJsonGoal = currentJsonGoal.getJSONArray("subgoals");
            ArrayList<Goal> subSubGoals = new ArrayList<Goal>();
            for(int goal = 0; goal < nextJsonGoal.length(); goal++){
                subgoals.add(loadGoal(nextJsonGoal.getJSONObject(goal), subSubGoals));
            }
            if(goalType.equals("AND")){
                ComplexAndGoal goal = new ComplexAndGoal(goalType, subgoals);
                return goal;
            } else {
                ComplexOrGoal goal = new ComplexOrGoal(goalType, subgoals);
                return goal;
            }
           // ComplexGoal goal = new ComplexGoal(goalType, subgoals);
        }
    }
}