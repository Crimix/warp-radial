package com.black_dog20.warpradial.common.events;

import com.black_dog20.warpradial.WarpRadial;
import com.black_dog20.warpradial.common.network.PacketHandler;
import com.black_dog20.warpradial.common.network.packets.PacketSyncPlayerFuel;
import com.black_dog20.warpradial.common.util.DataManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@Mod.EventBusSubscriber(modid = WarpRadial.MOD_ID)
public class ServerEvents {

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if(!event.getPlayer().world.isRemote) {
            ServerPlayerEntity playerEntity = (ServerPlayerEntity) event.getPlayer();
            DataManager.syncPlayerWarpsToClient(playerEntity);
            DataManager.syncServerWarpsToClient(playerEntity);
            PacketHandler.sendTo(new PacketSyncPlayerFuel(playerEntity), playerEntity);
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if(!event.getPlayer().world.isRemote) {
            ServerPlayerEntity playerEntity = (ServerPlayerEntity) event.getPlayer();
            PacketHandler.sendTo(new PacketSyncPlayerFuel(playerEntity), playerEntity);
        }
    }

    @SubscribeEvent
    public static void onServerStarting(FMLServerStartingEvent event) {
        try {
            ServerWorld world = event.getServer().getWorld(DimensionType.getById(0));

            DataManager.loadHomes(world);
            DataManager.loadPlayerWarps(world);
            DataManager.loadServerWarps(world);
        } catch (Exception e) {
            WarpRadial.getLogger().error(e.getMessage());
        }
    }
}
