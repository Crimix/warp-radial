package com.black_dog20.warpradial.common.util;


import com.black_dog20.bml.utils.file.FileUtil;
import com.black_dog20.warpradial.common.network.PacketHandler;
import com.black_dog20.warpradial.common.network.packets.PacketSyncPermissions;
import com.black_dog20.warpradial.common.network.packets.PacketSyncPlayerWarps;
import com.black_dog20.warpradial.common.network.packets.PacketSyncServerWarps;
import com.black_dog20.warpradial.common.util.data.PlayerPermissions;
import com.black_dog20.warpradial.common.util.data.WarpDestination;
import com.google.gson.reflect.TypeToken;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.thread.EffectiveSide;
import net.minecraftforge.fml.network.PacketDistributor;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

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

    public static ConcurrentHashMap<String, WarpDestination> getPlayerWarps(ServerPlayerEntity playerEntity) {
        if (EffectiveSide.get().isServer()) {
            String UUID = playerEntity.getUniqueID().toString();
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

    public static void loadHomes(ServerWorld world) {
        File warpDir = FileUtil.getDirRelativeToWorldFolder(world, "/warpradial");
        Type type = new TypeToken<ConcurrentHashMap<String, WarpDestination>>() {
        }.getType();
        HOMES = FileUtil.load(warpDir, "/homes.json", type, ConcurrentHashMap::new);
    }

    public static boolean saveHome(ServerWorld world) {
        File warpDir = FileUtil.getDirRelativeToWorldFolder(world, "/warpradial");
        Type type = new TypeToken<ConcurrentHashMap<String, WarpDestination>>() {
        }.getType();
        return FileUtil.save(warpDir, "/homes.json", HOMES, type);
    }

    public static void loadPlayerWarps(ServerWorld world) {
        File warpDir = FileUtil.getDirRelativeToWorldFolder(world, "/warpradial");
        Type type = new TypeToken<ConcurrentHashMap<String, ConcurrentHashMap<String, WarpDestination>>>() {
        }.getType();
        PLAYER_WARPS = FileUtil.load(warpDir, "/playerwarps.json", type, ConcurrentHashMap::new);
    }

    public static boolean savePlayerWarps(ServerWorld world) {
        File warpDir = FileUtil.getDirRelativeToWorldFolder(world, "/warpradial");
        Type type = new TypeToken<ConcurrentHashMap<String, ConcurrentHashMap<String, WarpDestination>>>() {
        }.getType();
        return FileUtil.save(warpDir, "/playerwarps.json", PLAYER_WARPS, type);
    }

    public static void loadServerWarps(ServerWorld world) throws FileNotFoundException {
        File warpDir = FileUtil.getDirRelativeToWorldFolder(world, "/warpradial");
        Type type = new TypeToken<ConcurrentHashMap<String, WarpDestination>>() {
        }.getType();
        SERVER_WARPS = FileUtil.load(warpDir, "/serverwarps.json", type, ConcurrentHashMap::new);
    }

    public static boolean saveServerWarps(ServerWorld world) {
        File warpDir = FileUtil.getDirRelativeToWorldFolder(world, "/warpradial");
        Type type = new TypeToken<ConcurrentHashMap<String, WarpDestination>>() {
        }.getType();
        return FileUtil.save(warpDir, "/serverwarps.json", SERVER_WARPS, type);
    }

    public static void loadPlayerPermissions(ServerWorld world) {
        File warpDir = FileUtil.getDirRelativeToWorldFolder(world, "/warpradial");
        Type type = new TypeToken<ConcurrentHashMap<UUID, PlayerPermissions>>() {
        }.getType();
        PLAYER_PERMISIONS = FileUtil.load(warpDir, "/permissions.json", type, ConcurrentHashMap::new);
    }

    public static boolean savePlayerPermissions(ServerWorld world) {
        File warpDir = FileUtil.getDirRelativeToWorldFolder(world, "/warpradial");
        Type type = new TypeToken<ConcurrentHashMap<UUID, PlayerPermissions>>() {
        }.getType();
        return FileUtil.save(warpDir, "/permissions.json", PLAYER_PERMISIONS, type);
    }

    public static void setHome(ServerPlayerEntity playerEntity, WarpDestination destination) {
        ServerWorld world = playerEntity.getServer().getWorld(DimensionType.getById(0));
        String UUID = playerEntity.getUniqueID().toString();
        HOMES.put(UUID, destination);
        saveHome(world);

    }

    public static void deleteHome(ServerPlayerEntity playerEntity) {
        ServerWorld world = playerEntity.getServer().getWorld(DimensionType.getById(0));
        String UUID = playerEntity.getUniqueID().toString();
        HOMES.remove(UUID);
        saveHome(world);
    }

    public static void addPlayerWarp(ServerPlayerEntity playerEntity, String name, WarpDestination destination) {
        ServerWorld world = playerEntity.getServer().getWorld(DimensionType.getById(0));
        String UUID = playerEntity.getUniqueID().toString();

        ConcurrentHashMap<String, WarpDestination> tempMap = new ConcurrentHashMap<>();
        if (PLAYER_WARPS.containsKey(UUID)) {
            tempMap = PLAYER_WARPS.get(UUID);
        }
        tempMap.put(name, destination);
        PLAYER_WARPS.put(UUID, tempMap);
        savePlayerWarps(world);
        syncPlayerWarpsToClient(playerEntity);
    }

    public static void deletePlayerWarp(ServerPlayerEntity playerEntity, String name) {
        ServerWorld world = playerEntity.getServer().getWorld(DimensionType.getById(0));
        String UUID = playerEntity.getUniqueID().toString();

        if (PLAYER_WARPS.containsKey(UUID)) {
            ConcurrentHashMap<String, WarpDestination> tempMap = PLAYER_WARPS.get(UUID);
            tempMap.remove(name);
            PLAYER_WARPS.put(UUID, tempMap);
        }
        savePlayerWarps(world);
        syncPlayerWarpsToClient(playerEntity);
    }

    public static void addServerWarp(ServerPlayerEntity playerEntity, String name, WarpDestination destination) {
        ServerWorld world = playerEntity.getServer().getWorld(DimensionType.getById(0));

        SERVER_WARPS.put(name, destination);
        saveServerWarps(world);
        syncServerWarpsToClients();
    }

    public static void deleteServerWarp(ServerPlayerEntity playerEntity, String name) {
        ServerWorld world = playerEntity.getServer().getWorld(DimensionType.getById(0));

        SERVER_WARPS.remove(name);
        saveServerWarps(world);
        syncServerWarpsToClients();
    }

    public static Optional<WarpDestination> getHomeFor(ServerPlayerEntity player) {
        String UUID = player.getUniqueID().toString();
        if (HOMES.containsKey(UUID)) {
            return Optional.of(HOMES.get(UUID));
        } else {
            return Optional.empty();
        }
    }

    public static Optional<WarpDestination> getPlayerWarpFor(ServerPlayerEntity player, String name) {
        String UUID = player.getUniqueID().toString();
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

    public static void syncServerWarpsToClient(ServerPlayerEntity playerEntity) {
        PacketHandler.sendTo(new PacketSyncServerWarps(SERVER_WARPS.entrySet()), playerEntity);
    }

    public static void syncServerWarpsToClients() {
        PacketHandler.NETWORK.send(PacketDistributor.ALL.noArg(), new PacketSyncServerWarps(SERVER_WARPS.entrySet()));
    }

    public static void syncPlayerWarpsToClient(ServerPlayerEntity playerEntity) {
        String UUID = playerEntity.getUniqueID().toString();
        if (PLAYER_WARPS.containsKey(UUID)) {
            ConcurrentHashMap<String, WarpDestination> temp = PLAYER_WARPS.get(UUID);
            PacketHandler.sendTo(new PacketSyncPlayerWarps(temp.entrySet()), playerEntity);
        }
    }

    public static void addPlayerPermission(ServerPlayerEntity playerEntity, Function<PlayerPermissions, PlayerPermissions> function) {
        ServerWorld world = playerEntity.getServer().getWorld(DimensionType.getById(0));
        String uuid = playerEntity.getUniqueID().toString();
        PlayerPermissions playerPermissions = getPlayerPermission(playerEntity);
        playerPermissions = function.apply(playerPermissions);
        PLAYER_PERMISIONS.put(uuid, playerPermissions);
        savePlayerPermissions(world);
        syncPermissionsToClient(playerEntity);
    }

    public static PlayerPermissions getPlayerPermission(ServerPlayerEntity playerEntity) {
        String uuid = playerEntity.getUniqueID().toString();
        String name = playerEntity.getDisplayName().getFormattedText();
        return PLAYER_PERMISIONS.getOrDefault(uuid, new PlayerPermissions(uuid, name, false, false));
    }

    public static void syncPermissionsToClient(ServerPlayerEntity playerEntity) {
        PlayerPermissions playerPermissions = getPlayerPermission(playerEntity);
        PacketHandler.sendTo(new PacketSyncPermissions(playerPermissions), playerEntity);
    }

}
