package corgitaco.enhancedcelestials.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.datafixers.util.Either;
import corgitaco.enhancedcelestials.EnhancedCelestials;
import corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import corgitaco.enhancedcelestials.lunarevent.EnhancedCelestialsLunarForecastWorldData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceOrTagKeyArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;

import java.util.Optional;

public class SetLunarEventCommand {

    private static final DynamicCommandExceptionType ERROR_LUNAR_EVENT_INVALID = new DynamicCommandExceptionType(obj -> Component.translatable("enhancedcelestials.commands.setlunarevent.invalid", obj));

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher) {
        return Commands.literal("setLunarEvent")
                .then(Commands.argument("lunarEvent", ResourceOrTagKeyArgument.resourceOrTagKey(EnhancedCelestialsRegistry.LUNAR_EVENT_KEY))
                        .executes(cs -> setLunarEvent(cs.getSource(), ResourceOrTagKeyArgument.getResourceOrTagKey(cs, "lunarEvent", EnhancedCelestialsRegistry.LUNAR_EVENT_KEY, ERROR_LUNAR_EVENT_INVALID))));
    }

    public static int setLunarEvent(CommandSourceStack source, ResourceOrTagKeyArgument.Result<LunarEvent> lunarEventResult) {
        ServerLevel world = source.getLevel();

        Optional<EnhancedCelestialsLunarForecastWorldData> lunarForecastWorldData = EnhancedCelestials.lunarForecastWorldData(world);

        if (lunarForecastWorldData.isEmpty()) {
            source.sendFailure(Component.translatable("enhancedcelestials.commands.disabled"));
            return 0;
        }
        EnhancedCelestialsLunarForecastWorldData data = lunarForecastWorldData.orElseThrow();

        if (world.isRaining() && data.getDimensionSettings().requiresClearSkies()) {
            source.sendFailure(Component.literal("Lunar events can only be started during clear skies!"));
            return 0;
        }

        Either<ResourceKey<LunarEvent>, TagKey<LunarEvent>> unwrap = lunarEventResult.unwrap();
        if (unwrap.left().isPresent()) {
            ResourceKey<LunarEvent> lunarEventResourceKey = unwrap.left().orElseThrow();
            Registry<LunarEvent> lunarEvents = world.registryAccess().registry(EnhancedCelestialsRegistry.LUNAR_EVENT_KEY).orElseThrow();
            if (lunarEvents.containsKey(lunarEventResourceKey) && lunarEvents.getHolderOrThrow(lunarEventResourceKey).isBound()) {
                data.setLunarEvent(lunarEventResourceKey);
                return 1;
            } else {
                source.sendFailure(Component.literal("Invalid lunar event \"%s\"!".formatted(lunarEventResourceKey.location())));
                return 0;
            }
        }

        if (unwrap.right().isPresent()) {
            Optional<HolderSet.Named<LunarEvent>> possibleTag = world.registryAccess().registry(EnhancedCelestialsRegistry.LUNAR_EVENT_KEY).orElseThrow().getTag(unwrap.right().orElseThrow());

            if (possibleTag.isPresent()) {
                HolderSet.Named<LunarEvent> possibleLunarEvents = possibleTag.orElseThrow();

                Optional<Holder<LunarEvent>> randomLunarEvent = possibleLunarEvents.getRandomElement(world.random);

                if (randomLunarEvent.isPresent()) {
                    source.getServer().submit(() -> data.setLunarEvent(randomLunarEvent.orElseThrow().unwrapKey().orElseThrow()));
                    return 1;
                } else {
                    source.sendFailure(Component.literal("Invalid lunar event tag \"%s\"!".formatted(possibleLunarEvents.key().location())));
                    return 0;
                }
            } else {
                source.sendFailure(Component.literal("Invalid lunar event tag."));
                return 0;
            }
        }
        source.sendFailure(Component.literal("Could not start Lunar Event..."));

        return 0;
    }
}
