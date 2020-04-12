package com.black_dog20.warpradial.common.items;

import com.black_dog20.warpradial.WarpRadial;
import com.black_dog20.warpradial.common.network.PacketHandler;
import com.black_dog20.warpradial.common.network.packets.PacketSyncPlayerFuel;
import com.black_dog20.warpradial.common.util.TranslationHelper;
import com.black_dog20.warpradial.common.util.WarpPlayerProperties;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.List;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.*;

@Mod.EventBusSubscriber(modid = WarpRadial.MOD_ID)
public class ItemEnderPealBread extends BaseItem {

    public ItemEnderPealBread() {
        super(ModItems.ITEM_GROUP.food(new Food.Builder()
                .hunger(3)
                .saturation(2f)
                .setAlwaysEdible()
                .build()));
    }

    @SubscribeEvent
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity) event.getEntity();

        if (player.world.isRemote) return;

        Item usedItem = event.getItem().getItem();
        if (!(usedItem instanceof ItemEnderPealBread)) return;
        WarpPlayerProperties.addFuel(player, 2);
        PacketHandler.sendTo(new PacketSyncPlayerFuel(player), (ServerPlayerEntity) player);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);

        tooltip.add(TranslationHelper.translate(CONTAINS_EP, TextFormatting.GRAY, 2));
        tooltip.add(TranslationHelper.translate(ALWAYS_EDIBLE, TextFormatting.GOLD));
    }
}
