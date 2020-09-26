package com.black_dog20.warpradial.common.network.packets;

import com.black_dog20.warpradial.client.ClientDataManager;
import com.black_dog20.warpradial.common.util.data.PlayerPermissions;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

public class PacketSyncPermissions {

    private PlayerPermissions permissions;

    public PacketSyncPermissions(PlayerPermissions permissions) {
        this.permissions = permissions;
    }

    public static void encode(PacketSyncPermissions msg, PacketBuffer buffer) {
        buffer.writeString(msg.permissions.getUuid());
        buffer.writeString(msg.permissions.getDisplayName());
        buffer.writeBoolean(msg.permissions.canCreateServerWarps());
        buffer.writeBoolean(msg.permissions.canDeleteServerWarps());
        buffer.writeBoolean(msg.permissions.canUseMenu());
    }

    public static PacketSyncPermissions decode(PacketBuffer buffer) {
        return new PacketSyncPermissions(new PlayerPermissions(buffer.readString(32767), buffer.readString(32767), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean()));
    }

    public static class Handler {
        public static void handle(PacketSyncPermissions msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
                ClientDataManager.PLAYER_PERMISSION = Optional.of(msg.permissions);
            }));
            ctx.get().setPacketHandled(true);
        }
    }
}
