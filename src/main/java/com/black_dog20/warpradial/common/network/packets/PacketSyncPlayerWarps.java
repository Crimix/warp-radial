package com.black_dog20.warpradial.common.network.packets;

import com.black_dog20.warpradial.client.ClientDataManager;
import com.black_dog20.warpradial.client.radial.items.ClientPlayerDestination;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PacketSyncPlayerWarps {

    private List<Pair<String, String>> warps;

	public PacketSyncPlayerWarps(List<Pair<String, String>> warps) {
	    this.warps = warps;
    }

    public static void encode(PacketSyncPlayerWarps msg, PacketBuffer buffer) {
	    buffer.writeInt(msg.warps.size());
	    for(Pair<String, String> p : msg.warps) {
	        buffer.writeString(p.getFirst());
	        buffer.writeString(p.getSecond());
        }
    }

    public static PacketSyncPlayerWarps decode(PacketBuffer buffer) {
        List<Pair<String, String>> warps = new ArrayList<>();
        int size = buffer.readInt();
        for (int i = 0; i < size; i++) {
            warps.add(new Pair<>(buffer.readString(32767), buffer.readString(32767)));
        }
        return new PacketSyncPlayerWarps(warps);
    }

    public static class Handler {
        public static void handle(PacketSyncPlayerWarps msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
                ClientDataManager.PLAYER_DESTINATION.clear();
                for(Pair<String,String> p : msg.warps) {
                    ClientDataManager.PLAYER_DESTINATION.add(new ClientPlayerDestination(p.getFirst(),p.getSecond()));
                }

            }));
            ctx.get().setPacketHandled(true);
        }
    }
}
