package com.black_dog20.warpradial.common.network.packets;

import com.black_dog20.warpradial.client.ClientDataManager;
import com.black_dog20.warpradial.client.radial.items.ClientPlayerDestination;
import com.black_dog20.warpradial.common.util.WarpDestination;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class PacketSyncPlayerWarps {

    private static class Triple<A, B, C> {
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
            playerWarps.add(new Triple<>(key, value.getDimension().getRegistryName().getPath(), value.getTimestamp()));
        }
        this.warps = playerWarps;
    }

    public PacketSyncPlayerWarps(List<Triple<String, String, Long>> warps) {
        this.warps = warps;
    }

    public static void encode(PacketSyncPlayerWarps msg, PacketBuffer buffer) {
        buffer.writeInt(msg.warps.size());
        for (Triple<String, String, Long> p : msg.warps) {
            buffer.writeString(p.getFirst());
            buffer.writeString(p.getSecond());
            buffer.writeLong(p.getThird());
        }
    }

    public static PacketSyncPlayerWarps decode(PacketBuffer buffer) {
        List<Triple<String, String, Long>> warps = new ArrayList<>();
        int size = buffer.readInt();
        for (int i = 0; i < size; i++) {
            warps.add(new Triple<>(buffer.readString(32767), buffer.readString(32767), buffer.readLong()));
        }
        return new PacketSyncPlayerWarps(warps);
    }

    public static class Handler {
        public static void handle(PacketSyncPlayerWarps msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
                ClientDataManager.PLAYER_DESTINATION.clear();
                for (Triple<String, String, Long> p : msg.warps) {
                    ClientDataManager.PLAYER_DESTINATION.add(new ClientPlayerDestination(p.getFirst(), p.getSecond(), p.getThird()));
                }

            }));
            ctx.get().setPacketHandled(true);
        }
    }
}
