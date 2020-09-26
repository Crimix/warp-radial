package com.black_dog20.warpradial.common.util;

import com.black_dog20.warpradial.Config;
import com.black_dog20.warpradial.WarpRadial;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;

public class PermissionHelper {

    public static boolean canCreateOrDelete(CommandSource source) {
        return canCreate(source) || canDelete(source);
    }

    public static boolean canCreate(CommandSource source) {
        try {
            return DataManager.getPlayerPermission(source.asPlayer()).canCreateServerWarps();
        } catch (CommandSyntaxException e) {
            return false;
        }
    }

    public static boolean canDelete(CommandSource source) {
        try {
            return DataManager.getPlayerPermission(source.asPlayer()).canDeleteServerWarps();
        } catch (CommandSyntaxException e) {
            return false;
        }
    }

    public static boolean onlyOpsRuleNotActiveOrCanUse(CommandSource source) {
        if (!Config.ONLY_PERMISSION_PLAYERS_CAN_USE_MENU.get())
            return true;
        try {
            return source.hasPermissionLevel(2) || WarpRadial.Proxy.isSinglePlayer() || DataManager.getPlayerPermission(source.asPlayer()).canUseMenu();
        } catch (CommandSyntaxException e) {
            return false;
        }
    }
}
