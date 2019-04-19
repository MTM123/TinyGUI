package lv.mtm123.tinygui;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;

public class PacketListener extends PacketAdapter {

    public PacketListener(Plugin plugin) {
        super(plugin, PacketType.Play.Client.WINDOW_CLICK, PacketType.Play.Client.CLOSE_WINDOW, PacketType.Play.Server.TRANSACTION);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {

        if(event.getPacketType() == PacketType.Play.Client.WINDOW_CLICK) {
            System.out.println("Click");





            PacketContainer resetCursor = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SET_SLOT);
            resetCursor.getIntegers().write(0, -1);
            resetCursor.getIntegers().write(1, -1);
            resetCursor.getItemModifier().write(0, null);



            PacketContainer resetItem = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SET_SLOT);

            int slot = event.getPacket().getIntegers().getValues().get(1);
            ItemStack it;
            switch (slot) {
                case 0:
                    it = new ItemStack(Material.ANVIL, 1);
                    break;
                case 1:
                    it = new ItemStack(Material.TNT, 25);
                    break;
                case 2:
                    it = new ItemStack(Material.GOLD_CHESTPLATE, 5);
                    break;

                    default:
                        it = new ItemStack(Material.GOLD_BLOCK);
            }

            resetItem.getIntegers().write(0, 1);
            resetItem.getIntegers().write(1, slot);
            resetItem.getItemModifier().write(0, it);

            PacketContainer transaction = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.TRANSACTION);
            transaction.getIntegers().write(0, 1);
            transaction.getShorts().write(0, (short) 66);
            transaction.getBooleans().write(0, false);

            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(event.getPlayer(), resetCursor);
                ProtocolLibrary.getProtocolManager().sendServerPacket(event.getPlayer(), resetItem);
                ProtocolLibrary.getProtocolManager().sendServerPacket(event.getPlayer(), transaction);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else if(event.getPacketType() == PacketType.Play.Client.CLOSE_WINDOW) {
            System.out.println("Close");
        }


    }

    @Override
    public void onPacketSending(PacketEvent event) {

        if(event.getPacketType() == PacketType.Play.Server.TRANSACTION) {
            System.out.println("transaction");
            event.getPacket().getShorts().getValues().forEach(System.out::println);
        }

    }
}
