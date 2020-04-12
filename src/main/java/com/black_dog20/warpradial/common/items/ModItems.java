package com.black_dog20.warpradial.common.items;

import com.black_dog20.warpradial.WarpRadial;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final Item.Properties ITEM_GROUP = new Item.Properties().group(WarpRadial.itemGroup);
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, WarpRadial.MOD_ID);

    public static final RegistryObject<Item> REDSTONE_BREAD = ITEMS.register("redstone_bread", ItemRedstoneBread::new);
    public static final RegistryObject<Item> LAPIS_BREAD = ITEMS.register("lapis_bread", ItemLapisBread::new);
    public static final RegistryObject<Item> ENDER_PEARL_BREAD = ITEMS.register("ender_pearl_bread", ItemEnderPealBread::new);
}
