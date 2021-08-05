# Assumptions

**Story points**: 1 story point is worth 2 hours of work (approximately).

## Battles

- A battle will commence if the character is inside a battle radius of an enemy
- The enemy does not have to be in a straight line along the path from the character to battle (enemies can cut across non-path tiles).
- Vampires will always target the character over allies in a battle.
- Slugs and Zombies will target the closest allies within their battle radius first, then the character after.
  - In Milestone2, this means all basic allied soldiers will get targeted before the character.
  - This leaves room in the future for if moving allies are implemented.
- Every "Battle Round", attacks occur alternatingly, where the character/ally attacks once, then an enemy attacks, and so on.
- Statuses that are applied every battle round are ticked at the start of the battle round
- A Battle Round is complete when all fighters on the friendly and enemy side has each attacked once.
- In the case of one side having more fighters, (eg the character 1v3 three slugs) then the side with fewer fighters must wait until the other side's fighters have all finished attacking.
- The character and their allies will prioritise attacking enemies that are closest to the character first.
- If the enemies are the same distance from the character, the earliest spawned enemy is prioritised first.
  - if enemies are spanwed at the same time the order would be of: enemy from the earliest placed spawner (zombie pit/ vampire castle/ boss spawners), then slugs, then doggie
- The character always attacks first before allies at the start of the Battle Round.
- Towers will always attack last from all possible ally attacks.

## [FRONTEND] Battle Screen

- The battle screen displays the battle, live as it happens
- The battle is autonomous so it cannot be paused
- Pausing means the main game/ loop world will be paused, while the battle will continue until completion.
  - When the battle is finished while the main game is paused, rewards are given out, enemies are killed off, but the game will remain paused.
- The battle screen, to thematically be consistent, makes a few frontend adjustments to what happens in the backend
  - Statuses: "Zombified" entities are displayed as "Confused"
  - Statuses: "Tranced" entities are displayed as "Confused"
  - Confused: Confused entities are those whom are fighting on the opposite intended side:
    - zombified allies becoming enemies
    - tranced enemies fighting for us as allies/ friendly units
    - the underlying behaviour of "Zombified" and "Tranced" remains unchanged, "Confused" is merely a frontend skin applied for these effects

## Pausing

- Pausing via the spacebar key will pause the game and bring up the main menu/settings

## Game States

- States include: Main menu, Looping, Battle, Shop, Settings, Quest Log, Win, Loss
- When the game is in a state outside of looping, the game world is paused.
- When the character reaches the hero's castle:
  - If there is a battle, the battle is fought
  - If reaching the hero's castle causes the player to win, the game state is set to Win.
  - Only THEN, will the shop be brought up.
  - Specific Interactions:
    - If reaching the hero's castle completes the goal's loop requirement, but the character dies in a battle, the player loses.
    - The shop won't be brought up on the victory screen.
    - If selling an item completes the goal's gold requirement, then the game is won after exiting the shop.

## Goals

- Goals are from categories:
  - Reaching a certain Level
  - Collecting enough gold
  - Completing enough cycles of the path
  - Killing enough enemies (Milestone 3)

## The Map/World

- Tile images: 32px x 32px
- Paths, grass, and all entities in the world (buildings, items) exist on a single tile
- a "Unit" distance = 1 tile width (for calculating battle, support, and other radii)
- Rendering: all grass with a directional path loop rendered on-top, then entities on top of that
- Entities per tile: unlimited (can stack on the game tile)
- A world 'tick' occurs once every character movement

## Character

- Health: Level 1 starts with a value of 100 HP
- Attack: Base value of 2 ATK
- Defense: Base value of 1 DEF
- CritChance: Base value of 25 (%)
- Experience:
  - 100 exp from Level 1 to Level 2
  - 150 exp from Level 2 to Level 3
  - 200 exp from Level 3 to Level 4
  - etc...
- Leveling Up will grant increased:
  - Health +5
  - Attack +0.5
  - Defense +0.3
- Can equip:
  - One weapon
  - One shield
  - One body armour
  - One head gear
  - One accessory

## Allies

- **Sprite:** Pikachu from Pokemon
- Health: Base value of 50 HP
- Attack: Base value of 1 ATK
- Defense: Base value of 1 DEF
- Cannot equip items

## Friendly Entities

- Additional Entities that assist the character in battle when within support radius

### Dragon

