package unsw.loopmania;

import java.util.List;

import org.json.JSONObject;

import unsw.loopmania.battle.BasicBattleBehaviour;
import unsw.loopmania.battle.PetBattleBehaviour;
import unsw.loopmania.buildings.StageBossBuilding;
import unsw.loopmania.cards.Card;
import unsw.loopmania.cards.StageBossCard;
import unsw.loopmania.enemies.Boss;
import unsw.loopmania.enemies.StageBoss;
import unsw.loopmania.enemies.StageBossPet;
import unsw.loopmania.goal.Goal;
import unsw.loopmania.status.Burned;
import unsw.loopmania.status.Confused;
import unsw.loopmania.status.Frozen;
import unsw.loopmania.status.Paralysed;
import unsw.loopmania.status.Poisoned;
import unsw.loopmania.status.Sleeping;
import unsw.loopmania.status.Status;
import unsw.loopmania.status.Stunned;
import unsw.loopmania.status.Tranced;
import unsw.loopmania.status.Zombified;

public class StageBossManager {

    private JSONObject json;
    private Goal challengeGoal;
    private boolean issuedCard;

    public StageBossManager(JSONObject stageBossJSON) {

        json = stageBossJSON;

        GoalLoader goalLoader = new GoalLoader();
        JSONObject jsonGoal = json.getJSONObject("challenge-condition");
        challengeGoal = goalLoader.loadGoals(jsonGoal);

        issuedCard = false;

    }

    /**
     * @pre LoopManiaWorld call this function only if canChallenge returned true
     * @post the Card to summon the StageBoss is given
     * @return the StageBossCard
     */
    public Card giveCard() {
        if (issuedCard) {
            return null;
        }
        StageBossCard bossCard = new StageBossCard(null, null);

        StageBossBuilding bossBuilding = new StageBossBuilding(null, null);

        String status = json.getString("battle_status");
        Status stageBossStatus = getStageBossStatus(status);

        JSONObject bossStats = json.getJSONObject("boss_stats");
        bossBuilding.setBossStats(
            json.getString("boss_name"),
            bossStats.getInt("hp"), 
            bossStats.getInt("atk"), 
            bossStats.getInt("def"), 
            bossStats.getInt("gold"), 
            bossStats.getInt("xp"));

        JSONObject petStats = json.getJSONObject("boss_stats");
        bossBuilding.setPetStats(
            json.getString("pet_name"),
            petStats.getInt("hp"), 
            petStats.getInt("atk"), 
            petStats.getInt("def"));

        bossBuilding.setBattleBehaviours(new BasicBattleBehaviour(), new PetBattleBehaviour(stageBossStatus));

        bossCard.setBuilding(bossBuilding);

        issuedCard = true;

        return bossCard;

    }

    public boolean canChallenge(Character character, LoopManiaWorld world) {

        // if gave card already, return so we dont keep on checking
        if (issuedCard) {
            return false;
        }

        if (challengeGoal.evaluateGoalReached(character, world)) {
            return true;
        } else {
            return false;
        }
    }

    public String showChallengeGoal(Character character, LoopManiaWorld world) {
        return challengeGoal.showGoals(character, world);
    }

    public String showStageBossGoal(Character character, LoopManiaWorld world){
        String complete = "";
        if(defeatedStageBossGoal(world)){
            complete = "X  ";
        } else {
            complete = "O  ";
        }
        return complete + "GYM: Defeat " + json.getString("boss_name");
    }

    public Boolean defeatedStageBossGoal(LoopManiaWorld world){
        List<Boss> defeatedBosses = world.getDefeatedBosses();

        // Check if stageBoss is defeated
        Boolean defeatedStageBoss = false;
        for(Boss boss: defeatedBosses){
            if(boss instanceof StageBoss){
                defeatedStageBoss = true;
            }
        }

        // Check if stageBossPet is defeated
        Boolean defeatedStageBossPet = false;
        for(Boss boss: defeatedBosses){
            if(boss instanceof StageBossPet){
                defeatedStageBossPet = true;
            }
        }

        if(defeatedStageBoss && defeatedStageBossPet){
            return true;
        } else {
            return false;
        }

    }

    /**
     * Get the unique status affect afflicted by this stage boss
     * @param status name
     * @return the status object
     */
    public Status getStageBossStatus(String status){
        Status battleStatus = null;
        switch(status){
            case "Burned":
                battleStatus = new Burned();
                break;
            case "Confused":
                battleStatus = new Confused();
                break;
            case "Frozen":
                battleStatus = new Frozen();
                break;
            case "Paralysed":
                battleStatus = new Paralysed();
                break;
            case "Poisoned":
                battleStatus = new Poisoned();
                break;
            case "Sleeping":
                battleStatus = new Sleeping();
                break;
            case "Stunned":
                battleStatus = new Stunned();
                break;
            case "Tranced":
                battleStatus = new Tranced();
                break;
            case "Zombified":
                battleStatus = new Zombified();
                break;
            
            default:
        }

        return battleStatus;
    }


}
