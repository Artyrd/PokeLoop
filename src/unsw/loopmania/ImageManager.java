package unsw.loopmania;

import java.io.File;

import javafx.scene.image.Image;
import unsw.loopmania.buildings.Building;
import unsw.loopmania.buildings.StageBossBuilding;
import unsw.loopmania.cards.Card;
import unsw.loopmania.items.Item;

public class ImageManager {
    
    /* The Character */
    private Image characterImage;

    /* Enemies */
    private Image slugImage;
    private Image zombieImage;
    private Image vampireImage;
    /* Bosses */
    private Image doggieImage;
    private Image elanImage;

    /* Campaign Boss */
    private Image stageBossImage;
    private Image stageBossPetImage;


    /* Other Moving Entities */
    private Image dragonImage;
    /* Items */

    /* Weapons */
    private Image swordImage1;
    private Image swordImage2;
    private Image swordImage3;
    private Image stakeImage1;
    private Image stakeImage2;
    private Image stakeImage3;
    private Image staffImage1;
    private Image staffImage2;
    private Image staffImage3;
    private Image andurilImage1;
    private Image andurilImage2;
    private Image andurilImage3;

    /* Protection */
    private Image armourImage1;
    private Image armourImage2;
    private Image armourImage3;
    private Image helmetImage1;
    private Image helmetImage2;
    private Image helmetImage3;
    private Image shieldImage1;
    private Image shieldImage2;
    private Image shieldImage3;
    private Image treeStumpImage1;
    private Image treeStumpImage2;
    private Image treeStumpImage3;

    /* Other Items */
    private Image goldImage;
    private Image potionImage;
    private Image ringImage;

    /* Cards and Buildings */
    private Image heroCastleImage;
    private Image barracksCardImage;
    private Image barracksBuildingImage;
    private Image campfireCardImage;
    private Image campfireBuildingImage;
    private Image towerCardImage;
    private Image towerBuildingImage;
    private Image trapCardImage;
    private Image trapBuildingImage;
    private Image villageCardImage;
    private Image villageBuildingImage;
    private Image zombiePitCardImage; 
    private Image zombiePitBuildingImage;
    private Image vampireCastleCardImage;
    private Image vampireCastleBuildingImage;
    private Image elanSpawnerCardImage;
    private Image bossPlatformImage;

    private Image stageBossCardImage;
    private Image stageBossPlatformImage;

    private Image stageBossBattleImage;
    private Image stageBossPetBattleImage;

    private Image dragonSpawnerCardImage;
    private Image dragonSpawnerBuildingImage;

    /* UI */
    private Image pauseButtonImage;
    private Image playButtonImage;

    /* Campaign Map: Badges */
    private Image badge1;
    private Image badge2;
    private Image badge3;
    private Image badge4;
    private Image badge5;
    private Image badge6;
    private Image badge7;
    private Image badge8;

