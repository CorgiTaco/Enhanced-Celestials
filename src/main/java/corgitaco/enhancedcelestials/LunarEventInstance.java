package corgitaco.enhancedcelestials;

public class LunarEventInstance {

    private final String lunarEventKey;
    private long timeUntil;

    public LunarEventInstance(String lunarEventKey, long timeUntil) {
        this.lunarEventKey = lunarEventKey;
        this.timeUntil = timeUntil;
    }

    public String getLunarEventKey() {
        return lunarEventKey;
    }

    public long getTimeUntil() {
        return timeUntil;
    }

    public void setTimeUntil(long timeUntil) {
        this.timeUntil = timeUntil;
    }
}
