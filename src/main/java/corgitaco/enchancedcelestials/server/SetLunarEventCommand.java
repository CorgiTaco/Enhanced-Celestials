package corgitaco.enchancedcelestials.server;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import corgitaco.enchancedcelestials.EnhancedCelestials;
import corgitaco.enchancedcelestials.data.network.NetworkHandler;
import corgitaco.enchancedcelestials.data.network.packet.LunarEventPacket;
import corgitaco.enchancedcelestials.lunarevent.LunarEvent;
import corgitaco.enchancedcelestials.lunarevent.LunarEventSystem;
import corgitaco.enchancedcelestials.util.EnhancedCelestialsUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.stream.Collectors;

public class SetLunarEventCommand {

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        List<String> weatherTypes = LunarEventSystem.LUNAR_EVENTS.stream().map(LunarEvent::getID).collect(Collectors.toList());

        return Commands.literal("setlunarevent").then(Commands.argument("lunarevent", StringArgumentType.string()).suggests((ctx, sb) -> ISuggestionProvider.suggest(weatherTypes.stream(), sb))
                .executes((cs) -> betterWeatherSetWeatherType(cs.getSource(), cs.getArgument("lunarevent", String.class))));
    }

    public static int betterWeatherSetWeatherType(CommandSource source, String lunarType) {
        LunarEvent weatherEvent = LunarEventSystem.LUNAR_EVENTS_MAP.get(lunarType);
        if (weatherEvent != null) {
            long dayTime = EnhancedCelestialsUtils.modulosDaytime(source.getWorld().getWorld().getWorldInfo().getDayTime());
            if (!(dayTime >= 13000 && dayTime <= 23500)) {
                source.sendFeedback(new TranslationTextComponent("enhancedcelestials.commands.failed.requiresnight", dayTime), true);
                return 0;
            }

            EnhancedCelestials.lunarData.setEvent(weatherEvent.getID());
            source.sendFeedback(weatherEvent.successTranslationTextComponent(), true);
            source.getWorld().getPlayers().forEach(player -> {
                NetworkHandler.sendToClient(player, new LunarEventPacket(lunarType));
            });
        } else {
            source.sendFeedback(new TranslationTextComponent("enhancedcelestials.commands.failed", lunarType), true);
            return 0;
        }
        return 1;
    }
}