- **Sprite:** Charizard from Pokemon
- A Late-Game companion to the character, available after reaching level 20 (The character has to be strong enough to command the Dragon after all!)
- Intended to be able to be able to get stronger to eventually help defeat the end game bosses.
- Spawning:
  - Upon reaching lvl 20, the character receives a Card which when dropped on the path, summons the Dragon
- Health: Base value of 200 HP
- Attack: Base value of 15 ATK
- Defense: Base value of 5 DEF
- Support Radius: 6 units
- Battle Behaviour:
  - Crit attacks: Apply the burned status to the target, in addition to dealing additional 1.5x dmg
  - Target Preference: The lowest hp available target, then the target with lowest max hp -> then the target which is closest to the character
    - Since all entities have differing max hp, any "ties" in selection after this does not matter to the dragon -> a 10 hp slug and another 10 hp slug which are equally as close to the player has no order difference.
  - Eat: Upon dealing the killing blow, the Dragon eats the target enemy and then receives:
    - Retored HP = half of the eaten enemy's max hp
    - Increased maxhp = enemy's maxhp / 10
    - Increased atk = enemy's atk / 10
    - Increased def = enemy's def / 10
    - If played correctly, the dragon can be "fed" to become a formidable ally against bosses
  - Statuses: Dragons are unaffected by any and all status effects
- Movement Behaviour:
  - Flies anticlockwise around the loop mania world
  - The flight path is defined as a rectangle, with its edges tangent to the topmost, rightmost, bottommost, and leftmost path tile.
  - The rectangle's edges are straight and parallel to the edges of the world map/ screen, if it wasn't obvious.
  - On spawn, attempts to move to the right edge of the defined flight path before proceeding to fly in a loop

## Enemies

- Spawning:
  - For Enemies whom spawn from spawners (which creates an enemy every X loop):
    - They are spawned as soon as the character reaches the hero's castle
    - This adds a gameplay element where the player is discouraged from placing spawners near the hero's castle
    - (Why would you want monsters next to where you live???)

### Slug

- **Sprite:** Muk from Pokemon
- **Spawn condition:** Has a 20% chance of spawning randomly on path tiles every world tick (max 2 at once in the world)
- Flat value of **4 ATK**
- Flat value of **12 HP**
- Flat value of **0 DEF**
- **80%** chance of dropping gold up to **5g**
- Movement:
  - Moves once for every 5 character moves
  - Can move clockwise or anticlockwise randomly
- **Experience:** Flat value of 20 exp
- **Battle radius:** 1 unit
- **Support radius:** 2 units
- **Crit attack chance:** 0%

#### Slug Item Drops

    40% chance of dropping SWORD, POTION
    20% chance of dropping ARMOUR, SHIELD, HELMET
    10% chance of dropping STAFF
    MAX DROPS PER SLUG = 2

#### Slug Card Drops

    40% chance of dropping TRAP, ZOMBIE PIT
    20% chance of dropping CAMPFIRE
    MAX DROPS PER SLUG = 2

### Zombie

- **Sprite:** Gengar from Pokemon
- **Spawn condition:** Spawns from zombie pit every 1 character cycle
- Flat value of **8 ATK**
- Flat value of **40 HP**
- Flat value of **1 DEF**
- **100%** chance of dropping gold up to **7g**, **AND 60%** chance of dropping gold up to **12g**
- **Movement:**
  - Moves once every 3 character moves
  - Can only move in one direction until a maximum distance reached, then reverses and travels in the opposite direction
- **Experience:** Flat value of 40 exp
- **Battle radius:** 2 units
- **Support radius:** 3 units
- **Crit attack chance:** 20%
- Critical attacks (bites) vs allied Entities turn them into zombified versions of that Entity
- Zombified targets are permanently zombified and cannot be cured
- All zombified allies will die at the end of battle, regardless of whether they are tranced or not.
- The character cannot be zombified

#### Zombie Item Drops

    40% chance of dropping SWORD, POTION, SHIELD
    30% chance of dropping ARMOUR, HELMET, STAFF
    15% chance of dropping STAKE
    MAX DROPS PER ZOMBIE = 3

#### Zombie Card Drops

    50% chance of dropping ZOMBIE PIT
    25% chance of dropping TOWER, VILLAGE, CAMPFIRE
    20% chance of dropping BARRACKS, VAMPIRE CASTLE
    MAX DROPS PER ZOMBIE = 3

### Vampire

