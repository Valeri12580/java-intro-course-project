package elements;

import other.Dice;
import other.Player;

public class Trap extends Element implements Activatable {
    private int investment;
    private boolean isActive;
    private Player owner;

    public Trap() {
        super("T");
    }


    public int getInvestment() {
        return investment;
    }

    public Player getOwner() {
        return owner;
    }


    private void setOwner(Player currentPlayer) {
        currentPlayer.removeMoney(this.investment);
        this.owner = currentPlayer;
        System.out.println(String.format("%s,заложихте капан %s,който ви коства %d !",currentPlayer.getName(), this.getName(),this.getInvestment()));
    }

    /**
     * activating the element
     * @param currentPlayer
     */
    @Override
    public void activate(Player currentPlayer) {

        if (owner == null) {
            if(currentPlayer.getPunishments().contains("Пропаганда")){
                System.out.println("Нямате право да поставяте повече капани в рамките на текущия цикъл(пропаганда)");
                return;
            }

            if (currentPlayer.getMoney() < this.investment) {
                System.out.println("Нямате $$$ за този капан!");
                return;
            }

            this.setOwner(currentPlayer);
            return;

        }

        if (this.owner.equals(currentPlayer)) {
            System.out.printf("%s,вие попаднахте в собствения си капан!\n",currentPlayer.getName());

            if (Dice.generateTenWallDiceNumber() % 3 == 0) {
                System.out.println("Но сте късметлия,за сега....");
                return;
            }

        }

        System.out.println(String.format("%s,вие активирахте %s,сложен от %s -%s ", currentPlayer.getName(),this.getName(),this.owner.getName(),this.getDescription()));
        currentPlayer.addPunishment(super.getName());

    }



}
