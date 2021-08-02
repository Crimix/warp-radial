package com.black_dog20.warpradial.client;

import com.black_dog20.warpradial.Config;
import com.black_dog20.warpradial.client.radial.items.ClientPlayerDestination;
import com.black_dog20.warpradial.client.radial.items.ClientServerDestination;
import com.black_dog20.warpradial.common.util.data.Permission;
import com.black_dog20.warpradial.common.util.data.PlayerPermissions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class ClientDataManager {
    public static List<ClientPlayerDestination> PLAYER_DESTINATION = new ArrayList<>();
    public static List<ClientServerDestination> SERVER_DESTINATION = new ArrayList<>();
    public static Optional<PlayerPermissions> PLAYER_PERMISSION = Optional.empty();

    public static boolean getPermissionOrIsOpOrSinglePlayer(Permission permission) {
        return Minecraft.getInstance().hasSingleplayerServer() || hasLevelTo(permission) || getPermission(permission);
    }

    public static boolean getPermission(Permission permission) {
        return PLAYER_PERMISSION.map(p -> p.hasPermission(permission))
                .orElse(false);
    }

    private static boolean hasLevelTo(Permission permission) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null)
            return false;
        switch (permission) {
            case CAN_CREATE_SERVER_WARPS:
                return player.hasPermissions(Config.SERVER_WARP_CREATE_PERMISSION_LEVEL.get());
            case CAN_DELETE_SERVER_WARPS:
                return player.hasPermissions(Config.SERVER_WARP_DELETE_PERMISSION_LEVEL.get());
            case CAN_USE_MENU:
                return player.hasPermissions(Config.USE_MENU_PERMISSION_LEVEL.get());
        }

        return false;
    }
}
