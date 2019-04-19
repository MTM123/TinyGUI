package lv.mtm123.tinygui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.HashMap;
import java.util.Map;

public class GUI {

    private final String title;
    private int size;
    private InventoryType type;
    private final Map<Integer, Icon> icons;


    public GUI(String title, InventoryType type, int size) {
        this.title = title;
        this.type = isValidType(type) ? type : InventoryType.CHEST;
        this.size = hasValidSize(type, size) ? size : toValidSize(type, size);
        icons = new HashMap<>();
    }

    public void setIcon(int slot, Icon icon) {
        this.icons.put(slot, icon);
    }

    public Icon getIcon(int slot) {
        return icons.get(slot);
    }

    public void openInventory(Player player) {

    }

    public void handleClick(Player player, int slot) {
        if (!icons.containsKey(slot)) {
            return;
        }

        icons.get(slot).click(player, slot);
    }

    public void fillEmpty(Icon icon) {
        for (int i = 0; i < this.size; ++i) {
            if (!icons.containsKey(i)) {
                setIcon(i, icon);
            }
        }
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
