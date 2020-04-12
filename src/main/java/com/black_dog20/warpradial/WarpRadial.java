package com.black_dog20.warpradial;

import com.black_dog20.warpradial.client.events.ClientKeyEvents;
import com.black_dog20.warpradial.common.items.ModItems;
import com.black_dog20.warpradial.common.network.PacketHandler;
import com.black_dog20.warpradial.common.proxy.ClientProxy;
import com.black_dog20.warpradial.common.proxy.IProxy;
import com.black_dog20.warpradial.common.proxy.ServerProxy;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(WarpRadial.MOD_ID)
public class WarpRadial
{
    public static final String MOD_ID = "warpradial";
    private static final Logger LOGGER = LogManager.getLogger();
    public static IProxy Proxy;

    public static ItemGroup itemGroup = new ItemGroup(WarpRadial.MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.ENDER_PEARL_BREAD.get());
        }
    };

    public WarpRadial() {
        IEventBus event = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);
        Config.loadConfig(Config.SERVER_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MOD_ID + "-server.toml"));
        Proxy = DistExecutor.runForDist(()->()->new ClientProxy(), ()->()->new ServerProxy());

        ModItems.ITEMS.register(event);
        event.addListener(this::setup);
        event.addListener(this::loadComplete);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        PacketHandler.register();
    }

    public void loadComplete(FMLLoadCompleteEvent event)
    {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> ClientKeyEvents::init);
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
