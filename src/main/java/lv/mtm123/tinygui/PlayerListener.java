package lv.mtm123.tinygui;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class PlayerListener implements Listener {

    private final ProtocolManager pm;
    public PlayerListener(ProtocolManager pm){
        this.pm = pm;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {

        if(true) {
            PacketContainer changeSky = pm.createPacket(PacketType.Play.Server.GAME_STATE_CHANGE);
            changeSky.getIntegers().write(0, 7);
            changeSky.getFloat().write(0, Float.parseFloat(event.getMessage().replace("/", "")));

            System.out.println(event.getMessage());

            try {
                pm.sendServerPacket(event.getPlayer(), changeSky);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return;
        }


        PacketContainer openWindow = pm.createPacket(PacketType.Play.Server.OPEN_WINDOW);
        openWindow.getIntegers().write(0, 1);
        openWindow.getStrings().write(0, "minecraft:chest");
        openWindow.getChatComponents().write(0, WrappedChatComponent.fromText(ChatColor.BLUE + "Test"));
        openWindow.getIntegers().write(1, 9);

        try {
            pm.sendServerPacket(event.getPlayer(), openWindow);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        PacketContainer sendItems = pm.createPacket(PacketType.Play.Server.WINDOW_ITEMS);
        sendItems.getIntegers().write(0, 1);


        List<ItemStack> items = new ArrayList<>(9);

        ItemStack it = new ItemStack(Material.ANVIL, 1);
        items.add(it);

        ItemStack it2 = new ItemStack(Material.TNT, 25);
        items.add(it2);

        ItemStack it3 = new ItemStack(Material.GOLD_CHESTPLATE, 5);
        items.add(it3);

        sendItems.getItemArrayModifier().write(0, items.toArray(new ItemStack[0]));
        //sendItems.getItemListModifier().write(1, items);

        try {
            pm.sendServerPacket(event.getPlayer(), sendItems);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

}
