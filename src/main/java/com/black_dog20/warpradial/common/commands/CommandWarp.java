package com.black_dog20.warpradial.common.commands;

import com.black_dog20.warpradial.Config;
import com.black_dog20.warpradial.common.util.DataManager;
import com.black_dog20.warpradial.common.util.PermissionHelper;
import com.black_dog20.warpradial.common.util.data.WarpDestination;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.DEL_WARP;
import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.SET_WARP;

public class CommandWarp implements ICommand {

    public static final SuggestionProvider<CommandSourceStack> SUGGESTIONS_PROVIDER = (context, suggestionsBuilder) -> {
        if (context.getSource().getEntity() instanceof ServerPlayer) {
            ServerPlayer player = (ServerPlayer) context.getSource().getEntity();
            List<String> strings = DataManager.getPlayerWarps(player).entrySet().stream()
                    .map(k -> k.getKey())
                    .collect(Collectors.toList());
            return SharedSuggestionProvider.suggest(strings, suggestionsBuilder);
        }
        return SharedSuggestionProvider.suggest(new ArrayList<>(), suggestionsBuilder);
    };

    @Override
    public void register(LiteralArgumentBuilder<CommandSourceStack> builder) {
        builder.then(Commands.literal("warp")
                .requires(source -> source.hasPermission(0) && PermissionHelper.onlyOpsRuleNotActiveOrCanUse(source))
                .then(registerSet())
                .then(registerDel())
        );
    }

    private ArgumentBuilder<CommandSourceStack, ?> registerSet() {
        return Commands.literal("add")
                .then(Commands.argument("warpName", MessageArgument.message())
                        .executes(this::set));
    }

    private ArgumentBuilder<CommandSourceStack, ?> registerDel() {
        return Commands.literal("remove")
                .then(Commands.argument("warpName", MessageArgument.message())
                        .suggests(SUGGESTIONS_PROVIDER)
                        .executes(this::del));
    }

    @Override
    public boolean shouldBeRegistered() {
        return Config.PLAYER_WARPS_ALLOWED.get();
    }

    public int set(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String warpName = MessageArgument.getMessage(context, "warpName").getString();
        Level world = player.level();
        WarpDestination destination = new WarpDestination(world.dimension(), player);
        DataManager.addPlayerWarp(player, warpName, destination);
        context.getSource().sendSuccess(() -> SET_WARP.get(warpName), Config.LOG_WARPS.get());
        return Command.SINGLE_SUCCESS;
    }

    public int del(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String warpName = MessageArgument.getMessage(context, "warpName").getString();
        DataManager.deletePlayerWarp(player, warpName);
        context.getSource().sendSuccess(() -> DEL_WARP.get(warpName), Config.LOG_WARPS.get());
        return Command.SINGLE_SUCCESS;
    }
}
