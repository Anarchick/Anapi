package fr.anarchick.anapi.bukkit;

import fr.anarchick.anapi.java.ColorX;
import fr.anarchick.anapi.java.FileUtils;
import fr.anarchick.anapi.java.Pair;
import org.bukkit.map.MinecraftFont;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.List;

@SuppressWarnings("unused")
public class Characters {

	protected static final Characters PIXEL_8 = new Characters("A010", "A017");
	protected static final Characters PIXEL_64 = new Characters("A000", "A007");

	private final List<String> chars = new ArrayList<>();
	protected Characters(String unicode) {
		this(unicode, unicode);
	}
	
	protected Characters(String unicodeStart, String unicodeEnd) {
		int start = Integer.valueOf(unicodeStart, 16);
		int end = Integer.valueOf(unicodeEnd, 16);
		for (int i = start; i <= end; i++) {
			this.chars.add(compute(i));
		}
	}
	
	public String getFirst() {
		return this.chars.get(0);
	}
	
	public List<String> getAll() {
		return this.chars;
	}
	
	private static String compute(String unicode) {
		int i = Integer.valueOf(unicode, 16);
		return compute(i);
	}
	
	private static String compute(int i) {
		char c = Character.toChars(i)[0];
		return Character.toString(c);
	}
	
	public static String spacing(Integer n) {
		StringBuilder spacing = new StringBuilder();
		if (n == 0) return spacing.toString();
		int sum = Math.abs(n);
		int a = (n > 0) ? 2 : 0;
		if (n >= -8 && n <= 8) return compute("F8"+ a + sum);
		//                   1    2    4    8    16   32   64  128  256  512  1024
		String[] indices = {"1", "2", "4", "8", "9", "A", "B", "C", "D", "E", "F"};
		int i = 10;
		int value = 1024;
		while (sum != 0) {
			if (sum >= value) {
				spacing.append(compute("F8"+ a +indices[i]));
				sum -= value;
			}
			value /= 2;
			i--;
		}
		return spacing.toString();
	}

	/**
	 * Return the substring of the given pixel length (According to the Minecraft font)
	 * @param input unformatted string
	 * @param pixelsLength final string pixel width
	 * @return new String with limited length
	 */
	public static @Nonnull
	String StringPixelLengthFormat(@Nullable String input, final int pixelsLength, String replacement) {
		if (pixelsLength <= 0) throw new IndexOutOfBoundsException("Invalid number of pixels. '" + input + "' must be greater than 0.");
		if (input == null) input = "";
		int width = MinecraftFont.Font.getWidth(input);
		if (width == pixelsLength) {
			return input;
		} else if (width < pixelsLength) {
			return input + spacing(pixelsLength - width);
		} else {
			if (input.length() >= replacement.length()+1) {
				input = input.substring(0, input.length() - replacement.length()+1) + replacement;
			} else {
				input = input.substring(0, 1);
			}
			return StringPixelLengthFormat(input, pixelsLength, replacement);
		}
	}

	private static final String DEFAULT_JSON = "{\"chars\":[\"\\u%04x\"],\"file\":\"%s%s\",\"type\":\"bitmap\",\"height\":%d,\"ascent\":%d}";
	public static void generateJson(final File folder, String hexStart, final String path, final int height, final int ascent) {
		if (!folder.isDirectory()) throw new IllegalArgumentException("folder must be a directory");
		int hex;
		try {
			hex = Integer.valueOf(hexStart, 16);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("hexStart must be a hexadecimal string", e);
		}
		LinkedList<String> jsons = new LinkedList<>();
		for (File file : FileUtils.grab(folder, "-", ".png")) {
			String temp = String.format(DEFAULT_JSON, hex, path, file.getName(), height, ascent);
			jsons.add(temp);
			hex++;
		}
		if (jsons.isEmpty()) {
			System.out.println("No files found in " + folder.getAbsolutePath());
			return;
		}
		String json = String.join(",", jsons.toArray(new String[0]));
		File file = new File(folder, "generatedJson.json");
		FileUtils.save(file, json);
		System.out.println("File generated at " + file);
	}

	public static final HashMap<UUID, Pair<String, String>> HEADS = new HashMap<>();

	/**
	 * Get a minimessage representing the player's head skin.
	 * Don't forget to remove the uuid cache from static variable 'HEADS' when the player is disconnected
	 * @param uuid of any player
	 * @return a pair where #first() is small head and #second() is large head
	 */
	@Nonnull
	public static Pair<String, String> playerHead(@Nonnull UUID uuid) {
		if (HEADS.containsKey(uuid)) return HEADS.get(uuid);
		StringBuilder small = new StringBuilder();
		StringBuilder big = new StringBuilder();
		List<String> pixelSmall = PIXEL_8.getAll();
		List<String> pixelBig = PIXEL_64.getAll();
		String backspace1 = spacing(-1);
		String backspace8 = spacing(-8);
		String backspace64 = spacing(-64);
		BufferedImage skin;
		ColorX colorX = null;
		int lastRGB;
		try {
			skin = ImageIO.read(new URL("https://cravatar.eu/avatar/"+ uuid +"/8.png"));
			for (int y = 0; y < 8; y++) {
				String smallUnicode = pixelSmall.get(y);
				String bigUnicode = pixelBig.get(y);
				lastRGB = -1;
				for (int x = 0; x < 8; x++) {
					int rgb = skin.getRGB(x, y);
					if (rgb != lastRGB) {
						int blue = rgb & 0xff;
						int green = (rgb & 0xff00) >> 8;
						int red = (rgb & 0xff0000) >> 16;
						colorX = ColorX.fromRGB(red, green, blue);
						small.append("<").append(colorX).append(">");
						big.append("<").append(colorX).append(">");
						lastRGB = rgb;
					}
					small.append(smallUnicode).append(backspace1);
					big.append(bigUnicode).append(backspace1);
				}
				small.append(backspace8);
				big.append(backspace64);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int length = small.length(); // same size as big
		if (length > 0) {
			small.replace(length-1, length, "");
			big.replace(length-1, length, "");
			small = small.append("</"+colorX+">");
			big = big.append("</"+colorX+">");
		}
		Pair<String, String> pair = new Pair<>(small.toString(), big.toString());
		HEADS.put(uuid, pair);
		return pair;
	}

}
