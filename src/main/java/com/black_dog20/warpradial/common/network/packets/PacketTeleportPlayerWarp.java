package com.black_dog20.warpradial.common.network.packets;

import com.black_dog20.bml.utils.player.TeleportDestination;
import com.black_dog20.bml.utils.player.TeleportationUtil;
import com.black_dog20.warpradial.Config;
import com.black_dog20.warpradial.common.util.DataManager;
import com.black_dog20.warpradial.common.util.TeleportationHelper;
import com.black_dog20.warpradial.common.util.WarpPlayerProperties.Cooldown;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
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

                if (!TeleportationHelper.canTeleport(player, Cooldown.PLAYER))
                    return;

                Optional<TeleportDestination> warp = DataManager.getPlayerWarpFor(player, msg.warpName);

                if (warp.isPresent()) {
                    if (TeleportationUtil.teleportPlayerToDestination(player, warp.get())) {
                        TeleportationHelper.handleCooldown(player, Cooldown.PLAYER);
                        player.sendMessage(TELPORTED_TO_WARP.getComponent(msg.warpName));
                    } else {
                        player.sendMessage(COULD_NOT_TELEPORT.getComponent());
                    }
                } else {
                    player.sendMessage(NO_WARP.getComponent());
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
