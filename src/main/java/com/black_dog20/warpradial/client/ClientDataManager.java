package com.black_dog20.warpradial.client;

import com.black_dog20.warpradial.client.radial.items.ClientPlayerDestination;
import com.black_dog20.warpradial.client.radial.items.ClientServerDestination;
import com.black_dog20.warpradial.common.util.data.PlayerPermissions;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ClientDataManager {
    public static boolean IS_OP = false;
    public static List<ClientPlayerDestination> PLAYER_DESTINATION = new ArrayList<>();
    public static List<ClientServerDestination> SERVER_DESTINATION = new ArrayList<>();
    public static Optional<PlayerPermissions> PLAYER_PERMISSION = Optional.empty();

    public static boolean getPermissionOrOpOrSP(Function<PlayerPermissions, Boolean> permissionsFunction) {
        return Minecraft.getInstance().isSingleplayer() || IS_OP || getPermission(permissionsFunction);
    }

    public static boolean getPermission(Function<PlayerPermissions, Boolean> permissionsFunction) {
        return PLAYER_PERMISSION.map(permissionsFunction)
                .filter(Boolean.TRUE::equals)
                .orElse(false);
    }
}
