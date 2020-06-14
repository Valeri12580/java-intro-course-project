package elements;

import other.Player;

public class PartyHard  extends Element implements Activatable {
    private int cost;

    public PartyHard() {
        super("PH");
    }

    public int getCost() {
        return cost;
    }

    /**
     * activating the element
     * @param currentPlayer
     */
    @Override
    public void activate(Player currentPlayer) {
        currentPlayer.removeMoney(cost);
        System.out.println(String.format("%s ,вие избрахте  да отидете на %s и да се разделите с %d - %s",currentPlayer.getName(),super.getName(),this.getCost(),super.getDescription()));
    }
}
