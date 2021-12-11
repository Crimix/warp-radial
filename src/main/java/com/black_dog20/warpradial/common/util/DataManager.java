package com.black_dog20.warpradial.common.util;


import com.black_dog20.bml.network.messages.PacketPermission;
import com.black_dog20.bml.utils.file.FileUtil;
import com.black_dog20.warpradial.common.network.PacketHandler;
import com.black_dog20.warpradial.common.network.packets.PacketSyncPlayerWarps;
import com.black_dog20.warpradial.common.network.packets.PacketSyncServerWarps;
import com.black_dog20.warpradial.common.util.data.Permission;
import com.black_dog20.warpradial.common.util.data.PlayerPermissions;
import com.black_dog20.warpradial.common.util.data.WarpDestination;
import com.google.gson.reflect.TypeToken;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import net.minecraftforge.network.PacketDistributor;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class DataManager {

    private static ConcurrentHashMap<String, WarpDestination> HOMES = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, ConcurrentHashMap<String, WarpDestination>> PLAYER_WARPS = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, WarpDestination> SERVER_WARPS = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, PlayerPermissions> PLAYER_PERMISIONS = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, WarpDestination> getHomes() {
        if (EffectiveSide.get().isServer())
            return HOMES;
        else
            throw new IllegalStateException("Trying to get homes on non server side");
    }

    public static ConcurrentHashMap<String, ConcurrentHashMap<String, WarpDestination>> getPlayerWarps() {
        if (EffectiveSide.get().isServer())
            return PLAYER_WARPS;
        else
            throw new IllegalStateException("Trying to get player warps on non server side");
    }

    public static ConcurrentHashMap<String, WarpDestination> getPlayerWarps(ServerPlayer playerEntity) {
        if (EffectiveSide.get().isServer()) {
            String UUID = playerEntity.getUUID().toString();
            if (PLAYER_WARPS.containsKey(UUID))
                return PLAYER_WARPS.get(UUID);
            else
                return new ConcurrentHashMap<>();
        } else
            throw new IllegalStateException("Trying to get player warps on non server side");
    }

    public static ConcurrentHashMap<String, WarpDestination> getServerWarps() {
        if (EffectiveSide.get().isServer())
            return SERVER_WARPS;
        else
            throw new IllegalStateException("Trying to get server warps on non server side");
    }

    public static void loadHomes(ServerLevel world) {
        File warpDir = FileUtil.getDirRelativeToWorldFolder(world, "/warpradial");
        Type type = new TypeToken<ConcurrentHashMap<String, WarpDestination>>() {
        }.getType();
        HOMES = FileUtil.load(warpDir, "/homes.json", type, ConcurrentHashMap::new);
    }

    public static boolean saveHome(ServerLevel world) {
        File warpDir = FileUtil.getDirRelativeToWorldFolder(world, "/warpradial");
        Type type = new TypeToken<ConcurrentHashMap<String, WarpDestination>>() {
        }.getType();
        return FileUtil.save(warpDir, "/homes.json", HOMES, type);
    }

    public static void loadPlayerWarps(ServerLevel world) {
        File warpDir = FileUtil.getDirRelativeToWorldFolder(world, "/warpradial");
        Type type = new TypeToken<ConcurrentHashMap<String, ConcurrentHashMap<String, WarpDestination>>>() {
        }.getType();
        PLAYER_WARPS = FileUtil.load(warpDir, "/playerwarps.json", type, ConcurrentHashMap::new);
    }

    public static boolean savePlayerWarps(ServerLevel world) {
        File warpDir = FileUtil.getDirRelativeToWorldFolder(world, "/warpradial");
        Type type = new TypeToken<ConcurrentHashMap<String, ConcurrentHashMap<String, WarpDestination>>>() {
        }.getType();
        return FileUtil.save(warpDir, "/playerwarps.json", PLAYER_WARPS, type);
    }

    public static void loadServerWarps(ServerLevel world) throws FileNotFoundException {
        File warpDir = FileUtil.getDirRelativeToWorldFolder(world, "/warpradial");
        Type type = new TypeToken<ConcurrentHashMap<String, WarpDestination>>() {
        }.getType();
        SERVER_WARPS = FileUtil.load(warpDir, "/serverwarps.json", type, ConcurrentHashMap::new);
    }

    public static boolean saveServerWarps(ServerLevel world) {
        File warpDir = FileUtil.getDirRelativeToWorldFolder(world, "/warpradial");
        Type type = new TypeToken<ConcurrentHashMap<String, WarpDestination>>() {
        }.getType();
        return FileUtil.save(warpDir, "/serverwarps.json", SERVER_WARPS, type);
    }

    public static void loadPlayerPermissions(ServerLevel world) {
        File warpDir = FileUtil.getDirRelativeToWorldFolder(world, "/warpradial");
        Type type = new TypeToken<ConcurrentHashMap<String, PlayerPermissions>>() {
        }.getType();
        PLAYER_PERMISIONS = FileUtil.load(warpDir, "/permissions.json", type, ConcurrentHashMap::new);
    }

    public static boolean savePlayerPermissions(ServerLevel world) {
        File warpDir = FileUtil.getDirRelativeToWorldFolder(world, "/warpradial");
        Type type = new TypeToken<ConcurrentHashMap<String, PlayerPermissions>>() {
        }.getType();
        return FileUtil.save(warpDir, "/permissions.json", PLAYER_PERMISIONS, type);
    }

    public static void setHome(ServerPlayer playerEntity, WarpDestination destination) {
        ServerLevel world = getServerWorld(playerEntity);
        String UUID = playerEntity.getUUID().toString();
        HOMES.put(UUID, destination);
        saveHome(world);

    }

    public static void deleteHome(ServerPlayer playerEntity) {
        ServerLevel world = getServerWorld(playerEntity);
        String UUID = playerEntity.getUUID().toString();
        HOMES.remove(UUID);
        saveHome(world);
    }

    public static void addPlayerWarp(ServerPlayer playerEntity, String name, WarpDestination destination) {
        ServerLevel world = getServerWorld(playerEntity);
        String UUID = playerEntity.getUUID().toString();

        ConcurrentHashMap<String, WarpDestination> tempMap = new ConcurrentHashMap<>();
        if (PLAYER_WARPS.containsKey(UUID)) {
            tempMap = PLAYER_WARPS.get(UUID);
        }
        tempMap.put(name, destination);
        PLAYER_WARPS.put(UUID, tempMap);
        savePlayerWarps(world);
        syncPlayerWarpsToClient(playerEntity);
    }

    public static void deletePlayerWarp(ServerPlayer playerEntity, String name) {
        ServerLevel world = getServerWorld(playerEntity);
        String UUID = playerEntity.getUUID().toString();

        if (PLAYER_WARPS.containsKey(UUID)) {
            ConcurrentHashMap<String, WarpDestination> tempMap = PLAYER_WARPS.get(UUID);
            tempMap.remove(name);
            PLAYER_WARPS.put(UUID, tempMap);
        }
        savePlayerWarps(world);
        syncPlayerWarpsToClient(playerEntity);
    }

    public static void addServerWarp(ServerPlayer playerEntity, String name, WarpDestination destination) {
        ServerLevel world = getServerWorld(playerEntity);

        SERVER_WARPS.put(name, destination);
        saveServerWarps(world);
        syncServerWarpsToClients();
    }

    public static void deleteServerWarp(ServerPlayer playerEntity, String name) {
        ServerLevel world = getServerWorld(playerEntity);

        SERVER_WARPS.remove(name);
        saveServerWarps(world);
        syncServerWarpsToClients();
    }

    public static Optional<WarpDestination> getHomeFor(ServerPlayer player) {
        String UUID = player.getUUID().toString();
        if (HOMES.containsKey(UUID)) {
            return Optional.of(HOMES.get(UUID));
        } else {
            return Optional.empty();
        }
    }

    public static Optional<WarpDestination> getPlayerWarpFor(ServerPlayer player, String name) {
        String UUID = player.getUUID().toString();
        if (PLAYER_WARPS.containsKey(UUID)) {
            ConcurrentHashMap<String, WarpDestination> temp = PLAYER_WARPS.get(UUID);
            if (temp.containsKey(name)) {
                return Optional.of(temp.get(name));
            }
        }
        return Optional.empty();
    }

    public static Optional<WarpDestination> getServerWarpFor(String name) {
        if (SERVER_WARPS.containsKey(name)) {
            return Optional.of(SERVER_WARPS.get(name));
        } else {
            return Optional.empty();
        }
    }

    public static void syncServerWarpsToClient(ServerPlayer playerEntity) {
        PacketHandler.sendTo(new PacketSyncServerWarps(SERVER_WARPS.entrySet()), playerEntity);
    }

    public static void syncServerWarpsToClients() {
        PacketHandler.NETWORK.send(PacketDistributor.ALL.noArg(), new PacketSyncServerWarps(SERVER_WARPS.entrySet()));
    }

    public static void syncPlayerWarpsToClient(ServerPlayer playerEntity) {
        String UUID = playerEntity.getUUID().toString();
        if (PLAYER_WARPS.containsKey(UUID)) {
            ConcurrentHashMap<String, WarpDestination> temp = PLAYER_WARPS.get(UUID);
            PacketHandler.sendTo(new PacketSyncPlayerWarps(temp.entrySet()), playerEntity);
        }
    }

    public static void addPlayerPermission(ServerPlayer playerEntity, Permission permission) {
        ServerLevel world = getServerWorld(playerEntity);

        String uuid = playerEntity.getUUID().toString();
        PlayerPermissions playerPermissions = getPlayerPermission(playerEntity);
        playerPermissions.grant(permission);
        PLAYER_PERMISIONS.put(uuid, playerPermissions);
        savePlayerPermissions(world);
        syncPermissionsToClient(playerEntity);
    }

    public static void removePlayerPermission(ServerPlayer playerEntity, Permission permission) {
        ServerLevel world = getServerWorld(playerEntity);

        String uuid = playerEntity.getUUID().toString();
        PlayerPermissions playerPermissions = getPlayerPermission(playerEntity);
        playerPermissions.revoke(permission);
        PLAYER_PERMISIONS.put(uuid, playerPermissions);
        savePlayerPermissions(world);
        syncPermissionsToClient(playerEntity);
    }

    public static PlayerPermissions getPlayerPermission(ServerPlayer playerEntity) {
        String uuid = playerEntity.getUUID().toString();
        String name = playerEntity.getName().getString();
        return PLAYER_PERMISIONS.getOrDefault(uuid, new PlayerPermissions(uuid, name));
    }

    public static boolean playerHasPermission(ServerPlayer playerEntity, Permission permission) {
        return getPlayerPermission(playerEntity).hasPermission(permission);
    }

    public static void syncPermissionsToClient(ServerPlayer playerEntity) {
        PlayerPermissions playerPermissions = getPlayerPermission(playerEntity);
        PacketHandler.sendTo(new PacketPermission<>(PlayerPermissions.class, playerPermissions), playerEntity);
    }

    private static ServerLevel getServerWorld(ServerPlayer playerEntity) {
        return playerEntity.getServer().overworld();
    }

}
