package com.black_dog20.warpradial.client;

import com.black_dog20.warpradial.WarpRadial;
import com.black_dog20.warpradial.client.keybinds.Keybinds;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@Mod.EventBusSubscriber( modid = WarpRadial.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientStartup {

    @SubscribeEvent
    public static void registerKeyBinding(RegisterKeyMappingsEvent event) {
        event.register(Keybinds.openWarpMenu);
    }
}
