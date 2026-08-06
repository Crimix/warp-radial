package com.black_dog20.warpradial.common.network.packets;

import com.black_dog20.bml.utils.objects.Triple;
import com.black_dog20.warpradial.WarpRadial;
import com.black_dog20.warpradial.common.network.Handlers;
import com.black_dog20.warpradial.common.util.data.WarpDestination;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class PacketSyncServerWarps extends BasePacketSyncWarps<PacketSyncServerWarps.Data> {

    public static final ResourceLocation ID = new ResourceLocation(WarpRadial.MOD_ID, "send_sever_warps");

    public static PacketSyncServerWarps.Data create(Set<Map.Entry<String, WarpDestination>> warps) {
        return new Data(getWarps(warps));
    }

    public static final record Data(List<Triple<String, String, Long>> warps) implements CustomPacketPayload {

        public Data(FriendlyByteBuf friendlyByteBuf) {
            this(readWraps(friendlyByteBuf));
        }

        @Override
        public void write(@NotNull FriendlyByteBuf pBuffer) {
            writeWraps(warps, pBuffer);
        }

        @Override
        public @NotNull ResourceLocation id() {
            return ID;
        }
    }

    @Override
    protected void handleClient(PacketSyncServerWarps.Data payload, IPayloadContext context) {
        context.workHandler().execute(() -> Handlers.handle(payload));
    }

    @Override
    protected void handleServer(PacketSyncServerWarps.Data payload, IPayloadContext context) {

    }
}
