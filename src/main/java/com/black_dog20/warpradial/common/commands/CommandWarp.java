package com.black_dog20.warpradial.common.commands;

import com.black_dog20.bml.utils.player.TeleportDestination;
import com.black_dog20.warpradial.Config;
import com.black_dog20.warpradial.common.util.DataManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.*;

public class CommandWarp implements ICommand {

    public static final SuggestionProvider<CommandSource> SUGGESTIONS_PROVIDER = (context, suggestionsBuilder) -> {
        if (context.getSource().getEntity() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) context.getSource().getEntity();
            List<String> strings = DataManager.getPlayerWarps(player).entrySet().stream()
                    .map(k -> k.getKey())
                    .collect(Collectors.toList());
            return ISuggestionProvider.suggest(strings, suggestionsBuilder);
        }
        return ISuggestionProvider.suggest(new ArrayList<>(), suggestionsBuilder);
    };

    @Override
    public void register(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(Commands.literal("warp")
                .requires(source -> source.hasPermissionLevel(0))
                .then(registerSet())
                .then(registerDel())
        );
    }

    private ArgumentBuilder<CommandSource, ?> registerSet() {
        return Commands.literal("add")
                .then(Commands.argument("warpName", StringArgumentType.string())
                        .executes(this::set));
    }

    private ArgumentBuilder<CommandSource, ?> registerDel() {
        return Commands.literal("remove")
                .then(Commands.argument("warpName", StringArgumentType.string())
                        .suggests(SUGGESTIONS_PROVIDER)
                        .executes(this::del));
    }

    @Override
    public boolean shouldBeRegistered() {
        return Config.PLAYER_WARPS_ALLOWED.get();
    }

    public int set(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        String warpName = StringArgumentType.getString(context, "warpName");
        World world = player.world;
        TeleportDestination destination = new TeleportDestination(world.dimension.getType(), player.getPosition(), player.rotationYaw, player.rotationPitch);
        DataManager.addPlayerWarp(player, warpName, destination);
        context.getSource().sendFeedback(SET_WARP.getComponent(warpName), true);
        return Command.SINGLE_SUCCESS;
    }

    public int del(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        String warpName = StringArgumentType.getString(context, "warpName");
        DataManager.deletePlayerWarp(player, warpName);
        context.getSource().sendFeedback(DEL_WARP.getComponent(warpName), true);
        return Command.SINGLE_SUCCESS;
    }
}
