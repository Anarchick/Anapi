package fr.anarchick.anapi.java;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.awt.*;
import java.time.Instant;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
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

	@SafeVarargs
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
    
    public static String phaseColor(String hex, float phase) {
    	Color color = Color.decode(hex);
    	float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
    	hsb[0] = (hsb[0] * 360f +  phase)  % 360f / 360f; 
    	color = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    	return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

	public final static Pattern UUID_REGEX_PATTERN =
			Pattern.compile("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$");

	public static boolean isValidUUID(String str) {
		if (str == null) {
			return false;
		}
		return UUID_REGEX_PATTERN.matcher(str).matches();
	}

	public static final Pattern PATTERN_UNICODE = Pattern.compile("\\\\u([0-9A-Fa-f]{4})");

	/**
	 * Replace all unicode \uABCD with it's corresponding character.
	 * @param input text to evaluate
	 * @return the transformed text
	 */
	public static String replaceUnicode(@NotNull String input) {
		final Matcher matcher = PATTERN_UNICODE.matcher(input);
		final StringBuilder result = new StringBuilder();

		while (matcher.find()) {
			int code = Integer.parseInt(matcher.group(1), 16);
			matcher.appendReplacement(result, Character.toString((char) code));
		}

		matcher.appendTail(result);
		return result.toString();
	}

	/**
	 * Extract regex groups from the given string. Only apply to the first occurrence !
	 * @param pattern the pattern
	 * @param text the string
	 * @return a map of regex groups or empty map if the pattern does not match.
	 */
	@NotNull
	public static Map<Integer, String> extractGroups(final @NotNull Pattern pattern, final String text) {
		final Map<Integer, String> groups = new HashMap<>();
		final Matcher matcher = pattern.matcher(text);

		if (matcher.find()) {

			for (int i = 1; i <= matcher.groupCount(); i++) {
				groups.put(i, matcher.group(i));
			}

		}

		return groups;
	}
}
