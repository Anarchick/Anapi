package fr.anarchick.anapi.bukkit.softdepend;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * These methods return the same input if PlaceHolderAPI is not available
 */
@SuppressWarnings("unused")
public class PlaceHolderAPIUtils {

    public static String setPlaceholders(@Nullable CommandSender sender, String message) {
        if ( !(sender instanceof OfflinePlayer) ) {
            sender = null;
        }
        return setPlaceholders(sender, message);
    }

    public static List<String> setPlaceholders(@Nullable CommandSender sender, List<String> messages) {
        if ( !(sender instanceof OfflinePlayer) ) {
            sender = null;
        }
        return setPlaceholders(sender, messages);
    }

    public static String setPlaceholders(@Nullable OfflinePlayer player, String message) {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            message = PlaceholderAPI.setPlaceholders(player, message);
        }
        return message;
    }

    public static List<String> setPlaceholders(@Nullable OfflinePlayer player, List<String> messages) {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            messages = PlaceholderAPI.setPlaceholders(player, messages);
        }
        return messages;
    }

    public static String setRelationalPlaceholders(@Nullable Player player1, @Nullable Player player2, String message) {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            message = PlaceholderAPI.setRelationalPlaceholders(player1, player2, message);
        }
        return message;
    }

    public static List<String> setRelationalPlaceholders(@Nullable Player player1, @Nullable Player player2, List<String> messages) {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            messages = PlaceholderAPI.setRelationalPlaceholders(player1, player2, messages);
        }
        return messages;
    }

}
