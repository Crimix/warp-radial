package com.black_dog20.warpradial.common.events;

import com.black_dog20.bml.event.PlayerOpChangeEvent;
import com.black_dog20.warpradial.WarpRadial;
import com.black_dog20.warpradial.common.network.PacketHandler;
import com.black_dog20.warpradial.common.network.packets.PacketOpSync;
import com.black_dog20.warpradial.common.network.packets.PacketSyncPermissions;
import com.black_dog20.warpradial.common.util.DataManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

@Mod.EventBusSubscriber(modid = WarpRadial.MOD_ID)
public class ServerEvents {

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getPlayer().world.isRemote) {
            ServerPlayerEntity playerEntity = (ServerPlayerEntity) event.getPlayer();
            DataManager.syncPlayerWarpsToClient(playerEntity);
            DataManager.syncServerWarpsToClient(playerEntity);

            boolean isOp = ServerLifecycleHooks.getCurrentServer().getPlayerList().canSendCommands(playerEntity.getGameProfile());
            PacketHandler.sendTo(new PacketOpSync(isOp), playerEntity);
            PacketHandler.sendTo(new PacketSyncPermissions(DataManager.getPlayerPermission(playerEntity)), playerEntity);
        }
    }

    @SubscribeEvent
    public static void onServerStarting(FMLServerStartingEvent event) {
        try {
            ServerWorld world = event.getServer().getWorld(DimensionType.getById(0));

            DataManager.loadHomes(world);
            DataManager.loadPlayerWarps(world);
            DataManager.loadServerWarps(world);
            DataManager.loadPlayerPermissions(world);
        } catch (Exception e) {
            WarpRadial.getLogger().error(e.getMessage());
        }
    }

    @SubscribeEvent
    public static void onPlayerChangeOpStatus(PlayerOpChangeEvent event) {
        PacketHandler.sendTo(new PacketOpSync(event.getNewStatus()), (ServerPlayerEntity) event.getPlayer());
    }
}
