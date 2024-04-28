package fr.anarchick.anapi.bukkit.customItem;

import fr.anarchick.anapi.bukkit.Scheduling;
import fr.anarchick.anapi.bukkit.inventory.GUI;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("unused")
public class CustomItemGUI extends GUI {

    public static final CustomItemGUI GUI = new CustomItemGUI();

    private CustomItemGUI() {
        super(9*4, "<#9C6DDB>CustomItems");
    }

    @Override
    protected void onOpen(InventoryOpenEvent event) {
        refresh();
    }

     @Override
     public void refresh() {
        getInventory().clear();

        for (CustomItem customItem : CustomItemManager.getAll()) {
            addItem(CustomItemManager.getItem(customItem, 1));
        }
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        final Inventory inv = getInventory();

        if (inv.equals(event.getInventory())) {
            event.setCancelled(true);
        }

        if (!inv.equals(event.getClickedInventory())) {
            return;
        }

        final ItemStack item = event.getCurrentItem();
        event.setCancelled(true);

        if (item != null) {
            event.getWhoClicked().getInventory().addItem(item);
        }
    }


}
