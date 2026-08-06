package com.black_dog20.warpradial.common.network;

import com.black_dog20.bml.utils.objects.Triple;
import com.black_dog20.warpradial.client.ClientDataManager;
import com.black_dog20.warpradial.client.radial.items.ClientPlayerDestination;
import com.black_dog20.warpradial.client.radial.items.ClientServerDestination;
import com.black_dog20.warpradial.common.network.packets.PacketSyncPlayerWarps;
import com.black_dog20.warpradial.common.network.packets.PacketSyncServerWarps;

public class Handlers {

    public static void handle(PacketSyncPlayerWarps.Data packet) {
        ClientDataManager.PLAYER_DESTINATION.clear();
        for (Triple<String, String, Long> p : packet.warps()) {
            ClientDataManager.PLAYER_DESTINATION.add(new ClientPlayerDestination(p.getFirst(), p.getSecond(), p.getThird()));
        }
    }

    public static void handle(PacketSyncServerWarps.Data packet) {
        ClientDataManager.SERVER_DESTINATION.clear();
        for (Triple<String, String, Long> p : packet.warps()) {
            ClientDataManager.SERVER_DESTINATION.add(new ClientServerDestination(p.getFirst(), p.getSecond(), p.getThird()));
        }
    }
}
