package com.black_dog20.warpradial.client.events;

import com.black_dog20.warpradial.WarpRadial;
import com.black_dog20.warpradial.client.radial.WarpRadialMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.*;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = WarpRadial.MOD_ID)
public class ClientKeyEvents {

    public static KeyBinding OPEN_WRAP_RADIAL;

    public static void init() {
        OPEN_WRAP_RADIAL = new KeyBinding(KEY_OPEN.getDescription(), GLFW.GLFW_KEY_R, KEY_CATEGORY.getDescription());
        ClientRegistry.registerKeyBinding(OPEN_WRAP_RADIAL);
    }

    private static boolean keyWasDown = false;

    @SubscribeEvent
    public static void handleKeys(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.currentScreen == null) {
            boolean keyIsDown = isKeyDown(OPEN_WRAP_RADIAL);
            if (keyIsDown && !keyWasDown)
            {
                while (OPEN_WRAP_RADIAL.isPressed())
                {
                    mc.displayGuiScreen(new WarpRadialMenu(new StringTextComponent("Wrap menu")));
                }
            }
            keyWasDown = keyIsDown;
        } else {
            keyWasDown = true;
        }
    }

    public static boolean isKeyDown(KeyBinding keybind)
    {
        if (keybind.isInvalid())
            return false;
        return InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), keybind.getKey().getKeyCode())
                && keybind.getKeyConflictContext().isActive() && keybind.getKeyModifier().isActive(keybind.getKeyConflictContext());
    }
}
