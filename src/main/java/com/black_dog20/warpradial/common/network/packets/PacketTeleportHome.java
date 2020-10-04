package com.black_dog20.warpradial.common.network.packets;

import com.black_dog20.bml.utils.player.TeleportationUtil;
import com.black_dog20.warpradial.Config;
import com.black_dog20.warpradial.common.util.DataManager;
import com.black_dog20.warpradial.common.util.TeleportationHelper;
import com.black_dog20.warpradial.common.util.WarpPlayerProperties.Cooldown;
import com.black_dog20.warpradial.common.util.data.WarpDestination;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Util;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.*;

public class PacketTeleportHome {

    public PacketTeleportHome() {
    }

    public static void encode(PacketTeleportHome msg, PacketBuffer buffer) {
    }

    public static PacketTeleportHome decode(PacketBuffer buffer) {
        return new PacketTeleportHome();
    }

    public static class Handler {
        public static void handle(PacketTeleportHome msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                if (player == null)
                    return;

                if (!Config.HOMES_ALLOWED.get())
                    return;

                if (!TeleportationHelper.checkPermission(player))
                    return;

                if (!TeleportationHelper.canTeleport(player, Cooldown.HOME))
                    return;

                Optional<WarpDestination> home = DataManager.getHomeFor(player);

                if (home.isPresent()) {
                    if (TeleportationUtil.teleportPlayerToDestination(player, home.get())) {
                        TeleportationHelper.handleCooldown(player, Cooldown.HOME);
                        player.sendMessage(TELPORTED_TO_HOME.get(), Util.DUMMY_UUID);
                    } else {
                        player.sendMessage(COULD_NOT_TELEPORT.get(), Util.DUMMY_UUID);
                    }
                } else {
                    player.sendMessage(NO_HOME.get(), Util.DUMMY_UUID);
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
