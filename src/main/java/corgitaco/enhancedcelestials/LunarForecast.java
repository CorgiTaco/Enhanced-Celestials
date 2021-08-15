package corgitaco.enhancedcelestials;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;

public class LunarForecast {

    public static final Codec<LunarForecast> CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(Codec.list(LunarEventInstance.CODEC).fieldOf("forecast").forGetter((lunarForecast) -> {
            return lunarForecast.forecast;
        }), Codec.LONG.fieldOf("lastCheckedGameTime").forGetter((lunarForecast -> {
            return lunarForecast.lastCheckedGameTime;
        }))).apply(builder, LunarForecast::new);
    });

    private final List<LunarEventInstance> forecast;
    private long lastCheckedGameTime;

    public LunarForecast(List<LunarEventInstance> forecast, long lastCheckedGameTime) {
        this.forecast = new ArrayList<>(forecast);
        this.lastCheckedGameTime = lastCheckedGameTime;
    }

    public List<LunarEventInstance> getForecast() {
        return forecast;
    }

    public long getLastCheckedGameTime() {
        return lastCheckedGameTime;
    }

    public void setLastCheckedGameTime(long lastCheckedGameTime) {
        this.lastCheckedGameTime = lastCheckedGameTime;
    }
}
