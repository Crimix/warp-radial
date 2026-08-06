package com.black_dog20.warpradial.common.network.packets;

import com.black_dog20.bml.network.messages.api.PlayPacket;
import com.black_dog20.bml.utils.objects.Triple;
import com.black_dog20.warpradial.common.util.data.WarpDestination;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class BasePacketSyncWarps<T extends CustomPacketPayload> extends PlayPacket<T> {

    protected static List<Triple<String, String, Long>> getWarps(Set<Map.Entry<String, WarpDestination>> warps) {
        List<Triple<String, String, Long>> result = new ArrayList<>();
        for (Map.Entry<String, WarpDestination> kvp : warps) {
            String key = kvp.getKey();
            WarpDestination value = kvp.getValue();
            result.add(new Triple<>(key, value.getDimension().registry().getPath(), value.getTimestamp()));
        }
        return result;
    }

    protected static void writeWraps(List<Triple<String, String, Long>> warps, FriendlyByteBuf pBuffer) {
        pBuffer.writeInt(warps.size());
        for (Triple<String, String, Long> p : warps) {
            pBuffer.writeUtf(p.getFirst());
            pBuffer.writeUtf(p.getSecond());
            pBuffer.writeLong(p.getThird());
        }
    }

    protected static List<Triple<String, String, Long>> readWraps(FriendlyByteBuf buffer) {
        List<Triple<String, String, Long>> warps = new ArrayList<>();
        int size = buffer.readInt();
        for (int i = 0; i < size; i++) {
            warps.add(new Triple<>(buffer.readUtf(32767), buffer.readUtf(32767), buffer.readLong()));
        }
        return warps;
    }
}
