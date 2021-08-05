package unsw.loopmania;

import java.util.ArrayList;
import org.javatuples.Pair;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import unsw.loopmania.items.Item;
import unsw.loopmania.items.Protection;
import unsw.loopmania.items.Reviver;
import unsw.loopmania.items.Weapon;

public class EquipComposite implements EquipComponent {

    ArrayList<EquipLeaf> equipment;
    
    public EquipComposite() {
        equipment = new ArrayList<EquipLeaf>();
    }

    /** 
     * Calculates the total bonus stats from equipped items
     * @return a Pair containing (bonusATK, bonusDEF)
    */
    public Pair<Double, Double> calculateStats() {
        double bonusATK = 0;
        double bonusDEF = 0;
        for(EquipLeaf e : equipment) {
            bonusATK += e.calculateStats().getValue0();
            bonusDEF += e.calculateStats().getValue1();
        }
        return new Pair<Double, Double>(bonusATK, bonusDEF);
    }
    
    /**
     * Equips the character with an item
     * @param item - the item to be equipped
     * @return the replaced equipped item, null otherwise
     */
    public Item equipItemLeaf(EquipLeaf newItemLeaf) {
        Item oldItem = removeByType(newItemLeaf.getEquipSlot());
        equipment.add(newItemLeaf);
        // add a listener, if the item breaks, then the leaf should be removed too
        newItemLeaf.getItem().shouldExist().addListener((obs, oldValue, newValue) -> {
            if (newValue.booleanValue() == false)
                equipment.remove(newItemLeaf);
        });

        return oldItem;
    }

    /**
     * Get a string of all equipment currently equipped 
     * @return string of equipment
     */
    public String listEquipment() {
        String answer = "[Equipment: "; 
        for(EquipLeaf e : equipment) {
            answer = answer + " " + e.getItem().getID();
        }    
        answer = answer + "]";
        return answer;
    }


    /**
     * For testing purposes.
     * @deprecated - more than one weapon can be equipped now, use getWeapons()
     * @return the currently equipped weapons, null otherwise
     */
    public Weapon getWeapon() {
        for (EquipLeaf e: equipment) {
            if (e.getItem() instanceof Weapon) {
                return (Weapon) e.getItem();
            }
        }
        // else no weapon equipped
        return null;
    }

    /**
     * @return the currently equipped weapons 
     */
    public ArrayList<Weapon> getWeapons() {
        ArrayList<Weapon> weaponsList = new ArrayList<Weapon>();
        for (EquipLeaf e: equipment) {
            if (e.getItem() instanceof Weapon) {
                weaponsList.add((Weapon) e.getItem());
            }
        }
        return weaponsList;
    }

    /**
     * Get a list of all currently equipped protection items
     * @return ArrayList of protection items
     */
	public ArrayList<Protection> getProtectionEquips() {
		ArrayList<Protection> protectionEquips = new ArrayList<Protection>();
		for (EquipLeaf e: equipment) {
			if (e.getItem() instanceof Protection) {
				protectionEquips.add((Protection) e.getItem());
			}
		}
		return protectionEquips;
	}

    /**
     * Unequip the given item passed as an equipment leaf child
     * @param child item
     * @return successful removal
     */
    public boolean remove(EquipLeaf child) {
        equipment.remove(child);
        return true;
    }

    /**
     * Removes the specified equipment type
     * @param type - the type of equipment: weapon, helmet, armour, accessory, etc...
     * @return the popped item
     */
    public EquipLeaf getLeafBySlot(String slot) {
        for (EquipLeaf e: equipment) {
            if (e.getEquipSlot().equals(slot)) {
                return e;
            }
        }
        // else
        return null;
    }

    /**
     * Removes the equipped item at the specified equipment slot
     * @param equipSlot - the equipment slot: weapon, helmet, armour, accessory, etc...
     * @return the popped item
     */
    public Item removeByType(String slot) {
        EquipLeaf leaf = getLeafBySlot(slot);
        if (leaf != null) {
            equipment.remove(leaf);
            // return item for refund
            return leaf.getItem();
        }
        // else
        return null;
    }

    /**
     * Queries if the character has a specified item equipped
     * @return the equipped Item, null otherwise
     */

     /*
    public Item hasItem(String itemID) {
        for (EquipLeaf e: equipment) {
            Item equippedItem = e.getItem();
            if (equippedItem.getID().equals(itemID)) {
                return equippedItem;
            }
        }
        // else
        return null;
    }
    */

    /**
     * Return a list of equipped items that have the reviver quality
     * @return List of equipped reviver items
     */
    public ArrayList<Reviver> hasRevivers() {
        ArrayList<Reviver> revivers = new ArrayList<Reviver>();
        for (EquipLeaf e: equipment) {
            Item equippedItem = e.getItem();
            if (equippedItem instanceof Reviver) {
                revivers.add((Reviver) equippedItem);
            }
        }
        if (revivers.size() > 0) {
            return revivers;
        } else {
            return null;
        }
    }
}
