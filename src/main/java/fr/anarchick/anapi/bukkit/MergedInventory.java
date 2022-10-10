package fr.anarchick.anapi.bukkit;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MergedInventory {

    private final List<Inventory> inventories;
    private final Map<Material, Integer> items = new HashMap<>();
    private final Map<Material, Integer> modifications = new HashMap<>();

    public MergedInventory(Inventory... inventories) {
        this(List.of(inventories));
    }

    public MergedInventory(List<Inventory> inventories) {
        this.inventories = inventories;
        for (Inventory inventory : inventories) {
            for (ItemStack item : inventory.getStorageContents()) {
                if (item != null) {
                    int amount = item.getAmount();
                    Material type = item.getType();
                    store(type, amountOf(type) + amount);
                }
            }
        }
    }

    private void store(Material type, int amount) {
        items.put(type, amount);
    }

    public int amountOf(Material type) {
        return items.getOrDefault(type, 0);
    }

    public boolean has(Material type, int amount) {
        return (amountOf(type) >= amount);
    }

    public boolean remove(ItemStack item, int amount) {
        Material type = item.getType();
        if (!has(type, amount)) return false;
        store(type, amountOf(type) - amount);
        int modificationsAmount = modifications.getOrDefault(type, 0);
        modifications.put(type, modificationsAmount - amount);
        return true;
    }

    /**
     * Apply changes to real inventories
     */
    public void apply() {
        for (Map.Entry<Material, Integer> entry : modifications.entrySet()) {
            Material type = entry.getKey();
            ItemStack item = new ItemStack(type);
            int amount = Math.abs(entry.getValue());
            for (Inventory inv : this.inventories) {
                if (inv.containsAtLeast(item, amount)) {
                    inv.removeItemAnySlot(item.asQuantity(amount));
                    break;
                } else while (amount > 0 && inv.containsAtLeast(item, 1)) {
                        inv.removeItemAnySlot(item);
                        amount--;
                        if (amount == 0) break;
                }
            }
        }
        modifications.clear();
    }

}
