package corgitaco.enhancedcelestials.api.lunarevent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;

import java.util.Map;

public record LunarDimensionSettings(Map<Holder<LunarEvent>, Entry> eventChance, Holder<LunarEvent> defaultEvent,
                                     long dayLength, long yearLengthInDays, long minDaysBetweenEvents) {

    public static final Codec<LunarDimensionSettings> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.unboundedMap(LunarEvent.CODEC, Entry.CODEC).fieldOf("event_chances").forGetter(LunarDimensionSettings::eventChance),
                    LunarEvent.CODEC.fieldOf("default").forGetter(LunarDimensionSettings::defaultEvent),
                    Codec.LONG.fieldOf("day_length").forGetter(LunarDimensionSettings::dayLength),
                    Codec.LONG.fieldOf("year_length_in_days").forGetter(LunarDimensionSettings::yearLengthInDays),
                    Codec.LONG.fieldOf("min_days_between_all_events").forGetter(LunarDimensionSettings::minDaysBetweenEvents)
            ).apply(builder, LunarDimensionSettings::new)
    );

    public record Entry(double chance, int minNumberOfNights) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(builder ->
                builder.group(
                        Codec.DOUBLE.fieldOf("chance").forGetter(Entry::chance),
                        Codec.INT.fieldOf("min_number_of_nights_between").forGetter(Entry::minNumberOfNights)
                ).apply(builder, Entry::new)
        );
    }
}
