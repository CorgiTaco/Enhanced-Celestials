package corgitaco.enhancedcelestials.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import corgitaco.enhancedcelestials.LunarEventInstance;
import corgitaco.enhancedcelestials.LunarForecast;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import corgitaco.enhancedcelestials.util.CustomTranslationTextComponent;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;

public class LunarForecastCommand {
    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("lunarForecast").executes(cs -> setLunarEvent(cs.getSource()));
    }

    public static final TextFormatting[] TEXT_FORMATTINGS = new TextFormatting[] {
            TextFormatting.WHITE,
            TextFormatting.BLUE,
    };

    public static int setLunarEvent(CommandSource source) {
        ServerWorld world = source.getWorld();
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) world).getLunarContext();

        if (lunarContext == null) {
            source.sendErrorMessage(new TranslationTextComponent("enhancedcelestials.commands.disabled"));
            return 0;
        }

        long dayLength = lunarContext.getLunarTimeSettings().getDayLength();
        long currentDay = (world.getDayTime() / dayLength);

        TranslationTextComponent textComponent = null;

        LunarForecast lunarForecast = lunarContext.getLunarForecast();

        for (int i = Math.min(100, lunarForecast.getForecast().size() - 1); i > 0 ; i--) {
            LunarEventInstance lunarEventInstance = lunarForecast.getForecast().get(i);
            LunarEvent event = lunarEventInstance.getEvent(lunarContext.getLunarEvents());
            CustomTranslationTextComponent name = event.getTextComponents().getName();
            TextFormatting style = TEXT_FORMATTINGS[i % TEXT_FORMATTINGS.length];

            if (textComponent == null) {
                textComponent = new TranslationTextComponent(name.getKey());
            } else {
                textComponent.appendString(", ").append(new TranslationTextComponent(name.getKey()));
            }
            textComponent.append(new TranslationTextComponent("enhancedcelestials.lunarforecast.days_left", lunarEventInstance.getDaysUntil(currentDay)));
        }

        if (textComponent != null) {
            source.sendFeedback(new TranslationTextComponent("enhancedcelestials.lunarforecast.header", textComponent.appendString(".")), true);
        } else {
            source.sendFeedback(new TranslationTextComponent("enhancedcelestials.lunarforecast.empty", textComponent).mergeStyle(TextFormatting.YELLOW), true);
        }

        return 1;
    }
}
