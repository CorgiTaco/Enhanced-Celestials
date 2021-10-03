package corgitaco.enhancedcelestials.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import corgitaco.enhancedcelestials.LunarEventInstance;
import corgitaco.enhancedcelestials.save.LunarEventSavedData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;

import java.util.Arrays;
import java.util.List;

public class SetLunarEventCommand {

    public static final String EC_NOT_ENABLED = "null";

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher) {
        return Commands.literal("setLunarEvent").then(
                Commands.argument("lunarEvent", StringArgumentType.string())
                        .suggests((ctx, sb) -> {
                            LunarContext weatherEventContext = ((EnhancedCelestialsWorldData) ctx.getSource().getLevel()).getLunarContext();
                            return SharedSuggestionProvider.suggest(weatherEventContext != null ? weatherEventContext.getLunarEvents().keySet().stream() : Arrays.stream(new String[]{EC_NOT_ENABLED}), sb);
                        }).executes(cs -> setLunarEvent(cs.getSource(), cs.getArgument("lunarEvent", String.class)))
        );
    }

    public static int setLunarEvent(CommandSourceStack source, String lunarEventKey) {
        ServerLevel world = source.getLevel();
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) world).getLunarContext();

        if (lunarEventKey.equals(EC_NOT_ENABLED) || lunarContext == null) {
            source.sendFailure(new TranslatableComponent("enhancedcelestials.commands.disabled"));
            return 0;
        }

        long dayLength = lunarContext.getLunarTimeSettings().getDayLength();
        long currentDay = (world.getDayTime() / dayLength);

        if (lunarContext.getLunarEvents().containsKey(lunarEventKey)) {
            if (!world.isNight()) {
                world.setDayTime((currentDay * dayLength) + 13000L);
            }
            LunarEventInstance commandInstance = new LunarEventInstance(lunarEventKey, currentDay);
            List<LunarEventInstance> forecast = lunarContext.getLunarForecast().getForecast();
            if (!forecast.isEmpty()) {
                if (forecast.get(0).active(currentDay)) {
                    forecast.remove(0);
                }
            }
            forecast.add(0, commandInstance);

            LunarEventSavedData.get(world).setForecast(lunarContext.getLunarForecast());
        } else {
            source.sendFailure(new TranslatableComponent("enhancedcelestials.commands.lunarevent_missing", lunarEventKey));
            return 0;
        }
        return 1;
    }
}
