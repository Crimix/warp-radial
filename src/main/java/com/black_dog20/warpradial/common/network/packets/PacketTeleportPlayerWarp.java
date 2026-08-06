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

public class PacketTeleportPlayerWarp extends PlayPacket<PacketTeleportPlayerWarp.Data> {

    public static final ResourceLocation ID = new ResourceLocation(WarpRadial.MOD_ID, "teleport_player_warp");

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
    protected void handleClient(PacketTeleportPlayerWarp.Data payload, IPayloadContext context) {

    }

    @Override
    protected void handleServer(PacketTeleportPlayerWarp.Data payload, IPayloadContext context) {
        context.workHandler().execute(() -> PacketTeleportPlayerWarp.Handler.handle(payload, context));
    }

    private static class Handler {
        private static void handle(PacketTeleportPlayerWarp.Data payload, IPayloadContext context) {
            Optional<Player> optionalPlayer = context.player();
            if (optionalPlayer.isEmpty())
                return;

            ServerPlayer player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(optionalPlayer.get().getUUID());
            if (player == null)
                return;

            if (!Config.PLAYER_WARPS_ALLOWED.get())
                return;

            if (!TeleportationHelper.checkPermission(player))
                return;

            if (!TeleportationHelper.canTeleport(player, Cooldown.PLAYER))
                return;

            Optional<WarpDestination> warp = DataManager.getPlayerWarpFor(player, payload.warpName);

            if (warp.isPresent()) {
                if (TeleportationUtil.teleportPlayerToDestination(player, warp.get())) {
                    TeleportationHelper.handleCooldown(player, Cooldown.PLAYER);
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
