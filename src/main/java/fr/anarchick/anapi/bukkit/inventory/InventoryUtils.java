package fr.anarchick.anapi.bukkit.inventory;

import fr.anarchick.anapi.bukkit.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public class InventoryUtils {

    public static Inventory createInventory(InventoryHolder owner, int size, String name) {
        return Bukkit.createInventory(owner, size, BukkitUtils.colored(name));
    }

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

    /**
     * Transfert some items from an inventory into an other if it contains similar items's types
     * @param from the inventory where we take items
     * @param to the inventory where we drop them
     */
    public static void transfert(@Nonnull Inventory from, @Nonnull Inventory to) {
        // slots of fromInventory similar with toInventory
        Set<Integer> slots = new HashSet<>();
        for (ItemStack item : to.getContents()) {
            if (item == null || item.getType().isAir()) continue;
            if (from.containsAtLeast(item, 1)) {
                Map<Integer, ? extends ItemStack> check = from.all(item.getType());
                for (Integer slot : check.keySet()) {
                    if (item.isSimilar(check.get(slot))) {
                        slots.add(slot);
                    }
                }
            }
        }
        InventoryMoveItemsEvent event = new InventoryMoveItemsEvent(from, slots, to);
        if (!event.callEvent()) return;
        ItemStack AIR = new ItemStack(Material.AIR);
        for (Integer slot : event.getSlots()) {
            ItemStack item = from.getItem(slot);
            Map<Integer, ItemStack> overload = to.addItem(item);
            ItemStack replace = AIR;
            if (!overload.isEmpty()) {
                replace = overload.values().toArray(new ItemStack[0])[0];
            }
            from.setItem(slot, replace);
        }
    }

    public static class InventoryMoveItemsEvent extends Event implements Cancellable {

        private static final HandlerList handlers = new HandlerList();
        private boolean cancelled;
        private final Inventory sourceInventory;
        private final Inventory destinationInventory;
        private Set<Integer> slots;

        public InventoryMoveItemsEvent(@NotNull final Inventory sourceInventory, @NotNull final Set<Integer> slots, @NotNull final Inventory destinationInventory) {
            this.sourceInventory = sourceInventory;
            this.slots = slots;
            this.destinationInventory = destinationInventory;
        }

        /**
         * Gets the Inventory that the ItemStack is being taken from
         *
         * @return Inventory that the ItemStack is being taken from
         */
        @NotNull
        public Inventory getSource() {
            return sourceInventory;
        }

        /**
         * Gets the Inventory that the ItemStack is being put into
         *
         * @return Inventory that the ItemStack is being put into
         */
        @NotNull
        public Inventory getDestination() {
            return destinationInventory;
        }

        /**
         * Gets the slots of the source inventory to transfer
         * @return
         */
        @NotNull
        public Set<Integer> getSlots() {
            return new HashSet<>(slots);
        }

        public void setSlots(@Nonnull final Set<Integer> slots) {
            this.slots = slots;
        }

        @NotNull
        @Override
        public HandlerList getHandlers() {
            return handlers;
        }

        @NotNull
        public static HandlerList getHandlerList() {
            return handlers;
        }

        @Override
        public boolean isCancelled() {
            return cancelled;
        }

        @Override
        public void setCancelled(boolean cancel) {
            this.cancelled = cancel;
        }

    }

}
