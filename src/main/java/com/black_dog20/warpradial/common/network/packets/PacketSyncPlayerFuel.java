package com.black_dog20.warpradial.common.network.packets;

import com.black_dog20.warpradial.WarpRadial;
import com.black_dog20.warpradial.common.util.WarpPlayerProperties;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncPlayerFuel {

    private final double fuel;

    public PacketSyncPlayerFuel(PlayerEntity player) {
        this.fuel = WarpPlayerProperties.getFuel(player);
    }

    public PacketSyncPlayerFuel(double fuel) {
        this.fuel = fuel;
    }

    public static void encode(PacketSyncPlayerFuel msg, PacketBuffer buffer) {
        buffer.writeDouble(msg.fuel);
    }

    public static PacketSyncPlayerFuel decode(PacketBuffer buffer) {
        return new PacketSyncPlayerFuel(buffer.readDouble());
    }

    public static class Handler {
        public static void handle(PacketSyncPlayerFuel msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
                WarpRadial.Proxy.setFuelClient(msg.fuel);
            }));
            ctx.get().setPacketHandled(true);
        }
    }
}
