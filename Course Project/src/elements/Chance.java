package elements;

import other.Player;

public class Chance extends Element implements Activatable {

    private int result;
    private int minInterval;
    private int maxInterval;

    private String type;

    public Chance() {
        super("C");
    }


    public int getMinInterval() {
        return minInterval;
    }

    public int getMaxInterval() {
        return maxInterval;
    }

    public String getType() {
        return type;
    }

    public int getResult() {
        return result;
    }

    /**
     * activating the element
     * @param currentPlayer
     */
    @Override
    public void activate(Player currentPlayer) {

        if (currentPlayer.getPunishments().contains("Хазартен бос")) {
            this.type = "negative";
            currentPlayer.getPunishments().remove("Хазартен бос");
        }

        if (currentPlayer.getEvilPlan().getName().equals("Завладяване на света") && currentPlayer.getEvilPlan().isActivated()) {
            System.out.println("Получавате +100 парички бонус(Завладяване на света)");
            currentPlayer.addMoney(100);
        }

        if (this.type.equals("negative")) {
            currentPlayer.removeMoney(this.result);
        } else {
            currentPlayer.addMoney(this.result);
        }

        System.out.printf("%s ,вие издърпахте %s.%s.Ти %s %d пари\n", currentPlayer.getName()
                , this.getName(), this.getDescription(), this.getType().equals("positive") ? "спечели" : "загуби", this.getResult());
    }
}
