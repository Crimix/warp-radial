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

public class PacketTeleportPlayerWarp {

    private String warpName;

    public PacketTeleportPlayerWarp(String name) {
        this.warpName = name;
    }

    public static void encode(PacketTeleportPlayerWarp msg, PacketBuffer buffer) {
        buffer.writeString(msg.warpName);
    }

    public static PacketTeleportPlayerWarp decode(PacketBuffer buffer) {
        return new PacketTeleportPlayerWarp(buffer.readString(32767));
    }

    public static class Handler {
        public static void handle(PacketTeleportPlayerWarp msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                if (player == null)
                    return;

                if (!Config.PLAYER_WARPS_ALLOWED.get())
                    return;

                if (!TeleportationHelper.checkPermission(player))
                    return;

                if (!TeleportationHelper.canTeleport(player, Cooldown.PLAYER))
                    return;

                Optional<WarpDestination> warp = DataManager.getPlayerWarpFor(player, msg.warpName);

                if (warp.isPresent()) {
                    if (TeleportationUtil.teleportPlayerToDestination(player, warp.get())) {
                        TeleportationHelper.handleCooldown(player, Cooldown.PLAYER);
                        player.sendMessage(TELPORTED_TO_WARP.get(msg.warpName), Util.DUMMY_UUID);
                    } else {
                        player.sendMessage(COULD_NOT_TELEPORT.get(), Util.DUMMY_UUID);
                    }
                } else {
                    player.sendMessage(NO_WARP.get(), Util.DUMMY_UUID);
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
