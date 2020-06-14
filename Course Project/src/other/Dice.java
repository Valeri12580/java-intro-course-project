package other;

import utils.RandomNumberGenerator;

public class Dice {

    /**
     * generate 1 or 2
     * @return generated number
     */
    public static int generateTwoWallDiceNumber() {
        return RandomNumberGenerator.generateRandomIntegerNumber(2) + 1;
    }

    /**
     * generate number from 1 to 10
     * @return generated number
     */
    public static int generateTenWallDiceNumber() {
        return RandomNumberGenerator.generateRandomIntegerNumber(10) + 1;
    }

    /**
     * generate number from 1 to 100
     * @return generated number
     */
    public static int generateOneHundredWallDiceNumber() {
        return RandomNumberGenerator.generateRandomIntegerNumber(100) + 1;
    }

    /**
     * used for generating number for player move
     * @return generated number
     */
    public static int generateMoveNumber(Player player) {
        int randomGeneratedTwoWallDiceNumber = generateTwoWallDiceNumber();
        System.out.printf("%s изкара %d\n", player.getName(), randomGeneratedTwoWallDiceNumber);
        return randomGeneratedTwoWallDiceNumber + player.getCurrentPositionIndex();
    }

}
