package unsw.loopmania;

import org.javatuples.Pair;

import unsw.loopmania.items.*;

public class EquipLeaf implements EquipComponent {

    //private String type; 
    private String equipSlot; 
    private Item item;
    
    /**
     * 
     * @param type - the type of equipment: weapon, helmet, armour, accessory, etc...
     * @param item - the item being equipped
     */
    //public EquipLeaf(String type, String equipSlot, Item item) {
    public EquipLeaf (Item item) {
        super();
        /*
        if (item instanceof Weapon) {
            equipSlot = "Weapon";
        } else if (item instanceof Helmet) {
            equipSlot = "Helmet";
        } else if (item instanceof Armour) {
            equipSlot = "Armour";
        } else if (item instanceof Shield) {
            equipSlot = "Shield";
        } else if (item instanceof TheOneRing) {
            equipSlot = "Accessory";
        }
        */
        equipSlot = item.getType();

        this.item = item;
    }

    public Pair<Double, Double> calculateStats() {
		return new Pair<Double, Double>(this.item.getATK(), this.item.getDEF());
    }
    /*
    public String getType() {
        return type;
    }
    */
    public String getEquipSlot() {
        return equipSlot;
    }
    public Item getItem() {
        return item;
    }

}
