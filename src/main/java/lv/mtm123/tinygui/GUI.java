package lv.mtm123.tinygui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class GUI {

    private final String title;
    private int size;
    private InventoryType type;
    private final Map<Integer, Icon> icons;

    private ItemStack[] cachedItems = null;

    public GUI(String title, InventoryType type, int size) {
        this.title = title;
        this.type = isValidType(type) ? type : InventoryType.CHEST;
        this.size = hasValidSize(type, size) ? size : toValidSize(type, size);
        icons = new HashMap<>();
    }

    public void setIcon(int slot, Icon icon) {
        if(slot >= size || slot < 0){
            throw new IndexOutOfBoundsException(String.format("Slot out of bounds! Expected: min: 0, max: %d, received: %d", size - 1, slot));
        }

        icons.put(slot, icon);
    }

    public Icon getIcon(int slot) {
        return icons.get(slot);
    }

    public ItemStack[] getItems() {

        if(cachedItems == null){
            cachedItems = new ItemStack[size];
            icons.forEach((k,v) -> cachedItems[k] = v.getItem());
        }

        return cachedItems;

    }

    public boolean handleClick(Player player, int slot) {
        if (!icons.containsKey(slot)) {
            return false;
        }

        icons.get(slot).click(player, slot);
        return true;
    }

    public void fillEmpty(Icon icon) {
        for (int i = 0; i < this.size; ++i) {
            if (!icons.containsKey(i)) {
                setIcon(i, icon);
            }
        }
    }

    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }

    public InventoryType getType() {
        return type;
    }

    public void handleClose(Player player) {
        //TODO: Handle closing of inventories
    }


    public static boolean hasValidSize(InventoryType type, int size) {

        if (type == InventoryType.CHEST) {
            return size % 9 == 0 && size <= 54;
        }

        return size == type.getDefaultSize();

    }

    public static int toValidSize(InventoryType type, int size) {
        if(type == InventoryType.CHEST) {
            return size <= 54 ? size : (int) (9 * Math.round(size / 9d));
        } else {
            return type.getDefaultSize();
        }
    }

    public static boolean isValidType(InventoryType type) {

        //TODO: Look into making this shorter?

        switch (type) {
            case CHEST:
            case ANVIL:
            case BEACON:
            case HOPPER:
            case BREWING:
            case FURNACE:
            case DISPENSER:
            case DROPPER:
            case ENCHANTING:
            case WORKBENCH:
            case MERCHANT:
                return true;

                default:
                    return false;
        }

    }

}
