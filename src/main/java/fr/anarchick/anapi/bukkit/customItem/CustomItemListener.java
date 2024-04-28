package fr.anarchick.anapi.bukkit.customItem;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class CustomItemListener implements Listener {

	@EventHandler
    public void onUse(final PlayerInteractEvent event) {
		final @Nullable CustomItem customItem = CustomItemManager.getFromItemStack(event.getItem());
		if (customItem == null) return;

		if (event.getAction().isRightClick()) {
			customItem.onRightClick(event);
		} else if (event.getAction().isLeftClick()) {
			customItem.onLeftClick(event);
		}
	}
	
	@EventHandler
    public void onConsume(final PlayerItemConsumeEvent event) {
		final @Nullable CustomItem customItem = CustomItemManager.getFromItemStack(event.getItem());
		if (customItem == null) return;
		customItem.onConsume(event);
	}
	
	@EventHandler
    public void onBreak(final PlayerItemBreakEvent event) {
		final @Nullable CustomItem customItem = CustomItemManager.getFromItemStack(event.getBrokenItem());
		if (customItem == null) return;
		customItem.onBreak(event);
	}
	
	@EventHandler
    public void onDrop(final PlayerDropItemEvent event) {
		final @Nullable CustomItem customItem = CustomItemManager.getFromItemStack(event.getItemDrop().getItemStack());
		if (customItem == null) return;
		customItem.onDrop(event);
	}
	
	@EventHandler
    public void onPickUp(final PlayerAttemptPickupItemEvent event) {
		final @Nullable CustomItem customItem = CustomItemManager.getFromItemStack(event.getItem().getItemStack());
		if (customItem == null) return;
		customItem.onPickUp(event);
	}
	
	@EventHandler
    public void onDispense(final BlockDispenseEvent event) {
		final @Nullable CustomItem customItem = CustomItemManager.getFromItemStack(event.getItem());
		if (customItem == null) return;
		customItem.onDispense(event);
	}
	
	@EventHandler
    public void onPlace(final BlockPlaceEvent event) {
		final PlayerInventory inv = event.getPlayer().getInventory();
		final ItemStack mainItem = inv.getItemInMainHand();
		final ItemStack offItem = inv.getItemInOffHand();
		final @Nullable CustomItem customItemMain = CustomItemManager.getFromItemStack(mainItem);
		final @Nullable CustomItem customItemOff = CustomItemManager.getFromItemStack(offItem);

		if (customItemMain != null) {
			event.setCancelled(true);
			customItemMain.onPlace(event);
		} else if (customItemOff != null) {
			event.setCancelled(true);
			customItemOff.onPlace(event);
		}
	}
	
}
