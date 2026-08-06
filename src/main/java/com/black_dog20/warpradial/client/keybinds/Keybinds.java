package com.black_dog20.warpradial.client.keybinds;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.KEY_CATEGORY;
import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.KEY_OPEN;

@OnlyIn(Dist.CLIENT)
public class Keybinds {
    public static final KeyMapping openWarpMenu = new KeyMapping(KEY_OPEN.getDescription(), KeyConflictContext.UNIVERSAL, KeyModifier.NONE, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, KEY_CATEGORY.getDescription());
}
