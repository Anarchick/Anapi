package fr.anarchick.anapi.bukkit.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class ItemGUI {

    public static final ItemGUI EMPTY = new ItemGUI(null);

    private ItemStack item;
    public boolean ignoreCancelledEvent = true;

    public ItemGUI(ItemStack item) {
        this.item = item;
    }

    public void setItem(@Nullable ItemStack item, @Nullable GUI gui) {
        this.item = item;
        if (gui != null) gui.update(this);
    }

    @Nullable
    public ItemStack getItem() {
        return item;
    }

    public void onAnyClick(Player player, InventoryClickEvent event) {}
    public void onLeftClick(Player player, InventoryClickEvent event) {}
    public void onRightClick(Player player, InventoryClickEvent event) {}

}
