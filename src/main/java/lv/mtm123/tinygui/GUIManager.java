package lv.mtm123.tinygui;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import lv.mtm123.tinygui.listeners.PacketListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class GUIManager<I> {

    private final ProtocolManager pm;

    private final Map<I, GUI> guis;
    private final Map<UUID, GUIInfo> openedGui;

    private int windowId;

    public GUIManager(Plugin plugin, ProtocolManager pm) {
        this.pm = pm;
        this.guis = new HashMap<>();
        this.openedGui = new HashMap<>();

        this.windowId = hashCode();

        pm.addPacketListener(new PacketListener(this, plugin));
    }

    public void register(I id, GUI gui) {
        guis.put(id, gui);
    }

    public void unregister(I id) {

        openedGui.entrySet().stream().filter(e -> e.getValue().equals(id))
                .map(Map.Entry::getKey).collect(Collectors.toList()).forEach(k -> {
                    openedGui.remove(k);
                    Player player = Bukkit.getPlayer(k);
                    if(player != null) {
                        sendWindowClose(player, 0);
                    }
        });

        guis.remove(id);
    }

    public Optional<GUI> getGUI(I id) {
        return Optional.ofNullable(guis.get(id));
    }

    public void handleClick(Player player, int slot, short action) {
        GUIInfo info = openedGui.get(player.getUniqueId());

        if(info == null){
            return;
        }

        if(guis.get(info.id).handleClick(player, slot)) {
            sendTransaction(player, info.windowId, action, false);
        }
    }

    public void openGUI(Player player, I id) {
        GUI gui = guis.get(id);
        if(gui == null) {
            return;
        }

        System.out.println("__windowId: " + windowId);

        sendWindowOpen(player, windowId, gui.getType(), gui.getTitle(), gui.getSize());
        sendWindowItems(player, windowId, gui.getItems());

        openedGui.put(player.getUniqueId(), new GUIInfo(id, windowId));

        windowId++;
    }

    public void sendGUISlotUpdate(Player player, int slot, ItemStack itemStack) {
        GUIInfo info = openedGui.get(player.getUniqueId());
        if(info == null){
            return;
        }

        sendGUISlotUpdate(player, info.windowId, itemStack);
    }

    public void handleWindowClose(Player player) {
        openedGui.remove(player.getUniqueId());
    }

    public void sendSlot(Player player, int windowId, int slot, ItemStack item) {
        PacketContainer sendSlot = pm.createPacket(PacketType.Play.Server.SET_SLOT);
        sendSlot.getIntegers().write(0, windowId);
        sendSlot.getIntegers().write(1, slot);
        sendSlot.getItemModifier().write(0, item);

        sendPacket(player, sendSlot);
    }

    public void sendWindowClose(Player player, int windowId) {
        PacketContainer sendWindowClose = pm.createPacket(PacketType.Play.Server.CLOSE_WINDOW);
        sendWindowClose.getIntegers().write(0, windowId);

        sendPacket(player, sendWindowClose);
    }

    public void sendWindowOpen(Player player, int windowId, InventoryType type, String title, int slots) {

        PacketContainer sendWindowOpen = pm.createPacket(PacketType.Play.Server.OPEN_WINDOW);
        sendWindowOpen.getIntegers().write(0, windowId);
        sendWindowOpen.getStrings().write(0, "minecraft:" + type.name().toLowerCase());
        sendWindowOpen.getChatComponents().write(0, WrappedChatComponent.fromText(title));
        sendWindowOpen.getIntegers().write(1, slots);

        sendPacket(player, sendWindowOpen);
    }

    public void sendWindowItems(Player player, int windowId, ItemStack[] items) {
        PacketContainer sendItems = pm.createPacket(PacketType.Play.Server.WINDOW_ITEMS);
        sendItems.getIntegers().write(0, windowId);
        sendItems.getItemArrayModifier().write(0, items);

        sendPacket(player, sendItems);
    }

    public void sendTransaction(Player player, int windowId, short action, boolean accepted) {
        PacketContainer sendTransaction = pm.createPacket(PacketType.Play.Server.TRANSACTION);
        sendTransaction.getIntegers().write(0, windowId);
        sendTransaction.getShorts().write(0, action);
        sendTransaction.getBooleans().write(0, accepted);

        sendPacket(player, sendTransaction);
    }

    public void sendPacket(Player player, PacketContainer packet) {

        try {
            pm.sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    private class GUIInfo {

        private final I id;
        private final int windowId;


        private GUIInfo(I id, int windowId) {
            this.id = id;
            this.windowId = windowId;
        }

        public I getId() {
            return id;
        }

        public int getWindowId() {
            return windowId;
        }

    }

}
