package lv.mtm123.tinygui.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import lv.mtm123.tinygui.GUIManager;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;

public class PacketListener extends PacketAdapter {

    private final GUIManager guiManager;

    public PacketListener(GUIManager guiManager, Plugin plugin) {
        super(plugin, PacketType.Play.Client.WINDOW_CLICK, PacketType.Play.Client.CLOSE_WINDOW, PacketType.Play.Server.TRANSACTION);
        this.guiManager = guiManager;
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {

/*        if(event.isAsync()) {
            System.out.println("yes");
            return;
        }*/

        if(event.getPacketType() == PacketType.Play.Client.WINDOW_CLICK) {

            int windowId = event.getPacket().getIntegers().read(0);
            System.out.println("windowId: " + windowId);

            int slot = event.getPacket().getIntegers().read(1);
            System.out.println("slot: " + slot);

            int button = event.getPacket().getIntegers().read(2);
            System.out.println("button: " + button);

            //int slot = event.getPacket().getIntegers().getValues().forEach(System.out::println);
            short action = event.getPacket().getShorts().read(0);
            System.out.println("action: " + action);

            System.out.println(event.getPacket().getItemModifier().read(0));

            int shift = event.getPacket().getIntegers().read(3);
            System.out.println("shift: " + shift);

            switch (shift) {
                case 0:


            }

            guiManager.handleClick(event.getPlayer(), slot, action);

            PacketContainer sendTransaction = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.TRANSACTION);
            sendTransaction.getIntegers().write(0, windowId);
            sendTransaction.getShorts().write(0, action);
            sendTransaction.getBooleans().write(0, false);

            event.getPacket().getItemSlots().getValues().forEach(s -> System.out.println(s.toString()));

            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(event.getPlayer(), sendTransaction);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }



//            PacketContainer resetCursor = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SET_SLOT);
//            resetCursor.getIntegers().write(0, -1);
//            resetCursor.getIntegers().write(1, -1);
//            resetCursor.getItemModifier().write(0, null);
//
//
//
//            PacketContainer resetItem = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SET_SLOT);
//
//            int slot = event.getPacket().getIntegers().getValues().get(1);
//            ItemStack it;
//            switch (slot) {
//                case 0:
//                    it = new ItemStack(Material.ANVIL, 1);
//                    break;
//                case 1:
//                    it = new ItemStack(Material.TNT, 25);
//                    break;
//                case 2:
//                    it = new ItemStack(Material.GOLD_CHESTPLATE, 5);
//                    break;
//
//                    default:
//                        it = new ItemStack(Material.GOLD_BLOCK);
//            }
//
//            resetItem.getIntegers().write(0, 1);
//            resetItem.getIntegers().write(1, slot);
//            resetItem.getItemModifier().write(0, it);
//
//            PacketContainer transaction = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.TRANSACTION);
//            transaction.getIntegers().write(0, 1);
//            transaction.getShorts().write(0, (short) 66);
//            transaction.getBooleans().write(0, false);
//
//            try {
//                ProtocolLibrary.getProtocolManager().sendServerPacket(event.getPlayer(), resetCursor);
//                ProtocolLibrary.getProtocolManager().sendServerPacket(event.getPlayer(), resetItem);
//                ProtocolLibrary.getProtocolManager().sendServerPacket(event.getPlayer(), transaction);
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            }
        } else if(event.getPacketType() == PacketType.Play.Client.CLOSE_WINDOW) {
            guiManager.handleWindowClose(event.getPlayer());
        } else if(event.getPacketType() == PacketType.Play.Client.TRANSACTION) {
            event.getPacket().getIntegers().getValues().forEach(System.out::println);
            event.getPacket().getShorts().getValues().forEach(System.out::println);
        }

    }

    @Override
    public void onPacketSending(PacketEvent event) {

/*        if(event.getPacketType() == PacketType.Play.Server.TRANSACTION) {
            System.out.println("transaction");
            event.getPacket().getShorts().getValues().forEach(System.out::println);
        }*/

    }
}
