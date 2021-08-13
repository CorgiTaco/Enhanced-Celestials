package corgitaco.enhancedcelestials;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;

import java.util.Map;

public class LunarEventInstance {

    public static final Codec<LunarEventInstance> CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(Codec.STRING.fieldOf("lunarEventKey").forGetter((lunarEventInstance) -> {
            return lunarEventInstance.lunarEventKey;
        }), Codec.INT.fieldOf("daysUntil").forGetter((lunarEventInstance -> {
            return lunarEventInstance.daysUntil;
        }))).apply(builder, LunarEventInstance::new);
    });


    private final String lunarEventKey;
    private int daysUntil;

    public LunarEventInstance(String lunarEventKey, int daysUntil) {
        this.lunarEventKey = lunarEventKey;
        this.daysUntil = daysUntil;
    }

    public String getLunarEventKey() {
        return lunarEventKey;
    }

    public LunarEvent getEvent(Map<String, LunarEvent> events) {
        return events.get(lunarEventKey);
    }

    public int getDaysUntil() {
        return daysUntil;
    }

    public void setDaysUntil(int timeUntil) {
        this.daysUntil = timeUntil;
    }
}
