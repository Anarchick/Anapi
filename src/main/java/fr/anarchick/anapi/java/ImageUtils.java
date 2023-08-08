package fr.anarchick.anapi.java;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class ImageUtils {

    public static byte[] imageToByteArray(BufferedImage image) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);
        return out.toByteArray();
    }

    public static BufferedImage byteArrayToImage(byte[] byteArray) throws IOException {
        return ImageIO.read(new ByteArrayInputStream(byteArray));
    }

    public static String imageToBase64(BufferedImage image) throws IOException {
        if (image == null)
            return "";
        return Base64.getEncoder().encodeToString(imageToByteArray(image));
    }

    public static BufferedImage base64ToImage(String base64) {
        if (base64.isEmpty())
            return null;
        byte[] decoded = Base64.getDecoder().decode(base64);
        InputStream stream = new ByteArrayInputStream(decoded);
        BufferedImage image = null;
        try {
            image = ImageIO.read(stream);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return image;
    }

    public static BufferedImage imageFile(String pathString) throws IOException {
        if (pathString == null || pathString.isEmpty()) return null;
        Path path = Paths.get(pathString);
        return (Files.isRegularFile(path)) ? ImageIO.read(path.toFile()) : null;
    }

    public static BufferedImage imageURL(String urlString) throws IOException {
        if (urlString == null || urlString.isEmpty()) return null;
        return ImageIO.read(new URL(urlString));
    }

    public static BufferedImage resize(BufferedImage img, int w, int h) {
        if ((w < 0) || (h < 0) || img == null)
            return null;
        BufferedImage resized = new BufferedImage(w, h, img.getType());
        Graphics2D g = resized.createGraphics();
        g.drawImage(img, 0, 0, w, h, null);
        g.dispose();
        return resized;
    }

    public static boolean contains(BufferedImage img, int x, int y) {
        if (img == null)
            return false;
        return (x >= 0 && y >= 0 && x <= img.getWidth() && y <= img.getHeight());
    }

}
