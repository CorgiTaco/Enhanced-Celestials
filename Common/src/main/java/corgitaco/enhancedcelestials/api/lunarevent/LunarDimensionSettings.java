package corgitaco.enhancedcelestials.api.lunarevent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import net.minecraft.resources.ResourceKey;

public record LunarDimensionSettings(ResourceKey<LunarEvent> defaultEvent, long trackedPastEventsMaxCount,
                                     long dayLength, long yearLengthInDays, long minDaysBetweenEvents, boolean requiresClearSkies) {

    public static final Codec<LunarDimensionSettings> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    ResourceKey.codec(EnhancedCelestialsRegistry.LUNAR_EVENT_KEY).fieldOf("default").forGetter(LunarDimensionSettings::defaultEvent),
                    Codec.LONG.fieldOf("tracked_past_events_max_count").forGetter(LunarDimensionSettings::trackedPastEventsMaxCount),
                    Codec.LONG.fieldOf("day_length").forGetter(LunarDimensionSettings::dayLength),
                    Codec.LONG.fieldOf("year_length_in_days").forGetter(LunarDimensionSettings::yearLengthInDays),
                    Codec.LONG.fieldOf("min_days_between_all_events").forGetter(LunarDimensionSettings::minDaysBetweenEvents),
                    Codec.BOOL.fieldOf("requires_clear_skies").orElse(true).forGetter(LunarDimensionSettings::requiresClearSkies)
            ).apply(builder, LunarDimensionSettings::new)
    );
}
