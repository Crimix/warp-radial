package com.black_dog20.warpradial.common.network.packets;

import com.black_dog20.warpradial.client.ClientDataManager;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketOpSync {

    private boolean isOp;

    public PacketOpSync(boolean isOp) {
        this.isOp = isOp;
    }

    public static void encode(PacketOpSync msg, PacketBuffer buffer) {
        buffer.writeBoolean(msg.isOp);
    }

    public static PacketOpSync decode(PacketBuffer buffer) {
        return new PacketOpSync(buffer.readBoolean());
    }

    public static class Handler {
        public static void handle(PacketOpSync msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
                ClientDataManager.IS_OP = msg.isOp;
            }));
            ctx.get().setPacketHandled(true);
        }
    }
}
