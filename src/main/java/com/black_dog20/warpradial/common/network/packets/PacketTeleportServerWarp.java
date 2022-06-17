package com.black_dog20.warpradial.common.network.packets;

import com.black_dog20.bml.utils.player.TeleportationUtil;
import com.black_dog20.warpradial.Config;
import com.black_dog20.warpradial.common.util.DataManager;
import com.black_dog20.warpradial.common.util.TeleportationHelper;
import com.black_dog20.warpradial.common.util.WarpPlayerProperties.Cooldown;
import com.black_dog20.warpradial.common.util.data.WarpDestination;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.*;

public class PacketTeleportServerWarp {

    private String warpName;

    public PacketTeleportServerWarp(String name) {
        this.warpName = name;
    }

    public static void encode(PacketTeleportServerWarp msg, FriendlyByteBuf buffer) {
        buffer.writeUtf(msg.warpName);
    }

    public static PacketTeleportServerWarp decode(FriendlyByteBuf buffer) {
        return new PacketTeleportServerWarp(buffer.readUtf(32767));
    }

    public static class Handler {
        public static void handle(PacketTeleportServerWarp msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                if (player == null)
                    return;

                if (!Config.SERVER_WARPS_ALLOWED.get())
                    return;

                if (!TeleportationHelper.checkPermission(player))
                    return;

                if (!TeleportationHelper.checkServerWarpPermission(player))
                    return;

                if (!TeleportationHelper.canTeleport(player, Cooldown.SERVER))
                    return;

                Optional<WarpDestination> warp = DataManager.getServerWarpFor(msg.warpName);

                if (warp.isPresent()) {
                    if (TeleportationUtil.teleportPlayerToDestination(player, warp.get())) {
                        TeleportationHelper.handleCooldown(player, Cooldown.SERVER);
                        player.sendSystemMessage(TELPORTED_TO_WARP.get(msg.warpName));
                    } else {
                        player.sendSystemMessage(COULD_NOT_TELEPORT.get());
                    }
                } else {
                    player.sendSystemMessage(NO_WARP.get());
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
