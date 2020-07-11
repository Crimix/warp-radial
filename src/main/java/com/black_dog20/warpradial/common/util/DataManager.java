package com.black_dog20.warpradial.common.util;


import com.black_dog20.warpradial.WarpRadial;
import com.black_dog20.warpradial.common.network.PacketHandler;
import com.black_dog20.warpradial.common.network.packets.PacketSyncPlayerWarps;
import com.black_dog20.warpradial.common.network.packets.PacketSyncServerWarps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.thread.EffectiveSide;
import net.minecraftforge.fml.network.PacketDistributor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class DataManager {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    private static ConcurrentHashMap<String, WarpDestination> HOMES = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, ConcurrentHashMap<String, WarpDestination>> PLAYER_WARPS = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, WarpDestination> SERVER_WARPS = new ConcurrentHashMap<>();

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

    public static void loadHomes(ServerWorld world) throws FileNotFoundException {
        File dir = world.getSaveHandler().getWorldDirectory();
        File warpDir = new File(dir.getPath() + "/warpradial");
        warpDir.mkdirs();
        File homes = new File(warpDir.getPath() + "/homes.json");
        if (HOMES != null)
            HOMES.clear();
        if (homes.exists()) {
            Type homesType = new TypeToken<ConcurrentHashMap<String, WarpDestination>>() {
            }.getType();
            JsonReader reader = new JsonReader(new FileReader(homes));
            HOMES = GSON.fromJson(reader, homesType);
            if (HOMES == null) {
                HOMES = new ConcurrentHashMap<>();
            }
        }
    }

    public static boolean saveHome(ServerWorld world) {
        try {
            File dir = world.getSaveHandler().getWorldDirectory();
            File warpDir = new File(dir.getPath() + "/warpradial");
            warpDir.mkdirs();

            File homes = new File(warpDir.getPath() + "/homes.json");
            FileWriter writer = new FileWriter(homes);
            GSON.toJson(HOMES, writer);
            writer.close();

            return true;
        } catch (Exception e) {
            WarpRadial.getLogger().error(e.getMessage());
            return false;
        }
    }

    public static void loadPlayerWarps(ServerWorld world) throws FileNotFoundException {
        File dir = world.getSaveHandler().getWorldDirectory();
        File warpDir = new File(dir.getPath() + "/warpradial");
        warpDir.mkdirs();
        File playerWarps = new File(warpDir.getPath() + "/playerwarps.json");
        if (PLAYER_WARPS != null)
            PLAYER_WARPS.clear();
        if (playerWarps.exists()) {
            Type playerWarpsType = new TypeToken<ConcurrentHashMap<String, ConcurrentHashMap<String, WarpDestination>>>() {
            }.getType();
            JsonReader reader = new JsonReader(new FileReader(playerWarps));
            PLAYER_WARPS = GSON.fromJson(reader, playerWarpsType);
        }
    }

    public static boolean savePlayerWarps(ServerWorld world) {
        try {
            File dir = world.getSaveHandler().getWorldDirectory();
            File warpDir = new File(dir.getPath() + "/warpradial");
            warpDir.mkdirs();

            File playerWarps = new File(warpDir.getPath() + "/playerwarps.json");
            FileWriter writer = new FileWriter(playerWarps);
            GSON.toJson(PLAYER_WARPS, writer);
            writer.close();

            return true;
        } catch (Exception e) {
            WarpRadial.getLogger().error(e.getMessage());
            return false;
        }
    }

    public static void loadServerWarps(ServerWorld world) throws FileNotFoundException {
        File dir = world.getSaveHandler().getWorldDirectory();
        File warpDir = new File(dir.getPath() + "/warpradial");
        warpDir.mkdirs();
        File serverWarps = new File(warpDir.getPath() + "/serverwarps.json");
        if (SERVER_WARPS != null)
            SERVER_WARPS.clear();
        if (serverWarps.exists()) {
            Type serverWarpsType = new TypeToken<ConcurrentHashMap<String, WarpDestination>>() {
            }.getType();
            JsonReader reader = new JsonReader(new FileReader(serverWarps));
            SERVER_WARPS = GSON.fromJson(reader, serverWarpsType);
        }
    }

    public static boolean saveServerWarps(ServerWorld world) {
        try {
            File dir = world.getSaveHandler().getWorldDirectory();
            File warpDir = new File(dir.getPath() + "/warpradial");
            warpDir.mkdirs();

            File serverWarps = new File(warpDir.getPath() + "/serverwarps.json");
            FileWriter writer = new FileWriter(serverWarps);
            GSON.toJson(SERVER_WARPS, writer);
            writer.close();

            return true;
        } catch (Exception e) {
            WarpRadial.getLogger().error(e.getMessage());
            return false;
        }
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

}
