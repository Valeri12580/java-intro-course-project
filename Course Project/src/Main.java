import elements.PlayerType;
import other.Game;
import other.Player;

public class Main {
    public static void main(String[] args) {

        Player player = new Player("Valeri", "P", 1000, PlayerType.PLAYER);

        Player bot = new Player("AI", "B", 1000,PlayerType.BOT);

        Game game = new Game(player,bot);

        game.start();



    }


}
