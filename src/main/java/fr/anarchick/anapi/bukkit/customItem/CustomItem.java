package fr.anarchick.anapi.bukkit.customItem;

import fr.anarchick.anapi.bukkit.MiniMessage;
import fr.anarchick.anapi.bukkit.PaperComponentUtils;
import fr.anarchick.anapi.bukkit.softdepend.PlaceHolderAPIUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public abstract class CustomItem {

	private final String id;
	private final JavaPlugin plugin;

	// TODO max stack size

	protected CustomItem(final @NotNull JavaPlugin plugin, final @NotNull String id) {
		this.id = id;
		this.plugin = plugin;

		if (this instanceof Listener listener) {
			Bukkit.getPluginManager().registerEvents(listener, plugin);
		}

		CustomItemManager.register(this);
	}

	@NotNull
	public String getId() {
		return id;
	}

	@NotNull
	public JavaPlugin getPlugin() {
		return plugin;
	}

	/**
	 * Return vanilla Item, not CustomItem.
	 * To get The custom item you have to use {@link CustomItemManager#getItem(CustomItem, int)}
	 * @return the item
	 */
	@NotNull
	abstract protected ItemStack getItem();
	
	protected void onRightClick(PlayerInteractEvent event) {}
	
	protected void onLeftClick(PlayerInteractEvent event) {}
	
	protected void onBreak(PlayerItemBreakEvent event) {}
	
	protected void onDrop(PlayerDropItemEvent event) {}
	
	protected void onPickUp(PlayerAttemptPickupItemEvent event) {}

	protected void onConsume(PlayerItemConsumeEvent event) {}
	
	protected void onDispense(BlockDispenseEvent event) {
		event.setCancelled(true);
	}
	
	protected void onPlace(BlockPlaceEvent event) {
		event.setCancelled(true);
	}

	@Override
	public String toString() {
		return "Custom item = " + id;
	}

	@NotNull
	public NamespacedKey getNamespacedKey(final @NotNull String key) {
		return new NamespacedKey(plugin, key);
	}

	@NotNull
	public String placeholders(final @Nullable OfflinePlayer offlinePlayer, final @NotNull String str) {
		return PlaceHolderAPIUtils.setPlaceholders(offlinePlayer, str);
	}

	@NotNull
	public Component deserialize(final @NotNull @MiniMessage String str) {
		return PaperComponentUtils.DEFAULT_MINIMESSAGE.deserialize(str);
	}

	/**
	 * Adapt an item to be able to trigger the custom item events.
	 * Be carful, the item will be modified and NOT cloned !
	 */
	@NotNull
	protected ItemStack adapt(@NotNull ItemStack item) {
		final ItemMeta itemMeta = item.getItemMeta();
		final PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
		pdc.set(CustomItemManager.namespaceKey, PersistentDataType.STRING, getId());
		item.setItemMeta(itemMeta);
		return item;
	}

}
