package com.black_dog20.warpradial.common.commands;

import com.black_dog20.warpradial.WarpRadial;
import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = WarpRadial.MOD_ID)
public class ModCommands {

    private static List<ICommand> COMMANDS = ImmutableList.of(new CommandHome(), new CommandWarp(), new CommandServerWarp(), new CommandPermission());

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {

        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();

        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("wr");
        for (ICommand command : COMMANDS) {
            if (command.shouldBeRegistered()) {
                command.register(builder);
            }
        }
        dispatcher.register(builder);

        LiteralArgumentBuilder<CommandSource> builder2 = Commands.literal("warpradial");
        for (ICommand command : COMMANDS) {
            if (command.shouldBeRegistered()) {
                command.register(builder2);
            }
        }
        dispatcher.register(builder2);
    }
}
