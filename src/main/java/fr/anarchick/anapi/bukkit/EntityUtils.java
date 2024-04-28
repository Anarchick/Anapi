package fr.anarchick.anapi.bukkit;

import com.jeff_media.morepersistentdatatypes.DataType;
import fr.anarchick.anapi.MainBukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class EntityUtils implements Listener {

    /**
     * Remove all potions effects
     * @param entity the entity
     */
    public static void milk(LivingEntity entity) {
        if (entity == null) return;

        for (PotionEffect potionEffect : entity.getActivePotionEffects()) {
            entity.removePotionEffect(potionEffect.getType());
        }
    }

    /**
     * Remove all potions effects of this category
     * @param entity the entity
     */
    public static void milk(LivingEntity entity, PotionEffectType.Category category) {
        if (entity == null) return;

        for (PotionEffect effect : entity.getActivePotionEffects()) {
            PotionEffectType type = effect.getType();

            if (type.getEffectCategory().equals(category)) {
                entity.removePotionEffect(type);
            }

        }
    }

    private static final NamespacedKey KEY_REMOVE_ON_LOAD = new NamespacedKey(MainBukkit.getInstance(), "remove_on_load");

    public static void mustBeRemovedWhenLoaded(@NotNull Entity entity, boolean remove) {
        PersistentDataContainer pdc = entity.getPersistentDataContainer();

        if (remove) {
            pdc.set(KEY_REMOVE_ON_LOAD, DataType.BOOLEAN, true);
        } else {
            pdc.remove(KEY_REMOVE_ON_LOAD);
        }
    }

    public static boolean mustBeRemovedWhenLoaded(@NotNull Entity entity) {
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        return pdc.getOrDefault(KEY_REMOVE_ON_LOAD, DataType.BOOLEAN, false);
    }

    @EventHandler
    public void removeOnLoad(EntitiesLoadEvent event) {
        for (Entity entity : event.getEntities()) {
            if (entity.hasMetadata("remove")) {
                entity.remove();
            }
        }
    }

}
