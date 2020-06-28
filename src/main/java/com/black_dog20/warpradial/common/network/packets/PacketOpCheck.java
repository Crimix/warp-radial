package com.black_dog20.warpradial.common.network.packets;

import com.black_dog20.warpradial.client.ClientDataManager;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketOpCheck {

    private boolean isOp;

    public PacketOpCheck(boolean isOp) {
        this.isOp = isOp;
    }

    public static void encode(PacketOpCheck msg, PacketBuffer buffer) {
        buffer.writeBoolean(msg.isOp);
    }

    public static PacketOpCheck decode(PacketBuffer buffer) {
        return new PacketOpCheck(buffer.readBoolean());
    }

    public static class Handler {
        public static void handle(PacketOpCheck msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
                ClientDataManager.IS_OP = msg.isOp;
            }));
            ctx.get().setPacketHandled(true);
        }
    }
}
