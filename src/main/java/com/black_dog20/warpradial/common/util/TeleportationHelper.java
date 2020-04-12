package com.black_dog20.warpradial.common.util;

import com.black_dog20.warpradial.Config;
import com.black_dog20.warpradial.common.network.PacketHandler;
import com.black_dog20.warpradial.common.network.packets.PacketSyncPlayerFuel;
import com.black_dog20.warpradial.common.util.WarpPlayerProperties.Cooldown;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.*;

public class TeleportationHelper {

    public static boolean canTeleport(PlayerEntity player, Cooldown cooldown) {
        if(WarpPlayerProperties.isOnGlobalCooldown(player, cooldown)){
            long remainingSecs = WarpPlayerProperties.getRemainingCooldown(player, Cooldown.GLOBAL);
            player.sendMessage(COOLDOWN_MSG.getComponent());
            player.sendMessage(COOLDOWN_MSG_2.getComponent(remainingSecs));
            return false;
        }
        if(WarpPlayerProperties.isOnCooldown(player, cooldown)){
            long remainingSecs = WarpPlayerProperties.getRemainingCooldown(player, cooldown);
            player.sendMessage(COOLDOWN_MSG.getComponent());
            player.sendMessage(COOLDOWN_MSG_2.getComponent(remainingSecs));
            return false;
        }

        if(!WarpPlayerProperties.hasFuelEnough(player, cooldown) && !Config.COOLDOWN_ONLY_WHEN_NO_FUEL.get()) {
            double fuel = WarpPlayerProperties.getFuel(player);
            player.sendMessage(FUEL_MSG.getComponent());
            player.sendMessage(FUEL_MSG_2.getComponent(fuel));
            return false;
        }
        return true;
    }

    public static void handleFuel(ServerPlayerEntity player, Cooldown cooldown) {
        if(cooldown.canWarpWithoutFuel())
            return;
        double fuel = WarpPlayerProperties.getFuel(player);
        if(fuel-1.0 >= 0.0) {
            WarpPlayerProperties.setFuel(player,fuel-1.0);
            PacketHandler.sendTo(new PacketSyncPlayerFuel(player), player);
        }
    }

    public static void handleCooldown(ServerPlayerEntity player, Cooldown cooldown) {
        if(Config.COOLDOWN_ONLY_WHEN_NO_FUEL.get() && WarpPlayerProperties.hasFuelEnough(player, cooldown))
            return;

        WarpPlayerProperties.setCooldown(player, cooldown);
    }
}
