package corgitaco.enhancedcelestials.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.lunarevent.LunarContext;
import corgitaco.enhancedcelestials.lunarevent.LunarForecast;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

public class LunarForecastCommand {
    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher) {
        return Commands.literal("lunarForecast").executes(cs -> displayLunarForecast(cs.getSource())).then(Commands.literal("recompute").executes(cs -> recompute(cs.getSource())));
    }


    public static int recompute(CommandSourceStack source) {
        ServerLevel world = source.getLevel();

        LunarContext lunarContext = ((EnhancedCelestialsWorldData) world).getLunarContext();

        if (lunarContext == null) {
            source.sendFailure(Component.translatable("enhancedcelestials.commands.disabled"));
            return 0;
        }
        LunarForecast lunarForecast = lunarContext.getLunarForecast();
        lunarForecast.recompute(world);
        source.sendSuccess(Component.translatable("enhancedcelestials.lunarforecast.recompute"), true);
        return 1;
    }


    public static int displayLunarForecast(CommandSourceStack source) {
        ServerLevel world = source.getLevel();
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) world).getLunarContext();

        if (lunarContext == null) {
            source.sendFailure(Component.translatable("enhancedcelestials.commands.disabled"));
            return 0;
        }

        source.sendSuccess(lunarContext.getLunarForecast().getForecastComponent(world.getDayTime()), true);
        return 1;
    }
}
