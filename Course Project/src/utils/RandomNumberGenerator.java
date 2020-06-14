package utils;

import java.util.Random;

public class RandomNumberGenerator {
    private  static Random random=new Random();


    /**
     * generate random number
     * @param bound the range
     * @return number
     */
    public static int generateRandomIntegerNumber(int bound){
        return random.nextInt(bound);
    }

    /**
     * generate random double number
     * @param min minimum
     * @param max maximum
     * @return value
     */
    public static double generateRandomDoubleNumber(double min,double max){
        double number=random.nextDouble()*(max-min)+min;
        return number;
    }
}
