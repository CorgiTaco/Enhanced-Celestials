package corgitaco.enhancedcelestials.api.lunarevent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;

public record LunarDimensionSettings(Holder<LunarEvent> defaultEvent, long trackedPastEventsMaxCount,
                                     long dayLength, long yearLengthInDays, long minDaysBetweenEvents) {

    public static final Codec<LunarDimensionSettings> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    LunarEvent.CODEC.fieldOf("default").forGetter(LunarDimensionSettings::defaultEvent),
                    Codec.LONG.fieldOf("tracked_past_events_max_count").forGetter(LunarDimensionSettings::trackedPastEventsMaxCount),
                    Codec.LONG.fieldOf("day_length").forGetter(LunarDimensionSettings::dayLength),
                    Codec.LONG.fieldOf("year_length_in_days").forGetter(LunarDimensionSettings::yearLengthInDays),
                    Codec.LONG.fieldOf("min_days_between_all_events").forGetter(LunarDimensionSettings::minDaysBetweenEvents)
            ).apply(builder, LunarDimensionSettings::new)
    );
}
