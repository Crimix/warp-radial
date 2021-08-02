package com.black_dog20.warpradial;

import com.black_dog20.warpradial.client.keybinds.Keybinds;
import com.black_dog20.warpradial.common.network.PacketHandler;
import com.black_dog20.warpradial.common.proxy.ClientProxy;
import com.black_dog20.warpradial.common.proxy.IProxy;
import com.black_dog20.warpradial.common.proxy.ServerProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fmlclient.registry.ClientRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(WarpRadial.MOD_ID)
public class WarpRadial {
    public static final String MOD_ID = "warpradial";
    private static final Logger LOGGER = LogManager.getLogger();
    public static IProxy Proxy;

    public WarpRadial() {
        IEventBus event = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        Config.loadConfig(Config.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MOD_ID + "-client.toml"));
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);
        Config.loadConfig(Config.SERVER_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MOD_ID + "-server.toml"));
        Proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

        event.addListener(this::setup);
        event.addListener(this::setupClient);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        PacketHandler.register();
    }

    private void setupClient(final FMLClientSetupEvent event) {
        ClientRegistry.registerKeyBinding(Keybinds.openWarpMenu);
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
