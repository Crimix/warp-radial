package com.black_dog20.warpradial.client.keybinds;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.*;

@OnlyIn(Dist.CLIENT)
public class Keybinds {
    public static final KeyBinding openWarpMenu = new KeyBinding(KEY_OPEN.getDescription(), KeyConflictContext.UNIVERSAL, KeyModifier.NONE, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_R, KEY_CATEGORY.getDescription());
}
