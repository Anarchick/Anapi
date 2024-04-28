package fr.anarchick.anapi.bukkit;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * A logger which support miniMessage
 */
@SuppressWarnings("unused")
public class Logger {

    private final Plugin plugin;
    private final @NotNull ComponentLogger logger;

    public Logger(Plugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getComponentLogger();
    }

    public Plugin plugin() {
        return plugin;
    }

    private Component deserialize(final @NotNull String message) {
        return PaperComponentUtils.DEFAULT_MINIMESSAGE.deserialize(message);
    }

    public void info(String miniMessage) {
        logger.info(PaperComponentUtils.getMiniMessageText(miniMessage));
    }

    public void info(CommandSender sender, String miniMessage) {
        if (sender instanceof Player player) {
            BukkitUtils.sendMessage(player, miniMessage);
        }
        logger.info(deserialize(miniMessage));
    }

    public void warn(String miniMessage) {
        logger.warn(deserialize(miniMessage));
    }

    public void warn(CommandSender sender, String miniMessage) {
        if (sender instanceof Player player) {
            BukkitUtils.sendMessage(player, "<yellow>"+miniMessage);
        }
        logger.warn(deserialize(miniMessage));
    }

    public void error(String miniMessage) {
        logger.error(deserialize(miniMessage));
    }

    public void error(CommandSender sender, String miniMessage) {
        if (sender instanceof Player player) {
            BukkitUtils.sendMessage(player, "<red>"+miniMessage);
        }
        logger.error(deserialize(miniMessage));
    }

}
