package other;

import elements.*;
import utils.RandomNumberGenerator;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Board {
    private List<ElementWrapper> fields;

    public Board() {
        fields = new ArrayList<>();
        init();
    }

    /**
     * Board initialization
     */
    private void init() {
        fillBoard(7, Trap.class);
        fillBoard(3, Invest.class);
        fillBoard(3, PartyHard.class);
        fillBoard(3, Chance.class);
        fillBoard(3, Steal.class);
    }

    /**
     * helper for init method
     *
     * @param num  number of elements to be inserted in the board
     * @param type type of the elements
     */
    private void fillBoard(int num, Class<? extends Element> type) {
        try {
            Element instance = (Element) type.getDeclaredConstructors()[0].newInstance();
            for (int i = 0; i < num; i++) {
                this.fields.add(new ElementWrapper(instance));
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }


    }

    /**
     * Print the game board
     */
    public void printBoard() {
        String res = String.format("%s%s%s%s%s%s%s%s\n" +
                        "%s%s%s\n" +
                        "%s%s%s\n" +
                        "%s%s%s%s%s%s%s%s", this.fields.get(10), this.fields.get(11), this.fields.get(12), this.fields.get(13)
                , this.fields.get(14), this.fields.get(15), this.fields.get(16), this.fields.get(17),
                this.fields.get(9), "    ".repeat(8), this.fields.get(18),
                this.fields.get(8), "    ".repeat(8), this.fields.get(19),
                this.fields.get(7), this.fields.get(6), this.fields.get(5), this.fields.get(4),
                this.fields.get(3), this.fields.get(2), this.fields.get(1), this.fields.get(0));
        System.out.println(res);

    }

    /**
     * Method that randomize fields on the board
     */
    public void randomizeFields() {
        for (int i = 0; i < fields.size(); i++) {
            int randomPosition = RandomNumberGenerator.generateRandomIntegerNumber(this.fields.size());
            Collections.swap(this.fields, i, randomPosition);
        }

        this.fields.add(0, new ElementWrapper(new Start()));
    }

    /**
     * get element(excluding players)from the ElementWrapper
     *
     * @param index index to be fetched
     * @return
     */
    public Element getElementOnIndex(int index) {
        return this.fields.get(index).getElement();
    }

    /**
     * this method move the figures
     *
     * @param desiredIndex desired position
     * @param currentIndex current index
     * @param player       player to be moved
     */
    public void setPlayer(int desiredIndex, int currentIndex, Player player) {
        if (player.getPlayerType().equals(PlayerType.PLAYER)) {
            this.fields.get(currentIndex).setPlayer(null);
            this.fields.get(desiredIndex).setPlayer(player);

        } else {
            this.fields.get(currentIndex).setBot(null);
            this.fields.get(desiredIndex).setBot(player);
        }

    }

    /**
     * This method set the current passive element(everything that is not instance of player)
     *
     * @param index
     * @param element
     */
    public void setElement(int index, Element element) {
        this.fields.get(index).setElement(element);

    }
}
