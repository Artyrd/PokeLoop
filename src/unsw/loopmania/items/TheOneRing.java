package unsw.loopmania.items;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Character;

public class TheOneRing extends Item implements Reviver {

    public TheOneRing(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setID("TheOneRing");
        setName("Cynthia's One Ring");
        setType("Accessory");
        setMaxDurability(1);
        setPrice(300);
        setStar(1);
    }

    /**
     * Create a one ring that doesnt exist in the world/ inventory yet
     */
    public TheOneRing() {
        this(null, null);
    }

    /**
     * Revive a dead character (single use)
     * @param character to be revived
     */
    @Override
    public boolean revive(Character character) {
        // If the character is dead and the ring is not broken, revive
        if (character.getCurrHP() <= 0 && this.getCurrDurability() > 0) {
            System.out.println("THE ONE RING ACTIVATED: REVIVING CHARACTER");
            character.restoreHP(character.getMaxHP());
            //character.getInventory().getEquipComposite().removeByType(this.getID());
            // Destroy the ring
            this.decreaseDurability(1);
            this.destroy();
            return true;
        } else {
            return false;
        }
    }
}
