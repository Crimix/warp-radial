package com.black_dog20.warpradial.common.datagen;

import com.black_dog20.warpradial.WarpRadial;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = WarpRadial.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModGenerator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
         if( event.includeServer() )
            registerServerProviders(event.getGenerator());

        if( event.includeClient() )
            registerClientProviders(event.getGenerator(), event);
    }

    private static void registerServerProviders(DataGenerator generator) {
        generator.addProvider(new GeneratorRecipes(generator));
    }

    private static void registerClientProviders(DataGenerator generator, GatherDataEvent event) {
        ExistingFileHelper helper = event.getExistingFileHelper();

        generator.addProvider(new GeneratorItemModels(generator, helper));
        generator.addProvider(new GeneratorLanguage(generator));
    }


}
