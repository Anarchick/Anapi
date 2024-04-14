package fr.anarchick.anapi.bukkit.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class GUIListener implements Listener {

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof GUI gui) {
            event.setCancelled(gui.disableInteraction);
            gui.onDrag(event);
        }
    }


    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof GUI gui) {
            event.setCancelled(gui.disableInteraction);
            if (event.getClickedInventory() == gui.getInventory()) {
                ItemGUI itemGUI = gui.ItemGUI_MAP.getOrDefault(event.getSlot(), ItemGUI.EMPTY);
                if (itemGUI.ignoreCancelledEvent || !event.isCancelled()) {
                    event.setCancelled(itemGUI != ItemGUI.EMPTY);
                    Player player = (Player) event.getWhoClicked();
                    itemGUI.onAnyClick(player, event);
                    switch (event.getClick()) {
                        case LEFT, SHIFT_LEFT -> itemGUI.onLeftClick(player, event);
                        case RIGHT, SHIFT_RIGHT -> itemGUI.onRightClick(player, event);
                    }
                }
            }
            gui.onClick(event);
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory().getHolder() instanceof GUI gui) {
            gui.onOpen(event);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof GUI gui) {
            if (gui.preventPlayerClose && event.getReason() == InventoryCloseEvent.Reason.PLAYER) {
                gui.open(event.getPlayer());
            } else {
                gui.onClose(event);
            }
        }
    }

}
