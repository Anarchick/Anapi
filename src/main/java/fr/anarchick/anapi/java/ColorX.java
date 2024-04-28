package fr.anarchick.anapi.java;

import java.awt.*;

@SuppressWarnings("unused")
public class ColorX {

    private static final String HEX_PATTERN = "^#([A-Fa-f0-9]{6})$";

    private double h, s, l;
    private int r, g, b;

    private ColorX(double h, double s, double l) {
        this.h = h;
        this.s = s;
        this.l = l;
        updateRGB();
    }

    private ColorX(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
        updateHSL();
    }

    private void updateRGB() {
        //If saturation is 0, the color is a shade of gray
        if (s == 0) {
            this.r = this.g = this.b = (int) (l * 255);
        } else {
            Color c = Color.getHSBColor((float) h, (float) s, (float) l);
            this.r = c.getRed();
            this.g = c.getGreen();
            this.b = c.getBlue();
        }
    }

    private void updateHSL() {
        double rd = (double) r / 255;
        double gd = (double) g / 255;
        double bd = (double) b / 255;
        double max = Math.max(Math.max(rd, gd), bd);
        double min = Math.min(Math.min(rd, gd), bd);
        double h = 0;
        double s = 0;
        double l = (max + min) / 2;

        if (max != min) {
            double d = max - min;
            s = l > 0.5 ? d / (2 - max - min) : d / (max + min);
            if (max == rd) {
                h = (gd - bd) / d + (gd < bd ? 6 : 0);
            } else if (max == gd) {
                h = (bd - rd) / d + 2;
            } else if (max == bd) {
                h = (rd - gd) / d + 4;
            }
            h /= 6;
        }
        this.h = h;
        this.s = s;
        this.l = l;
    }

    public static ColorX fromHSL(double h, double s, double l) {
        if (s < 0d || s > 1d) {
            throw new ArithmeticException("Saturation is " + s + " but must be between 0 and 1");
        }
        if (l < 0d || l > 1d) {
            throw new ArithmeticException("Luminosity is " + l + " but must be between 0 and 1");
        }
        return new ColorX(h % 1.0, s, l);
    }

    public static ColorX fromRGB(int r, int g, int b) {
        if (r < 0 || r > 255) {
            throw new ArithmeticException("Red is " + r + "but must be between 0 and 255");
        }
        if (g < 0 || g > 255) {
            throw new ArithmeticException("Green is " + g + "but must be between 0 and 255");
        }
        if (b < 0 || b > 255) {
            throw new ArithmeticException("Blue is " + b + "but must be between 0 and 255");
        }
        return new ColorX(r, g, b);
    }

    /**
     * La chaîne de caractères HEX doit être au format "#RRGGBB"
     * @param hex the hex string
     * @return the returned ColorX
     */
    public static ColorX fromHex(String hex) {
        if (!hex.matches(HEX_PATTERN)) {
            throw new IllegalArgumentException("'"+ hex + "' does not appear to be a valid hexadecimal representation. try using #RRGGBB instead");
        }
        int r = Integer.valueOf(hex.substring(1, 3), 16);
        int g = Integer.valueOf(hex.substring(3, 5), 16);
        int b = Integer.valueOf(hex.substring(5, 7), 16);
        return fromRGB(r, g, b);
    }

    public static ColorX fromInt(int rgb) {
        if (rgb < 0 || rgb > 16777215) {
            throw new ArithmeticException("'" + rgb + "' is out of range 0 and 16777215");
        }
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        return fromRGB(r, g, b);
    }

    public int[] toRGB() {
        return new int[]{r, g, b};
    }

    public double[] toHSL() {
        return new double[] {h, s, l};
    }

    public int toInt() {
        return (r << 16) + (g << 8) + b;
    }

    @Override
    public ColorX clone() {
        return fromHSL(h, s, l);
    }

    @Override
    public int hashCode() {
        return toInt();
    }

    public int getRed() {
        return r;
    }

    public int getGreen() {
        return g;
    }

    public int getBlue() {
        return b;
    }

    public double getHue() {
        return h;
    }

    public double getSaturation() {
        return s;
    }

    public double getLuminosity() {
        return l;
    }

    /**
     * La chaîne de caractères HEX au format "#RRGGBB"
     * @return hexadecimal representation
     */
    @Override
    public String toString() {
        return "#"+toHexString();
    }

    /**
     * La chaîne de caractères HEX au format "RRGGBB"
     * @return hexadecimal representation
     */
    public String toHexString() {
        return String.format("%02x%02x%02x", r, g, b);
    }

    public ColorX setR(int r) {
        this.r = r;
        updateHSL();
        return this;
    }

    public ColorX setG(int g) {
        this.g = g;
        updateHSL();
        return this;
    }

    public ColorX setB(int b) {
        this.b = b;
        updateHSL();
        return this;
    }

    public ColorX setH(double h) {
        this.h = h;
        updateRGB();
        return this;
    }

    public ColorX setS(double s) {
        this.s = s;
        updateRGB();
        return this;
    }

    public ColorX setL(double l) {
        this.l = l;
        updateRGB();
        return this;
    }

    public ColorX getComplementary() {
        return fromHSL(h + 0.5, s, l);
    }

    public ColorX mix(float ratio, ColorX color) {
        if (ratio == 0) return clone();
        ratio = NumberUtils.clamp(ratio, 0f, 1f);
        int red = r + (int) ((color.r - r) * ratio);
        int green = g + (int) ((color.g - g) * ratio);
        int blue = b + (int) ((color.b - b) * ratio);
        return fromRGB(red, green, blue);
    }

}
