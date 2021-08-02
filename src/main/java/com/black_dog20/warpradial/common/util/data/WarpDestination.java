package com.black_dog20.warpradial.common.util.data;

import com.black_dog20.bml.utils.player.TeleportDestination;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class WarpDestination extends TeleportDestination {

    private long created = 0;

    public WarpDestination(ResourceKey<Level> dimension, ServerPlayer player) {
        super(dimension, player);
        this.created = System.currentTimeMillis();
    }

    public WarpDestination(ResourceKey<Level> dimension, BlockPos pos, float yaw, float pitch) {
        super(dimension, pos, yaw, pitch);
        this.created = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return created;
    }
}
