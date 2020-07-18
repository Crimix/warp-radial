package com.black_dog20.warpradial.common.commands;

import com.black_dog20.warpradial.Config;
import com.black_dog20.warpradial.WarpRadial;
import com.black_dog20.warpradial.common.util.DataManager;
import com.black_dog20.warpradial.common.util.data.WarpDestination;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.List;
import java.util.stream.Collectors;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.*;

public class CommandServerWarp implements ICommand {

    public static final SuggestionProvider<CommandSource> SUGGESTIONS_PROVIDER = (context, suggestionsBuilder) -> {
        List<String> strings = DataManager.getServerWarps().entrySet().stream()
                .map(k -> k.getKey())
                .collect(Collectors.toList());
        return ISuggestionProvider.suggest(strings, suggestionsBuilder);
    };

    @Override
    public void register(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(Commands.literal("serverwarp")
                .requires(source -> source.hasPermissionLevel(2) || WarpRadial.Proxy.isSinglePlayer() || canCreateOrDelete(source))
                .then(registerSet())
                .then(registerDel())
        );
    }


    private ArgumentBuilder<CommandSource, ?> registerSet() {
        return Commands.literal("add")
                .requires(source -> source.hasPermissionLevel(2) || WarpRadial.Proxy.isSinglePlayer() || canCreate(source))
                .then(Commands.argument("warpName", MessageArgument.message())
                        .executes(this::set));
    }

    private ArgumentBuilder<CommandSource, ?> registerDel() {
        return Commands.literal("remove")
                .requires(source -> source.hasPermissionLevel(2) || WarpRadial.Proxy.isSinglePlayer() || canCreate(source))
                .then(Commands.argument("warpName", MessageArgument.message())
                        .suggests(SUGGESTIONS_PROVIDER)
                        .executes(this::del));
    }

    @Override
    public boolean shouldBeRegistered() {
        return Config.SERVER_WARPS_ALLOWED.get();
    }

    public int set(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        String warpName = MessageArgument.getMessage(context, "warpName").getFormattedText();
        World world = player.world;
        WarpDestination destination = new WarpDestination(world.dimension.getType(), player);
        DataManager.addServerWarp(player, warpName, destination);
        context.getSource().sendFeedback(SET_WARP.getComponent(warpName), Config.LOG_WARPS.get());
        return Command.SINGLE_SUCCESS;
    }

    public int del(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        String warpName = MessageArgument.getMessage(context, "warpName").getFormattedText();
        DataManager.deleteServerWarp(player, warpName);
        context.getSource().sendFeedback(DEL_WARP.getComponent(warpName), Config.LOG_WARPS.get());
        return Command.SINGLE_SUCCESS;
    }

    private boolean canCreateOrDelete(CommandSource source) {
        return canCreate(source) || canDelete(source);
    }

    private boolean canCreate(CommandSource source) {
        try {
            return DataManager.getPlayerPermission(source.asPlayer()).canCreateServerWarps();
        } catch (CommandSyntaxException e) {
            return false;
        }
    }

    private boolean canDelete(CommandSource source) {
        try {
            return DataManager.getPlayerPermission(source.asPlayer()).canDeleteServerWarps();
        } catch (CommandSyntaxException e) {
            return false;
        }
    }
}
