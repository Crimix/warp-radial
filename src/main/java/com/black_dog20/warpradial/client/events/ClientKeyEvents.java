package com.black_dog20.warpradial.client.events;

import com.black_dog20.bml.utils.keybinds.KeybindsUtil;
import com.black_dog20.warpradial.WarpRadial;
import com.black_dog20.warpradial.client.keybinds.Keybinds;
import com.black_dog20.warpradial.client.radial.WarpRadialMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = WarpRadial.MOD_ID)
public class ClientKeyEvents {

    private static boolean keyWasDown = false;

    @SubscribeEvent
    public static void handleKeys(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.currentScreen == null) {
            boolean keyIsDown = KeybindsUtil.isKeyDown(Keybinds.openWarpMenu);
            if (keyIsDown && !keyWasDown) {
                while (Keybinds.openWarpMenu.isPressed()) {
                    mc.displayGuiScreen(new WarpRadialMenu(new StringTextComponent("Wrap menu")));
                }
            }
            keyWasDown = keyIsDown;
        } else {
            keyWasDown = true;
        }
    }
}
