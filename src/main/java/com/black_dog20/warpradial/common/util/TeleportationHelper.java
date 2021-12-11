package com.black_dog20.warpradial.common.util;

import com.black_dog20.warpradial.Config;
import com.black_dog20.warpradial.common.util.WarpPlayerProperties.Cooldown;
import com.black_dog20.warpradial.common.util.data.Permission;
import net.minecraft.Util;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.server.ServerLifecycleHooks;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.*;

public class TeleportationHelper {

    public static boolean canTeleport(Player player, Cooldown cooldown) {
        if (WarpPlayerProperties.isOnGlobalCooldown(player, cooldown)) {
            long remainingSecs = WarpPlayerProperties.getRemainingCooldown(player, Cooldown.GLOBAL);
            player.sendMessage(COOLDOWN_MSG.get(), Util.NIL_UUID);
            player.sendMessage(COOLDOWN_MSG_2.get(remainingSecs), Util.NIL_UUID);
            return false;
        }
        if (WarpPlayerProperties.isOnCooldown(player, cooldown)) {
            long remainingSecs = WarpPlayerProperties.getRemainingCooldown(player, cooldown);
            player.sendMessage(COOLDOWN_MSG.get(), Util.NIL_UUID);
            player.sendMessage(COOLDOWN_MSG_2.get(remainingSecs), Util.NIL_UUID);
            return false;
        }
        return true;
    }

    public static void handleCooldown(ServerPlayer player, Cooldown cooldown) {
        WarpPlayerProperties.setCooldown(player, cooldown);
    }

    public static boolean checkPermission(ServerPlayer player) {
        if (!Config.ONLY_PERMISSION_PLAYERS_CAN_USE_MENU.get())
            return true;
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        int level = server.getProfilePermissions(player.getGameProfile());
        if (DataManager.playerHasPermission(player, Permission.CAN_USE_MENU) || level >= Config.USE_MENU_PERMISSION_LEVEL.get()) {
            return true;
        }
        player.sendMessage(COULD_NOT_TELEPORT_INSUFFICIENT_PERMISSION.get(), Util.NIL_UUID);
        return false;
    }

    public static boolean checkServerWarpPermission(ServerPlayer player) {
        if (!Config.ONLY_PERMISSION_PLAYERS_CAN_USE_SERVER_WARPS.get())
            return true;
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        int level = server.getProfilePermissions(player.getGameProfile());
        if (DataManager.playerHasPermission(player, Permission.CAN_USE_SERVER_WARP) || level >= Config.USE_SERVER_WARP_PERMISSION_LEVEL.get()) {
            return true;
        }
        player.sendMessage(COULD_NOT_TELEPORT_INSUFFICIENT_PERMISSION.get(), Util.NIL_UUID);
        return false;
    }
}
