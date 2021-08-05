package unsw.loopmania.cards;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.StaticEntity;
import unsw.loopmania.buildings.Building;

/**
 * a Card in the world
 * which doesn't move
 */
public abstract class Card extends StaticEntity {

    private Building building;
    private int value;
    private String name;

    public Card(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        this.value = 60;
        this.name = "Card";
    }

    public Building getBuilding() {
        return building;
    }

    public Building getBuiltBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        building.changeIntegerProperty(x, y);
        return building;
    }

    public int getValue() {
        return value;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public abstract boolean canPlaceBuilding(int x, int y, LoopManiaWorld world);
}
