package com.black_dog20.warpradial.common.commands;

import com.black_dog20.warpradial.Config;
import com.black_dog20.warpradial.common.util.DataManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TextFormatting;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.*;

public class CommandPermission implements ICommand {

    @Override
    public void register(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(Commands.literal("permission")
                .requires(source -> source.hasPermissionLevel(2))
                .then(serverWarps()));
    }

    private ArgumentBuilder<CommandSource, ?> serverWarps() {
        return Commands.literal("serverwarp")
                .then(Commands.literal("create")
                        .then(Commands.argument("playerName", EntityArgument.player())
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes(this::create))))
                .then(Commands.literal("delete")
                        .then(Commands.argument("playerName", EntityArgument.player())
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes(this::delete))));
    }

    @Override
    public boolean shouldBeRegistered() {
        return Config.SERVER_WARPS_ALLOWED.get();
    }

    public int create(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = EntityArgument.getPlayer(context, "playerName");
        boolean bool = BoolArgumentType.getBool(context, "value");
        DataManager.addPlayerPermission(player, p -> {
            p.setCreateServerWarps(bool);
            return p;
        });
        if (bool)
            context.getSource().sendFeedback(PERMISSIONS_CAN_CREATE_ADDED.getComponent(player.getName().applyTextStyle(TextFormatting.BLUE).getFormattedText()), true);
        else
            context.getSource().sendFeedback(PERMISSIONS_CAN_CREATE_REMOVED.getComponent(player.getName().applyTextStyle(TextFormatting.BLUE).getFormattedText()), true);
        return Command.SINGLE_SUCCESS;
    }

    public int delete(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = EntityArgument.getPlayer(context, "playerName");
        boolean bool = BoolArgumentType.getBool(context, "value");
        DataManager.addPlayerPermission(player, p -> {
            p.setDeleteServerWarps(bool);
            return p;
        });
        if (bool)
            context.getSource().sendFeedback(PERMISSIONS_CAN_DELETE_ADDED.getComponent(player.getName().applyTextStyle(TextFormatting.BLUE).getFormattedText()), true);
        else
            context.getSource().sendFeedback(PERMISSIONS_CAN_DELETE_REMOVED.getComponent(player.getName().applyTextStyle(TextFormatting.BLUE).getFormattedText()), true);
        return Command.SINGLE_SUCCESS;
    }
}
