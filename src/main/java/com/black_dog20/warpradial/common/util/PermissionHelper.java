package com.black_dog20.warpradial.common.util;

import com.black_dog20.warpradial.Config;
import com.black_dog20.warpradial.WarpRadial;
import com.black_dog20.warpradial.common.util.data.Permission;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;

public class PermissionHelper {

    public static boolean canCreateOrDelete(CommandSource source) {
        return canCreate(source) || canDelete(source);
    }

    public static boolean canCreate(CommandSource source) {
        try {
            return DataManager.playerHasPermission(source.asPlayer(), Permission.CAN_CREATE_SERVER_WARPS);
        } catch (CommandSyntaxException e) {
            return false;
        }
    }

    public static boolean canDelete(CommandSource source) {
        try {
            return DataManager.playerHasPermission(source.asPlayer(), Permission.CAN_DELETE_SERVER_WARPS);
        } catch (CommandSyntaxException e) {
            return false;
        }
    }

    public static boolean onlyOpsRuleNotActiveOrCanUse(CommandSource source) {
        if (!Config.ONLY_PERMISSION_PLAYERS_CAN_USE_MENU.get())
            return true;
        try {
            return source.hasPermissionLevel(2) || WarpRadial.Proxy.isSinglePlayer() || DataManager.playerHasPermission(source.asPlayer(), Permission.CAN_USE_MENU);
        } catch (CommandSyntaxException e) {
            return false;
        }
    }
}
