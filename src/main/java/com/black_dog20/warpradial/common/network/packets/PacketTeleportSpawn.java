package com.black_dog20.warpradial.common.network.packets;

import com.black_dog20.bml.utils.player.TeleportationUtil;
import com.black_dog20.warpradial.Config;
import com.black_dog20.warpradial.common.util.TeleportationHelper;
import com.black_dog20.warpradial.common.util.WarpPlayerProperties.Cooldown;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.COULD_NOT_TELEPORT;
import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.TELPORTED_TO_SPAWN;

public class PacketTeleportSpawn {

    public PacketTeleportSpawn() {
    }

    public static void encode(PacketTeleportSpawn msg, FriendlyByteBuf buffer) {
    }

    public static PacketTeleportSpawn decode(FriendlyByteBuf buffer) {
        return new PacketTeleportSpawn();
    }

    public static class Handler {
        public static void handle(PacketTeleportSpawn msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                if (player == null)
                    return;

                if (!Config.WARP_TO_SPAWN_ALLOWED.get())
                    return;

                if (!TeleportationHelper.checkPermission(player))
                    return;

                if (!TeleportationHelper.canTeleport(player, Cooldown.SPAWN))
                    return;

                if (TeleportationUtil.teleportPlayerToSpawn(player)) {
                    TeleportationHelper.handleCooldown(player, Cooldown.SPAWN);
                    player.sendSystemMessage(TELPORTED_TO_SPAWN.get());
                } else {
                    player.sendSystemMessage(COULD_NOT_TELEPORT.get());
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
