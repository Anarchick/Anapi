package fr.anarchick.anapi;

import fr.anarchick.anapi.bukkit.commands.TabCompleteEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public final class Main extends JavaPlugin {

    private static Main INSTANCE;
    public static final PluginManager PLUGIN_MANAGER = Bukkit.getServer().getPluginManager();

    @Override
    public void onEnable() {
        this.INSTANCE = this;
        registerEvents(new TabCompleteEvent());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Main getInstance() {
        return INSTANCE;
    }

    public static void registerEvents(@Nonnull Listener listener) {
        PLUGIN_MANAGER.registerEvents(listener, getInstance());
    }

    public static void unregisterEvents(@Nonnull Listener listener) {
        HandlerList.unregisterAll(listener);
    }

}
