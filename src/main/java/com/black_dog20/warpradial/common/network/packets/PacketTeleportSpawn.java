package com.black_dog20.warpradial.common.network.packets;

import com.black_dog20.bml.utils.player.TeleportationUtil;
import com.black_dog20.warpradial.Config;
import com.black_dog20.warpradial.common.util.TeleportationHelper;
import com.black_dog20.warpradial.common.util.WarpPlayerProperties.Cooldown;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.*;

public class PacketTeleportSpawn {

	public PacketTeleportSpawn() {
    }

    public static void encode(PacketTeleportSpawn msg, PacketBuffer buffer) {
    }

    public static PacketTeleportSpawn decode(PacketBuffer buffer) {
        return new PacketTeleportSpawn();
    }

    public static class Handler {
        public static void handle(PacketTeleportSpawn msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                if (player == null)
                    return;

                if(!Config.WARP_TO_SPAWN_ALLOWED.get())
                    return;

                if(!TeleportationHelper.canTeleport(player, Cooldown.SPAWN))
                    return;

                if(TeleportationUtil.teleportPlayerToSpawn(player)) {
                    TeleportationHelper.handleCooldown(player, Cooldown.SPAWN);
                    TeleportationHelper.handleFuel(player, Cooldown.SPAWN);
                    player.sendMessage(TELPORTED_TO_SPAWN.getComponent());
                } else {
                    player.sendMessage(COULD_NOT_TELEPORT.getComponent());
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
