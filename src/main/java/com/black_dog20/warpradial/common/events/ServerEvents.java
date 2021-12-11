package com.black_dog20.warpradial.common.events;

import com.black_dog20.bml.network.messages.PacketPermission;
import com.black_dog20.warpradial.WarpRadial;
import com.black_dog20.warpradial.common.network.PacketHandler;
import com.black_dog20.warpradial.common.util.DataManager;
import com.black_dog20.warpradial.common.util.data.PlayerPermissions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WarpRadial.MOD_ID)
public class ServerEvents {

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getPlayer().level.isClientSide) {
            ServerPlayer playerEntity = (ServerPlayer) event.getPlayer();
            DataManager.syncPlayerWarpsToClient(playerEntity);
            DataManager.syncServerWarpsToClient(playerEntity);

            PacketHandler.sendTo(new PacketPermission<>(PlayerPermissions.class, DataManager.getPlayerPermission(playerEntity)), playerEntity);
        }
    }

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        try {
            ServerLevel world = event.getServer().overworld();

            DataManager.loadHomes(world);
            DataManager.loadPlayerWarps(world);
            DataManager.loadServerWarps(world);
            DataManager.loadPlayerPermissions(world);
        } catch (Exception e) {
            WarpRadial.getLogger().error(e.getMessage());
        }
    }

}