- **Sprite:** Darkrai from Pokemon
- **Spawn condition:** Spawns from vampire castle every 5 character cycle
- Flat value of **16 ATK**
- Flat value of **120 HP**
- Flat value of **3 DEF**
- **100%** chance of dropping gold up to **15g**, **AND 40%** chance of dropping gold up to **25g**
- **Movement:**
  - Moves once every 3 character moves
  - Moves anti-clockwise towards the player
  - Being in a 3-tile radius of a Campfires causes it to reverse its direction and run away
- **Experience:** Flat value of 100 exp
- **Battle radius:** 3 units
- **Support radius:** 4 units
- **Crit attack chance:** 40%
- Critical attacks (bites) inflict a debuff on the character, increasing damage taken from the next X attacks from the vampire, including the first attack
- Critical attacks do not stack
- Vampire Debuff from critical attacks can only be applied to allies. If a tranced enemy is targetted for a critical attack by a vampire, no debuff is applied and only damage is dealt.

#### Vampire Item Drops

    40% chance of dropping SWORD, POTION, SHIELD, ARMOUR, HELMET, STAFF
    25% chance of dropping STAKE
    MAX DROPS PER VAMPIRE = 3

#### Vampire Card Drops

    50% chance of dropping ZOMBIE PIT, CAMPFIRE
    35% chance of dropping TOWER, VILLAGE, BARRACKS, VAMPIRE CASTLE
    MAX DROPS PER VAMPIRE = 3

### Doggie

- **Sprite:** Meowth from Pokemon: it even has a coin on its forehead 
- **Spawn Condition:** Spawns once per game, after 20 cycles
- Flat value of **25 ATK**
- Flat value of **300 HP**
- Flat value of **3 DEF**
- **100%** chance of dropping gold up to **150g**, **AND 40%** chance of dropping gold up to **250g**
- **Movement:**
  - Moves once every character moves
  - Can move clockwise or anticlockwise randomly
- **Experience:** Flat value of 1000 exp
- **Battle Radius:** 1 unit
- **Support Radius:** 2 units
- **Crit attack chance:** 50%
- Critical attacks will stun character if the character is the target, critical attacks will not stun allies
- Cannot be tranced or zombified
- There will only be 1 of this boss per world

#### Doggie Item Drops

    40% chance of dropping one of THE ONE RING, ANDURIL, TREE STUMP
    50% chance of dropping SWORD, POTION, SHIELD, ARMOUR, HELMET, STAFF, STAKE
    MAX DROPS PER DOGGIE = 4

#### Doggie Card Drops

    50% chance of dropping ZOMBIE PIT, CAMPFIRE, TOWER, VILLAGE, BARRACKS, VAMPIRE CASTLE
    MAX DROPS PER DOGGIE = 3

### Elan Muske

- **Sprite:** Giovanni, leader of Team Rocket
- Flat value of **40 ATK**
- Flat value of **500 HP**
- Flat value of **5 DEF**
- **100%** chance of dropping gold up to **400g**, **AND 40%** chance of dropping gold up to **650g**
- **Movement:**
  - Moves once every 5 character moves
  - Can move clockwise or anticlockwise randomly
- **Experience:** Flat value of 3000 exp
- **Battle Radius:** 1 unit
- **Support Radius:** 2 units
- **Crit attack chance:** 40%
- Critical attacks will heal all enemies (including converted allies) by 30% of their maximum HP, excluding Elan Muske.
- Cannot be tranced or zombified
- There will only be 1 of this boss per world
- **Spawn Condition:** Spawns once per game, after 40 cycles and 10000 exp
  - When conditions are met, the player is given a card to spawn/ summon Elan
  - This is so the player could spawn Elan before reaching the shop, in a location far away from the character, and thus safely sell doggie coin at a high price

#### Elan Muske Item Drops

    100% chance of dropping one of THE ONE RING, ANDURIL, TREE STUMP
    70% chance of dropping SWORD, POTION, SHIELD, ARMOUR, HELMET, STAFF, STAKE
    MAX DROPS PER ELAN MUSKE, EXCLUDING RARE ITEM = 4

#### Elan Muske Card Drops

    70% chance of dropping ZOMBIE PIT, CAMPFIRE, TOWER, VILLAGE, BARRACKS, VAMPIRE CASTLE
    MAX DROPS PER ELAN MUSKE = 4

## Cards/ Buildings

