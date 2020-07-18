package com.black_dog20.warpradial.common.network.packets;

import com.black_dog20.bml.utils.player.TeleportationUtil;
import com.black_dog20.warpradial.Config;
import com.black_dog20.warpradial.common.util.DataManager;
import com.black_dog20.warpradial.common.util.TeleportationHelper;
import com.black_dog20.warpradial.common.util.WarpPlayerProperties.Cooldown;
import com.black_dog20.warpradial.common.util.data.WarpDestination;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.*;

public class PacketTeleportServerWarp {

    private String warpName;

    public PacketTeleportServerWarp(String name) {
        this.warpName = name;
    }

    public static void encode(PacketTeleportServerWarp msg, PacketBuffer buffer) {
        buffer.writeString(msg.warpName);
    }

    public static PacketTeleportServerWarp decode(PacketBuffer buffer) {
        return new PacketTeleportServerWarp(buffer.readString(32767));
    }

    public static class Handler {
        public static void handle(PacketTeleportServerWarp msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                if (player == null)
                    return;

                if (!Config.SERVER_WARPS_ALLOWED.get())
                    return;

                if (!TeleportationHelper.canTeleport(player, Cooldown.SERVER))
                    return;

                Optional<WarpDestination> warp = DataManager.getServerWarpFor(msg.warpName);

                if (warp.isPresent()) {
                    if (TeleportationUtil.teleportPlayerToDestination(player, warp.get())) {
                        TeleportationHelper.handleCooldown(player, Cooldown.SERVER);
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
