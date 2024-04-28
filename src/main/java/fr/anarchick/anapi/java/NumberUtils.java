package fr.anarchick.anapi.java;

import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.DoubleStream;

@SuppressWarnings("unused")
public class NumberUtils {

    private static final Random RANDOM = new Random();
    private final static TreeMap<Integer, String> map = new TreeMap<>();

    static {

        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");

    }

    public static String toRoman(int number) {
        int l =  map.floorKey(number);
        if ( number == l ) {
            return map.get(number);
        }
        return map.get(l) + toRoman(number-l);
    }

    /** Linear interpolation from range [a;b] to [c;d]
     *
     * @since 1.0
     *
     * @param x between [a;b]
     * @param a [a
     * @param b b]
     * @param c [c
     * @param d d]
     * @return Double between [c;d]
     */
    public static Double linear(double x, double a, double b, double c, double d) {
        if (a >= b) return 0D;
        if (x <= a) return c;
        if (x >= b) return d;
        return c + (x - a) * (d - c) / (b - a);
    }

    public static int[] getIntegersBetween(int start, int end) {
        final int min = Math.min(start, end);
        final int size = Math.abs(start - end) + 1;
        int[] ints = new int[size];
        for (int i = 0; i < size; i++) {
            ints[i] = min+i;
        }
        return ints;
    }

    public static <T extends Comparable<T>> T clamp(T val, T min, T max) {
        if (val.compareTo(min) < 0) return min;
        else if (val.compareTo(max) > 0) return max;
        else return val;
    }

    public static <T extends Comparable<T>> boolean isBetween(T val, T min, T max) {
        return !(val.compareTo(min) < 0 || val.compareTo(max) > 0);
    }

    /**
     * The sum of probabilities can exceed 100% without error
     * @param probabilities list of probabilities
     * @return the index of the probability distribution
     */
    public static int getProbability(double... probabilities) {
        double sum = DoubleStream.of(probabilities).sum();
        double r = getRandomDouble(0, 100);
        double s = 0;
        int prob = 0;
        for (double probability : probabilities) {
            s += ( 100.0 * probability / sum );
            if (r > s) prob++;
        }
        return prob;
    }

    /**
     *
     * @param chance [0;100]
     */
    public static boolean chance(double chance) {
        return (RANDOM.nextDouble(0, 100) <= chance);
    }

    /**
     * Use a static Random instead of using new Random().nextDouble(min, max)
     * @param min inclusive
     * @param max inclusive
     * @return random number between [min;max]
     */
    public static Double getRandomDouble(double min, double max) {
        return RANDOM.nextDouble(Math.min(min, max), Math.max(min, max) + 1);
    }

    public static int getProbability(List<Double> probs) {
        double[] probabilities = new double[probs.size()];
        for (int i = 0; i < probs.size(); i++) {
            probabilities[i] = probs.get(i);
        }
        return getProbability(probabilities);
    }

}
