package fr.anarchick.anapi.bukkit;

import fr.anarchick.anapi.java.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BukkitUtils {
	
	public static final double PI = Math.PI;
	public static final double HALF_PI = PI / 2;
	public static final double DEG_TO_RAD = PI / 180;
	public static final double RAD_TO_DEG =  180 / PI;
	
	public static float wrapAngleDeg(float angle) {
		angle %= 360f;
		if (angle <= -180) {
			return angle + 360;
		} else if (angle > 180) {
			return angle - 360;
		} else {
			return angle;
		}
	}
	
	public static Vector createVector(Float yaw, Float pitch, Float length) {
		yaw = wrapAngleDeg(yaw) + 90;
		if (yaw > 360){
			yaw -= 360;
		}
		pitch = -wrapAngleDeg(pitch);
		double y = Math.sin(pitch * DEG_TO_RAD);
		double div = Math.cos(pitch * DEG_TO_RAD);
		double x = Math.cos(yaw * DEG_TO_RAD);
		double z = Math.sin(yaw * DEG_TO_RAD);
		x *= div;
		z *= div;
		Vector vec = new Vector(x,y,z);
		return vec.multiply(length);
	}
	
	public static Vector createVector(Location from, Location to) {
		if (from == null || to == null)
			return null;
		return to.toVector().subtract(from.toVector());
	}
	
	public static Vector createRandomVector(double x, double y, double z) {
		return new Vector(Utils.getRandomDouble(-x, x), Utils.getRandomDouble(-y, y), Utils.getRandomDouble(-z, z));
	}
	
	public static Double convertToPositiveAngle(double a) {
		if (a < 0)
			return 360 + a;
		return a;
	}
	
	public static void spawnColoredParticuleDust(Location loc, Color color, boolean force) {
		spawnColoredParticuleDust(loc, color, force, null);
	}
	public static void spawnColoredParticuleDust(Location loc, Color color,boolean force, @Nullable List<Player> players) {
		Particle.DustOptions options = new Particle.DustOptions(color, 1);
		if (players == null) {
			loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 0, 0d, 0d, 0d, 0d, options, force);
		} else {
			for (Player player : players) {
				player.spawnParticle(Particle.REDSTONE, loc, 0, 0d, 0d, 0d, 0d, options);
			}
		}
	}

	final private static Pattern HEX_PATTERN = Pattern.compile("&?#[a-fA-F0-9]{6}");
    public static String colored(String txt) {
    	final String EMPTY = "";
    	Matcher match = HEX_PATTERN.matcher(txt);
    	while (match.find()) {
    		String color = txt.substring(match.start(), match.end());
    		txt = txt.replace(color, ChatColor.of(color)+EMPTY);
    		match = HEX_PATTERN.matcher(txt);
    	}
		return ChatColor.translateAlternateColorCodes('&', txt);
	}

    public static List<String> colored(List<String> txt) {
		for (int i = 0; i < txt.size(); i++) {
			txt.set(i, colored(txt.get(i)));
		}
		return txt;
	}
    
    public static List<String> colored(String[] txt) {
    	return colored(new ArrayList<>(List.of(txt)));
    }
    
    public static List<Block> AABB(Location min, Location max) {
    	List<Block> blocks = new ArrayList<>();
    	World world = min.getWorld();
    	double xMin = Math.min(min.getX(), max.getX());
    	double yMin = Math.min(min.getY(), max.getY());
    	double zMin = Math.min(min.getZ(), max.getZ());
    	double xMax = Math.max(min.getX(), max.getX());
    	double yMax = Math.max(min.getY(), max.getY());
    	double zMax = Math.max(min.getZ(), max.getZ());
    	for (double x = xMin; x <= xMax; x++) {
    	    for (double y = yMin; y <= yMax; y++) {
    	        for (double z = zMin; z <= zMax; z++) {
    	        	blocks.add(world.getBlockAt((int) x, (int) y, (int) z));
    	        }
    	    }
    	}
    	return blocks;
    }
    
    public static List<Block> ellipsoid(Location center, float rx, float ry, float rz) {
    	List<Block> blocks = new ArrayList<>();
    	Vector vCenter = center.toVector(), vLoc, vRadius = new Vector(rx, ry, rz);
    	Location max = center.clone().add(vRadius);
    	Location min = center.clone().subtract(vRadius);
    	for (Block block : AABB(min, max)) {
    		vLoc = block.getLocation().toVector().subtract(vCenter).divide(vRadius);
    		if (vLoc.lengthSquared() < 1d) {
    			blocks.add(block);
    		}
    	}
    	return blocks;
    }
    
    public static String gradientString(String message, java.awt.Color in, java.awt.Color out) {
    	StringBuilder sb = new StringBuilder();
    	double percent = 0d;
    	double x = 100d/message.length();
    	for (char c : message.toCharArray()) {
    		sb.append(ChatColor.of(Utils.gradient(in, out, percent)));
    		sb.append(c);
    		percent += x;
    	}
    	return sb.toString();
    }
    
    public static Integer getXPAtLevel(int level) {
    	if (level <= 15) return 2*level+7;
    	if (level <= 30) return 5*level-38;
    	return 9*level-158;
    }
    
    public static int getTotalXP(Player player) {
        int exp = Math.round(getXPAtLevel(player.getLevel()) * player.getExp());
        int currentLevel = player.getLevel();
        while (currentLevel > 0) {
            currentLevel--;
            exp += getXPAtLevel(currentLevel);
        }
        return Math.max(exp, 0);
    }

    public static void setTotalXP(Player player, int level) {
        player.setLevel(0);
        player.setExp(0);
        player.giveExp(level);
    }

	public static Double getXPToLevelUp(int level) {
		if (level <= 16) return level*level + 6.0*level;
		if (level <= 31) return 2.5*level*level -40.5*level +360.0;
		return 4.5*level*level -162.5*level +2220.0;
	}
    
	public static void sendColoredMessage(Player player, String message) {
		player.sendMessage(colored(message));
	}


	public static void sendColorActionBar(Player player, String message) {
		TextComponent tc = new TextComponent(colored(message));
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, tc);
	}

}
