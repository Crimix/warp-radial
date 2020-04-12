package com.black_dog20.warpradial.common.datagen;

import com.black_dog20.bml.datagen.BaseRecipeProvider;
import com.black_dog20.warpradial.WarpRadial;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;

import java.util.function.Consumer;

public class GeneratorRecipes extends BaseRecipeProvider {

	public GeneratorRecipes(DataGenerator generator) {
		super(generator, WarpRadial.MOD_ID);
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {

	}

}
