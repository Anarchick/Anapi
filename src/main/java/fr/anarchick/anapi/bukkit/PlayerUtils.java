package fr.anarchick.anapi.bukkit;

import fr.anarchick.anapi.bukkit.inventory.ItemBuilder;
import fr.anarchick.anapi.bukkit.softdepend.PlaceHolderAPIUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
public class PlayerUtils {

    public static void kick(Player player, String miniMessage) {
        player.kick(PaperComponentUtils.getMiniMessageTextComponent(miniMessage));
    }

    /**
     * Does not send a message to console
     * @param miniMessage the mini message as string
     */
    public static void broadcastMessage(@Nonnull String miniMessage) {
        broadcastMessage(Bukkit.getOnlinePlayers(), miniMessage);
    }

    /**
     * Does not send a message to console
     * @param component the mini message as component
     */
    public static void broadcastMessage(@Nonnull Component component) {
        broadcastMessage(Bukkit.getOnlinePlayers(), component);
    }

    /**
     * Does not send a message to console
     * @param players receivers
     * @param miniMessage the mini message as string
     */
    public static void broadcastMessage(@Nonnull Collection<? extends Player> players, @Nonnull String miniMessage) {
        Component component = PaperComponentUtils.DEFAULT_MINIMESSAGE.deserialize(miniMessage);
        broadcastMessage(players, component);
    }

    /**
     * Does not send a message to console
     * @param players receivers
     * @param component the mini message as component
     */
    public static void broadcastMessage(@Nonnull Collection<? extends Player> players, @Nonnull Component component) {
        players.forEach(player -> player.sendMessage(component));
    }

    public static void sendActionBar(Player player, String miniMessage) {
        sendActionBar(player, miniMessage, true);
    }

    public static void sendActionBar(Player player, String miniMessage, boolean usePlaceHolderAPI) {
        if (usePlaceHolderAPI) {
            miniMessage = PlaceHolderAPIUtils.setPlaceholders((OfflinePlayer) player, miniMessage);
        }
        player.sendActionBar(PaperComponentUtils.DEFAULT_MINIMESSAGE.deserialize(miniMessage));
    }

    public static void sendMessage(Player player, String miniMessage) {
        sendMessage(player, miniMessage, true);
    }

    public static void sendMessage(Player player, String miniMessage, boolean usePlaceHolderAPI) {
        BukkitUtils.sendMessage(player, miniMessage, usePlaceHolderAPI);
    }

    public static void sendTitle(@Nonnull Player player, @Nonnull String title, @Nonnull String subtitle, int fadeIn, int stay, int fadeOut) {
        sendTitle(List.of(player), title, subtitle, fadeIn, stay, fadeOut);
    }
    public static void sendTitle(@Nonnull List<Player> players, @Nonnull String title, @Nonnull String subtitle, int fadeIn, int stay, int fadeOut) {
        if (players.isEmpty()) return;
        Component componentTitle = PaperComponentUtils.getMiniMessageTextComponent(title);
        Component componentSubtitle = PaperComponentUtils.getMiniMessageTextComponent(subtitle);
        Duration in = Duration.ofMillis(fadeIn * 50L);
        Duration st = Duration.ofMillis(stay *50L);
        Duration out = Duration.ofMillis(fadeOut *50L);
        Audience.audience(players).showTitle(Title.title(componentTitle, componentSubtitle, Title.Times.times(in, st, out)));
    }

    public static void playTotemAnimation(final @NotNull Player player, int customModelData) {
        PlayerInventory inv = player.getInventory();
        ItemStack itemHand = inv.getItemInMainHand();
        new Animation(false)
                .put(1, () -> {
                    ItemStack totem = new ItemBuilder(Material.TOTEM_OF_UNDYING)
                            .setCustomModelData(customModelData)
                            .build();
                    inv.setItemInMainHand(totem);
                    player.playEffect(EntityEffect.TOTEM_RESURRECT);
                })
                .put(2, () -> {
                    inv.setItemInMainHand(itemHand);
                })
                .play();
    }

}
