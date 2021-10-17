package com.black_dog20.warpradial.common.util;

import com.black_dog20.warpradial.Config;
import com.black_dog20.warpradial.WarpRadial;
import com.black_dog20.warpradial.common.util.data.Permission;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;

public class PermissionHelper {

    public static boolean canCreateOrDelete(CommandSourceStack source) {
        return canCreate(source) || canDelete(source);
    }

    public static boolean canCreate(CommandSourceStack source) {
        try {
            return DataManager.playerHasPermission(source.getPlayerOrException(), Permission.CAN_CREATE_SERVER_WARPS);
        } catch (CommandSyntaxException e) {
            return false;
        }
    }

    public static boolean canDelete(CommandSourceStack source) {
        try {
            return DataManager.playerHasPermission(source.getPlayerOrException(), Permission.CAN_DELETE_SERVER_WARPS);
        } catch (CommandSyntaxException e) {
            return false;
        }
    }

    public static boolean onlyOpsRuleNotActiveOrCanUse(CommandSourceStack source) {
        if (!Config.ONLY_PERMISSION_PLAYERS_CAN_USE_MENU.get())
            return true;
        try {
            return source.hasPermission(2) || WarpRadial.Proxy.isSinglePlayer() || DataManager.playerHasPermission(source.getPlayerOrException(), Permission.CAN_USE_MENU);
        } catch (CommandSyntaxException e) {
            return false;
        }
    }
}
