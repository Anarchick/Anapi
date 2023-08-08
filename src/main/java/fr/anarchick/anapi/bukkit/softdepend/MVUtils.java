package fr.anarchick.anapi.bukkit.softdepend;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MVUtils {

    public static final MultiverseCore CORE = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
    public static final MVWorldManager MV_WORLD_MANAGER = CORE.getMVWorldManager();

    public static MultiverseWorld getMVWorld(String worldName) {
        return MV_WORLD_MANAGER.getMVWorld(worldName);
    }
    @Nullable
    public static MultiverseWorld getMVWorld(@Nonnull World world) {
        return MV_WORLD_MANAGER.getMVWorld(world);
    }

    public static boolean loadMVWorld(@Nonnull String worldName) {
        return ( Bukkit.getWorld(worldName) == null ) && ( MV_WORLD_MANAGER.loadWorld(worldName) );
    }

    public static boolean unloadMVWorld(@Nonnull String worldName) {
        return ( Bukkit.getWorld(worldName) != null ) && ( MV_WORLD_MANAGER.unloadWorld(worldName, true) );
    }

    public static boolean cloneMVWorld(@Nonnull String worldName, @Nonnull String newWorldName) {
        return MV_WORLD_MANAGER.cloneWorld(worldName, newWorldName);
    }

    public static void deleteMVWorld(@Nonnull String worldName, boolean removeConfig, boolean removeFolder) {
        MV_WORLD_MANAGER.deleteWorld(worldName, removeConfig, removeFolder);
    }

    public static Location getMVSpawnLocation(@Nonnull MultiverseWorld mvWorld) {
        return mvWorld.getSpawnLocation();
    }

}
