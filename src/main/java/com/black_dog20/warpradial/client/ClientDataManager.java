package com.black_dog20.warpradial.client;

import com.black_dog20.warpradial.client.radial.items.ClientPlayerDestination;
import com.black_dog20.warpradial.client.radial.items.ClientServerDestination;
import com.black_dog20.warpradial.common.util.data.PlayerPermissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientDataManager {
    public static boolean IS_OP = false;
    public static List<ClientPlayerDestination> PLAYER_DESTINATION = new ArrayList<>();
    public static List<ClientServerDestination> SERVER_DESTINATION = new ArrayList<>();
    public static Optional<PlayerPermissions> PLAYER_PERMISSION = Optional.empty();
}
