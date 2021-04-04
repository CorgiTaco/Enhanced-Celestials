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
        List<String> lunarEventType = LunarEventSystem.LUNAR_EVENTS.stream().map(LunarEvent::getID).collect(Collectors.toList());

        return Commands.literal("setlunarevent").then(Commands.argument("lunarevent", StringArgumentType.string()).suggests((ctx, sb) -> ISuggestionProvider.suggest(lunarEventType.stream(), sb))
                .executes((cs) -> setLunarEventType(cs.getSource(), cs.getArgument("lunarevent", String.class))));
    }

    public static int setLunarEventType(CommandSource source, String lunarType) {
        LunarEvent oldLunarEvent = EnhancedCelestials.currentLunarEvent;
        LunarEvent newLunarEvent = LunarEventSystem.LUNAR_EVENTS_MAP.get(lunarType);
        if (newLunarEvent != null) {
            long dayTime = EnhancedCelestialsUtils.modulosDaytime(source.getWorld().getWorld().getWorldInfo().getDayTime());
            if (dayTime >= 12000 && dayTime <= 24000) {
                EnhancedCelestials.lunarData.setEvent(newLunarEvent.getID());
                source.sendFeedback(newLunarEvent.successTranslationTextComponent(), true);
                source.getWorld().getPlayers().forEach(player -> {
                    if (!oldLunarEvent.getID().equals(newLunarEvent.getID())) {
                        oldLunarEvent.sendSettingNotification(player);
                    }
                    NetworkHandler.sendToClient(player, new LunarEventPacket(lunarType));
                });
            } else {
                EnhancedCelestials.nextNightLunarEvent = newLunarEvent;
                source.sendFeedback(newLunarEvent.successNextNightTranslationTextComponent(), true);
            }
            return 1;
        }
        source.sendFeedback(new TranslationTextComponent("enhancedcelestials.commands.failed", lunarType), true);
        return 0;
    }
}