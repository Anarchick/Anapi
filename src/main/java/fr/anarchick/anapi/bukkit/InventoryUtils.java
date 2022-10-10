package fr.anarchick.anapi.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class InventoryUtils {

    public static Integer getAmount(@Nonnull Inventory inv, @Nonnull Material material) {
        int amount = 0;
        for (ItemStack item : inv.all(material).values()) {
            amount += item.getAmount();
        }
        return amount;
    }

    public static Integer getAmount(@Nonnull Inventory inv, @Nonnull ItemStack itemStack) {
        int amount = 0;
        for (ItemStack item : inv.all(itemStack).values()) {
            amount += item.getAmount();
        }
        return amount;
    }

    public static void dropInventory(@Nonnull Inventory inv, @Nonnull Location loc, boolean naturally) {
        World world = loc.getWorld();
        for (ItemStack item : inv.getContents()) {
            if (item != null) {
                if (naturally) {
                    world.dropItemNaturally(loc, item);
                } else {
                    world.dropItem(loc, item);
                }

            }
        }
    }

    public static boolean hasSpace(@Nonnull Inventory inventory , ItemStack... items) {
        Inventory inv = Bukkit.createInventory(null, inventory.getSize() + 1);
        inv.setContents(inventory.getContents());
        return inv.addItem(items).isEmpty();
    }

}
