package corgitaco.enhancedcelestials.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.datafixers.util.Pair;
import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import corgitaco.enhancedcelestials.core.EnhancedCelestialsContext;
import corgitaco.enhancedcelestials.lunarevent.LunarForecast;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceOrTagLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

public class SetLunarEventCommand {

    private static final DynamicCommandExceptionType ERROR_LUNAR_EVENT_INVALID = new DynamicCommandExceptionType(obj -> Component.translatable("enhancedcelestials.commands.setlunarevent.invalid", obj));

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher) {
        return Commands.literal("setLunarEvent")
                .then(Commands.argument("lunarEvent", ResourceOrTagLocationArgument.resourceOrTag(EnhancedCelestialsRegistry.LUNAR_EVENT_KEY))
                .executes(cs -> setLunarEvent(cs.getSource(), ResourceOrTagLocationArgument.getRegistryType(cs, "lunarEvent", EnhancedCelestialsRegistry.LUNAR_EVENT_KEY, ERROR_LUNAR_EVENT_INVALID))));
    }

    public static int setLunarEvent(CommandSourceStack source, ResourceOrTagLocationArgument.Result<LunarEvent> lunarEventResult) {
        ServerLevel world = source.getLevel();
        EnhancedCelestialsContext enhancedCelestialsContext = ((EnhancedCelestialsWorldData) world).getLunarContext();
        if (enhancedCelestialsContext == null) {
            source.sendFailure(Component.translatable("enhancedcelestials.commands.disabled"));
            return 0;
        }


        LunarForecast forecast = enhancedCelestialsContext.getLunarForecast();

        long dayLength = forecast.getDimensionSettingsHolder().value().dayLength();
        long currentDay = (world.getDayTime() / dayLength);

        Pair<Component, Boolean> component = forecast.setOrReplaceEventWithResponse(lunarEventResult, currentDay, source.getLevel().getRandom());

        if (component.getSecond()) {
            if (!world.isNight()) {
                world.setDayTime((currentDay * dayLength) + 13000L);
            }
            source.sendSuccess(component.getFirst(), true);
            return 1;
        } else {
            source.sendFailure(component.getFirst());
            return 0;
        }
    }
}
