package lv.mtm123.tinygui.util;

import org.bukkit.Bukkit;

public final class PlayerUtil {

    private static Class<?> NMS_PLAYER;
    private static Class<?> CRAFT_PLAYER;
    //private static Field

    static {
        String pckg = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        try {
            NMS_PLAYER = Class.forName("net.minecraft.server." + pckg + ".EntityPlayer");
            CRAFT_PLAYER = Class.forName("org.bukkit.craftbukkit." + pckg + ".entity.CraftPlayer");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

/*
    public static int incrementContainerCounter(Player player) {



    }

    public static int getContainerCounter(Player player) {

    }

    public static Object getNMSPlayer(Player player) {
        if(NMS_PLAYER == null || CRAFT_PLAYER == null) {
            return null;
        }

        CRAFT_PLAYER.cast(player);
    }
*/

}
