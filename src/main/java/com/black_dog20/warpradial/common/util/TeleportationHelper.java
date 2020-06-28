package com.black_dog20.warpradial.common.util;

import com.black_dog20.warpradial.common.util.WarpPlayerProperties.Cooldown;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.*;

public class TeleportationHelper {

    public static boolean canTeleport(PlayerEntity player, Cooldown cooldown) {
        if (WarpPlayerProperties.isOnGlobalCooldown(player, cooldown)) {
            long remainingSecs = WarpPlayerProperties.getRemainingCooldown(player, Cooldown.GLOBAL);
            player.sendMessage(COOLDOWN_MSG.getComponent());
            player.sendMessage(COOLDOWN_MSG_2.getComponent(remainingSecs));
            return false;
        }
        if (WarpPlayerProperties.isOnCooldown(player, cooldown)) {
            long remainingSecs = WarpPlayerProperties.getRemainingCooldown(player, cooldown);
            player.sendMessage(COOLDOWN_MSG.getComponent());
            player.sendMessage(COOLDOWN_MSG_2.getComponent(remainingSecs));
            return false;
        }
        return true;
    }

    public static void handleCooldown(ServerPlayerEntity player, Cooldown cooldown) {
        WarpPlayerProperties.setCooldown(player, cooldown);
    }
}