    public ImageManager() {
        /* The Character */
        characterImage = new Image((new File("src/images/red.png")).toURI().toString());

        /* Enemies */
        slugImage = new Image((new File("src/images/ditto.png")).toURI().toString());
        zombieImage = new Image((new File("src/images/gengar.png")).toURI().toString());
        vampireImage = new Image((new File("src/images/darkrai.png")).toURI().toString());
        /* Bosses */
        doggieImage = new Image((new File("src/images/meowth.png")).toURI().toString());
        elanImage = new Image((new File("src/images/elan.png")).toURI().toString());

        /* Moving Allies */
        dragonImage = new Image((new File("src/images/charizard.gif")).toURI().toString());
        /* Items */

        /* Weapons */
        swordImage1 = new Image((new File("src/images/item_images/sword1.png")).toURI().toString());
        swordImage2 = new Image((new File("src/images/item_images/sword2.png")).toURI().toString());
        swordImage3 = new Image((new File("src/images/item_images/sword3.png")).toURI().toString());
        stakeImage1 = new Image((new File("src/images/item_images/stake1.png")).toURI().toString());
        stakeImage2 = new Image((new File("src/images/item_images/stake2.png")).toURI().toString());
        stakeImage3 = new Image((new File("src/images/item_images/stake3.png")).toURI().toString());
        staffImage1 = new Image((new File("src/images/item_images/staff1.png")).toURI().toString());
        staffImage2 = new Image((new File("src/images/item_images/staff2.png")).toURI().toString());
        staffImage3 = new Image((new File("src/images/item_images/staff3.png")).toURI().toString());
        andurilImage1 = new Image((new File("src/images/item_images/anduril1.png")).toURI().toString());
        andurilImage2 = new Image((new File("src/images/item_images/anduril2.png")).toURI().toString());
        andurilImage3 = new Image((new File("src/images/item_images/anduril3.png")).toURI().toString());

        /* Protection */
        armourImage1 = new Image((new File("src/images/item_images/armour1.png")).toURI().toString());
        armourImage2 = new Image((new File("src/images/item_images/armour2.png")).toURI().toString());
        armourImage3 = new Image((new File("src/images/item_images/armour3.png")).toURI().toString());
        helmetImage1 = new Image((new File("src/images/item_images/helmet1.png")).toURI().toString());
        helmetImage2 = new Image((new File("src/images/item_images/helmet2.png")).toURI().toString());
        helmetImage3 = new Image((new File("src/images/item_images/helmet3.png")).toURI().toString());
        shieldImage1 = new Image((new File("src/images/item_images/shield1.png")).toURI().toString());
        shieldImage2 = new Image((new File("src/images/item_images/shield2.png")).toURI().toString());
        shieldImage3 = new Image((new File("src/images/item_images/shield3.png")).toURI().toString());
        treeStumpImage1 = new Image((new File("src/images/item_images/tree_stump1.png")).toURI().toString());
        treeStumpImage2 = new Image((new File("src/images/item_images/tree_stump2.png")).toURI().toString());
        treeStumpImage3 = new Image((new File("src/images/item_images/tree_stump3.png")).toURI().toString());
        // tree stump image

        /* Other Items */
        goldImage = new Image((new File("src/images/item_images/gold.gif")).toURI().toString());
        potionImage = new Image((new File("src/images/potion.png")).toURI().toString());
        ringImage = new Image((new File("src/images/item_images/ring.png")).toURI().toString());

        /* Cards and Buildings */
        heroCastleImage = new Image((new File("src/images/pkmn_center.png")).toURI().toString());

        barracksCardImage = new Image((new File("src/images/building_images/barracks_card.jpg")).toURI().toString());
        barracksBuildingImage = new Image((new File("src/images/building_images/barracks.png")).toURI().toString());
        
        campfireCardImage = new Image((new File("src/images/building_images/campfire_card_new.png")).toURI().toString());
        campfireBuildingImage = new Image((new File("src/images/building_images/campfire.png")).toURI().toString());

        towerCardImage = new Image((new File("src/images/building_images/tower_card.png")).toURI().toString());
        towerBuildingImage = new Image((new File("src/images/building_images/tower.png")).toURI().toString());

        trapCardImage = new Image((new File("src/images/building_images/trap_card.jpg")).toURI().toString());
        trapBuildingImage = new Image((new File("src/images/building_images/trap_bomb.png")).toURI().toString());

        villageCardImage = new Image((new File("src/images/building_images/village_card.jpg")).toURI().toString());
        villageBuildingImage = new Image((new File("src/images/building_images/village.png")).toURI().toString());

        zombiePitCardImage = new Image((new File("src/images/building_images/zombie_card.jpg")).toURI().toString());
        zombiePitBuildingImage = new Image((new File("src/images/building_images/zombie_card.jpg")).toURI().toString());

        vampireCastleCardImage = new Image((new File("src/images/building_images/vampire_card.jpg")).toURI().toString());
        vampireCastleBuildingImage = new Image((new File("src/images/building_images/vampire_castle.png")).toURI().toString());

        elanSpawnerCardImage = new Image((new File("src/images/building_images/rocket_card.jpg")).toURI().toString());
        bossPlatformImage = new Image((new File("src/images/building_images/gym_platform.png")).toURI().toString());

        dragonSpawnerCardImage = new Image((new File("src/images/pokeball_card.png")).toURI().toString());
        dragonSpawnerBuildingImage = new Image((new File("src/images/pokeball.png")).toURI().toString());

        /* UI */
        pauseButtonImage = new Image((new File("src/images/game_interface/pause.png")).toURI().toString());
        playButtonImage = new Image((new File("src/images/game_interface/play.png")).toURI().toString());
        
        badge1 = new Image((new File("src/images/campaign_images/badge1.png")).toURI().toString());
        badge2 = new Image((new File("src/images/campaign_images/badge2.png")).toURI().toString());
        badge3 = new Image((new File("src/images/campaign_images/badge3.png")).toURI().toString());
        badge4 = new Image((new File("src/images/campaign_images/badge4.png")).toURI().toString());
        badge5 = new Image((new File("src/images/campaign_images/badge5.png")).toURI().toString());
        badge6 = new Image((new File("src/images/campaign_images/badge6.png")).toURI().toString());
        badge7 = new Image((new File("src/images/campaign_images/badge7.png")).toURI().toString());
        badge8 = new Image((new File("src/images/campaign_images/badge8.png")).toURI().toString());
    }

