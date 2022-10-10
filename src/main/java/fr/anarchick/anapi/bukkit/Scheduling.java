package fr.anarchick.anapi.bukkit;

import fr.anarchick.anapi.Main;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Scheduling {

    private static final BukkitScheduler scheduler = Bukkit.getScheduler();
    private static final Plugin plugin = Main.getInstance();

    public static void sync(Runnable runnable) {
        scheduler.runTask(plugin, runnable);
    }
    
    public static void async(Runnable runnable) {
        scheduler.runTaskAsynchronously(plugin, runnable);
    }

    public static void syncDelay(int ticks, Runnable runnable) {
        scheduler.runTaskLater(plugin, runnable, ticks);
    }

    public static void asyncDelay(int ticks, Runnable runnable) {
        scheduler.runTaskLaterAsynchronously(plugin, runnable, ticks);
    }
    
}
