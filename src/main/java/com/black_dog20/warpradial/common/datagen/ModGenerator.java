package com.black_dog20.warpradial.common.datagen;

import com.black_dog20.warpradial.WarpRadial;
import net.minecraft.data.DataGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod.EventBusSubscriber(modid = WarpRadial.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModGenerator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        if (event.includeClient())
            registerClientProviders(event.getGenerator(), event);
    }

    private static void registerClientProviders(DataGenerator generator, GatherDataEvent event) {
        ExistingFileHelper helper = event.getExistingFileHelper();

        generator.addProvider(true, new GeneratorLanguage(generator));
    }


}
