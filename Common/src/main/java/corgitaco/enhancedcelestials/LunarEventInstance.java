package corgitaco.enhancedcelestials;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class LunarEventInstance {

    public static final Codec<LunarEventInstance> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    ResourceKey.codec(EnhancedCelestialsRegistry.LUNAR_EVENT_KEY).fieldOf("lunarEventKey").forGetter(lunarEventInstance -> lunarEventInstance.lunarEventKey),
                    Codec.LONG.fieldOf("scheduledDay").forGetter(lunarEventInstance -> lunarEventInstance.scheduledDay),
                    Codec.BOOL.fieldOf("forced").forGetter(lunarEventInstance -> lunarEventInstance.forced)
            ).apply(builder, LunarEventInstance::new));


    private final ResourceKey<LunarEvent> lunarEventKey;
    private final long scheduledDay;
    private final boolean forced;

    public LunarEventInstance(ResourceKey<LunarEvent> lunarEventKey, long scheduledDay) {
        this(lunarEventKey, scheduledDay, false);
    }

    public LunarEventInstance(ResourceKey<LunarEvent> lunarEventKey, long scheduledDay, boolean forced) {
        this.lunarEventKey = lunarEventKey;
        this.scheduledDay = scheduledDay;
        this.forced = forced;
    }

    public ResourceKey<LunarEvent> getLunarEventKey() {
        return lunarEventKey;
    }

    public Holder<LunarEvent> getEvent(Registry<LunarEvent> events) {
        return events.getHolderOrThrow(lunarEventKey);
    }

    public long scheduledDay() {
        return scheduledDay;
    }

    public long getDaysUntil(long currentDay) {
        return this.scheduledDay - currentDay;
    }

    public boolean passed(long currentDay) {
        return this.scheduledDay - currentDay <= -1;
    }

    public boolean active(long currentDay) {
        return this.scheduledDay - currentDay == 0;
    }
}
