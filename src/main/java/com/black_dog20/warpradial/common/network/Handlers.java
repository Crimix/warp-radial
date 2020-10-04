package com.black_dog20.warpradial.common.network;

import com.black_dog20.warpradial.client.ClientDataManager;
import com.black_dog20.warpradial.client.radial.items.ClientPlayerDestination;
import com.black_dog20.warpradial.client.radial.items.ClientServerDestination;
import com.black_dog20.warpradial.common.network.packets.PacketSyncPlayerWarps;
import com.black_dog20.warpradial.common.network.packets.PacketSyncServerWarps;
import net.minecraftforge.fml.DistExecutor;

public class Handlers {

    public static DistExecutor.SafeRunnable handle(PacketSyncPlayerWarps packet) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                ClientDataManager.PLAYER_DESTINATION.clear();
                for (PacketSyncPlayerWarps.Triple<String, String, Long> p : packet.getWarps()) {
                    ClientDataManager.PLAYER_DESTINATION.add(new ClientPlayerDestination(p.getFirst(), p.getSecond(), p.getThird()));
                }
            }
        };
    }

    public static DistExecutor.SafeRunnable handle(PacketSyncServerWarps packet) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                ClientDataManager.SERVER_DESTINATION.clear();
                for (PacketSyncServerWarps.Triple<String, String, Long> p : packet.getWarps()) {
                    ClientDataManager.SERVER_DESTINATION.add(new ClientServerDestination(p.getFirst(), p.getSecond(), p.getThird()));
                }
            }
        };
    }
}
