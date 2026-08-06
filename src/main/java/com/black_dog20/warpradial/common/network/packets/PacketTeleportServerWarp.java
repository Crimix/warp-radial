package com.black_dog20.warpradial.common.network.packets;

import com.black_dog20.bml.network.messages.api.PlayPacket;
import com.black_dog20.bml.utils.player.TeleportationUtil;
import com.black_dog20.warpradial.Config;
import com.black_dog20.warpradial.WarpRadial;
import com.black_dog20.warpradial.common.util.DataManager;
import com.black_dog20.warpradial.common.util.TeleportationHelper;
import com.black_dog20.warpradial.common.util.WarpPlayerProperties.Cooldown;
import com.black_dog20.warpradial.common.util.data.WarpDestination;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.*;

public class PacketTeleportServerWarp extends PlayPacket<PacketTeleportServerWarp.Data> {

    public static final ResourceLocation ID = new ResourceLocation(WarpRadial.MOD_ID, "teleport_server_warp");

    public static final record Data(String warpName) implements CustomPacketPayload {

        public Data(FriendlyByteBuf friendlyByteBuf) {
            this(friendlyByteBuf.readUtf(32767));
        }

        @Override
        public void write(FriendlyByteBuf pBuffer) {
            pBuffer.writeUtf(warpName);
        }

        @Override
        public @NotNull ResourceLocation id() {
            return ID;
        }
    }

    @Override
    protected void handleClient(PacketTeleportServerWarp.Data payload, IPayloadContext context) {

    }

    @Override
    protected void handleServer(PacketTeleportServerWarp.Data payload, IPayloadContext context) {
        context.workHandler().execute(() -> PacketTeleportServerWarp.Handler.handle(payload, context));
    }

    public static class Handler {
        public static void handle(PacketTeleportServerWarp.Data payload, IPayloadContext context) {
            Optional<Player> optionalPlayer = context.player();
            if (optionalPlayer.isEmpty())
                return;

            ServerPlayer player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(optionalPlayer.get().getUUID());
            if (player == null)
                return;

            if (!Config.SERVER_WARPS_ALLOWED.get())
                return;

            if (!TeleportationHelper.checkPermission(player))
                return;

            if (!TeleportationHelper.checkServerWarpPermission(player))
                return;

            if (!TeleportationHelper.canTeleport(player, Cooldown.SERVER))
                return;

            Optional<WarpDestination> warp = DataManager.getServerWarpFor(payload.warpName);

            if (warp.isPresent()) {
                if (TeleportationUtil.teleportPlayerToDestination(player, warp.get())) {
                    TeleportationHelper.handleCooldown(player, Cooldown.SERVER);
                    player.sendSystemMessage(TELPORTED_TO_WARP.get(payload.warpName));
                } else {
                    player.sendSystemMessage(COULD_NOT_TELEPORT.get());
                }
            } else {
                player.sendSystemMessage(NO_WARP.get());
            }
        }
    }
}
