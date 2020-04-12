package com.black_dog20.warpradial.common.datagen;

import com.black_dog20.bml.datagen.BaseItemModelProvider;
import com.black_dog20.warpradial.WarpRadial;
import com.black_dog20.warpradial.common.items.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ExistingFileHelper;

public class GeneratorItemModels extends BaseItemModelProvider {
    public GeneratorItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, WarpRadial.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // Register all of the upgrade items
        ModItems.ITEMS.getEntries().forEach(item -> {
            String path = item.get().getRegistryName().getPath();
            singleTexture(path, mcLoc("item/handheld"), "layer0", modLoc("item/" + path));
        });
    }

    @Override
    public String getName() {
        return "Item Models";
    }
}
