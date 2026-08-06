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

public class PacketTeleportHome extends PlayPacket<PacketTeleportHome.Data> {

    public static final ResourceLocation ID = new ResourceLocation(WarpRadial.MOD_ID, "teleport_home_warp");

    public static final record Data() implements CustomPacketPayload {

        public Data(FriendlyByteBuf friendlyByteBuf) {
            this();
        }

        @Override
        public void write(@NotNull FriendlyByteBuf pBuffer) {

        }

        @Override
        public @NotNull ResourceLocation id() {
            return ID;
        }
    }

    @Override
    protected void handleClient(PacketTeleportHome.Data payload, IPayloadContext context) {

    }

    @Override
    protected void handleServer(PacketTeleportHome.Data payload, IPayloadContext context) {
        context.workHandler().execute(() -> Handler.handle(payload, context));
    }

    private static class Handler {
        private static void handle(PacketTeleportHome.Data payload, IPayloadContext context) {
            Optional<Player> optionalPlayer = context.player();
            if (optionalPlayer.isEmpty())
                return;

            ServerPlayer player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(optionalPlayer.get().getUUID());
            if (player == null)
                return;

            if (!Config.HOMES_ALLOWED.get())
                return;

            if (!TeleportationHelper.checkPermission(player))
                return;

            if (!TeleportationHelper.canTeleport(player, Cooldown.HOME))
                return;

            Optional<WarpDestination> home = DataManager.getHomeFor(player);

            if (home.isPresent()) {
                if (TeleportationUtil.teleportPlayerToDestination(player, home.get())) {
                    TeleportationHelper.handleCooldown(player, Cooldown.HOME);
                    player.sendSystemMessage(TELPORTED_TO_HOME.get());
                } else {
                    player.sendSystemMessage(COULD_NOT_TELEPORT.get());
                }
            } else {
                player.sendSystemMessage(NO_HOME.get());
            }
        }
    }
}
