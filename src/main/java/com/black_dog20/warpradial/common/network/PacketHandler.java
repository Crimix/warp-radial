package com.black_dog20.warpradial.common.network;

import com.black_dog20.bml.network.messages.PacketPermission;
import com.black_dog20.warpradial.WarpRadial;
import com.black_dog20.warpradial.common.network.packets.PacketSyncPlayerWarps;
import com.black_dog20.warpradial.common.network.packets.PacketSyncServerWarps;
import com.black_dog20.warpradial.common.network.packets.PacketTeleportHome;
import com.black_dog20.warpradial.common.network.packets.PacketTeleportPlayerWarp;
import com.black_dog20.warpradial.common.network.packets.PacketTeleportServerWarp;
import com.black_dog20.warpradial.common.network.packets.PacketTeleportSpawn;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = Integer.toString(1);
    private static short index = 0;

    public static final SimpleChannel NETWORK = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(WarpRadial.MOD_ID, "network"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public static void register() {
        registerMessage(PacketTeleportHome.class, PacketTeleportHome::encode, PacketTeleportHome::decode, PacketTeleportHome.Handler::handle);
        registerMessage(PacketTeleportSpawn.class, PacketTeleportSpawn::encode, PacketTeleportSpawn::decode, PacketTeleportSpawn.Handler::handle);
        registerMessage(PacketTeleportPlayerWarp.class, PacketTeleportPlayerWarp::encode, PacketTeleportPlayerWarp::decode, PacketTeleportPlayerWarp.Handler::handle);
        registerMessage(PacketTeleportServerWarp.class, PacketTeleportServerWarp::encode, PacketTeleportServerWarp::decode, PacketTeleportServerWarp.Handler::handle);
        registerMessage(PacketSyncPlayerWarps.class, PacketSyncPlayerWarps::encode, PacketSyncPlayerWarps::decode, PacketSyncPlayerWarps.Handler::handle);
        registerMessage(PacketSyncServerWarps.class, PacketSyncServerWarps::encode, PacketSyncServerWarps::decode, PacketSyncServerWarps.Handler::handle);
        registerMessage(PacketPermission.class, PacketPermission::encode, PacketPermission::decode, PacketPermission.Handler::handle);
    }

    public static void sendTo(Object msg, ServerPlayer player) {
        if (!(player instanceof FakePlayer))
            NETWORK.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToAll(Object msg, Level world) {
        for (Player player : world.players()) {
            if (!(player instanceof FakePlayer))
                NETWORK.sendTo(msg, ((ServerPlayer) player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        }
    }

    public static void sendToServer(Object msg) {
        NETWORK.sendToServer(msg);
    }

    private static <MSG> void registerMessage(Class<MSG> messageType, BiConsumer<MSG, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer) {
        NETWORK.registerMessage(index, messageType, encoder, decoder, messageConsumer);
        index++;
        if (index > 0xFF)
            throw new RuntimeException("Too many messages!");
    }

    private static <MSG> void registerMessage(Class<MSG> messageType, BiConsumer<MSG, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer, NetworkDirection direction) {
        NETWORK.registerMessage(index, messageType, encoder, decoder, messageConsumer, Optional.of(direction));
        index++;
        if (index > 0xFF)
            throw new RuntimeException("Too many messages!");
    }
}
