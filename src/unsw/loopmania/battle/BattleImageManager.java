package unsw.loopmania.battle;

import java.io.File;

import javafx.scene.image.Image;
import unsw.loopmania.MovingEntity;
import unsw.loopmania.buildings.Building;
import unsw.loopmania.cards.Card;
import unsw.loopmania.enemies.Enemy;
import unsw.loopmania.Character;
import unsw.loopmania.items.Item;

public class BattleImageManager {
    
    /* The Character */
    private Image characterBackImage;

    /* Enemies */
    private Image slugImage;
    private Image slugBackImage;
    private Image zombieImage;
    private Image zombieBackImage;
    private Image vampireImage;
    private Image vampireBackImage;
    /* Bosses */
    private Image doggieImage;
    private Image elanImage;
    private Image stageBossImage;
    private Image stageBossPetImage;

    /* Other Moving Entities */
    private Image allyImage;
    private Image allyBackImage;
    private Image towerBackImage;
    private Image dragonBackImage;

    /* Status FX */
    private Image confusedImage;
    private Image stunnedImage;
    private Image vampireDebuffImage;
    private Image burnedImage;

    private Image backgroundImage;


    public BattleImageManager() {
        /* The Character */
        characterBackImage = new Image((new File("src/images/battle_images/character_back_large.png")).toURI().toString());
        backgroundImage = new Image((new File("src/images/battle_images/battle_background (3).png")).toURI().toString());
    }

    public Image getEnemyImage(MovingEntity enemy) {
        Image enemyImage = null;
        System.out.println("Loading: " + enemy.getID());
        switch(enemy.getID()) {
            case "Slug":
                slugImage = new Image((new File("src/images/battle_images/ditto.gif")).toURI().toString());
                enemyImage = slugImage;
                break;
            case "Zombie":
                zombieImage = new Image((new File("src/images/battle_images/gengar.gif")).toURI().toString());
                enemyImage = zombieImage;
                break;
            case "Vampire":
                vampireImage = new Image((new File("src/images/battle_images/darkrai.gif")).toURI().toString());
                enemyImage = vampireImage;
                break;
            case "Doggie":
                doggieImage = new Image((new File("src/images/battle_images/meowth.gif")).toURI().toString());
                enemyImage = doggieImage;
                break;
            case "ElanMuske":
                elanImage = new Image((new File("src/images/battle_images/elan.gif")).toURI().toString());
                enemyImage = elanImage;
                break;
            case "StageBoss":
                enemyImage = stageBossImage;
                break;
            case "StageBossPet":
                enemyImage = stageBossPetImage;
                break;
            case "Ally":
                allyImage = new Image((new File("src/images/battle_images/pikachu.gif")).toURI().toString());
                enemyImage = allyImage;
                break;
            default:
                System.err.println("BattleImageManager could not find a front image for: " + enemy.getID());

        }
        return enemyImage;
    }

    public Image getFriendlyImage(MovingEntity friendly) {
        Image friendlyImage = null;
        System.out.println("Loading: " + friendly.getID());
        switch(friendly.getID()) {
            case "Character":
                friendlyImage = characterBackImage;
                break;
            case "Ally":
                allyBackImage = new Image((new File("src/images/battle_images/pikachu_back.gif")).toURI().toString());
                friendlyImage = allyBackImage;
                break;
            case "TowerBattler":
                towerBackImage = new Image((new File("src/images/battle_images/regirock_back.gif")).toURI().toString());
                friendlyImage = towerBackImage;
            case "Dragon":
                dragonBackImage = new Image((new File("src/images/battle_images/charizard_back.gif")).toURI().toString());
                friendlyImage = dragonBackImage;
                break;
            case "Slug":
                slugBackImage = new Image((new File("src/images/battle_images/ditto_back.gif")).toURI().toString());
                friendlyImage = slugBackImage;
                break;
            case "Zombie":
                zombieBackImage = new Image((new File("src/images/battle_images/gengar_back.gif")).toURI().toString());
                friendlyImage = zombieBackImage;
                break;
            case "Vampire":
                vampireBackImage = new Image((new File("src/images/battle_images/darkrai_back.gif")).toURI().toString());
                friendlyImage = vampireBackImage;
                break;
            default:
                System.err.println("BattleImageManager could not find a back image for: " + friendly.getID());
        }

        return friendlyImage;
    }

    public Image getBackgroundImage() {
        return backgroundImage;
    }

    /**
     * get the image fx of Frontend Statuses
     */
    public Image getStatusImage(String status) {
        Image statusImage = null;
        switch(status) {
            case "Confused":
                confusedImage = new Image((new File("src/images/battle_images/confusedFX.gif")).toURI().toString());
                statusImage = confusedImage;
                break;
            case "Stunned":
                stunnedImage = new Image((new File("src/images/battle_images/stunnedFX.gif")).toURI().toString());
                statusImage = stunnedImage;
                break;
            case "VampireDebuff":
                vampireDebuffImage = new Image((new File("src/images/battle_images/vampireDebuffFX.gif")).toURI().toString());
                statusImage = vampireDebuffImage;
                break;
            case "Burned":
                burnedImage = new Image((new File("src/images/battle_images/burnedFX.gif")).toURI().toString());
                statusImage = burnedImage;
                break;
            case "Frozen":
                statusImage = new Image((new File("src/images/battle_images/frozenFX.gif")).toURI().toString());
                break;
            case "Poisoned":
                statusImage = new Image((new File("src/images/battle_images/poisonedFX.gif")).toURI().toString());
                break;
            case "Sleeping":
                statusImage = new Image((new File("src/images/battle_images/sleepFX.gif")).toURI().toString());
                break;
            case "Paralysed":
                statusImage = new Image((new File("src/images/battle_images/paralysedFX.gif")).toURI().toString());
                break;
            default:
                System.err.println("BattleImageManager could not find a Status image for: " + status);
        }
        return statusImage;
    }

    public void setStageBossBattleImages(Image stageBossBattleImage, Image stageBossPetBattleImage) {
        stageBossImage = stageBossBattleImage;
        stageBossPetImage = stageBossPetBattleImage;
    }

}
