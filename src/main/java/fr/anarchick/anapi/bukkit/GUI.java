package fr.anarchick.anapi.bukkit;

import fr.anarchick.anapi.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class GUI implements Listener {

    @SuppressWarnings("deprecation")
    public static Inventory createInventory(String title, int size) {
        return Bukkit.createInventory(null, size, BukkitUtils.colored(title));
    }

    @Nonnull private Inventory inv;
    @Nullable private GUI previousGUI;

    protected GUI(@Nonnull Inventory inv, @Nullable GUI previousGUI) {
        this.inv = inv;
        this.previousGUI = previousGUI;
    }

    public void open(HumanEntity... players) {
        if (inv.getViewers().size() == 0) {
            Main.registerEvents(this);
        }
        for (HumanEntity player : players) {
            player.openInventory(inv);
        }
    }

    public void close() {
        close(inv.getViewers().toArray(new HumanEntity[0]));
    }

    public void close(HumanEntity... players) {
        for (HumanEntity player : players) {
            player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
        }
    }

    public void openPrevious(HumanEntity... players) {
        for (HumanEntity player : players) {
            close(player);
        }
        if (previousGUI != null) {
            getPreviousGUI().open(players);
        }
    }

    public Inventory getInventory() {
        return inv;
    }

    public GUI getPreviousGUI() {
        return previousGUI;
    }

    public boolean equals(Inventory other) {
        return (this.inv != null && inv.equals(other));
    }

    private static ItemStack previousButton = new ItemBuilder(Material.PAPER)
            .setName("&cMenu précédent")
            .setCustomModelData(1)
            .build();
    /**
     * Can produce indexOutOfBoundsException if theslot is not valid
     * @param slot to format
     */
    protected void setPreviousButton(int slot) {
        inv.setItem(slot, previousButton);
    }

    protected void setItem(ItemStack item, int... slots) {
        for (int slot : slots) {
            inv.setItem(slot, item);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (equals(event.getInventory())) {
            Scheduling.syncDelay(1, () -> {
                if (inv.getViewers().size() == 0) {
                    Main.unregisterEvents(this);
                }
            });
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!equals(event.getInventory())) return;
        event.setCancelled(true);
        if (previousButton.isSimilar(event.getCurrentItem())) {
            openPrevious(event.getWhoClicked());
        }
        onClick(event);
    }

    @EventHandler
    public void onGUIOpen(InventoryCloseEvent event) {
        if (event.getInventory().equals(this.inv)) {
            onOpen(event);
        }
    }

    @EventHandler
    public void onGUIClose(InventoryCloseEvent event) {
        if (event.getInventory().equals(this.inv)) {
            onClose(event);
        }
    }

    protected void onOpen(InventoryCloseEvent event) {}

    protected void onClose(InventoryCloseEvent event) {}

    protected void onClick(InventoryClickEvent event) {}

}
