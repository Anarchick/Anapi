package fr.anarchick.anapi.bukkit;

import java.util.ArrayList;
import java.util.List;

public abstract class Characters {

	private final List<String> chars = new ArrayList<>();
	protected Characters(String unicode) {
		this(unicode, unicode);
	}
	
	protected Characters(String unicodeStart, String unicodeEnd) {
		Integer start = Integer.valueOf(unicodeStart, 16);
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
		Integer i = Integer.valueOf(unicode, 16);
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
	
}
