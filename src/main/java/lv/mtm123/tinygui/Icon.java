package lv.mtm123.tinygui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Icon {

    private final ItemStack item;
    private final ClickAction action;


    public Icon(ItemStack item, ClickAction action) {
        this.item = item;
        this.action = action;
    }

    public void click(Player player, int slot) {
        action.click(player, slot);
    }

    public ItemStack getItem() {
        return item;
    }

}
