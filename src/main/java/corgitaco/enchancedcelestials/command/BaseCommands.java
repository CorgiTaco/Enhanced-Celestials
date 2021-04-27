package corgitaco.enchancedcelestials.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import corgitaco.enchancedcelestials.EnhancedCelestials;
import net.minecraft.command.CommandSource;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BaseCommands {
    @SubscribeEvent
    public static void onCommandsRegistered(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                LiteralArgumentBuilder.<CommandSource>literal("lunarevent")
                        .requires(cs -> cs.hasPermissionLevel(3))
                        .then(GetLunarEventCommand.register(event.getDispatcher()))
                        .then(SetLunarEventCommand.register(event.getDispatcher()))
        );
        event.getDispatcher().register(
                LiteralArgumentBuilder.<CommandSource>literal(EnhancedCelestials.MOD_ID)
                        .requires(cs -> cs.hasPermissionLevel(3))
                        .then(ReloadConfigurationsCommand.register(event.getDispatcher()))
        );
    }
}