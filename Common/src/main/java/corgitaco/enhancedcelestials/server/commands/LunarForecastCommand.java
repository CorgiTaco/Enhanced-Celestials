package corgitaco.enhancedcelestials.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import corgitaco.enhancedcelestials.LunarEventInstance;
import corgitaco.enhancedcelestials.LunarForecast;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import corgitaco.enhancedcelestials.save.LunarEventSavedData;
import corgitaco.enhancedcelestials.util.CustomTranslationTextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;

public class LunarForecastCommand {
    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher) {
        return Commands.literal("lunarForecast").executes(cs -> setLunarEvent(cs.getSource())).then(Commands.literal("recompute").executes(cs -> recompute(cs.getSource())));
    }


    public static int recompute(CommandSourceStack source) {
        ServerLevel world = source.getLevel();

        LunarContext lunarContext = ((EnhancedCelestialsWorldData) world).getLunarContext();

        if (lunarContext == null) {
            source.sendFailure(Component.translatable("enhancedcelestials.commands.disabled"));
            return 0;
        }
        LunarForecast lunarForecast = lunarContext.getLunarForecast();
        lunarForecast.getForecast().clear();
        lunarForecast.setLastCheckedGameTime(Long.MIN_VALUE);
        lunarContext.computeLunarForecast(world, lunarForecast, world.getGameTime());
        LunarEventSavedData.get(world).setForecast(lunarContext.getLunarForecast());
        source.sendSuccess(Component.translatable("enhancedcelestials.lunarforecast.recompute"), true);
        return 1;
    }


    public static int setLunarEvent(CommandSourceStack source) {
        ServerLevel world = source.getLevel();
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) world).getLunarContext();

        if (lunarContext == null) {
            source.sendFailure(Component.translatable("enhancedcelestials.commands.disabled"));
            return 0;
        }

        long dayLength = lunarContext.getLunarTimeSettings().getDayLength();
        long currentDay = (world.getDayTime() / dayLength);

        MutableComponent textComponent = null;

        LunarForecast lunarForecast = lunarContext.getLunarForecast();

        for (int i = Math.min(100, lunarForecast.getForecast().size() - 1); i > 0; i--) {
            LunarEventInstance lunarEventInstance = lunarForecast.getForecast().get(i);
            LunarEvent event = lunarEventInstance.getEvent(lunarContext.getLunarEvents());
            CustomTranslationTextComponent name = event.getTextComponents().getName();

            if (textComponent == null) {
                textComponent = Component.translatable(name.getKey());
            } else {
                textComponent.append(", ").append(Component.translatable(name.getKey()));
            }
            textComponent.append(Component.translatable("enhancedcelestials.lunarforecast.days_left", lunarEventInstance.getDaysUntil(currentDay)));
        }

        if (textComponent != null) {
            source.sendSuccess(Component.translatable("enhancedcelestials.lunarforecast.header", textComponent.append(".")), true);
        } else {
            source.sendSuccess(Component.translatable("enhancedcelestials.lunarforecast.empty", textComponent).withStyle(ChatFormatting.YELLOW), true);
        }

        return 1;
    }
}