    /**
     * Get the correct image for the given item
     * @param item
     * @return Image of the item
     */
    public Image getItemImage(Item item) {
        // If no item, return nothing
        if (item == null) return null;

        Image itemImage = null;
        // Get the item according to the ID of the item, then by the star tier
        switch(item.getID()) {
            case "Sword":
                switch(item.getStar()) {
                    case 1:
                        itemImage = swordImage1;
                        break;
                    case 2:
                        itemImage = swordImage2;
                        break;
                    case 3:
                        itemImage = swordImage3;
                        break;
                }
                break;
            case "Staff":
                switch(item.getStar()) {
                    case 1:
                        itemImage = staffImage1;
                        break;
                    case 2:
                        itemImage = staffImage2;
                        break;
                    case 3:
                        itemImage = staffImage3;
                        break;
                }
                break;
            case "Stake":
                switch(item.getStar()) {
                    case 1:
                        itemImage = stakeImage1;
                        break;
                    case 2:
                        itemImage = stakeImage2;
                        break;
                    case 3:
                        itemImage = stakeImage3;
                        break;
                }
                break;
            case "Anduril":
                switch(item.getStar()) {
                    case 1:
                        itemImage = andurilImage1;
                        break;
                    case 2:
                        itemImage = andurilImage2;
                        break;
                    case 3:
                        itemImage = andurilImage3;
                        break;
                }
                break;

            case "Armour":
                switch(item.getStar()) {
                    case 1:
                        itemImage = armourImage1;
                        break;
                    case 2:
                        itemImage = armourImage2;
                        break;
                    case 3:
                        itemImage = armourImage3;
                        break;
                }
                break;
            case "Helmet":
                switch(item.getStar()) {
                    case 1:
                        itemImage = helmetImage1;
                        break;
                    case 2:
                        itemImage = helmetImage2;
                        break;
                    case 3:
                        itemImage = helmetImage3;
                        break;
                }
                break;
            case "Shield":
                switch(item.getStar()) {
                    case 1:
                        itemImage = shieldImage1;
                        break;
                    case 2:
                        itemImage = shieldImage2;
                        break;
                    case 3:
                        itemImage = shieldImage3;
                        break;
                }
                break;
            case "TreeStump":
                switch(item.getStar()) {
                    case 1:
                        itemImage = treeStumpImage1;
                        break;
                    case 2:
                        itemImage = treeStumpImage2;
                        break;
                    case 3:
                        itemImage = treeStumpImage3;
                        break;
                }
                break;
            
            // Items with no star tiers
            case "TheOneRing":
                itemImage = ringImage;
                break;
            case "Gold":
                itemImage = goldImage;
                break;
            case "Potion":
                itemImage = potionImage;
                break;
            default:
                System.err.println("Error: invalid Item or Item.ID for:" + item);
        }
        return itemImage;
    }

    /**
     * Get the correct image for the given card
     * @param card
     * @return Image of the card
     */
    public Image getCardImage(Card card) {
        System.out.print(card);
        Image cardImage = null;
        
        // Get the image for the card according to the card ID
        switch(card.getID()) {
            case "BarracksCard":
                cardImage = barracksCardImage;
                break;
            case "CampfireCard":
                cardImage = campfireCardImage;
                break;
            case "TowerCard":
                cardImage = towerCardImage;
                break;
            case "TrapCard":
                cardImage = trapCardImage;
                break;
            case "VampireCastleCard":
                cardImage = vampireCastleCardImage;
                break;
            case "VillageCard":
                cardImage = villageCardImage;
                break;
            case "ZombiePitCard":
                cardImage = zombiePitCardImage;
                break;
            case "ElanSpawnerCard":
                cardImage = elanSpawnerCardImage;
                break;
            case "StageBossCard":
                cardImage = stageBossCardImage;
                break;
            case "DragonSpawnerCard":
                cardImage = dragonSpawnerCardImage;
                break;
            default:
                System.err.println("Error: invalid Card or Card.ID for: " + card);

        }
        return cardImage;
    }

