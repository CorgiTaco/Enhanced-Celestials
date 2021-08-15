package corgitaco.enhancedcelestials;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;

import java.util.Map;

public class LunarEventInstance {

    public static final Codec<LunarEventInstance> CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(Codec.STRING.fieldOf("lunarEventKey").forGetter((lunarEventInstance) -> {
            return lunarEventInstance.lunarEventKey;
        }), Codec.LONG.fieldOf("scheduledDay").forGetter((lunarEventInstance -> {
            return lunarEventInstance.scheduledDay;
        }))).apply(builder, LunarEventInstance::new);
    });


    private final String lunarEventKey;
    private long scheduledDay;

    public LunarEventInstance(String lunarEventKey, long scheduledDay) {
        this.lunarEventKey = lunarEventKey;
        this.scheduledDay = scheduledDay;
    }

    public String getLunarEventKey() {
        return lunarEventKey;
    }

    public LunarEvent getEvent(Map<String, LunarEvent> events) {
        return events.get(lunarEventKey);
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

    public void setScheduledDay(int scheduledDay) {
        this.scheduledDay = scheduledDay;
    }
}
