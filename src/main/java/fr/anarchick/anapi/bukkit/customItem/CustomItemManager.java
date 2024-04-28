package fr.anarchick.anapi.bukkit.customItem;

import fr.anarchick.anapi.MainBukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class CustomItemManager {
	
	final private static Map<String, CustomItem> MANAGER = new HashMap<>();
	protected static final NamespacedKey namespaceKey = new NamespacedKey(MainBukkit.getInstance(), "custom_item");

	protected static void register(final @NotNull CustomItem customItem) {
		if (MANAGER.containsKey(customItem.getId())) {
			throw new IllegalArgumentException("CustomItem with id " + customItem.getId() + " already registered");
		}

		MANAGER.put(customItem.getId(), customItem);
	}

	public static void unregister(final @NotNull CustomItem customItem) {
		MANAGER.remove(customItem.getId());
	}

	/**
	 * Should be called when a plugin is disabled
	 */
	public static void unregister(final @NotNull JavaPlugin plugin) {
		MANAGER.entrySet().removeIf(entry -> entry.getValue().getPlugin().equals(plugin));
	}
	
	protected static List<CustomItem> getAll() {
		return List.of( MANAGER.values().toArray(new CustomItem[0]) );
	}
	
	@Nullable
	public static CustomItem getFromId(final String customItemId) {
		return MANAGER.get(customItemId);
	}

	public static boolean exist(final String customItemId) {
		return MANAGER.containsKey(customItemId);
	}
	
	@Nullable
	public static CustomItem getFromItemStack(final @Nullable ItemStack item) {
		if (item == null) {
			return null;
		}

		final ItemMeta itemMeta = item.getItemMeta();

		if (itemMeta == null) {
			return null;
		}

		final PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
		final String id = pdc.getOrDefault(namespaceKey, PersistentDataType.STRING, "");
		return getFromId(id);
	}

	@NotNull
	public static ItemStack getItem(final @NotNull CustomItem customItem, final int amount) {
		final ItemStack item = customItem.getItem().clone();
		item.setAmount(amount);
		return customItem.adapt(item);
	}
	
	@Nullable
	public static ItemStack getItem(final String customItemId, final int amount) {
		final CustomItem customItem = getFromId(customItemId);
		return (customItem != null) ? getItem(customItem, amount) : null;
	}
	
	public static boolean is(final @NotNull CustomItem customItem, final @Nullable ItemStack item) {
		return customItem.equals(getFromItemStack(item));
	}
	
}
