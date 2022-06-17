package com.black_dog20.warpradial.common.network.packets;

import com.black_dog20.warpradial.common.network.Handlers;
import com.black_dog20.warpradial.common.util.data.WarpDestination;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class PacketSyncPlayerWarps {

    public static class Triple<A, B, C> {
        private final A v1;
        private final B v2;
        private final C v3;

        public Triple(A v1, B v2, C v3) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
        }

        public A getFirst() {
            return v1;
        }

        public B getSecond() {
            return v2;
        }

        public C getThird() {
            return v3;
        }
    }

    private List<Triple<String, String, Long>> warps;

    public PacketSyncPlayerWarps(Set<Map.Entry<String, WarpDestination>> warps) {
        List<Triple<String, String, Long>> playerWarps = new ArrayList<>();
        for (Map.Entry<String, WarpDestination> kvp : warps) {
            String key = kvp.getKey();
            WarpDestination value = kvp.getValue();
            playerWarps.add(new Triple<>(key, value.getDimension().registry().getPath(), value.getTimestamp()));
        }
        this.warps = playerWarps;
    }

    public PacketSyncPlayerWarps(List<Triple<String, String, Long>> warps) {
        this.warps = warps;
    }

    public List<Triple<String, String, Long>> getWarps() {
        return warps;
    }

    public static void encode(PacketSyncPlayerWarps msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.warps.size());
        for (Triple<String, String, Long> p : msg.warps) {
            buffer.writeUtf(p.getFirst());
            buffer.writeUtf(p.getSecond());
            buffer.writeLong(p.getThird());
        }
    }

    public static PacketSyncPlayerWarps decode(FriendlyByteBuf buffer) {
        List<Triple<String, String, Long>> warps = new ArrayList<>();
        int size = buffer.readInt();
        for (int i = 0; i < size; i++) {
            warps.add(new Triple<>(buffer.readUtf(32767), buffer.readUtf(32767), buffer.readLong()));
        }
        return new PacketSyncPlayerWarps(warps);
    }

    public static class Handler {
        public static void handle(PacketSyncPlayerWarps msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> Handlers.handle(msg)));
            ctx.get().setPacketHandled(true);
        }
    }
}
