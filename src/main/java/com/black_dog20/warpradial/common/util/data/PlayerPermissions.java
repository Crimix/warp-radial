package com.black_dog20.warpradial.common.util.data;

import com.black_dog20.bml.utils.player.AbstractPlayerPermissions;
import com.black_dog20.warpradial.client.ClientDataManager;

import java.util.Optional;

public class PlayerPermissions extends AbstractPlayerPermissions {

    public PlayerPermissions(String uuid, String displayName, PlayerPermissions.IPermission... permissions) {
        super(uuid, displayName, permissions);
    }

    @Override
    public void onReceiveClientMessage() {
        ClientDataManager.PLAYER_PERMISSION = Optional.of(this);
    }
}
