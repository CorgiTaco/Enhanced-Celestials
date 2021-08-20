package corgitaco.enhancedcelestials.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import corgitaco.enhancedcelestials.LunarEventInstance;
import corgitaco.enhancedcelestials.LunarForecast;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import corgitaco.enhancedcelestials.util.CustomTranslationTextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;

public class LunarForecastCommand {
    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher) {
        return Commands.literal("lunarForecast").executes(cs -> setLunarEvent(cs.getSource()));
    }

    public static final ChatFormatting[] TEXT_FORMATTINGS = new ChatFormatting[] {
            ChatFormatting.WHITE,
            ChatFormatting.BLUE,
    };

    public static int setLunarEvent(CommandSourceStack source) {
        ServerLevel world = source.getLevel();
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) world).getLunarContext();

        if (lunarContext == null) {
            source.sendFailure(new TranslatableComponent("enhancedcelestials.commands.disabled"));
            return 0;
        }

        long dayLength = lunarContext.getLunarTimeSettings().getDayLength();
        long currentDay = (world.getDayTime() / dayLength);

        TranslatableComponent textComponent = null;

        LunarForecast lunarForecast = lunarContext.getLunarForecast();

        for (int i = Math.min(100, lunarForecast.getForecast().size() - 1); i > 0 ; i--) {
            LunarEventInstance lunarEventInstance = lunarForecast.getForecast().get(i);
            LunarEvent event = lunarEventInstance.getEvent(lunarContext.getLunarEvents());
            CustomTranslationTextComponent name = event.getTextComponents().getName();
            ChatFormatting style = TEXT_FORMATTINGS[i % TEXT_FORMATTINGS.length];

            if (textComponent == null) {
                textComponent = new TranslatableComponent(name.getKey());
            } else {
                textComponent.append(", ").append(new TranslatableComponent(name.getKey()));
            }
            textComponent.append(new TranslatableComponent("enhancedcelestials.lunarforecast.days_left", lunarEventInstance.getDaysUntil(currentDay)));
        }

        if (textComponent != null) {
            source.sendSuccess(new TranslatableComponent("enhancedcelestials.lunarforecast.header", textComponent.append(".")), true);
        } else {
            source.sendSuccess(new TranslatableComponent("enhancedcelestials.lunarforecast.empty", textComponent).withStyle(ChatFormatting.YELLOW), true);
        }

        return 1;
    }
}
