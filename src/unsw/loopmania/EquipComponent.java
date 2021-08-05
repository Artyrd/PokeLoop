package unsw.loopmania;


import org.javatuples.Pair;

public interface EquipComponent {
    /** 
     * Calculates the total bonus stats from equipped items
     * @return a Pair containing (bonusATK, bonusDEF)
    */
    public Pair<Double, Double> calculateStats();
}
