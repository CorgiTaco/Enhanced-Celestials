package corgitaco.enchancedcelestials.server;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import corgitaco.enchancedcelestials.EnhancedCelestials;
import corgitaco.enchancedcelestials.lunarevent.LunarEvent;
import corgitaco.enchancedcelestials.util.EnhancedCelestialsUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class GetLunarEventCommand {
    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("get").executes((cs) -> getLunarEventType(cs.getSource()));
    }

    public static int getLunarEventType(CommandSource source) {
        long dayTime = EnhancedCelestialsUtils.modulosDaytime(source.getWorld().getWorld().getWorldInfo().getDayTime());
        source.sendFeedback((dayTime >= 12000 && dayTime <= 24000) ? new TranslationTextComponent("enhancedcelestials.commands.get.success", new TranslationTextComponent(EnhancedCelestials.currentLunarEvent.getName()))
                                                                   : new TranslationTextComponent("enhancedcelestials.commands.get.success.no_event"),
                                                                   true);
        return 1;
    }
}