package com.black_dog20.warpradial.common.network;

import com.black_dog20.warpradial.WarpRadial;
import com.black_dog20.warpradial.common.network.packets.PacketSyncPlayerWarps;
import com.black_dog20.warpradial.common.network.packets.PacketSyncServerWarps;
import com.black_dog20.warpradial.common.network.packets.PacketTeleportHome;
import com.black_dog20.warpradial.common.network.packets.PacketTeleportPlayerWarp;
import com.black_dog20.warpradial.common.network.packets.PacketTeleportServerWarp;
import com.black_dog20.warpradial.common.network.packets.PacketTeleportSpawn;
import com.black_dog20.warpradial.common.network.packets.PacketWarpRadialPermission;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

@Mod.EventBusSubscriber(modid = WarpRadial.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PacketHandler {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlerEvent event) {
        final IPayloadRegistrar registrar = event.registrar(WarpRadial.MOD_ID)
                .versioned("1.0.0")
                .play(PacketSyncPlayerWarps.ID, PacketSyncPlayerWarps.Data::new, new PacketSyncPlayerWarps().handlers())
                .play(PacketSyncServerWarps.ID, PacketSyncServerWarps.Data::new, new PacketSyncServerWarps().handlers())
                .play(PacketTeleportHome.ID, PacketTeleportHome.Data::new, new PacketTeleportHome().handlers())
                .play(PacketTeleportPlayerWarp.ID, PacketTeleportPlayerWarp.Data::new, new PacketTeleportPlayerWarp().handlers())
                .play(PacketTeleportServerWarp.ID, PacketTeleportServerWarp.Data::new, new PacketTeleportServerWarp().handlers())
                .play(PacketTeleportSpawn.ID, PacketTeleportSpawn.Data::new, new PacketTeleportSpawn().handlers())
                .play(PacketWarpRadialPermission.ID, PacketWarpRadialPermission.Data::new, new PacketWarpRadialPermission().handlers())
                .optional();
    }
}
