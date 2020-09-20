package util;

import java.util.Random;

/**
 * @author Catherine
 * @create 2020-09-15 20:39
 */
public class RandomUtil {
    public static boolean ifDo(double rate) {
        Random random = new Random();
        double a = random.nextDouble();
        return !(a > rate);
    }

    public static int ramInt(Integer[] index) {
        Random random = new Random();
        return index[random.nextInt(index.length)];
    }
}
