package fr.anarchick.anapi;

import fr.anarchick.anapi.bukkit.EntityUtils;
import fr.anarchick.anapi.bukkit.Logger;
import fr.anarchick.anapi.bukkit.commands.TabCompleteEvent;
import fr.anarchick.anapi.bukkit.customItem.CustomItemCommand;
import fr.anarchick.anapi.bukkit.customItem.CustomItemListener;
import fr.anarchick.anapi.bukkit.inventory.GUIListener;
import fr.anarchick.anapi.bukkit.softdepend.CharactersPlaceHolder;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.Objects;

@SuppressWarnings("unused")
public final class MainBukkit extends JavaPlugin implements Listener {

    private static MainBukkit INSTANCE;
    private static Logger LOGGER;
    public static final PluginManager PLUGIN_MANAGER = Bukkit.getServer().getPluginManager();

    @Override
    public void onEnable() {
        INSTANCE = this;
        LOGGER = new Logger(this);
        registerEvents(new EntityUtils());
        registerEvents(new TabCompleteEvent());
        registerEvents(new GUIListener());
        registerEvents(new CustomItemListener());

        Objects.requireNonNull(this.getCommand("customitem")).setExecutor(new CustomItemCommand());

        if (PLUGIN_MANAGER.isPluginEnabled("PlaceholderAPI")) {
            new CharactersPlaceHolder().register();
            CharactersPlaceHolder.reload();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static MainBukkit getInstance() {
        return INSTANCE;
    }

    public static void registerEvents(@Nonnull Listener listener) {
        PLUGIN_MANAGER.registerEvents(listener, getInstance());
    }

    public static void unregisterEvents(@Nonnull Listener listener) {
        HandlerList.unregisterAll(listener);
    }

    public static Logger logger() {
        return LOGGER;
    }


}