- Placement: Only one building can be placed on any given tile (can't be stacked)
- Buying: Cards cannot be bought form the shop, only obtained through battle
- Selling: Cards can be sold on the shop for gold
- Each card has their own value which is given to the player when card inventory is full
- Max Cards: as wide as the world
  - added benefit of a larger world allowing us to use and hold onto more cards.

### Tower

- Placement: only on non-path tiles adjacent to the path
- Support radius: 2 units
- Damage: Flat value of 5
- Only enters battle when the character is in the support radius of the tower
- Cannot lose damage, permanent.
- Can be sold at the shop for 10 gold

### Village

- Placement: only on path tiles
- Health regeneration: 25 HP
- Applies when character passes on top of the village tile
- Can be sold at the shop for 10 gold

### Barracks

- Placement: only on path tiles
- Allies gained: 1
- Applies when character passes on top of the village tile
- Can be sold at the shop for 10 gold

### Vampire Castle

- Placement: only on non-path tiles
- Spawns a vampire every 5 rounds, inclusive of the round the castle is placed.
- Can be sold at the shop for 20 gold

### Zombie Pit

- Placement: only on non-path tiles
- Spawns a zombie every 1 rounds, inclusive of the the round the pit is placed.
- Can be sold at the shop for 15 gold

### Campfire

- Placement: only on path tiles
- Battle radius: 2 units
- Applies when character is inside battle radius, enemy can be outside battle radius and this will still apply
- Can be sold at the shop for 5 gold

### Trap

- Placement: only on path tiles
- Applies when enemy steps on top of trap tile
- Does NOT grant the character rewards of dead enemies (you're not at the dead body to loot it after all!)
- Damage dealt: 10 ATK
- Can be sold at the shop for 5 gold

### Elan Spawner

- Placement: only on path tiles
- When placed, spawns Elan Muske (only once)
- Can be sold at the shop for 0 gold [Why wouldn't you want to fight the boss??? :) ]


### Shop

- We assume that a player can only sell items in their immediate inventory. ie, they cannot sell an item which is currently equipped.
  - The exception to this is cards, which are not held in inventory but is part of the world and the possession of the character.
- We assume that once a player buys an item, they would not want to instantly resell it, hence it does not appear in the sell shop.
- We assume that since the character can only buy from the shop at certain intervals, that the shop only occurs at certain intervals. ie, they will not be able to sell during intervals where no buying is allowed.
- We assume that rare items cannot be bought in the shop, but can be sold.
- We assume that the final sell value of an item is its purchase value * (current durability / maximum durability) rounded to the nearest integer

## Items

### Spawnable Items

- Gold and Potions may spawn in the world on path tiles
- Only 2 spawned item may exist in the world at once
  - While this limit isn't reached, there is a 5% chance of an item spawning at each "world tick"
  - Within this chance, there is a 70% chance of gold spawning, and a 30% chance of a potion spawning

### Potions

- Restore 30 HP
- Purchased for 10 gold
- Can hold many potions
- Not a part of the inventory
- Potions are automatically consumed when the character drops below a certain HP (60 HP for 1 potion, 30 HP for 2 potions)
- If potions are consumed, the character cannot attack for that turn.

### Gold

- Can be found on the path as a random amount
- Not a part of the inventory

### Weapons and Armour
* Durability: Weapons and armour have limited durability which deays through battle, and eventually breaks. When an item breaks, there is no salvageable value (Milestone 3)
* The value of a weapon is proportional to its durability, i.e. as the weapon is used, its value is decreases by a scalar amount. A weapon that has 50% durability will be worth 50% of its original value (rounded to a whole number).
* Durability value (uses):
    * Sword (and Anduril): 20
    * Stake: 10
    * Staff: 15
    * Armour: 20
    * Shield (and TreeStump): 20
    * Helmet: 15
    * TheOneRing: 1
* With each increase in star tier from 1 star tier in each item, the durabilty of each item increases by 10
* Item attack value:
    * Anduril: 15 ATK
    * Sword: 5 ATK
    * Stake: 3 ATK
    * Staff: 1 ATK
* Item defense value:
    * Armour: 3 DEF
    * Helmet: 1 DEF
    * Shield: 1 DEF
    * TreeStump: 1 DEF
* Item base purchase value:
    * Sword: 60
    * Stake: 55
    * Staff: 60
    * Armour: 50
    * Shield: 40
    * Helmet: 30
    * HealingPotion: 10
* With each increase in star tier from 1 star tier in each item, the purchase value increases by 25

* Stake: Stakes enable the character to do triple damage to vampires
* Staff: upon critical hit, puts the enemy into a trance. This has a 25% chance of occurring.
* Trance: Enemies whom are tranced become firendly and fight for the Character
  * ex: friendly Slug, friendly Zombie, friendly Vampire
  * When a trance is cast, no damage is dealt. When a trance is cast but fails (and no trance is effectively cast), the attack value is dealt
  * A tranced enemy cannot use their special attack behaviours, such as a tranced Zombie cannot 'zombify' enemies.
  * A trance status will last 5 battle turns, after which will wear off and the enemy will be converted back.
* Helmet: when worn, the character's attacks are reduced by 1atk due to poor visibility
    * Incoming attacks are reduced by 10%
* Shield: 
  * [Change from Spec] Has a 60% chance of resisting Vampire's critical hit debuff
  * Gives a flat dmg reduction of 1 (+ 1 char DEF)

* Salvaging: Excess items get salvaged for half their price in gold + their full price in xp.

### Rare Items

- Rare Items existance is defined by the world JSON that gets read in
- Rare items have a 1 in 1000 chance to drop after any battle
- In the case of more than one rare item existing, the dropped item is randomly chosen from a limited pool
- Update from previous: Rare items can no longer be bought in the shop, and can only be obtained through chance.

### The One Ring
- We assume that the drop chance of the One Ring during any battle is as outlined in the enemy drop section
- We assume that for the ring to be activated upon death of the character, the character must be wearing the ring. ie, it is not enough for the ring to only be in the inventory.
- The ring can be worn on an accessory slot on the character
- After the ring is activated, the ring is removed from inventory and equipment

### Anduril
- We assume that the drop chance of the flaming sword during any battle is as outlined in the enemy drop section
- We assume that the flaming sword deals increased damage to all enemies with a 3x multiplier against bosses
- Anduril inflicts the "Burned" status effect onto enemies upon ciritcal attacks

### Tree Stump
- We assume that the drop chance of the tree stump during any battle is as outlined in the enemy drop section
- We assume that the tree stump reduces incoming damage from all enemies with a 2x multiplier reduction against bosses

### Confused Items
- We assume that each confused item can only contain attributes from two rare items at a time (eg, the one ring and anduril: revive and bonus attack damage against bosses)
- We assume that each confused item has a base item with an extra attribute added on (eg, the one ring as a base and anduril as extra attribute)
- We assume that the base item has hierarchy in item durability (eg, the character loses attack bonuses after using the ring to revive / the character loses the ability to revive after using the sword too many times)
- In addition, only actions relating to the base item will decrease durability (eg, base: anduril, extra: tree stump, reducing damage will not decrease durability but attacking will)
- Anduril's burn effect is only applied when Anduril is the base item (a tree/ring isnt exactly on fire in the first place), but the 3x damage effect is still active

### Statuses
- Statuses include:
  - **Zombified:** Caused by zombie crit attacks, and converts allies into zombified allies
  - **Vampire Debuff:** Caused by Vampire crit attacks, vampires deal bonus damage to those inflicted by the debuff
  - **Campfire Buff:** Caused by being in range of a campfire, doubles the character's attack
  - **Stunned:** Caused by Doggie crit attacks, makes the character unable to attack
  - **Tranced:** Caused by the Staff Weapon's crit attacks, turns enemies friendly
  - **Burned:** Caused by crit attacks by Dragon / Anduril Weapon. Lasts randomly between 2-6 turns. Each battle round/ turn, the affected entity is burned for 1-5 dmg, ignored DEF.
- Refer to the sources of these status inflictions for more detail

### Item Tiers
- Each equippable item can have 3 star tiers, with the exception of The One Ring which cannot have any star tier apart from 1
- Each star tier increases the respective item's ATK or DEF by the scalar of the star tier from its base
  - Eg, Base ATK of sword: 5, 2-tier ATK: 10, 3-tier ATK: 15
- With each increase in star tier from 1 star tier in each item, the purchase value increases by 25
- Confused items can have tier levels only corresponding to their base item, with the exception of The One Ring base item 

### [UX] Equipping Items

- To minimise user frustration, equipped items will "snap" into its appropriate slot.
  - eg: if a helmet is dragged to a weapon slot, it will snap to the helmet slot anyway.
- This enables the player to quickly equip the appropriate items in a hurry, such as swapping from a sword to a stake.

### Campaign

- The campaign for the game involves the player venturing to 8 stages (gyms) across the Pokemon Kanto Region.
- Completing the goals of each stage awards the player with the gym badge of that stage.
- The player beats the campaign by collecting all 8 gym badges, then venturing to the Pokemon League to defeat the Elite 4.
- Progress is saved only on completed Stages.
