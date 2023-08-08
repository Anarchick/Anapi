package fr.anarchick.anapi.java;

import com.google.common.collect.Lists;

import javax.annotation.Nullable;
import java.awt.*;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.DoubleStream;

public class Utils {

	private static final Random RANDOM = new Random();
	
	public static Long now() {
		return Instant.now().getEpochSecond();
	}

	public static long timeSince(long time) {
		return now() - time;
	}
	
	@SafeVarargs
	public static <T> boolean contains(T test, T... list) {
	    for (T value : list) {
	    	if (value.equals(test)) return true;
	    }
	    return false;
	}
	
	public static <T> T getRandom(T[] array) {
        int rnd = RANDOM.nextInt(array.length);
        return array[rnd];
    }

	@Nullable
	public static <T> T getRandom(List<T> list) {
		if (list.isEmpty()) return null;
		int index = RANDOM.nextInt(list.size());
		return list.get(index);
	}

	/**
	 * Use a static Random instead of using new Random().nextDouble(min, max)
	 * @param min
	 * @param max
	 * @return
	 */
    public static Double getRandomDouble(double min, double max) {
        return RANDOM.nextDouble(Math.min(min, max), Math.max(min, max) + 1);
    }

	public static int getProbability(List<Double> probs) {
		double[] probabilities = new double[probs.size()];
		for (int i = 0; i < probs.size(); i++) {
			probabilities[i] = probs.get(i).doubleValue();
		}
		return getProbability(probabilities);
	}

	/**
	 * The sum of probabilities can exceed 100% without error
	 * @param probabilities
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

	public static <E> List<E> newArrayList(boolean shuffle, E... elements) {
		List<E> list = Lists.newArrayList(elements);
		if (shuffle) Collections.shuffle(list);
		return list;
	}

    public static Color gradient(Color in, Color out, double percent) {
    	int rI = in.getRed(), gI = in.getGreen(), bI = in.getBlue();
    	int rO = out.getRed(), gO = out.getGreen(), bO = out.getBlue();
    	double xI = (100d - percent)/100d;
    	double xO = percent/100d;
    	long r = Math.round(xI*rI+xO*rO);
    	long g = Math.round(xI*gI+xO*gO);
    	long b = Math.round(xI*bI+xO*bO);
    	return new Color((int) r, (int) g, (int) b);
    }
    
    public static <T extends Comparable<T>> T clamp(T val, T min, T max) {
	    if (val.compareTo(min) < 0) return min;
	    else if (val.compareTo(max) > 0) return max;
	    else return val;
	}

	public static <T extends Comparable<T>> boolean isBetween(T val, T min, T max) {
		return !(val.compareTo(min) < 0 || val.compareTo(max) > 0);
	}
    
    public static String phaseColor(String hex, float phase) {
    	Color color = Color.decode(hex);
    	float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
    	hsb[0] = (hsb[0] * 360f +  phase)  % 360f / 360f; 
    	color = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    	return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
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

	private final static Pattern UUID_REGEX_PATTERN =
			Pattern.compile("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$");

	public static boolean isValidUUID(String str) {
		if (str == null) {
			return false;
		}
		return UUID_REGEX_PATTERN.matcher(str).matches();
	}

	/** Linear interpolation from range [a;b] to [c;d]
	 *
	 * @since 1.0
	 *
	 * @param x between [a;b]
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return Double between [c;d]
	 */
	public static Double linear(double x, double a, double b, double c, double d) {
		if (a >= b) return 0D;
		if (x <= a) return c;
		if (x >= b) return d;
		return c + (x - a) * (d - c) / (b - a);
	}

}