    /**
     * Get the correct image for the given building
     * @param building
     * @return Image of the building
     */
    public Image getBuildingImage(Building building) {
        Image buildingImage = null;

        // Get the building image according to the building ID
        switch(building.getID()) {
            case "HeroCastleBuilding":
                buildingImage = heroCastleImage;
                break;
            case "BarracksBuilding":
                buildingImage = barracksBuildingImage;
                break;
            case "CampfireBuilding":
                buildingImage = campfireBuildingImage;
                break;
            case "TowerBuilding":
                buildingImage = towerBuildingImage;
                break;
            case "TrapBuilding":
                buildingImage = trapBuildingImage;
                break;
           case "VampireCastleBuilding":
                buildingImage = vampireCastleBuildingImage;
                break;
            case "VillageBuilding":
                buildingImage = villageBuildingImage;
                break;
            case "ZombiePitBuilding":
                buildingImage = zombiePitBuildingImage;
                break;
            case "ElanSpawnerBuilding":
                buildingImage = bossPlatformImage;
                break;
            case "StageBossBuilding":
                buildingImage = stageBossPlatformImage;
                break;
            case "DragonSpawnerBuilding":
                buildingImage = dragonSpawnerBuildingImage;
                break;
            default:
                System.err.println("\nError in ImageManager: invalid Building or Building.ID for: " + building + "\n");
        }
        return buildingImage;
    }

    /**
     * Get the correct image for the given entity
     * @param entity
     * @return Image of the entity
     */
    public Image getMovingEntityImage(MovingEntity entity) {
        Image entityImage = null;

        // Get the image for the entity according to the ID of the entity
        switch(entity.getID()) {
            case "Slug":
                entityImage = slugImage;
                break;
            case "Zombie":
                entityImage = zombieImage;
                break;
            case "Vampire":
                entityImage = vampireImage;
                break;
            case "Doggie":
                entityImage = doggieImage;
                break;
            case "ElanMuske":
                entityImage = elanImage;
                break;
            case "StageBoss":
                entityImage = stageBossImage;
                break;
            case "StageBossPet":
                entityImage = stageBossPetImage;
                break;
            case "Dragon":
                entityImage = dragonImage;
                break;
            default:
                System.err.println("\nError in ImageManager: invalid Entity or entity.ID for: " + entity + "\n");

        }
        return entityImage;
    }

    /**
     * Get the correct image for the given badge
     * @param badge number
     * @return Image of the badge
     */
    public Image getColouredBadgeImage(int badge) {
        Image badgeImage = null;
        switch(badge) {
            case 1:
                badgeImage = badge1;
                break;
            case 2:
                badgeImage = badge2;
                break;
            case 3:
                badgeImage = badge3;
                break;
            case 4:
                badgeImage = badge4;
                break;
            case 5:
                badgeImage = badge5;
                break;
            case 6:
                badgeImage = badge6;
                break;
            case 7:
                badgeImage = badge7;
                break;
            case 8:
                badgeImage = badge8;
                break;
            default:
                System.err.println("Cannot find coloured badge image for badge #" + badge);
        }

        return badgeImage;
    }

    /**
     * Get the correct image for the given buttons
     * @param image_name
     * @return Image of the button
     */
    public Image getGameInterfaceImage(String image_name) {
        Image image = null;
        switch(image_name) {
            case "Pause":
                image = pauseButtonImage;
                break;
            case "Play":
                image = playButtonImage;
                break;
        }
        return image;
    }

    /**
     * The images of the stage boss and its pet depends on the campaign world's json file
     * @param stageBossImageUrl - the url to the stage boss' sprite
     * @param stageBossPetImageUrl - the url to the stage boss' pet's sprite
     * @param stageBossCardImageUrl - the url to the stage boss' summoning card (looks like the gym badge)
     * @param stageBossBuildingImageUrl - the url to the stage boss' platform which it stands on
     */
    public void setStageBossImages(String stageBossImageUrl, String stageBossPetImageUrl, 
                                   String stageBossBattleImageUrl, String stageBossPetBattleImageUrl,
                                   String stageBossCardImageUrl, String stageBossBuildingImageUrl) {
        stageBossImage = new Image((new File(stageBossImageUrl)).toURI().toString());
        stageBossPetImage = new Image((new File(stageBossPetImageUrl)).toURI().toString());
        stageBossBattleImage = new Image((new File(stageBossBattleImageUrl)).toURI().toString());
        stageBossPetBattleImage = new Image((new File(stageBossPetBattleImageUrl)).toURI().toString());
        stageBossCardImage = new Image((new File(stageBossCardImageUrl)).toURI().toString());
        stageBossPlatformImage = new Image((new File(stageBossBuildingImageUrl)).toURI().toString());
    }

    public Image getStageBossBattleImage() {
        System.out.println("giving gym leader battle image");
        return stageBossBattleImage;
    }

    public Image getStageBossPetBattleImage() {
        return stageBossPetBattleImage;
    }
}
