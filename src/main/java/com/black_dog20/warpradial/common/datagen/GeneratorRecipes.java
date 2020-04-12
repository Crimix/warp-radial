package com.black_dog20.warpradial.common.datagen;

import com.black_dog20.bml.datagen.BaseRecipeProvider;
import com.black_dog20.warpradial.WarpRadial;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

import static com.black_dog20.warpradial.common.items.ModItems.*;

public class GeneratorRecipes extends BaseRecipeProvider {

	public GeneratorRecipes(DataGenerator generator) {
		super(generator, WarpRadial.MOD_ID);
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shapedRecipe(REDSTONE_BREAD.get())
				.key('r', Tags.Items.DUSTS_REDSTONE)
				.key('b', Items.BREAD)
				.patternLine("rrr")
				.patternLine("rbr")
				.patternLine("rrr")
				.addCriterion("has_bread", hasItem(Items.BREAD))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(LAPIS_BREAD.get())
				.key('l', Tags.Items.GEMS_LAPIS)
				.key('b', Items.BREAD)
				.patternLine("lll")
				.patternLine("lbl")
				.patternLine("lll")
				.addCriterion("has_bread", hasItem(Items.BREAD))
				.build(consumer);

		ShapelessRecipeBuilder.shapelessRecipe(ENDER_PEARL_BREAD.get())
				.addIngredient(Tags.Items.ENDER_PEARLS)
				.addIngredient(Items.BREAD)
				.addCriterion("has_bread", hasItem(Items.BREAD))
				.build(consumer);
	}

}
