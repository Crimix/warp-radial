package com.black_dog20.warpradial.client.events;

import com.black_dog20.bml.utils.keybinds.KeybindsUtil;
import com.black_dog20.warpradial.Config;
import com.black_dog20.warpradial.WarpRadial;
import com.black_dog20.warpradial.client.ClientDataManager;
import com.black_dog20.warpradial.client.keybinds.Keybinds;
import com.black_dog20.warpradial.client.radial.WarpRadialMenu;
import com.black_dog20.warpradial.common.util.TranslationHelper;
import com.black_dog20.warpradial.common.util.data.Permission;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = WarpRadial.MOD_ID)
public class ClientKeyEvents {

    private static boolean keyWasDown = false;

    @SubscribeEvent
    public static void handleKeys(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.currentScreen == null) {
            boolean keyIsDown = KeybindsUtil.isKeyDown(Keybinds.openWarpMenu);
            if (keyIsDown && !keyWasDown) {
                boolean allowed = isAllowed();
                if (!allowed && Config.INFORM_USER_OF_MISSING_PERMISSION.get()) {
                    Optional.ofNullable(Minecraft.getInstance().player)
                            .ifPresent(player -> player.sendStatusMessage(TranslationHelper.Translations.DENIED_MENU_USE.get(), true));
                }

                while (Keybinds.openWarpMenu.isPressed()) {
                    if (allowed) {
                        mc.displayGuiScreen(new WarpRadialMenu(new StringTextComponent("Wrap menu")));
                    }
                }
            }
            keyWasDown = keyIsDown;
        } else {
            keyWasDown = true;
        }
    }

    private static boolean isAllowed() {
        if (Config.ONLY_PERMISSION_PLAYERS_CAN_USE_MENU.get()) {
            return ClientDataManager.getPermissionOrIsOpOrSinglePlayer(Permission.CAN_USE_MENU);
        } else {
            return true;
        }
    }
}
