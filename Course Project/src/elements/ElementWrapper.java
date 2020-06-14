package elements;

import other.Player;

public class ElementWrapper {
    private Element element;
    private Player player;
    private Player bot;

    public ElementWrapper(Element element) {
        this.element = element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setBot(Player bot) {
        this.bot = bot;
    }

    public Element getElement() {
        return element;
    }

    /**
     * This method create representation of every element on the field
     * @return
     */
    @Override
    public String toString() {
        String playerToString=this.player==null?"":this.player.toString();
        String botToString=this.bot==null?"":this.bot.toString();
        return String.format("|%s %s %s|",playerToString,this.element.toString(),botToString);
    }
}
