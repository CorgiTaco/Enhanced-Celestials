package corgitaco.enhancedcelestials.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import corgitaco.enhancedcelestials.LunarEventInstance;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;

import java.util.Arrays;
import java.util.List;

public class SetLunarEventCommand {

    public static final String EC_NOT_ENABLED = "null";

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("setLunarEvent").then(
                Commands.argument("lunarEvent", StringArgumentType.string())
                        .suggests((ctx, sb) -> {
                            LunarContext weatherEventContext = ((EnhancedCelestialsWorldData) ctx.getSource().getWorld()).getLunarContext();
                            return ISuggestionProvider.suggest(weatherEventContext != null ? weatherEventContext.getLunarEvents().keySet().stream() : Arrays.stream(new String[]{EC_NOT_ENABLED}), sb);
                        }).executes(cs -> betterWeatherSetSeason(cs.getSource(), cs.getArgument("lunarEvent", String.class)))
        );
    }

    public static int betterWeatherSetSeason(CommandSource source, String lunarEventKey) {
        if (lunarEventKey.equals(EC_NOT_ENABLED)) {
            source.sendErrorMessage(new TranslationTextComponent("enhancedcelestials.commands.disabled"));
            return 0;
        }
        long dayLength = 24000L;

        ServerWorld world = source.getWorld();
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) world).getLunarContext();
        long currentDay = (world.getDayTime() / dayLength);

        if (lunarContext != null) {
            if (lunarContext.getLunarEvents().containsKey(lunarEventKey)) {
                if (!world.isNightTime()) {
                    world.setDayTime((currentDay * dayLength) + 13000L);
                }
                LunarEventInstance commandInstance = new LunarEventInstance(lunarEventKey, currentDay);
                List<LunarEventInstance> forecast = lunarContext.getLunarForecast().getForecast();
                if (forecast.get(0).active(currentDay)) {
                    forecast.remove(0);
                }
                forecast.add(0, commandInstance);
            } else {
                source.sendErrorMessage(new TranslationTextComponent("enhancedcelestials.commands.lunarevent_missing", lunarEventKey));
                return 0;
            }
        }
        return 1;
    }
}
