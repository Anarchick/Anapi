package fr.anarchick.anapi.bukkit.inventory;

import fr.anarchick.anapi.bukkit.MiniMessage;
import fr.anarchick.anapi.bukkit.Scheduling;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

@SuppressWarnings("unused")
public abstract class GUI implements InventoryHolder {

    @NotNull
    private final Inventory inv;
    public boolean disableInteraction = true;
    public boolean preventPlayerClose = false;
    @Nullable
    public GUI previousGUI = null;
    protected final HashMap<Integer, ItemGUI> ItemGUI_MAP = new HashMap<>();

    protected GUI(int size, @NotNull @MiniMessage String title) {
        this.inv = InventoryUtils.createInventory(this, size, title);
    }

    protected GUI(@NotNull InventoryType type, @NotNull @MiniMessage String title) {
        this.inv = InventoryUtils.createInventory(this, type, title);
    }

    abstract public void refresh();

    public void open(HumanEntity... players) {
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
        Scheduling.syncDelay(1, () -> {
            close(players);
            if (previousGUI != null) previousGUI.open(players);
        });
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }

    @Nullable
    public GUI getPreviousGUI() {
        return previousGUI;
    }

    public boolean equals(Inventory other) {
        return (inv.equals(other));
    }

    private final ItemGUI previousButton = new ItemGUI(new ItemBuilder(Material.PAPER)
            .setName("<red>Menu précédent")
            .setCustomModelData(1)
            .build()) {
        @Override
        public void onAnyClick(Player player, InventoryClickEvent event) {
            openPrevious(player);
        }
    };

    protected void update(@NotNull ItemGUI itemGUI) {
        ItemStack item = itemGUI.getItem();
        ItemGUI_MAP.forEach((key, value) -> {
            if (value == itemGUI) inv.setItem(key, item);
        });
    }

    /**
     * Can produce indexOutOfBoundsException if theslot is not valid
     * @param slot to format
     */
    protected void setPreviousButton(int slot) {
        setItem(previousButton, slot);
    }

    protected void setItem(@Nullable ItemStack item, int... slots) {
        for (int slot : slots) {
            inv.setItem(slot, item);
        }
    }

    protected void setItem(@NotNull ItemGUI itemGUI, int... slots) {
        for (int slot : slots) {
            ItemGUI_MAP.put(slot, itemGUI);
        }
        setItem(itemGUI.getItem(), slots);
    }

    protected @NotNull HashMap<Integer, ItemStack> addItem(@NotNull ItemStack... items) {
        return inv.addItem(items);
    }

    protected void onOpen(InventoryOpenEvent event) {}

    protected void onClose(InventoryCloseEvent event) {}

    protected void onClick(InventoryClickEvent event) {}

    protected void onDrag(InventoryDragEvent event) {}

}
